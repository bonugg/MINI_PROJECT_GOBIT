package com.gobit.minipj_gobit.Entity;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @Column
    @NotNull
    private String USER_NAME;
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
    private String USER_EXIT;
    @Column
    private char USER_EXIT_CHK;

    @Lob
    @Column(name = "USER_IMAGE", columnDefinition = "LONGBLOB", nullable = false)
    private byte[] USERIMAGE = getDefaultImage();

    private byte[] getDefaultImage() {
        try {
            String defaultImagePath = "static/img/user.jpg";
            Path path = Paths.get(defaultImagePath);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // 기본 이미지를 읽을 수 없는 경우 null 또는 빈 배열을 반환합니다.
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserOnOff> userOnOffList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Calendar> calendarList = new ArrayList<>();
}
