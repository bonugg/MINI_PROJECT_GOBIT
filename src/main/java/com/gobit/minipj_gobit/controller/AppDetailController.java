package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.dto.ApprovalDTO;
import com.gobit.minipj_gobit.dto.ResponseDTO;
import com.gobit.minipj_gobit.dto.VacationDTO;
import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.entity.Vacation;
import com.gobit.minipj_gobit.service.ApprovalService;
import com.gobit.minipj_gobit.service.VacationService;
import com.gobit.minipj_gobit.service.impl.VacationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/meeting/{appNum}")
    public ModelAndView deleteMeeting(@PathVariable long appNum){
        System.out.println("=======================meeting approval delete result=======================");
        System.out.println("approvalDTO 출력 결과:" + appNum);
//        System.out.println("approvalDTO.getAppNum() 결과: " + approvalDTO.getAppNum());
        ModelAndView mv = new ModelAndView();
        approvalService.deleteApproval(appNum);
        mv.setViewName("appDetailPage.html");
        return mv;
    }

    @PostMapping("/buisness/{appNum}")
    public ModelAndView deleteBuisness(@PathVariable long appNum){
        System.out.println("=======================buisness approval delete result=======================");
        System.out.println("approvalDTO 출력 결과:" + appNum);
//        System.out.println("approvalDTO.getAppNum() 결과: " + approvalDTO.getAppNum());
        ModelAndView mv = new ModelAndView();
        approvalService.deleteApproval(appNum);
        mv.setViewName("appDetailPage.html");
        return mv;
    }

    @PostMapping("/vacation/{appNum}")
    @ResponseBody
    public ResponseEntity<?> deleteVacation(@PathVariable long appNum){
        System.out.println("=======================vacation approval delete result=======================");
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<Map<String, String>>();
        Map<String, String> returnMap = new HashMap<String, String>();
        long userNum = approvalService.getApproval(appNum).getUserNum().getUSERNUM();

        long appVacReq = approvalService.getAppVacReq(appNum);
        long vacUsed = vacationService.getVacUsed(userNum);
        long vacTotal = vacationService.getVacTotal(userNum);
        long vacLeft = vacationService.getVacLeft(userNum);
        System.out.println("신청한 휴가일수: " + appVacReq);
        System.out.println("기존 연차 사용일수: " + vacUsed);
        System.out.println("기존 연차 잔여일수: " + vacLeft);

        try{
            vacUsed -= appVacReq;
            vacLeft += appVacReq;
            System.out.println("결재 삭제 시 연차 사용일: " + vacUsed);
            System.out.println("결재 삭제 시 잔여 연차일: " + vacLeft);
            approvalService.deleteApproval(appNum);
            vacationService.saveVacation(vacUsed, vacLeft, userNum);
            returnMap.put("msg", "휴가 결재가 삭제되었습니다.");
            returnMap.put("result", "success");
            returnMap.put("redirectUrl", "/appDetail");
            System.out.println(appNum + "번 휴가 결재가 삭제됨");

            responseDTO.setItem(returnMap);
            return ResponseEntity.ok().body(responseDTO);


        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    
}
