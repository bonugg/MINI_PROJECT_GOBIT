package com.gobit.minipj_gobit.controller;
//2023.06.30 16:34

import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//@RestController   // @Controller + @ResponseBody
@Controller
@RequiredArgsConstructor
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping("/approvalList")
    public String getLeaderList(Model model,
                                @PageableDefault(page = 0, size = 10, sort = "appNum", direction = Sort.Direction.DESC) Pageable pageable)  {
//                                                   page 인덱스 0부터 10개씩, appNum기준으로 역순 제공

        Page<Approval> appForLeader = approvalService.getApprovalList(pageable);

        int nowPage = appForLeader.getNumber() + 1;
        //Page<Approval>에서 getNumber를 하여 인덱스 숫자 반환. 인덱스는 -0부터 시작하니까 + 1

        int startPage = Math.max(1, nowPage - 5);
        int endPage = Math.min(nowPage + 5, appForLeader.getTotalPages());

        model.addAttribute("appForLeader", appForLeader);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "approvalPage";  //html 리턴
    }


}
