package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.Entity.User;
import com.gobit.minipj_gobit.Entity.Calendar;
import com.gobit.minipj_gobit.Entity.UserOnOff;
import com.gobit.minipj_gobit.repository.CalendarRepository;
import com.gobit.minipj_gobit.repository.UserOnOffRepository;
import com.gobit.minipj_gobit.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class HomeController {
    @Autowired
    private HttpSession httpSession;
    private final UserRepository userRepository;
    private final UserOnOffRepository userOnOffRepository;
    private final CalendarRepository calendarRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/")
    public String homePage(Model model) throws ParseException {
        User user = (User) httpSession.getAttribute("user");
        LocalDate currentDate = LocalDate.now(); // 현재 날짜 가져오기
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 출력 형식 지정
        String formattedDate = currentDate.format(format); // 현재 날짜를 지정된 형식으로 변환

        SimpleDateFormat startEndDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        List<User> userList = userRepository.findByUSERDEPT(user.getUSERDEPT());
        List<Integer> useroncheck = new ArrayList<>();
        List<Integer> useroffcheck = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            useroncheck.add(i, 0);
            for (int j = 0; j < userList.get(i).getUserOnOffList().size(); j++) {
                if (userList.get(i).getUserOnOffList().get(j).getSTART().contains(formattedDate)) {
                    Date startDate = startEndDateFormat.parse(userList.get(i).getUserOnOffList().get(j).getSTART());
                    java.util.Calendar startDateCalendar = java.util.Calendar.getInstance();
                    startDateCalendar.setTime(startDate);
                    if (startDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) < 9) {
                        useroncheck.set(i, 1);
                    }else if (startDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) >= 9) {
                        useroncheck.set(i, 2);
                    }
                }
            }
        }
        for (int i = 0; i < userList.size(); i++) {
            useroffcheck.add(i, 0);
            for (int j = 0; j < userList.get(i).getUserOnOffList().size(); j++) {
                if (userList.get(i).getUserOnOffList().get(j).getEND().contains(formattedDate)) {
                    Date startDate = startEndDateFormat.parse(userList.get(i).getUserOnOffList().get(j).getEND());
                    java.util.Calendar endDateCalendar = java.util.Calendar.getInstance();
                    endDateCalendar.setTime(startDate);
                    if (endDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) >= 18) {
                        useroffcheck.set(i, 1);
                    }else if (endDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) < 18) {
                        useroffcheck.set(i, 2);
                    }
                }
            }
        }

        List<Map<String, Object>> combinedList = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("user", userList.get(i));
            dataMap.put("oncheck", useroncheck.get(i));
            dataMap.put("offcheck", useroffcheck.get(i));
            combinedList.add(dataMap);
        }
        model.addAttribute("userMap", combinedList);
        model.addAttribute("abc","abc");
        return "mainPage";
    }
    @GetMapping("/empty")
    public String empty(){
        return "emptyPage";
    }
    @GetMapping("/login")
    public String loginPage(){
        return "loginPage";
    }
    @GetMapping("/signup")
    public String signupPage(){
        return "signupPage";
    }
    @PostMapping("/signup")
    public String sginupMember(User user) {
        user.setUSER_PWD(passwordEncoder.encode(user.getUSER_PWD()));
        userRepository.save(user);
        return "redirect:/login";
    }

    @ResponseBody
    @RequestMapping(value = "/onadd", method = RequestMethod.POST)
    public String onadd(HttpServletRequest request) throws Exception {
        UserOnOff userOnOff = new UserOnOff();
        Calendar calendar = new Calendar();
        User user = (User) httpSession.getAttribute("user");
        Date today = new Date();
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date inputDate = inputDateFormat.parse(String.valueOf(today));
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String outputDateString = outputDateFormat.format(inputDate);

        userOnOff.setUser(user);
        userOnOff.setSTART(outputDateString);
        userOnOff.setEND("0");
        calendar.setUser(user);
        calendar.setCALTYPE("출퇴근");
        calendar.setCALSTART(outputDateString);
        calendar.setCALEND("0");
        userOnOffRepository.save(userOnOff);
        calendarRepository.save(calendar);

        return outputDateString;
    }

    @ResponseBody
    @RequestMapping(value = "/offadd", method = RequestMethod.POST)
    public String offadd(@RequestParam("start") String start) throws Exception {
        System.out.println(start);
        User user = (User) httpSession.getAttribute("user");
        UserOnOff userOnOff = userOnOffRepository.findByUSERANDSTART(user.getUSERNUM(),start).get();
        Calendar calendar = calendarRepository.findByUNandCS(user.getUSERNUM(),start).get();
        System.out.println(calendar + "---------------");
        Date today = new Date();
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        Date inputDate = inputDateFormat.parse(String.valueOf(today));
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

        int starthours = startDate.getHours();
        int endthours = endDate.getHours();
        userOnOff.setCOMMUTETIME(endthours-starthours);

        if (startDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) < 9 && endDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) >= 18) {
            userOnOff.setCOMMUTETYPE("출석");
            calendar.setCALTITLE("출석");
        }else if (startDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) < 9 && endDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) < 18) {
            userOnOff.setCOMMUTETYPE("조퇴");
            calendar.setCALTITLE("조퇴");
        }else if (startDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) >= 9 && endDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) >= 18) {
            userOnOff.setCOMMUTETYPE("지각");
            calendar.setCALTITLE("지각");
        }else {
            userOnOff.setCOMMUTETYPE("결석");
            calendar.setCALTITLE("결석");
        }
        userOnOffRepository.save(userOnOff);
        calendarRepository.save(calendar);

        return outputDateString;
    }
    @ResponseBody
    @RequestMapping(value = "/onaddcheck", method = RequestMethod.POST) //좋아요 체크 및 좋아요 수
    public String onaddcheck(@RequestParam("start") String start) throws Exception {
        User user = (User) httpSession.getAttribute("user");
        System.out.println(start);
        String startdate = userOnOffRepository.findByCLASSIFYANDSTART(user.getUSERNUM(),start);
        System.out.println(startdate);
        return startdate;
    }

    @ResponseBody
    @RequestMapping(value = "/offaddcheck", method = RequestMethod.POST) //좋아요 체크 및 좋아요 수
    public String offaddcheck(@RequestParam("end") String end) throws Exception {
        User user = (User) httpSession.getAttribute("user");
        String enddate = userOnOffRepository.findByCLASSIFYANDEND(user.getUSERNUM(),end);
        return enddate;
    }
    @ResponseBody
    @RequestMapping(value = "/chart", method = RequestMethod.POST) //좋아요 체크 및 좋아요 수
    public Map<String, Integer> chart() throws Exception {
        User user = (User) httpSession.getAttribute("user");
        LocalDate today = LocalDate.now();

        String yearMonth = today.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String year = today.format(DateTimeFormatter.ofPattern("yyyy"));
        int yearMonthChart = userOnOffRepository.findByYearMonthChart(yearMonth, user.getUSERNUM());
        int yearChart =userOnOffRepository.findByYearMonthChart(year, user.getUSERNUM());

        Map<String, Integer> result = new HashMap<>();
        result.put("yearMonthChart", yearMonthChart);
        result.put("yearChart", yearChart);

        return result;
    }
}
