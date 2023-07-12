package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.dto.ApprovalDTO;
import com.gobit.minipj_gobit.dto.ResponseDTO;
import com.gobit.minipj_gobit.dto.VacationDTO;
import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.entity.Vacation;
import com.gobit.minipj_gobit.service.ApprovalService;
import com.gobit.minipj_gobit.service.VacationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/appDetail")
public class AppDetailController {

    @Autowired
    private HttpSession httpSession;

    private ApprovalService approvalService;

    private VacationService vacationService;

    @Autowired
    public AppDetailController(ApprovalService approvalService, VacationService vacationService) {
        this.approvalService = approvalService;
        this.vacationService = vacationService;
    }

    //결재 상세페이지로 이동
    @GetMapping("/{appNum}")
    public ModelAndView getApproval(@PathVariable long appNum) {
        ModelAndView mv = new ModelAndView();
        Approval approval = approvalService.getApproval(appNum);
        System.out.println("회원번호: " + approval.getUserNum().getUSERNUM());
        System.out.println("vacation 결과: " + vacationService.getVacation(approval.getUserNum().getUSERNUM()));
        Vacation vacation = vacationService.getVacation(approval.getUserNum().getUSERNUM());
        if(approval.getAppSort().equals("M")){
            mv.setViewName("appMeetingDetail.html");
        }
        else if(approval.getAppSort().equals("V")){
            mv.setViewName("appVacationDetail.html");
        }else if(approval.getAppSort().equals("B")){
            mv.setViewName("appBuisnessDetail.html");
        } else {
            System.out.println("다음 종류를 찾지 못했습니다.");
        }
        //알람값 설정
        if(approval.getAppState() != "미승인"){
            approvalService.updateAlarm(1, appNum);
            Approval newApproval = approvalService.getApproval(appNum);
            ApprovalDTO approvalDTO = newApproval.toDTO();
            mv.addObject("approval", approvalDTO);
        }
        ApprovalDTO approvalDTO = approval.toDTO();
        VacationDTO vacationDTO = vacation.toDTO();
        mv.addObject("approval", approvalDTO);
        mv.addObject("vacation", vacationDTO);
        return mv;
    }

    @PostMapping("/buisness")
    @ResponseBody
    public ResponseEntity<?> updateBuisness(ApprovalDTO approvalDTO) {
        System.out.println("=======================buisness approval update result=======================");
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<Map<String, String>>();
        Map<String, String> returnMap = new HashMap<String, String>();

        LocalDateTime appStart = approvalDTO.getAppStart();
        LocalDateTime appEnd = approvalDTO.getAppEnd();
        System.out.println("수정한 출장 시작일: " + appStart);
        System.out.println("수정한 출장 종료일: " + appEnd);

        try {
            if (appStart.isBefore(appEnd)) {
                approvalService.saveApproval(approvalDTO.toEntity());
                returnMap.put("msg", "출장 결재가 수정되었습니다");
                returnMap.put("result", "success");
                returnMap.put("redirectUrl", "/appDetail");
                System.out.println("출장 수정됨");
            } else {
                returnMap.put("msg", "출장 시작일과 출장 종료일을 다시 입력해주세요");
                returnMap.put("result", "fail");
                System.out.println("출장 날짜 입력 오류로 출장 결재 신청되지 않음");
            }
            responseDTO.setItem(returnMap);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    @PostMapping("/meeting")
    @ResponseBody
    public ResponseEntity<?> updateMeeting(ApprovalDTO approvalDTO) {
        System.out.println("=======================meeting approval update result=======================");
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<Map<String, String>>();
        Map<String, String> returnMap = new HashMap<String, String>();

        LocalDateTime appStart = approvalDTO.getAppStart();
        LocalDateTime appEnd = approvalDTO.getAppEnd();
        System.out.println("수정한 회의 시작일: " + appStart);
        System.out.println("수정한 회의 종료일: " + appEnd);

        try {
            if (appStart != null && appEnd != null && appStart.isBefore(appEnd)) {
                approvalService.saveApproval(approvalDTO.toEntity());
                returnMap.put("msg", "회의 결재가 수정되었습니다");
                returnMap.put("result", "success");
                returnMap.put("redirectUrl", "/appDetail");
                System.out.println("회의 수정됨");
            } else {
                returnMap.put("msg", "회의 시작일과 회의 종료일을 다시 입력해주세요");
                returnMap.put("result", "fail");
                System.out.println("회의 날짜 입력 오류로 회의 결재 신청되지 않음");
            }
            responseDTO.setItem(returnMap);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    //        vacationService.updateVacation(RequestSecond);
    @PostMapping("/vacation")
    @ResponseBody
    public ResponseEntity<?> updateVacation(ApprovalDTO approvalDTO) {
        System.out.println("=======================vacation approval update result=======================");
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<Map<String, String>>();
        Map<String, String> returnMap = new HashMap<String, String>();

        LocalDateTime appStart = approvalDTO.getAppStart();
        LocalDateTime appEnd = approvalDTO.getAppEnd();
        LocalDate appStartDay = approvalDTO.getAppStartDay();
        LocalDate appEndDay = approvalDTO.getAppEndDay();
        boolean isDateFormatOk = false;
        long userNum = approvalDTO.getUserNum().getUSERNUM();
        long appNum = approvalDTO.getAppNum();

        if(appStart != null && appEnd != null){
            System.out.println("수정한 휴가 시작일(시간단위): " + appStart);
            System.out.println("수정한 휴가 종료일(시간단위): " + appStart);
            if(appStart.isBefore(appEnd)){
                isDateFormatOk = true;
            }
        }else{
            System.out.println("수정한 휴가 시작일(하루단위): " + appStartDay);
            System.out.println("수정한 휴가 종료일(하루단위): " + appEndDay);
            if(appStartDay.isBefore(appEndDay)){
                isDateFormatOk = true;
            }
        }

        long newVacReq = approvalDTO.getAppVacReq();
        long appVacReq = approvalService.getAppVacReq(appNum);
        long vacUsed = vacationService.getVacUsed(userNum);
        long vacLeft = vacationService.getVacLeft(userNum);
        long vacTotal = vacationService.getVacTotal(userNum);
        System.out.println("수정한 휴가일수: " + newVacReq);

        try {
            if (isDateFormatOk != true) {
                returnMap.put("msg", "휴가 시작일과 휴가의 종료일을 다시 입력해주세요");
                returnMap.put("result", "fail");
                System.out.println("휴가 날짜 입력 오류로 휴가 결재 신청되지 않음");
            } else {
                if(vacLeft + appVacReq > newVacReq){
                    //복구
                    approvalService.updateApproval(approvalDTO.toEntity());
                    vacUsed -= appVacReq;
                    vacLeft += appVacReq;
                    //수정 반영
                    vacUsed += newVacReq;  //연차 사용일 증가
                    vacLeft = vacTotal - vacUsed; //잔여 연차 차감
                    System.out.println("결재 수정 시 연차 사용일: " + vacUsed);
                    System.out.println("결재 수정 시 잔여 연차일: " + vacLeft);
                    vacationService.saveVacation(vacUsed, vacLeft, userNum);
                    returnMap.put("msg", "휴가 결재가 수정되었습니다");
                    returnMap.put("result", "success");
                    returnMap.put("redirectUrl", "/appDetail");
                    System.out.println(appNum + "번 휴가 결재가 수정됨");
                } else {
                    returnMap.put("msg", "연차 잔여일이 부족합니다.");
                    returnMap.put("result", "fail");
                    System.out.println("잔여 연차 부족으로 결재 수정되지 않음");
                }
            }
            responseDTO.setItem(returnMap);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    @PostMapping("/meeting/{appNum}")
    public ModelAndView deleteMeeting(@PathVariable long appNum) {
        System.out.println("=======================meeting approval delete result=======================");
        System.out.println("삭제한 회의결재 번호:" + appNum);
        ModelAndView mv = new ModelAndView();
        approvalService.deleteApproval(appNum);
        mv.setViewName("appDetailPage.html");
        return mv;
    }

    @PostMapping("/buisness/{appNum}")
    public ModelAndView deleteBuisness(@PathVariable long appNum) {
        System.out.println("=======================buisness approval delete result=======================");
        System.out.println("삭제한 출장결재 번호:" + appNum);
        ModelAndView mv = new ModelAndView();
        approvalService.deleteApproval(appNum);
        mv.setViewName("appDetailPage.html");
        return mv;
    }

    @PostMapping("/vacation/{appNum}")
    @ResponseBody
    public ResponseEntity<?> deleteVacation(@PathVariable long appNum) {
        System.out.println("=======================vacation approval delete result=======================");
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<Map<String, String>>();
        Map<String, String> returnMap = new HashMap<String, String>();
        long userNum = approvalService.getApproval(appNum).getUserNum().getUSERNUM();

        long appVacReq = approvalService.getAppVacReq(appNum);
        long vacUsed = vacationService.getVacUsed(userNum);
        long vacTotal = vacationService.getVacTotal(userNum);
        long vacLeft = vacationService.getVacLeft(userNum);
        System.out.println("기존 신청한 휴가일수: " + appVacReq);
        System.out.println("기존 연차 사용일수: " + vacUsed);
        System.out.println("기존 연차 잔여일수: " + vacLeft);

        try {
            vacUsed -= appVacReq;
            vacLeft += appVacReq;
            System.out.println("결재 삭제 시 연차 사용일: " + vacUsed);
            System.out.println("결재 삭제 시 잔여 연차일: " + vacLeft);
            approvalService.deleteApproval(appNum);
            vacationService.saveVacation(vacUsed, vacLeft, userNum);
            returnMap.put("msg", "휴가 결재가 삭제되었습니다.");
            returnMap.put("result", "success");
            returnMap.put("redirectUrl", "/appDetail");
            System.out.println("삭제한 휴가결재 번호: "+ appNum);

            responseDTO.setItem(returnMap);
            return ResponseEntity.ok().body(responseDTO);


        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }

    }


}
