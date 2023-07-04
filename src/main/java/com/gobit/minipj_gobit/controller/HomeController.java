package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.repository.UserRepository;
import com.gobit.minipj_gobit.service.MainPageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;

@Controller
@RequiredArgsConstructor
public class HomeController {
    //테스트
    @Autowired
    private HttpSession httpSession;
    private final MainPageService mainPageService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/")
    public String homePage(Model model, Principal principal) throws ParseException {
        long userNum = Long.parseLong(principal.getName());
        User user = userRepository.findByUSERENO(userNum).get();
        model.addAttribute("user", user);
        model.addAttribute("userMap", mainPageService.UserOnOffList(userNum));
        return "mainPage";
    }

    @GetMapping("/empty")
    public String empty() {
        return "emptyPage";
    }


    //(06.20 17:26) 결재 요청 리스트 페이지로 이동
    @GetMapping("/approvalList")
    public String approvalList() {
        return "approvalPage";
    }

    @GetMapping("/appDetail")
    public String approvalDetail() {
        return "appDetailPage";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "exception", required = false) String exception,
                            Model model) {
        if (httpSession.getAttribute("user") != null) {
            User user = (User) httpSession.getAttribute("user");
            if (user.getUSERPOSITION().equals("관리자")) {
                return "redirect:/admin/main";
            } else {
                return "redirect:/";
            }
        }
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "loginPage";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signupPage";
    }

    @PostMapping("/signup")
    public String sginupMember(User user) {
        user.setUSER_PWD(passwordEncoder.encode(user.getUSER_PWD()));
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/appBuisness")
    public String appBuisness() {
        return "appBuisness.html";
    }

    @GetMapping("/appMeeting")
    public String appMeeting() {
        return "appMeeting.html";
    }

    @GetMapping("/appVacation")
    public String appVacation() {
        return "appVacation.html";
    }


}
