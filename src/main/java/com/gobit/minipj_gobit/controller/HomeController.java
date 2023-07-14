package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.dto.VacationDTO;
import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.entity.Vacation;
import com.gobit.minipj_gobit.repository.UserRepository;
import com.gobit.minipj_gobit.service.MainPageService;
import com.gobit.minipj_gobit.service.VacationService;
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

@Controller
@RequiredArgsConstructor
public class HomeController {
    //테스트
    @Autowired
    private HttpSession httpSession;
    private final MainPageService mainPageService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final VacationService vacationService;

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
    public ModelAndView appVacation() {
        ModelAndView mv = new ModelAndView();
        User user = (User) httpSession.getAttribute("user");
        mv.setViewName("appVacation.html");

        Vacation vacation = vacationService.getVacation(user.getUSERNUM());
        mv.addObject("vacation", vacation.toDTO());
        return mv;
    }


}
