package com.gobit.minipj_gobit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticeController {

    @GetMapping("/notice")
    public String getNotice() {
        return "emptyPage.html";
    }
}
