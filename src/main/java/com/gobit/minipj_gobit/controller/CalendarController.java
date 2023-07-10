package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.entity.Calendar;
import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.repository.CalendarRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main")
public class CalendarController {
    @Autowired
    private HttpSession httpSession;
    private final CalendarRepository calendarRepository;

    @RequestMapping(value = "/calendar", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> monthPlan() {
        User user = (User) httpSession.getAttribute("user");
        List<Calendar> calendarList = calendarRepository.findByCalList(user.getUSERDEPT());
        List<Map<String, Object>> mapList = calendarList.stream().map(calendar -> {
            Map<String, Object> map = new HashMap<>();
            map.put("no", calendar.getUser().getUSERNUM());
            map.put("title", calendar.getCALTITLE());
            map.put("description", calendar.getCALCONTENT());
            map.put("start", calendar.getCALSTART());
            map.put("end", calendar.getCALEND());
            map.put("classify", calendar.getCALTYPE());
            if(calendar.getApproval() != null){
                map.put("vacationtype", calendar.getApproval().getAppVacType());
                map.put("applocate", calendar.getApproval().getAppLocation());
                map.put("meetingPT", calendar.getApproval().getAppParticipant());
                map.put("approvedName", calendar.getApproval().getAppUserNum().getUSERNAME());
            }else {
                map.put("vacationtype", null);
                map.put("applocate", null);
                map.put("meetingPT", null);
                map.put("approvedName", null);
            }
            return map;
        }).collect(Collectors.toList());

        List<Map<String, Object>> list = mapList;

        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        HashMap<String, Object> hash = new HashMap<String, Object>();

        for (int i = 0; i < list.size(); i++) {
            hash.put("no", list.get(i).get("no")); //제목
            hash.put("title", list.get(i).get("title")); //
            hash.put("description", list.get(i).get("description")); //설명
            hash.put("start", list.get(i).get("start")); //시작일자
            hash.put("end", list.get(i).get("end")); //종료일자
            hash.put("classify", list.get(i).get("classify")); //구분
            hash.put("vacationtype", list.get(i).get("vacationtype")); //구분
            hash.put("applocate", list.get(i).get("applocate")); //구분
            hash.put("meetingPT", list.get(i).get("meetingPT")); //구분
            hash.put("approvedName", list.get(i).get("approvedName")); //구분

            jsonObj = new JSONObject(hash);
            jsonArr.add(jsonObj);
        }
        return jsonArr;
    }
}
