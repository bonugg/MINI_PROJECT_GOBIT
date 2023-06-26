package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.Entity.User;
import com.gobit.minipj_gobit.service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MyPageController {
    @Autowired
    private MyPageService myPageService;

    @GetMapping("/myPage/{USERNO}")
    public String getMyPage(@PathVariable Long USERNO, Model model) {
        User user = myPageService.getPage(USERNO);

        model.addAttribute("user", user);

        return "myPage";
    }

    @GetMapping("/myPageUpdate/{USERNO}")
    public String updateGetMyPage(@PathVariable Long USERNO, Model model) {
//        User user = myPageService.updateMyPage();

        return "myPageUpdate";
    }

    @PostMapping("/myPage/update")
    public String updatemyPage (Long USERNO, User user){
        myPageService.updateMyPage(user, USERNO);

        return "redirect:/myPage";
    }



}
