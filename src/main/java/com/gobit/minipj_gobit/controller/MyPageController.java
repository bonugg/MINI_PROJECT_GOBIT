package com.gobit.minipj_gobit.controller;

import com.gobit.minipj_gobit.Entity.User;
import com.gobit.minipj_gobit.service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class MyPageController {
    @Autowired
    private MyPageService myPageService;

    @GetMapping("/myPage")
    public String myPage(){
        return "myPage";
    }

//    @GetMapping("/myPageUpdate/{USERNO}")
//    public String updateGetMyPage(@PathVariable Long USERNO, Model model) {
//        User user = myPageService.updateMyPage();
//
//        return "myPageUpdate";
//    }


    @GetMapping("/myPageUpdate/{USERNO}")
    public String updateGetMyPage(@PathVariable Long USERNO, Model model) {
//        User user = myPageService.updateMyPage();
        return "myPageUpdate";
    }

    @PostMapping("/myPage/update")
    public String updatemyPage(@ModelAttribute User user,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               @RequestParam(value = "USERNUM", required = false) Long userNum,
                               Model model) throws IOException {

        if (!imageFile.isEmpty()) {
            // Save the image file to the server
            String fileName = user.getUSERNUM() + "_" + imageFile.getOriginalFilename();
            String imagesDirectory = "src/main/resources/static/img/user/";
            Path path = Paths.get(imagesDirectory, fileName);
            try {
                Files.write(path, imageFile.getBytes());

                // Update the imagePath field in the User entity
                user.setImagePath("img/user/" + fileName);
            } catch (IOException ex) {
                ex.getStackTrace();
            }
        } else {
            user.setImagePath("img/user/user.png");
        }

        myPageService.updateMyPage(user, userNum, imageFile);

        model.addAttribute("imagePath", user.getImagePath());
        return "redirect:/myPage";
    }



}
