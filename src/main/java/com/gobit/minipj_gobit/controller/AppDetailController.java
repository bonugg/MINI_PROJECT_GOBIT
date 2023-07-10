package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.dto.ApprovalDTO;
import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.repository.ApprovalRepository;
import com.gobit.minipj_gobit.service.ApprovalService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/appDetail")
public class AppDetailController {

    @Autowired
    private HttpSession httpSession;

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
        mv.addObject("approval", approvalDTO);
        return mv;
    }

//    @PutMapping("/approval")

    @PostMapping("/approval")
    public ModelAndView updateApproval(ApprovalDTO approvalDTO) {
        System.out.println("=======================update result=======================");
        System.out.println("approvalDTO 출력 결과:" + approvalDTO);
        ModelAndView mv = new ModelAndView();
        approvalService.updateApproval(approvalDTO.toEntity());
        mv.setViewName("appDetailPage.html");
        return mv;
    }

    @PostMapping("/approval/{appNum}")
    public ModelAndView deleteApproval(@PathVariable long appNum){
        System.out.println("=======================delete result=======================");
        System.out.println("approvalDTO 출력 결과:" + appNum);
//        System.out.println("approvalDTO.getAppNum() 결과: " + approvalDTO.getAppNum());
        ModelAndView mv = new ModelAndView();
        approvalService.deleteApproval(appNum);
        mv.setViewName("appDetailPage.html");
        return mv;
    }





    
}
