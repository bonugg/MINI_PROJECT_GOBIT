package com.gobit.minipj_gobit.Entity;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
    @Column
    private char USER_EXIT_CHK;
    @Column
    @ColumnDefault("'img/user/user.png'")
    private String imagePath;

    @Lob
    @Column(name = "USER_IMAGE", columnDefinition = "LONGBLOB", nullable = true)
    private byte[] USERIMAGE = getDefaultImage();

    @Lob
    @Column(name = "default_image")
    private byte[] defaultImage;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserOnOff> userOnOffList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Calendar> calendarList = new ArrayList<>();
}
