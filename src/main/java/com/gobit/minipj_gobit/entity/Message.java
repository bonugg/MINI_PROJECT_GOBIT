//package com.gobit.minipj_gobit.entity;
//
//
//import groovyjarjarantlr4.v4.runtime.misc.NotNull;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@EntityListeners(MessageListener.class)
//@Table(name = "T_Message")
//public class Message {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "MESSAGE_NUM")
//    private long messageNum;
//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @JoinColumn(name = "USER_NUM")
//    @NotNull
//    private User user;
//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @JoinColumn(name = "RECEIVE_EUSER_NUM")
//    @NotNull
//    private User receiveUser;
//    @Column(name = "CHAT_SEND", length = 3000)
//    private String chatSend;
//    @Column(name = "CHAT_CHECK")
//    private int chatCheck;
//    @Column(name = "APP_WRI_DATE")
//    private LocalDateTime chatSendDate = LocalDateTime.now();
//}
