package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.Entity.CustomUserDetails;
import com.gobit.minipj_gobit.Entity.User;
import com.gobit.minipj_gobit.dto.PasswordChangeRequestDTO;
import com.gobit.minipj_gobit.dto.ResponseDTO;
import com.gobit.minipj_gobit.repository.UserRepository;
import com.gobit.minipj_gobit.service.MyPageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class MyPageController {
    @Autowired
    private MyPageService myPageService;
    @Autowired
    private HttpSession httpSession;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/myPage")
    public String myPage(Model model){
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("user", myPageService.userget(user.getUSERNUM()));

        return "myPage";
    }

    @GetMapping("/myPageUpdate11")
    public String updateGetMyPage(Model model) {
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("user", myPageService.userget(user.getUSERNUM()));

        return "myPageUpdate";
    }

    @PostMapping("/myPage/update")
    public String updatemyPage(@ModelAttribute User user,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               @RequestParam(value = "USERNUM", required = false) Long userNum,
                               Model model) throws IOException {
        System.out.println(user);

        if (!imageFile.isEmpty()) {

            String fileName = userNum + "_" + imageFile.getOriginalFilename();
            String imagesDirectory = "src/main/resources/static/img/user/";
            Path path = Paths.get(imagesDirectory, fileName);
            try {
                Files.write(path, imageFile.getBytes());

                user.setImagePath("img/user/" + fileName);
            } catch (IOException ex) {
                ex.getStackTrace();
            }
        } else {
            user.setImagePath("img/user/user.png");
        }

        myPageService.updateMyPage(user, userNum, imageFile);

        return "redirect:/myPage";
    }

    @GetMapping("/pwChange")
    public String pwChangeGet() {
        return "pwChange";
    }
    @PostMapping("/originPw-check")
    public ResponseEntity<?> originPwCheck(@RequestParam String encryptedOriginPw,
                                           @RequestParam long usereno) {
        ResponseDTO<Map<String, String>> responseDTO =
                new ResponseDTO<>();

        try {
            // encryptedOriginPw를 인자로 넘깁니다.
            User user = myPageService.originPwCheck(usereno, encryptedOriginPw);

            Map<String, String> returnMap = new HashMap<>();

            // 조건문으로 메시지를 다르게 리턴
            if(user != null) {
                returnMap.put("pwCheckMsg", "pwOk");
            } else {
                returnMap.put("pwCheckMsg", "pwFail");
            }

            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch(Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/pwChange")
    public String changePw(@RequestParam("encryptedOriginPw") String encryptedOriginPw,
                           @RequestParam("NEW_USER_PWD") String newPass,
                           @RequestParam long usereno) {
        boolean result = myPageService.changePw(usereno, encryptedOriginPw, newPass);

        if (result) {
            return "redirect:/loginPage";
        } else {
            return "pwChange";
        }
    }

}
