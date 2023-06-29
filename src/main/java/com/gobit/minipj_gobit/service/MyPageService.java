package com.gobit.minipj_gobit.service;

import com.gobit.minipj_gobit.Entity.User;
import com.gobit.minipj_gobit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MyPageService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public MyPageService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User getMyPage(Long USERNO) {
        User user = userRepository.findById(USERNO)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        return user;
    }

    public User getUpdateMypage(Long USERNO) {
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

    public User userget(long usernum) {
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

        if (imageFile != null && !imageFile.isEmpty()) {
            updateMyPage.setImagePath(user.getImagePath());
        }

        userRepository.save(updateMyPage);

        return updateMyPage;
    }

    public boolean changePw(long usereno, String encryptedOriginPw, String changePw) {
        Optional<User> userOptional = userRepository.findByUSERENO(usereno);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(encryptedOriginPw, user.getUSER_PWD())) {
                // 새로운 비밀번호 저장
                user.setUSER_PWD(passwordEncoder.encode(changePw));
                userRepository.save(user);
                return true;
            }
        }

        return false;
    }


    public User originPwCheck(long usereno, String encryptedOriginPw) {
        Optional<User> userOptional = userRepository.findByUSERENO(usereno);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(encryptedOriginPw, user.getUSER_PWD())) {
                return user;
            }
        }

        return null;
    }

}
