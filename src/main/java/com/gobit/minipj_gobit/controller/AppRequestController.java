package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.dto.ApprovalDTO;
import com.gobit.minipj_gobit.dto.ResponseDTO;
import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.entity.Vacation;
import com.gobit.minipj_gobit.service.ApprovalService;
import com.gobit.minipj_gobit.service.VacationService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/appRequest")
public class AppRequestController {

    private ApprovalService approvalService;
    private VacationService vacationService;

    @Autowired
    public AppRequestController(ApprovalService approvalService, VacationService vacationService) {
        this.approvalService = approvalService;
        this.vacationService = vacationService;
    }


    @PostMapping("/meeting")
    @ResponseBody
    public ResponseEntity<?> saveMeeting(ApprovalDTO approvalDTO) {
        System.out.println("=======================meeting approval result=======================");
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<Map<String, String>>();
        Map<String, String> returnMap = new HashMap<String, String>();

        LocalDateTime appStart = approvalDTO.getAppStart();
        LocalDateTime appEnd = approvalDTO.getAppEnd();
        String appSort = approvalDTO.getAppSort();
        System.out.println("신청한 startDate: " + appStart);
        System.out.println("신청한 endDate: " + appEnd);

        try {
            if (appStart != null && appEnd != null && appStart.isBefore(appEnd)) {
                approvalService.saveApproval(approvalDTO.toEntity());
                returnMap.put("msg", "회의 결재가 신청되었습니다");
                returnMap.put("result", "success");
                returnMap.put("redirectUrl", "/appDetail");
                System.out.println("회의 신청됨");
            } else {
                returnMap.put("msg", "회의 시작일과 회의 종료일을 다시 입력해주세요");
                returnMap.put("result", "fail");
                System.out.println("회의 날짜 입력 오류로 휴가 결재 신청되지 않음");
            }
            responseDTO.setItem(returnMap);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    @PostMapping("/buisness")
    @ResponseBody
    public ResponseEntity<?> saveBuisness(ApprovalDTO approvalDTO) {
        System.out.println("=======================buisness approval result=======================");
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<Map<String, String>>();
        Map<String, String> returnMap = new HashMap<String, String>();

        LocalDateTime appStart = approvalDTO.getAppStart();
        LocalDateTime appEnd = approvalDTO.getAppEnd();
        String appSort = approvalDTO.getAppSort();
        System.out.println("신청한 startDate: " + appStart);
        System.out.println("신청한 endDate: " + appEnd);

        try {
            if (appStart != null && appEnd != null && appStart.isBefore(appEnd)) {
                approvalService.saveApproval(approvalDTO.toEntity());
                returnMap.put("msg", "출장 결재가 신청되었습니다");
                returnMap.put("result", "success");
                returnMap.put("redirectUrl", "/appDetail");
                System.out.println("출장 신청됨");
            } else {
                returnMap.put("msg", "출장 시작일과 출장 종료일을 다시 입력해주세요");
                returnMap.put("result", "fail");
                System.out.println("출장 날짜 입력 오류로 휴가 결재 신청되지 않음");
            }
            responseDTO.setItem(returnMap);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/vacation")
    @ResponseBody
    public ResponseEntity<?> saveVacation(ApprovalDTO approvalDTO) {
        System.out.println("=======================vacation approval result=======================");
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<Map<String, String>>();
        Map<String, String> returnMap = new HashMap<String, String>();

        LocalDateTime appStart = approvalDTO.getAppStart();
        LocalDateTime appEnd = approvalDTO.getAppEnd();
        LocalDate appStart2 = approvalDTO.getAppStart2();
        LocalDate appEnd2 = approvalDTO.getAppEnd2();
        long appVacReq = approvalDTO.getAppVacReq();
        System.out.println(appVacReq);
        boolean isDateTimeFormat = (appStart != null && appEnd != null);

        if (isDateTimeFormat) {
            System.out.println("신청한 endDate: " + appEnd);
        } else {
            System.out.println("신청한 startDate: " + appStart2);
            System.out.println("신청한 endDate: " + appEnd2);
        }

        try {
            long vacUsed;
            long vacLeft;
            long vacTotal;
            boolean isVacationApproved = false;
            vacUsed = vacationService.getVacUsed(approvalDTO.getUserNum().getUSERNUM());
            vacLeft = vacationService.getVacLeft(approvalDTO.getUserNum().getUSERNUM());
            vacTotal = vacationService.getVacTotal(approvalDTO.getUserNum().getUSERNUM());

            if (isDateTimeFormat && vacLeft > appVacReq || !isDateTimeFormat && vacLeft > appVacReq) {
                approvalService.saveApproval(approvalDTO.toEntity());
                long userNum = approvalDTO.getUserNum().getUSERNUM();
                vacUsed += appVacReq;  //연차 사용일 증가
                vacLeft = vacTotal - vacUsed; //잔여 연차 차감
                System.out.println("결재 신청 시 연차 사용일: " + vacUsed);
                System.out.println("결재 신청 시 잔여 연차일: " + vacLeft);
                vacationService.saveVacation(vacUsed, vacLeft, userNum);
                returnMap.put("msg", "휴가 결재가 신청되었습니다.");
                returnMap.put("result", "success");
                returnMap.put("redirectUrl", "/appDetail");
                isVacationApproved = true;
            }

            if (!isVacationApproved) {
                returnMap.put("msg", "잔여 연차가 부족합니다.");
                returnMap.put("result", "fail");
                System.out.println("잔여 연차 부족으로 휴가 결재 신청되지 않음");
            }

            responseDTO.setItem(returnMap);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


}
