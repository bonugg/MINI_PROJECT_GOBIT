package com.gobit.minipj_gobit.service;

import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.repository.CalendarRepository;
import com.gobit.minipj_gobit.repository.UserOnOffRepository;
import com.gobit.minipj_gobit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MainPageService {
    private final UserRepository userRepository;
    private final UserOnOffRepository userOnOffRepository;
    private final CalendarRepository calendarRepository;

    public List<Map<String, Object>> UserOnOffList(long userNum) throws ParseException {
        User user = userRepository.findByUSERENO(userNum).get();
        LocalDate currentDate = LocalDate.now(); // 현재 날짜 가져오기
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 출력 형식 지정
        String formattedDate = currentDate.format(format); // 현재 날짜를 지정된 형식으로 변환

        SimpleDateFormat startEndDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        List<User> userList = userRepository.findByUSERDEPT(user.getUSERDEPT());
        List<Integer> useroncheck = new ArrayList<>();
        List<Integer> useroffcheck = new ArrayList<>();
        List<String> userontime = new ArrayList<>();
        List<String> userofftime = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            useroncheck.add(i, 0);
            userontime.add(i, "");
            for (int j = 0; j < userList.get(i).getUserOnOffList().size(); j++) {
                if (userList.get(i).getUserOnOffList().get(j).getSTART().contains(formattedDate)) {
                    Date startDate = startEndDateFormat.parse(userList.get(i).getUserOnOffList().get(j).getSTART());
                    userontime.set(i, userList.get(i).getUserOnOffList().get(j).getSTART());
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
            userofftime.add(i,"");
            for (int j = 0; j < userList.get(i).getUserOnOffList().size(); j++) {
                if (userList.get(i).getUserOnOffList().get(j).getEND().contains(formattedDate)) {
                    Date endDate = startEndDateFormat.parse(userList.get(i).getUserOnOffList().get(j).getEND());
                    userofftime.set(i, userList.get(i).getUserOnOffList().get(j).getEND());
                    java.util.Calendar endDateCalendar = java.util.Calendar.getInstance();
                    endDateCalendar.setTime(endDate);
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
            dataMap.put("userontime", userontime.get(i));
            dataMap.put("userofftime", userofftime.get(i));
            dataMap.put("oncheck", useroncheck.get(i));
            dataMap.put("offcheck", useroffcheck.get(i));
            combinedList.add(dataMap);
        }

        return combinedList;
    }
}
