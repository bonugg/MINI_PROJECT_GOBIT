package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.dto.ApprovalDTO;
import com.gobit.minipj_gobit.dto.VacationDTO;
import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.entity.Vacation;
import com.gobit.minipj_gobit.service.ApprovalService;
import com.gobit.minipj_gobit.service.VacationService;
import com.gobit.minipj_gobit.service.impl.VacationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/appDetail")
public class AppDetailController {

    private ApprovalService approvalService;

    private VacationService vacationService;

    @Autowired
    public AppDetailController(ApprovalService approvalService, VacationService vacationService){
        this.approvalService = approvalService;
        this.vacationService = vacationService;
    }
    
    //결재 상세페이지로 이동
    @GetMapping("/{appNum}")
    public ModelAndView getApproval(@PathVariable long appNum) {
        ModelAndView mv = new ModelAndView();
        Approval approval = approvalService.getApproval(appNum);
        Vacation vacation = vacationService.getVacation(approval.getUserNum().getUSERNUM());
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
        VacationDTO vacationDTO = vacation.toDTO();
        mv.addObject("approval", approvalDTO);
        mv.addObject("vacation", vacationDTO);
        return mv;
    }

    @PostMapping("/approval")
    public ModelAndView updateApproval(ApprovalDTO approvalDTO) {
        System.out.println("=======================update result=======================");
        System.out.println("approvalDTO 출력 결과:" + approvalDTO);
        ModelAndView mv = new ModelAndView();

        //repository를 활용해서 Vacation Entity에 접근하여 Vacation Entity의 total을 가져온다.
        //Vacation의 total이 종료일-시작일 보다 크면 appEnd와 appStart에 넣고
        // 그리고 Vacation Entity의 vacTotal에서도 차감하고, vacUsed에 추가한다.
        //만약 Vacation의 total이 종료일-시작일보다 작으면 java에서 html의 alert창 띄우게 하는방법도 찾기

        //requestDuration: 휴가 신청일 수
        Duration requestDuration = Duration.between(approvalDTO.getAppEnd(), approvalDTO.getAppStart());
        long RequestSecond = requestDuration.getSeconds();
        System.out.println("requestDuration 결과: " + requestDuration);
        System.out.println("신청 휴가 second: " + requestDuration.getSeconds());

        //다시 수정하기
        vacationService.updateVacation(RequestSecond);
//        approvalService.updateApproval(approvalDTO.toEntity());
//        vacationService.updateVacation(approvalDTO.getAppStart(), approvalDTO.getAppEnd());
//        mv.addObject(approvalDTO);
//        mv.setViewName("appDetailPage.html");
        mv.setViewName("appVacationDetail.html");
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
