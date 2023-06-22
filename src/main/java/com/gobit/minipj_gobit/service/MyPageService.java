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
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;

@Service
public class MyPageService {
    private final UserRepository userRepository;

    @Autowired
    public MyPageService(UserRepository userRepository){
        this.userRepository=userRepository;
    }


    public User getPage(Long USERNO) {
        User user = userRepository.findById(USERNO)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사원입니다."));

        userRepository.save(user);

        return user;
    }

    public byte[] convertImageToByteArray(String imagePath) throws IOException {
        Path path = Paths.get(imagePath);
        return Files.readAllBytes(path);
    }

    public User updateMyPage(User user, Long USERENO, MultipartFile imageFile) throws IOException {
        User updateMyPage = userRepository.findById(USERENO)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사원입니다."));
        updateMyPage.setUSER_PHONE(user.getUSER_PHONE());
        updateMyPage.setUSER_ADDRESS(user.getUSER_ADDRESS());
        updateMyPage.setUSER_PWD(user.getUSER_PWD());

        String imagePath = "static/img/user.jpg";
        byte[] imageBytes = convertImageToByteArray(imagePath);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // 파일명 생성 (현재 시간 기반)
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                // 파일을 업로드할 경로 생성
                String filePath = Paths.get(imagePath, fileName).toString();
                // 파일 저장
                Files.copy(imageFile.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

                // 업로드된 파일의 경로를 엔티티에 저장
                updateMyPage.setUSERIMAGE(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        userRepository.save(updateMyPage);

        return updateMyPage;

    }





}
