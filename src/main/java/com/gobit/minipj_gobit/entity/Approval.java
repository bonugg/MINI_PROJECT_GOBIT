package com.gobit.minipj_gobit.entity;//package com.gobit.minipj_gobit.Entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Data
//@NoArgsConstructor
//@Entity
//@Table(name = "T_APPROVAL")
//public class Approval {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) //결재문서ID. auto_increase
//    @Column(name = "APP_NUM")
//    private long appNum;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "USER_NUM")
//    private User user; //사원ID
//
//    @Column(name = "APP_WRI_DATE")
//    private LocalDateTime appWriDate = LocalDateTime.now(); //결재 작성일
//
//    @Column(name = "APP_CONTENT", nullable = false)
//    private String appContent; //결재내용
//
//    @Column(name = "APP_STATE", nullable = false)
//    private String appState; // 결재상태. 영문자 코드로 해서 char(1)로 하는건?
//
//    @Column(name = "APP_STATE_DATE")
//    private LocalDateTime appStateDate = LocalDateTime.now();
//
//    @Column(name = "APP_USER_NUM", nullable = false)
//    private long appUserNum;
//
//
//    //임의 값 설정함 V 휴가, T 출장, M 회의. char(1)
//    @Column(name = "APP_SORT", nullable = false)
//    private char appSort;
//
//
//}
//
