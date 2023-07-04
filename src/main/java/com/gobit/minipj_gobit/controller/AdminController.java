package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.dto.ResponseDTO;
import com.gobit.minipj_gobit.entity.Calendar;
import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping("/main")
    public ModelAndView admin() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("admin/adminMemberPage");
        return mv;
    }

    @PostMapping("/changePosition")
    public String changePosition(@RequestParam("usernum") long usernum,
                                 @RequestParam("position") String position) {
        if (position.equals(null)) {
            return "실패";
        } else {
            User user = userRepository.findById(usernum).get();
            user.setUSER_POSITION(position);
            userRepository.save(user);
            return "성공";
        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam("usernum") long usernum) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        try {
            User user = userRepository.findById(usernum).get();
            userRepository.delete(user);

            Map<String, String> returnMap = new HashMap<>();
            returnMap.put("msg", "정상적으로 삭제되었습니다.");
            returnMap.put("dept", user.getUSERDEPT());
            responseDTO.setItem(returnMap);
            return ResponseEntity.ok().body(responseDTO);
        }
        catch (Exception e){
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage("삭제 실패");
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/searchDept")
    public List<Map<String, Object>> searchit(@RequestParam("searchText") String searchText
    ,@RequestParam("userdept") String userdept){
        System.out.println(searchText);
        List<User> userList = userRepository.findByUSERNAMEContainingAndUSERDEPT(searchText, userdept);
        List<Map<String, Object>> searchuserListmap = userList.stream().map(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("userNum", user.getUSERNUM());
            map.put("userName", user.getUSERNAME());
            map.put("userEno", user.getUSERENO());
            map.put("userPo", user.getUSER_POSITION());
            map.put("userImage", user.getUSERIMAGE());
            map.put("userExitChk", user.getUSER_EXIT_CHK());
            return map;
        }).collect(Collectors.toList());

        return searchuserListmap;
    }

    @GetMapping("/listDept")
    public List<Map<String, Object>> listIT(@RequestParam("dept") String dept) {
        List<User> userList = userRepository.findByUSERDEPT(dept);
        List<Map<String, Object>> userListmap = userList.stream().map(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("userNum", user.getUSERNUM());
            map.put("userName", user.getUSERNAME());
            map.put("userEno", user.getUSERENO());
            map.put("userPo", user.getUSER_POSITION());
            map.put("userImage", user.getUSERIMAGE());
            map.put("userExitChk", user.getUSER_EXIT_CHK());
            return map;
        }).collect(Collectors.toList());

        return userListmap;
    }
}
