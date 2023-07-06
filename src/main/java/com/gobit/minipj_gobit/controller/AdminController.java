package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.dto.ResponseDTO;
import com.gobit.minipj_gobit.entity.ApprovalListener;
import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.repository.UserRepository;
import jakarta.persistence.EntityListeners;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@EntityListeners(ApprovalListener.class)
public class AdminController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/main")
    public ModelAndView admin() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("admin/adminMemberPage");
        return mv;
    }

    @PostMapping("/changePosition")
    public String changePosition(@RequestParam("usernum") long usernum,
                                 @RequestParam("position") String position) {
        if (position.equals("")) {
            return "실패";
        } else {
            User user = userRepository.findById(usernum).get();
            user.setUSERPOSITION(position);
            userRepository.save(user);
            return "성공";
        }
    }

    @PostMapping("/chkmodify")
    public String changePositionChk(HttpServletRequest req,
                                    @RequestParam("userposition") String userposition) {
        // ajax를 통해 넘어온 배열 데이터 선언
        String[] usernums = req.getParameterValues("usernums");
        if (userposition.equals("")) {
            return "직급없음";
        }else if(usernums[0].equals("데이터없음")){
            return "체크없음";
        } else {
            for (int i = 0; i< usernums.length; i++){
                User user = userRepository.findById(Long.valueOf(usernums[i])).get();
                if(!(user.getUSERPOSITION().equals(userposition))){
                    user.setUSERPOSITION(userposition);
                    userRepository.save(user);
                }
            }
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
    ,@RequestParam("userdept") String userdept, @RequestParam("searchTag") String searchTag){
        System.out.println(searchText);
        List<User> userList = userRepository.findByUSERNAMEContainingAndUSERPOSITIONContainingAndUSERDEPT(searchText, searchTag, userdept);
        List<Map<String, Object>> searchuserListmap = userList.stream().map(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("userNum", user.getUSERNUM());
            map.put("userName", user.getUSERNAME());
            map.put("userEno", user.getUSERENO());
            map.put("userPo", user.getUSERPOSITION());
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
            map.put("userPo", user.getUSERPOSITION());
            map.put("userImage", user.getUSERIMAGE());
            map.put("userExitChk", user.getUSER_EXIT_CHK());
            return map;
        }).collect(Collectors.toList());

        return userListmap;
    }

    @GetMapping("/detailuser")
    public Map<String, Object> detailuser(@RequestParam("id") long id) {
        User user = userRepository.findById(id).get();
        Map<String, Object> usermap = new HashMap<>();
        usermap.put("userNum", user.getUSERNUM());
        usermap.put("userName", user.getUSERNAME());
        usermap.put("userEno", user.getUSERENO());
        usermap.put("userPo", user.getUSERPOSITION());
        usermap.put("userImage", user.getUSERIMAGE());
        usermap.put("userJoinDate", user.getUSER_JOIN());
        usermap.put("userPhone", user.getUSER_PHONE());
        usermap.put("userDept", user.getUSERDEPT());
        usermap.put("userEmail", user.getUSER_EMAIL());
        usermap.put("userAddress", user.getUSER_ADDRESS());

        return usermap;
    }

    @GetMapping("/changePwd")
    public String changePwd(@RequestParam("id") long id) {
        User user = userRepository.findById(id).get();
        String user_pwd = String.valueOf(user.getUSERENO());
        user.setUSER_PWD(passwordEncoder.encode(user_pwd));
        userRepository.save(user);
        return "성공";
    }
}
