package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.Entity.User;
import com.gobit.minipj_gobit.Entity.Calendar;
import com.gobit.minipj_gobit.Entity.UserOnOff;
import com.gobit.minipj_gobit.repository.CalendarRepository;
import com.gobit.minipj_gobit.repository.UserOnOffRepository;
import com.gobit.minipj_gobit.repository.UserRepository;
import com.gobit.minipj_gobit.service.MainPageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        User user = (User) httpSession.getAttribute("user");
        if (user.getUSER_POSITION().equals("관리자")){
            return "redirect:/memberSign";
        }
        model.addAttribute("userMap", mainPageService.UserOnOffList(userNum));
        return "mainPage";
    }

    //(06.20 17:26) 결재 요청 리스트 페이지로 이동
    @GetMapping("/approvalList")
    public String approvalList(){
        return "approvalPage";
    }

    @GetMapping("/appDetail")
    public String approvalDetail(){
        return "appDetailPage";
    }
    @GetMapping("/empty")
    public String empty(){
        return "appBuisness";
    }
    @GetMapping("/login")
    public String loginPage(){
        if (httpSession.getAttribute("user") != null) {
            return "mainPage";
        }
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

    @GetMapping("/appBuisness-view")
    public ModelAndView appBuisnessView() {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("appBuisness.html");

        return mv;
    }

    @GetMapping("/appMeeting-view")
    public ModelAndView appMeetingView() {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("appMeeting.html");

        return mv;
    }

    @GetMapping("/appVacation-view")
    public ModelAndView appVacationView() {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("appVacation.html");

        return mv;
    }

    @GetMapping("/memberSign")
    public String memberSign(){
        return "AdminPage";
    }
    @PostMapping("/memberSign")
    public String memberSignPost(){
        return "AdminPage";
    }
}
