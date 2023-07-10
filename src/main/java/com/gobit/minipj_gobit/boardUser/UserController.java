package com.gobit.minipj_gobit.boardUser;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public ModelAndView userList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/userList/userListPage");
        return mv;
    }

    // 전체 게시글 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    @ResponseBody
    public Response<?> getBoards(String category, String categoryDetail, String keyword) {
        List<UserDTO> userDTOList = userService.searchUser(category, categoryDetail, keyword);
        return new Response("성공", "검색 성공", userDTOList);
    }
}
