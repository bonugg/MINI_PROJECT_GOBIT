package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.Entity.Calendar;
import com.gobit.minipj_gobit.Entity.User;
import com.gobit.minipj_gobit.Entity.UserOnOff;
import com.gobit.minipj_gobit.repository.CalendarRepository;
import com.gobit.minipj_gobit.repository.UserOnOffRepository;
import com.gobit.minipj_gobit.repository.UserRepository;
import com.gobit.minipj_gobit.service.MainPageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MainApiController {
    @Autowired
    private HttpSession httpSession;
    private final UserOnOffRepository userOnOffRepository;
    private final CalendarRepository calendarRepository;

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
        calendar.setUser(user);
        calendar.setCALTYPE("출퇴근");
        calendar.setCALSTART(outputDateString);
        calendar.setCALEND("0");
        userOnOffRepository.save(userOnOff);
        userOnOffRepository.flush();
        calendarRepository.save(calendar);
        calendarRepository.flush();

        return outputDateString;
    }

    @PostMapping(value = "/offadd")
    public String offadd(@RequestParam("start") String start) throws Exception {
        User user = (User) httpSession.getAttribute("user");
        UserOnOff userOnOff = userOnOffRepository.findByUSERANDSTART(user.getUSERNUM(),start).get();
        Calendar calendar = calendarRepository.findByUNandCS(user.getUSERNUM(),start).get();
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
        }else if (startDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) < 9 && endDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) < 18) {
            userOnOff.setCOMMUTETYPE("조퇴");
            calendar.setCALTITLE("조퇴");
        }else if (startDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) >= 9 && endDateCalendar.get(java.util.Calendar.HOUR_OF_DAY) >= 18) {
            userOnOff.setCOMMUTETYPE("지각");
            calendar.setCALTITLE("지각");
        }else {
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
        String startdate = userOnOffRepository.findByCLASSIFYANDSTART(user.getUSERNUM(),start);
        return startdate;
    }

    @PostMapping(value = "/offaddcheck") //좋아요 체크 및 좋아요 수
    public String offaddcheck(@RequestParam("end") String end) throws Exception {
        User user = (User) httpSession.getAttribute("user");
        String enddate = userOnOffRepository.findByCLASSIFYANDEND(user.getUSERNUM(),end);
        return enddate;
    }

    @PostMapping(value = "/chart") //좋아요 체크 및 좋아요 수
    public Map<String, Double> chart() {
        User user = (User) httpSession.getAttribute("user");
        LocalDate today = LocalDate.now();

        String yearMonth = today.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String year = today.format(DateTimeFormatter.ofPattern("yyyy"));
        Double yearMonthChart = userOnOffRepository.findByYearMonthChart(yearMonth, user.getUSERNUM());
        Double yearChart =userOnOffRepository.findByYearMonthChart(year, user.getUSERNUM());
        double yearMonthChartValue = (yearMonthChart != null) ? yearMonthChart : 0.0;
        double yearChartValue = (yearChart != null) ? yearChart : 0.0;

        Map<String, Double> result = new HashMap<>();
        result.put("yearMonthChart", yearMonthChartValue);
        result.put("yearChart", yearChartValue);

        return result;
    }
}
