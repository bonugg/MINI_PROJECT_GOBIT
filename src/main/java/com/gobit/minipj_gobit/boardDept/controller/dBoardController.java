package com.gobit.minipj_gobit.boardDept.controller;

import com.gobit.minipj_gobit.boardDept.file.FileUtils;
import com.gobit.minipj_gobit.boardDept.service.FileService;
import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import com.gobit.minipj_gobit.boardDept.entity.dBoardFile;
import com.gobit.minipj_gobit.boardDept.service.dBoardService;
import com.gobit.minipj_gobit.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/boardDept")
@RequiredArgsConstructor
public class dBoardController {

    private final dBoardService dBoardService;
    private final UserRepository userRepository;
    private final FileUtils fileUtils;
    private final FileService fileService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "category", defaultValue = "전체") String category,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        System.out.println("category: " + category);
        Page<dBoard> paging;
        if (category.equals("전체")) {
            paging = this.dBoardService.getList(page, kw);
        } else {
            paging = this.dBoardService.getListByCategory(page, category, kw);
        }
        model.addAttribute("paging", paging);
        return "boardDept/dboardListPage";
    }

    @GetMapping("/sorted-by-cnt")
    public String getAllPostsSortedByCnt(Pageable pageable, Model model) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), 10);
        Page<dBoard> paging = dBoardService.getAllPostsSortedByCnt(pageRequest);
        model.addAttribute("paging", paging);
        return "boardDept/dboardListPage";
    }

    @GetMapping("/sorted-by-like")
    public String getAllPostsSortedByLike(Pageable pageable, Model model) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), 10);
        Page<dBoard> paging = dBoardService.getAllPostsSortedByLike(pageRequest);
        model.addAttribute("paging", paging);
        return "boardDept/dboardListPage";
    }

    @GetMapping("/updateCnt/{id}")
    public String updateCnt(@PathVariable("id") Long id) {

        this.dBoardService.updateCnt(id);

        return "redirect:/boardDept/detail/" + id;
    }


    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        dBoard dBoard = this.dBoardService.getBoard(id);
        model.addAttribute("board", dBoard);
        return "boardDept/dboardDetailPage";
    }

    @GetMapping("/create")
    public String create() {
        return "boardDept/dboardWritePage";
    }

    @GetMapping("/modify/{id}")
    public ModelAndView modify(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView();
        dBoard modifyBoard = this.dBoardService.getBoard(id);
        mv.addObject("board",modifyBoard);
        mv.setViewName("boardDept/dboardModifyPage");
        return mv;
    }

    @PostMapping("/modify")
    public String modifyPost(@RequestParam("id") Long id,
                             @RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @RequestParam(value = "items", required = false, defaultValue = "null") List<String> exFilesStr,
                             @RequestParam("files") List<MultipartFile> multipartFiles) {
        //게시글 수정
        this.dBoardService.modify(id, title, content);

        if (exFilesStr != null) {
            System.out.println(exFilesStr);
            System.out.println("--파일 리스트1---");
            //기존 파일리스트 조회
            List<dBoardFile> exFiles = fileService.findByFiles(id);
            System.out.println(exFiles);
            System.out.println("--파일 리스트2---");
            //수정된 파일 삭제
            fileService.modifyFiles(exFilesStr, exFiles);
        }

        //새로운 파일 저장
        List<dBoardFile> files = fileUtils.uploadFiles(multipartFiles);
        fileService.saveFiles(id, files);

        return "redirect:/boardDept/list";
    }

    @PostMapping("/create")
    public String fileCreate(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @RequestParam("files") List<MultipartFile> multipartFiles,
                             Principal principal) {
        User user = this.userRepository.findByUSERENO(Integer.parseInt(principal.getName())).get();
        Long id = dBoardService.create(title, content, user);
        List<dBoardFile> files = fileUtils.uploadFiles(multipartFiles);
        fileService.saveFiles(id, files);

        return "redirect:/boardDept/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        dBoard board = this.dBoardService.getBoard(id);
        this.dBoardService.delete(board);
        return "redirect:/boardDept/list";
    }

    @GetMapping("/like/{id}")
    public String boardLike(@PathVariable("id") Long id, Principal principal) {
        dBoard board = this.dBoardService.getBoard(id);
        User user = this.userRepository.findByUSERENO(Integer.parseInt(principal.getName())).get();
        this.dBoardService.like(board, user);
        return "redirect:/boardDept/detail/" + id;
    }

    @GetMapping("/down/{fileId}")
    public void fileDown(@PathVariable("fileId") Long fileId, HttpServletResponse response) throws IOException {
        dBoardFile file = fileService.findById(fileId);
        Resource resource = fileUtils.readFileAsResource(file);

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
