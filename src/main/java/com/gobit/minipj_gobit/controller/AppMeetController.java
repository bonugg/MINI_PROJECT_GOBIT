package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.dto.AppMeetingDTO;
import com.gobit.minipj_gobit.dto.ResponseDTO;
import com.gobit.minipj_gobit.service.AppMeetService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/app")
public class AppMeetController {

    AppMeetService appMeetService;

    @Autowired
    public AppMeetController(AppMeetService appMeetService) {
        this.appMeetService = appMeetService;
    }

    @PostMapping("/meet")
    public ResponseEntity<?> saveAppMeet(AppMeetingDTO appMeetingDTO) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        try {
            //BoardEntity에 지정한 boardRegdate의 기본값은
            //기본생성자 호출할 때만 기본값으로 지정되는데
            //builder()는 모든 매개변수를 갖는 생성자를 호출하기 때문에
            //boardRegdate의 값이 null값으로 들어간다.

            appMeetService.saveAppMeet(appMeetingDTO.toEntity());
            Map<String, String> returnMap =
                    new HashMap<String, String>();
            returnMap.put("msg", "회의결재가 정상적으로 신청되었습니다.");
            responseDTO.setItem(returnMap);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


}

