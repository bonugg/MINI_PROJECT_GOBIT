package com.gobit.minipj_gobit.controller;
//2023.06.30 16:34

import com.gobit.minipj_gobit.dto.ApprovalDTO;
import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.repository.ApprovalRepository;
import com.gobit.minipj_gobit.service.ApprovalService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@RestController   // @Controller + @ResponseBody
@Controller
@RequiredArgsConstructor
public class ApprovalController {

    @Autowired
    private HttpSession httpSession;

    private final ApprovalService approvalService;

    private final ApprovalRepository approvalRepository;


    @GetMapping("/approvalList")
    public String getLeaderList(Model model, String cls, String sWord,
                                @PageableDefault(page = 0, size = 8, sort = "appNum", direction = Sort.Direction.DESC) Pageable pageable) {
//                                                   page 인덱스 0부터 10개씩, appNum기준으로 역순 제공

        User user = (User) httpSession.getAttribute("user");
        Page<Approval> appForLeader = null;
        if (sWord == null) {
            if(cls == null)
                cls = "";
            appForLeader = approvalService.findByDept(pageable, user.getUSERDEPT(), cls);
//            System.out.println(appForLeader);


        } else {
            appForLeader = approvalService.searchAppLeaderDept(pageable, user.getUSERDEPT(), sWord);
        }


        int nowPage = appForLeader.getNumber() + 1;
        //Page<Approval>에서 getNumber를 하여 인덱스 숫자 반환. 인덱스는 -0부터 시작하니까 + 1

        int startPage = ((nowPage - 1) / 5) * 5 + 1;
        int endPage = Math.min(startPage + 4, appForLeader.getTotalPages());
        int lastPage = appForLeader.getTotalPages();
        model.addAttribute("cls", cls);
        model.addAttribute("appForLeader", appForLeader);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("pageSize", 5);


        // // 2023.07.05 추가
        // 전체 결재문서, 승인 대기, 반려, 승인 완료

        int cntLeadTotalApp = approvalService.cntLeadTotalApp(user.getUSERDEPT());
        int cntLeadWaitApp = approvalService.cntLeadWaitApp(user.getUSERDEPT());
        int cntLeadRejectApp = approvalService.cntLeadRejectApp(user.getUSERDEPT());
        int cntLeadFinApp = approvalService.cntLeadFinApp(user.getUSERDEPT());

        model.addAttribute("cntLeadTotalApp", cntLeadTotalApp);
        model.addAttribute("cntLeadWaitApp", cntLeadWaitApp);
        model.addAttribute("cntLeadRejectApp", cntLeadRejectApp);
        model.addAttribute("cntLeadFinApp", cntLeadFinApp);

//        int cntMemTotalApp = approvalService.cntMemTotalApp(user.getUSERNUM());
//        model.addAttribute("cntMemTotalApp", cntMemTotalApp);


        return "approvalPage";  //html 리턴
    }


    //
    @GetMapping("/appDetail")
    public String getUserList(Model model, String cls, String sWord,
                              @PageableDefault(page = 0, size = 8, sort = "appNum", direction = Sort.Direction.DESC) Pageable pageable) {
//                                                   page 인덱스 0부터 10개씩, appNum기준으로 역순 제공
        User user = (User) httpSession.getAttribute("user");

        Page<Approval> appForUser = null;

        if (sWord == null) {
            if(cls == null) cls = "";
            appForUser = approvalService.findByUser(pageable, user, cls);
        } else {
            appForUser = approvalService.searchAppUser(pageable, user, sWord);
        }

        int nowPage = appForUser.getNumber() + 1;
        //Page<Approval>에서 getNumber를 하여 인덱스 숫자 반환. 인덱스는 -0부터 시작하니까 + 1

        int startPage = ((nowPage - 1) / 5) * 5 + 1;
        int endPage = Math.min(startPage + 4, appForUser.getTotalPages());
        int lastPage = appForUser.getTotalPages();
        model.addAttribute("cls", cls);
        model.addAttribute("appForUser", appForUser);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("pageSize", 5);

//        System.out.println("end= " + endPage + "last =" + lastPage);

        // // 2023.07.05 추가
        // 전체 결재문서, 승인 대기, 반려, 승인 완료

        long cntMemTotalApp = approvalService.cntMemTotalApp(user);
        long cntMemWaitApp = approvalService.cntMemWaitApp(user);
        long cntMemRejectApp = approvalService.cntMemRejectApp(user);
        long cntMemFinApp = approvalService.cntMemFinApp(user);

        model.addAttribute("cntMemTotalApp", cntMemTotalApp);
        model.addAttribute("cntMemWaitApp", cntMemWaitApp);
        model.addAttribute("cntMemRejectApp", cntMemRejectApp);
        model.addAttribute("cntMemFinApp", cntMemFinApp);


        return "appDetailPage";  //html 리턴


    }

}
