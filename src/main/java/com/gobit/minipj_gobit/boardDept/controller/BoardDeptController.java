package com.gobit.minipj_gobit.boardDept.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/boardDept")
public class BoardDeptController {
    @GetMapping("/list")
    public String list(Model model) {
        return "boardDept/board/boardListPage";
    }
}
