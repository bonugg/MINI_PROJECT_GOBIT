package com.gobit.minipj_gobit.entity;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_MEMBER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NUM")
    private long USERNUM;
    @Column(unique = true, name = "USER_ENO") //중복 제거
    @NotNull
    private long USERENO;
    @Column(name = "USER_NAME")
    @NotNull
    private String USERNAME;
    @NotNull
    @Column(name = "USER_DEPT")
    private String USERDEPT;
    @NotNull
    @Column
    private String USER_POSITION;
    @NotNull
    @Column
    private String USER_PWD;
    @NotNull
    @Column
    private String USER_EMAIL;
    @NotNull
    @Column
    private String USER_PHONE;
    @Column
    private String USER_ADDRESS;
    @Column
    private String USER_JOIN;
    @Column
    private String USER_BIRTH;
    @Column
    private String USER_EXIT;
    @Column(columnDefinition = "char default 'N'")
    private char USER_EXIT_CHK;
    @Column
    private String imagePath;

    @Column(name = "USER_IMAGE")
    private String USERIMAGE;

    @Lob
    @Column(name = "default_image")
    private byte[] defaultImage;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserOnOff> userOnOffList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Calendar> calendarList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Testentity> testentityList = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "USERNUM=" + USERNUM +
                ", USERENO=" + USERENO +
                ", USERNAME='" + USERNAME + '\'' +
                ", USERDEPT='" + USERDEPT + '\'' +
                ", USER_POSITION='" + USER_POSITION + '\'' +
                ", USER_PWD='" + USER_PWD + '\'' +
                ", USER_EMAIL='" + USER_EMAIL + '\'' +
                ", USER_PHONE='" + USER_PHONE + '\'' +
                ", USER_ADDRESS='" + USER_ADDRESS + '\'' +
                ", USER_JOIN='" + USER_JOIN + '\'' +
                ", USER_BIRTH='" + USER_BIRTH + '\'' +
                ", USER_EXIT='" + USER_EXIT + '\'' +
                ", USER_EXIT_CHK=" + USER_EXIT_CHK +
                ", imagePath='" + imagePath + '\'' +
                ", USERIMAGE='" + USERIMAGE + '\'' +
                '}';
    }
}
