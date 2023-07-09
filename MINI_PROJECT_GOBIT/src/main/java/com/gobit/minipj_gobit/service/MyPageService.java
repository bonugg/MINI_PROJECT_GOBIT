package com.gobit.minipj_gobit.service;

import com.gobit.minipj_gobit.dto.PasswordChangeRequestDTO;
import com.gobit.minipj_gobit.entity.CustomUserDetails;
import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
            updateMyPage.setUSERIMAGE(user.getUSERIMAGE());
        }

        userRepository.save(updateMyPage);

        return updateMyPage;
    }

    public User findById (long id) {
        return userRepository.findById(id).get();
    }

    public void changePw (User user) {
        userRepository.save(user);

    }

    public UserDetails loadUserById (long ENO) throws UsernameNotFoundException{
        Optional<User> userOptional = userRepository.findByUSERNUM(ENO);

        if (userOptional.isEmpty()) {
            return null;
        }

        return CustomUserDetails.builder()
                .user(userOptional.get())
                .build();
    }

}
