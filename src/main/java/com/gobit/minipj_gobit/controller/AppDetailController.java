package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.dto.ApprovalDTO;
import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/appDetail")
public class AppDetailController {

    private ApprovalService approvalService;

    @Autowired
    public AppDetailController(ApprovalService approvalService){
        this.approvalService = approvalService;
    }
    
    //결재 상세페이지로 이동
    @GetMapping("/{appNum}")
    public ModelAndView getApproval(@PathVariable long appNum) {
        ModelAndView mv = new ModelAndView();
        Approval approval = approvalService.getApproval(appNum);
        System.out.println("<<<----------------------체크---------------------->>>");
        System.out.println(approval.getAppSort());
        if(approval.getAppSort() == 'M'){
            mv.setViewName("appMeetingDetail.html");
        }
        else if(approval.getAppSort() == 'V'){
            mv.setViewName("appVacationDetail.html");
        }else if(approval.getAppSort() == 'B'){
            mv.setViewName("appBuisnessDetail.html");
        }else{
            System.out.println("다음 종류를 찾지 못했습니다.");
        }
        ApprovalDTO approvalDTO = approval.toDTO();
        System.out.println("===========approvalDTO 정보 출력===========");
        System.out.println(approvalDTO);
        mv.addObject("approval", approvalDTO);
        return mv;
    }

    @PostMapping("/approval")
    public ModelAndView updateApproval(ApprovalDTO approvalDTO) {
        ModelAndView mv = new ModelAndView();
        approvalService.updateApproval(approvalDTO.toEntity());
        mv.setViewName("appDetailPage.html");
        return mv;
    }

    @DeleteMapping("/approval")
    public ModelAndView deleteApproval(long appNum){
        ModelAndView mv = new ModelAndView();
        approvalService.deleteApproval(appNum);
        mv.setViewName("appDetailPage.html");
        return mv;
    }

    
}
