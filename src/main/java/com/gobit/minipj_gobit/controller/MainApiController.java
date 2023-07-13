package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import com.gobit.minipj_gobit.boardDept.repository.dBoardRepository;
import com.gobit.minipj_gobit.boardUser.UserDTO;
import com.gobit.minipj_gobit.dto.ChatUserDTO;
import com.gobit.minipj_gobit.entity.*;
import com.gobit.minipj_gobit.entity.Calendar;
import com.gobit.minipj_gobit.noticeDept.entity.nBoard;
import com.gobit.minipj_gobit.noticeDept.repository.nBoardRepository;
import com.gobit.minipj_gobit.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MainApiController {
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private MessageListener messageListener;
    private final UserOnOffRepository userOnOffRepository;
    private final CalendarRepository calendarRepository;
    private final dBoardRepository dBoardRepository;
    private final nBoardRepository nBoardRepository;
    private final ApprovalRepository approvalRepository;
    private final VacationRepository vacationRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @GetMapping("/messagePage")
    public ModelAndView MessagePage() {
        User loginuser = (User) httpSession.getAttribute("user");
        ModelAndView mv = new ModelAndView();
        List<ChatUserDTO> userDTOList = new ArrayList<>();

        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            ChatUserDTO userDTO = ChatUserDTO.ChatUserToDTO(user);
            if(userDTO.getId() != loginuser.getUSERNUM()){
                userDTO.setCount(messageRepository.findBycnt(user, loginuser));
                userDTOList.add(userDTO);
            }
        }
        mv.addObject("userDTOList", userDTOList);
        mv.setViewName("/chat/chatUserList.html");
        return mv;
    }

    @GetMapping("/messageSendPage/{id}")
    public ModelAndView messageSendPage(@PathVariable long id) throws IOException {
        ModelAndView mv = new ModelAndView();
        User user = (User) httpSession.getAttribute("user");
        User receiveUser = userRepository.findById(id).get();

        UserDTO userDTO = UserDTO.userToDTO(user);
        UserDTO receiveUserDTO = UserDTO.userToDTO(receiveUser);

        List<Message> sendList = messageRepository.findByUserAndReceiveUser(user, receiveUser);
        List<Message> sendList2 = messageRepository.findByUserAndReceiveUser(receiveUser, user);
        sendList.addAll(sendList2);
        sendList.sort(Comparator.comparing(Message::getChatSendDate));

        List<Message> addSetCheck = new ArrayList<>();
        for(Message ms : sendList){
            if(ms.getReceiveUser().getUSERNUM() == user.getUSERNUM() && ms.getChatCheck() != 1){
                ms.setChatCheck(1);
                addSetCheck.add(ms);
            }
        }

        messageRepository.saveAll(addSetCheck);
        messageRepository.flush();
        // 발행 후 이벤트 수정
        messageListener.afterSaveAll(addSetCheck);

        mv.addObject("sendId", userDTO);
        mv.addObject("receiveId", receiveUserDTO);
        mv.addObject("sendList", sendList);
        mv.setViewName("/chat/chatUser.html");
        return mv;
    }

    @PostMapping("/sendMessage")
    public String sendMessage(@RequestParam("receiveuser") long receiveuser,
                              @RequestParam("sendText") String sendText,
                              @RequestParam("usercheck") String usercheck){
        System.out.println("유저 체크 : " + usercheck);
        User user = (User) httpSession.getAttribute("user");
        Message message = new Message();
        message.setChatSend(sendText);
        message.setUser(user);
        if(usercheck.equals("1")){
            message.setChatCheck(1);
        }else if (usercheck.equals("0")){
            message.setChatCheck(0);
        }
        message.setReceiveUser(userRepository.findById(receiveuser).get());
        messageRepository.save(message);
        messageRepository.flush();

        return null;
    }

    @GetMapping("/approvalList/{appNum}")
    public ModelAndView approvalListDetail(@PathVariable long appNum) {
        ModelAndView mv = new ModelAndView();
        Approval approval = approvalRepository.findById(appNum).orElse(null);
        System.out.println(approval);
        mv.addObject("approval", approval);
        mv.setViewName("approvalListDetail.html");
        return mv;
    }

    @PostMapping("/approvalList/submit")
    public String approvalListDetailSubmit(@RequestParam("id") long id,
                                           @RequestParam("sign") String sign) {
        String caltype = "";
        User user = (User) httpSession.getAttribute("user");
        Approval approval = approvalRepository.findById(id).orElse(null);
        approval.setAppUserNum(user);
        approval.setAppState("승인");
        approval.setAppSign(sign);
        approval.setAppCancleReason(null);
        approval.setAppStateDate(LocalDateTime.now());
        approvalRepository.save(approval);
        approvalRepository.flush();
        Calendar calendar = new Calendar();
        calendar.setUser(approval.getUserNum());
        calendar.setApproval(approval);

        if (approval.getAppSort().equals("V")) {
            caltype = "휴가";
        } else if (approval.getAppSort().equals("B")) {
            caltype = "출장";
        } else if (approval.getAppSort().equals("M")) {
            caltype = "회의";
        }
        calendar.setCALTITLE(approval.getUserNum().getUSERNAME() + " " + caltype);
        calendar.setCALSTART(String.valueOf(approval.getAppStart()));
        calendar.setCALEND(String.valueOf(approval.getAppEnd()));
        calendar.setCALTYPE(String.valueOf(approval.getAppSort()));
        calendar.setCALCONTENT(approval.getAppContent());
        calendarRepository.save(calendar);
        calendarRepository.flush();
        return "성공";
    }

    @PostMapping("/approvalList/cancle")
    public String approvalListDetailCancle(@RequestParam("id") long id,
                                           @RequestParam("sign") String sign,
                                           @RequestParam("canclereason") String canclereason) {
        System.out.println(canclereason);
        User user = (User) httpSession.getAttribute("user");
        Approval approval = approvalRepository.findById(id).orElse(null);
        Vacation vacation = vacationRepository.findByUserNum(approval.getUserNum().getUSERNUM());
        vacation.setVacUsed(vacation.getVacUsed() - approval.getAppVacReq());
        vacation.setVacLeft(vacation.getVacLeft() + approval.getAppVacReq());
        vacationRepository.save(vacation);
        vacationRepository.flush();
        approval.setAppUserNum(user);
        approval.setAppCancleReason(canclereason);
        approval.setAppState("반려");
        approval.setAppSign(sign);
        approval.setAppStateDate(LocalDateTime.now());
        approvalRepository.save(approval);
        approvalRepository.flush();
        Optional<Calendar> calendar = Optional.ofNullable(calendarRepository.findByApproval(approval).orElse(null));
        if (calendar.isEmpty()) {
            return "성공";
        } else {
            calendarRepository.delete(calendar.get());
            calendarRepository.flush();
            return "성공";
        }
    }

    @PostMapping(value = "/onadd")
    public String onadd() throws Exception {
        UserOnOff userOnOff = new UserOnOff();
        Calendar calendar = new Calendar();
        User user = (User) httpSession.getAttribute("user");
        Date today = new Date();
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date inputDate = inputDateFormat.parse(String.valueOf(today));
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String outputDateString = outputDateFormat.format(inputDate);

        userOnOff.setUser(user);
        userOnOff.setSTART(outputDateString);
        userOnOff.setEND("0");
        userOnOffRepository.save(userOnOff);
        userOnOffRepository.flush();
        calendar.setUser(user);
        calendar.setUserOnOff(userOnOff);
        calendar.setCALTYPE("출퇴근");
        calendar.setCALSTART(outputDateString);
        calendar.setCALEND("0");
        calendarRepository.save(calendar);
        calendarRepository.flush();

        return outputDateString;
    }

    @PostMapping(value = "/offadd")
    public String offadd(@RequestParam("start") String start) throws Exception {
        User user = (User) httpSession.getAttribute("user");
        UserOnOff userOnOff = userOnOffRepository.findByUSERANDSTART(user.getUSERNUM(), start).get();
        System.out.println("----------------------");
        System.out.println(userOnOff);
        System.out.println("----------------------");
        Calendar calendar = calendarRepository.findByUserOnOff(userOnOff).get();
        Date today = new Date();
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date inputDate = inputDateFormat.parse(String.valueOf(today));
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String outputDateString = outputDateFormat.format(inputDate);
        userOnOff.setEND(outputDateString);
        calendar.setCALEND(outputDateString);

        // start와 end 시간을 Date 형식으로 변환
        Date startDate = outputDateFormat.parse(userOnOff.getSTART());
        Date endDate = outputDateFormat.parse(outputDateString);

        // start가 오전 9시 전이고 end가 오후 6시 이후인 경우
        java.util.Calendar startDateCalendar = java.util.Calendar.getInstance();
        startDateCalendar.setTime(startDate);
        java.util.Calendar endDateCalendar = java.util.Calendar.getInstance();
        endDateCalendar.setTime(endDate);

        int a = endDate.getHours() - startDate.getHours();
        int b = endDate.getMinutes() - startDate.getMinutes();
        String commutetime = String.format("%.1f", (double) ((a * 60) + b) / 60);
        userOnOff.setCOMMUTETIME(Double.parseDouble(commutetime));

        if (startDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) < 9 && endDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) >= 18) {
            userOnOff.setCOMMUTETYPE("출근");
            calendar.setCALTITLE("출근");
        } else if (startDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) < 9 && endDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) < 18) {
            userOnOff.setCOMMUTETYPE("조퇴");
            calendar.setCALTITLE("조퇴");
        } else if (startDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) >= 9 && endDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) >= 18) {
            userOnOff.setCOMMUTETYPE("지각");
            calendar.setCALTITLE("지각");
        } else {
            userOnOff.setCOMMUTETYPE("결근");
            calendar.setCALTITLE("결근");
        }
        userOnOffRepository.save(userOnOff);
        userOnOffRepository.flush();
        calendarRepository.save(calendar);
        calendarRepository.flush();

        return outputDateString;
    }

    @PostMapping(value = "/onaddcheck") //좋아요 체크 및 좋아요 수
    public String onaddcheck(@RequestParam("start") String start) throws Exception {
        User user = (User) httpSession.getAttribute("user");
        String startdate = userOnOffRepository.findByCLASSIFYANDSTART(user.getUSERNUM(), start);
        return startdate;
    }

    @PostMapping(value = "/offaddcheck") //좋아요 체크 및 좋아요 수
    public String offaddcheck(@RequestParam("end") String end) throws Exception {
        User user = (User) httpSession.getAttribute("user");
        String enddate = userOnOffRepository.findByCLASSIFYANDEND(user.getUSERNUM(), end);
        return enddate;
    }

    @PostMapping(value = "/chart") //좋아요 체크 및 좋아요 수
    public Map<String, Double> chart() {
        User user = (User) httpSession.getAttribute("user");
        LocalDate today = LocalDate.now();

        String yearMonth = today.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String year = today.format(DateTimeFormatter.ofPattern("yyyy"));
        Double yearMonthChart = userOnOffRepository.findByYearMonthChart(yearMonth, user.getUSERNUM());
        Double yearChart = userOnOffRepository.findByYearMonthChart(year, user.getUSERNUM());
        double yearMonthChartValue = (yearMonthChart != null) ? yearMonthChart : 0.0;
        double yearChartValue = (yearChart != null) ? yearChart : 0.0;

        Map<String, Double> result = new HashMap<>();
        result.put("yearMonthChart", yearMonthChartValue);
        result.put("yearChart", yearChartValue);

        return result;
    }

    @GetMapping("/dboardList")
    public List<Map<String, Object>> dBoardListGet() {
        List<dBoard> dBoardList = dBoardRepository.findBydBoardDept();
        List<Map<String, Object>> dBoardListmap = dBoardList.stream().map(dboard -> {
            Map<String, Object> map = new HashMap<>();
            map.put("dboardNum", dboard.getId());
            map.put("dboardDept", dboard.getUser().getUSERDEPT());
            map.put("dboardTitle", dboard.getTitle());
            map.put("dboardContent", dboard.getContent());
            map.put("dboardWriter", dboard.getUser().getUSERNAME());
            map.put("dboardCnt", dboard.getCnt());
            return map;
        }).collect(Collectors.toList());
        return dBoardListmap;
    }

    @GetMapping("/nboardList")
    public List<Map<String, Object>> nBoardListGet() {
        List<nBoard> nBoardList = nBoardRepository.findBynBoardTop5();
        List<Map<String, Object>> nBoardListmap = nBoardList.stream().map(nboard -> {
            Map<String, Object> map = new HashMap<>();
            map.put("nboardNum", nboard.getId());
            map.put("nboardTitle", nboard.getTitle());
            map.put("nboardContent", nboard.getContent());
            map.put("nboardWriter", nboard.getUser().getUSERNAME());
            map.put("nboardCnt", nboard.getCnt());
            return map;
        }).collect(Collectors.toList());
        return nBoardListmap;
    }
}
