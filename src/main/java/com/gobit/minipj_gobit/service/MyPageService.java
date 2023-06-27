package com.gobit.minipj_gobit.service;

import com.gobit.minipj_gobit.Entity.User;
import com.gobit.minipj_gobit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

@Service
public class MyPageService {
    private final UserRepository userRepository;

    @Autowired
    public MyPageService(UserRepository userRepository){
        this.userRepository=userRepository;
    }


    public User getMyPage(Long USERNO) {
        User user = userRepository.findById(USERNO)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        return user;
    }

    public User getUpdateMypage (Long USERNO) {
        User user = userRepository.findById(USERNO)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        return user;
    }

    public byte[] convertImageToByteArray(String imagePath) throws IOException {
        Path path = Paths.get(imagePath);
        return Files.readAllBytes(path);
    }

    private byte[] getDefaultImage() {
        try {
            String defaultImagePath = "static/img/user.jpg";
            Path path = Paths.get(defaultImagePath);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User userget(long usernum){
        User user = userRepository.findByUSERNUM(usernum)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사원입니다."));
        return user;
    }

    public User updateMyPage(User user, Long userNum, MultipartFile imageFile) throws IOException {
        System.out.println(user);
        User updateMyPage = userRepository.findByUSERNUM(userNum)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사원입니다."));
        System.out.println(updateMyPage);
        updateMyPage.setUSER_EMAIL(user.getUSER_EMAIL());
        updateMyPage.setUSER_PHONE(user.getUSER_PHONE());
        updateMyPage.setUSER_ADDRESS(user.getUSER_ADDRESS());

        // 이미지 파일이 있는 경우 imagePath 업데이트
        if (imageFile != null && !imageFile.isEmpty()) {
            updateMyPage.setImagePath(user.getImagePath());
        }

        userRepository.save(updateMyPage);

        return updateMyPage;
    }





}
