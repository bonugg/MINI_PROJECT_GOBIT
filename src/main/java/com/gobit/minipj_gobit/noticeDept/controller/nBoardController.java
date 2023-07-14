package com.gobit.minipj_gobit.noticeDept.controller;

import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.noticeDept.entity.nBoard;
import com.gobit.minipj_gobit.noticeDept.entity.nBoardFile;
import com.gobit.minipj_gobit.noticeDept.file.FileUtil;
import com.gobit.minipj_gobit.noticeDept.repository.NfileRepository;
import com.gobit.minipj_gobit.noticeDept.service.NfileService;
import com.gobit.minipj_gobit.noticeDept.service.nBoardService;
import com.gobit.minipj_gobit.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/noticeDept")
@RequiredArgsConstructor
public class nBoardController {

    @Autowired
    private nBoardService boardService;
    @Autowired
    private NfileRepository nfileRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NfileService nfileService;

    @Autowired
    private FileUtil fileUtil;


    @GetMapping("/list")
    public String NoticeList(Model model,
                             @PageableDefault (page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                             String searchKeyword) {
        Page<nBoard> boardList = null;
        if(searchKeyword == null) {
            boardList = boardService.getNoticeList(pageable);
        }
        else {
            boardList = boardService.searchBoard(searchKeyword,searchKeyword, pageable);
        }

        int nowPage = boardList.getNumber() + 1;
        int totalPageCount = boardList.getTotalPages();
        int fixedPageCount = 5; // 고정으로 보여줄 페이지 수
        int startPage = Math.max(1, nowPage - (fixedPageCount / 2));
        int endPage = Math.min(startPage + fixedPageCount - 1, totalPageCount);

        model.addAttribute("postList", boardList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/noticeDept/noticelist";
    }

    @GetMapping("/updateCnt/{id}")
    public String updateCnt(@PathVariable("id") Long id) {

        this.boardService.updateCnt(id);

        return "redirect:/noticeDept/detail/" + id;
    }


    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        System.out.println("============");
        System.out.println(id);
        System.out.println("============");
        nBoard board = this.boardService.getBoard(id);
        System.out.println("============");
        model.addAttribute("nBoard", board);
        return "noticeDept/noticeDetail";
    }

    @PostMapping("/noticeWrite")
    public String write(@RequestParam("title") String title,
                        @RequestParam("content") String content,
                        @RequestParam("files") List<MultipartFile> multipartFiles,
                        Principal principal) {
        User user = this.userRepository.findByUSERENO(Integer.parseInt(principal.getName())).get();
        Long id = boardService.write(title, content, user);
        List<nBoardFile> files = fileUtil.uploadFiles(multipartFiles);

        nfileService.saveFiles(id, files);
        return "redirect:/noticeDept/list";
    }

    @GetMapping("/modify/{id}")
    public String modify(Model model,
                         @PathVariable("id") Long id) {
        nBoard board = boardService.getBoard(id);
        model.addAttribute("nBoard", board);
        return "noticeDept/noticeEdit";
    }

    @PostMapping("/modify")
    public String modifyPost(@RequestParam("id") Long id,
                             @RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @RequestParam(value = "items", required = false, defaultValue = "null") List<String> exFilesStr,
                             @RequestParam(value = "files") List<MultipartFile> multipartFiles) {
        System.out.println("-----------시작---------------");
        if(multipartFiles != null){
            System.out.println(multipartFiles.get(0));
        }else {
            System.out.println(multipartFiles);
        }

        boardService.modify(id, title, content);

        if (exFilesStr != null) {
            System.out.println(exFilesStr);
            System.out.println("--기존 파일 리스트---");
            //기존 파일리스트 조회
            List<nBoardFile> exFiles = nfileService.findByFiles(id);
            System.out.println(exFiles);
            System.out.println("--기존 파일 리스트---");
            //수정된 파일 삭제
            nfileService.modifyFiles(exFilesStr, exFiles);
        }

        //새로운 파일 저장
        List<nBoardFile> files = fileUtil.uploadFiles(multipartFiles);
        nfileService.saveFiles(id, files);

        return "redirect:/noticeDept/detail/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        nBoard nBoard = this.boardService.getBoard(id);
        this.boardService.delete(nBoard);
        return  "redirect:/noticeDept/list";
    }

    @GetMapping("/noticeWrite")
    public String noticeWrite() {
        return "/noticeDept/noticeWrite";
    }

    @GetMapping("/down/{fileId}")
    public void fileDown(@PathVariable("fileId") Long fileId, HttpServletResponse response) throws IOException {
        nBoardFile file = nfileService.findById(fileId);
        Resource resource = fileUtil.readFileAsResource(file);

//        String originalFilename = file.getOriginalName();
//        String encodedFilename = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8.toString());

        try {
            String filename = URLEncoder.encode(file.getOriginalName(), "UTF-8");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + filename + "\";");
            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.getSize()));
            FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("파일 다운로드 중에 오류가 발생했습니다: " + e.getMessage());
        }

    }
}
