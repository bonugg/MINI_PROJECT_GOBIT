package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.dto.ApprovalDTO;
import com.gobit.minipj_gobit.dto.ResponseDTO;
import com.gobit.minipj_gobit.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/appRequest")
public class AppRequestController {

    ApprovalService approvalService;

    @Autowired
    public AppRequestController(ApprovalService approvalService){
        this.approvalService =approvalService;
    }

    //휴가, 출장, 회의 결재신청
    @PostMapping("/approval")
    public ModelAndView saveApproval(ApprovalDTO approvalDTO){
        System.out.println("=======================save result=======================");
        System.out.println("approvalDTO 출력 결과:" + approvalDTO);
        ModelAndView mv =new ModelAndView();
        approvalService.saveApproval(approvalDTO.toEntity());
        mv.setViewName("/appDetailPage.html");
        return mv;

    }

}
