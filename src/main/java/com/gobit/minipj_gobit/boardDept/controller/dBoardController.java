package com.gobit.minipj_gobit.boardDept.controller;

import com.gobit.minipj_gobit.boardDept.repository.dBoardFileRepository;
import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.boardDept.entity.BoardForm;
import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import com.gobit.minipj_gobit.boardDept.entity.dBoardFile;
import com.gobit.minipj_gobit.boardDept.file.FileHandler;
import com.gobit.minipj_gobit.boardDept.service.dBoardService;
import com.gobit.minipj_gobit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/boardDept")
@RequiredArgsConstructor
public class dBoardController {

    private final dBoardService dBoardService;
    private final UserRepository userRepository;
    private final FileHandler fileHandler;
    private final dBoardFileRepository boardFileRepository;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<dBoard> paging = this.dBoardService.getList(page);
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
    public String create(BoardForm boardForm) {
        return "boardDept/dboardWritePage";
    }

//    @PostMapping("/create")
//    public String create(dBoard board, Principal principal) {
//        User user = this.userRepository.findByUSERENO(Integer.parseInt(principal.getName())).get();
//        this.dBoardService.create(board, user);
//        return "redirect:/boardDept/list";
//    }

    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, BoardForm boardForm, List<MultipartFile> files) {
        dBoard board = this.dBoardService.getBoard(id);

        boardForm.setTitle(board.getTitle());
        boardForm.setContent(board.getContent());
        boardForm.setFiles(files);

        return "boardDept/dboardWritePage";
    }

    @PostMapping("/modify/{id}")
    public String modifyPost(@PathVariable("id") Long id, BoardForm boardForm) {
        dBoard board = this.dBoardService.getBoard(id);
        this.dBoardService.modify(board, boardForm.getTitle(), boardForm.getContent());
        return "redirect:/boardDept/detail/" + id;
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

    @PostMapping("/create")
    public String fileCreate(@RequestParam("files") List<MultipartFile> files,
                             @RequestParam("title") String title,
                             @RequestParam("content") String content,
                             Principal principal) throws Exception {
        List<dBoardFile> fileList = fileHandler.parseFileInfo(files);
        User user = this.userRepository.findByUSERENO(Integer.parseInt(principal.getName())).get();
        dBoard board = dBoard.builder()
                .title(title)
                .content(content)
                .user(user)
                .cnt(0)
                .like(0)
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .fileList(fileList)
                .build();

        //파일 첨부하기
        dBoardService.create(board, fileList);


        return "redirect:/boardDept/list";
    }

    @GetMapping("/down/{fileId}")
    public ResponseEntity<Resource> fileDown(@PathVariable("fileId") Long fileId) throws IOException {
        dBoardFile file = boardFileRepository.findById(fileId).get();
        Path path = Paths.get(file.getSaveName());
        Resource  resource = new InputStreamResource(Files.newInputStream(path));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                        file.getOriginalName() + "\"")
                .body(resource);
    }
}
