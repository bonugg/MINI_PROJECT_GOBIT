package com.gobit.minipj_gobit.service.impl;

import com.gobit.minipj_gobit.dto.AppMeetingDTO;
import com.gobit.minipj_gobit.entity.AppMeeting;
import com.gobit.minipj_gobit.repository.AppMeetRepository;
import com.gobit.minipj_gobit.service.AppMeetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppMeetServiceImpl implements AppMeetService {
    AppMeetRepository appMeetRepository;

    @Autowired
    public AppMeetServiceImpl(AppMeetRepository appMeetRepository){
        this.appMeetRepository = appMeetRepository;
    }

    @Override
    public void saveAppMeet(AppMeeting appMeeting) {
        appMeetRepository.save(appMeeting);
        appMeetRepository.flush();
    }
}
