package com.gobit.minipj_gobit.service;

import com.gobit.minipj_gobit.Entity.UserOnOff;
import com.gobit.minipj_gobit.repository.UserOnOffRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class DatabaseWatcherService {
    @Autowired
    UserOnOffRepository userOnOffRepository;

    public Optional<LocalDateTime> checkForDatabaseChange() {
        return userOnOffRepository.checkForDatabaseChange();
    }

    public Optional<UserOnOff> findByUPDATEAT(LocalDateTime UPDATEAT){
        return userOnOffRepository.findByUPDATEAT(UPDATEAT);
    }

}