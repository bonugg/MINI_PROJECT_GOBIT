//package com.gobit.minipj_gobit.service.impl;
//
//import com.gobit.minipj_gobit.entity.AppMeeting;
//import com.gobit.minipj_gobit.repository.AppMeetRepository;
//import com.gobit.minipj_gobit.service.AppMeetService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AppMeetServiceImpl implements AppMeetService {
//    AppMeetRepository appMeetRepository;
//
//    @Autowired
//    public AppMeetServiceImpl(AppMeetRepository appMeetRepository){
//        this.appMeetRepository = appMeetRepository;
//    }
//
//    // 회의결재 신청 처리
//    @Override
//    public void saveAppMeet(AppMeeting appMeeting) {
//        appMeetRepository.save(appMeeting);
//        appMeetRepository.flush();
//    }
//
//    //특정 회의결재 불러오기
//    public AppMeeting getAppMeet(Integer metNum) {
//        return appMeetRepository.findById(metNum).get();
//    }
//}
