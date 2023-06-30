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

    //회의 결재신청
    @PostMapping("/meeting")
    public ResponseEntity<?> saveAppMeet(ApprovalDTO approvalDTO){
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        try{
            approvalService.saveApproval(approvalDTO.toEntity());
            Map<String, String> returnMap =
                    new HashMap<String, String>();
            returnMap.put("msg", "회의결재가 정상적으로 신청되었습니다.");
            responseDTO.setItem(returnMap);
            return ResponseEntity.ok().body(responseDTO);
        }catch(Exception e){
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //휴가 결재신청
    @PostMapping("/vacation")
    public ResponseEntity<?> saveAppVacation(ApprovalDTO approvalDTO){
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        try{
            approvalService.saveApproval(approvalDTO.toEntity());
            Map<String, String> returnMap =
                    new HashMap<String, String>();
            returnMap.put("msg", "휴가결재가 정상적으로 신청되었습니다.");
            responseDTO.setItem(returnMap);
            return ResponseEntity.ok().body(responseDTO);
        }catch(Exception e){
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //출장 결재신청
    @PostMapping("/buisness")
    public ResponseEntity<?> saveAppBuisness(ApprovalDTO approvalDTO){
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        try{
            approvalService.saveApproval(approvalDTO.toEntity());
            Map<String, String> returnMap =
                    new HashMap<String, String>();
            returnMap.put("msg", "출장결재가 정상적으로 신청되었습니다.");
            responseDTO.setItem(returnMap);
            return ResponseEntity.ok().body(responseDTO);
        }catch(Exception e){
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
