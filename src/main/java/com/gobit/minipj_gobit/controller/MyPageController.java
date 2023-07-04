package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.entity.CustomUserDetails;
import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.dto.PasswordChangeRequestDTO;
import com.gobit.minipj_gobit.dto.ResponseDTO;
import com.gobit.minipj_gobit.repository.UserRepository;
import com.gobit.minipj_gobit.service.MyPageService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@Slf4j
public class MyPageController {
    @Value("${file.path}")
    private String[] filePaths;

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
            File directory = new File(filePaths[1]);
            if(!directory.exists()) {
                directory.mkdir();
            }
            File uploadFile = new File(filePaths[1] + fileName);
            imageFile.transferTo(uploadFile);
                user.setImagePath(filePaths[1]);
                user.setUSERIMAGE(fileName);
            System.out.println(user.getUSERNAME());
        } else {
            user.setImagePath(filePaths[1]);
        }

        myPageService.updateMyPage(user, userNum, imageFile);

        return "redirect:/myPage";
    }


    @GetMapping("/pwChange")
    public String pwChangeGet() {
        return "pwChange";
    }

    @ResponseBody
    @PostMapping("/changePw")
    public ResponseEntity<?> changePw (@RequestBody PasswordChangeRequestDTO pcrDTO, HttpSession session) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        System.out.printf(pcrDTO.getOriginPw());
        System.out.printf(String.valueOf(pcrDTO.getUSERNUM()));
        System.out.printf(pcrDTO.getUSER_PWD());


        try {
            User user = myPageService.findById(pcrDTO.getUSERNUM());

            Map<String, String> returnMap = new HashMap<>();

            if (!passwordEncoder.matches(pcrDTO.getOriginPw(), user.getUSER_PWD())) {
                returnMap.put("originPwCheckMsg", "wrongPw");
            } else {
                User checkPw = null;

                if(pcrDTO.getOriginPw() == null || pcrDTO.getOriginPw().equals("")) {
                    checkPw = User.builder()
                            .USERNUM(pcrDTO.getUSERNUM())
                            .USER_PWD(passwordEncoder.encode(pcrDTO.getOriginPw())).build();

                } else {
                    checkPw = User.builder()
                            .USERNUM(pcrDTO.getUSERNUM())
                            .USER_PWD(passwordEncoder.encode(pcrDTO.getUSER_PWD()))
                            .build();
                }

                myPageService.changePw(pcrDTO);

                UserDetails userDetails = myPageService.loadUserById(pcrDTO.getUSERNUM());

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContext securityContext =
                        SecurityContextHolder.getContext();

                securityContext.setAuthentication(authentication);

                session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

                returnMap.put("originPwCheckMsg", "pwOK");
            }

                responseDTO.setItem(returnMap);
                responseDTO.setStatusCode(HttpStatus.OK.value());

                return ResponseEntity.ok().body(responseDTO);
        } catch (IllegalArgumentException e) {
        responseDTO.setErrorMessage(e.getMessage());
        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(responseDTO);
        } catch (NoSuchElementException e) {
        responseDTO.setErrorMessage(e.getMessage());
        responseDTO.setStatusCode(HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        } catch (Exception e) {
        responseDTO.setErrorMessage(e.getMessage());
        responseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }
    }


}
