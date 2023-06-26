package com.gobit.minipj_gobit.service;

import com.gobit.minipj_gobit.Entity.User;
import com.gobit.minipj_gobit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public User updateMyPage(User user, Long USERENO){
        User updateMyPage = userRepository.findById(USERENO)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사원입니다."));
        updateMyPage.setUSER_PHONE(user.getUSER_PHONE());
        updateMyPage.setUSER_ADDRESS(user.getUSER_ADDRESS());
        updateMyPage.setUSER_PWD(user.getUSER_PWD());

        userRepository.save(updateMyPage);

        return updateMyPage;

    }





}
