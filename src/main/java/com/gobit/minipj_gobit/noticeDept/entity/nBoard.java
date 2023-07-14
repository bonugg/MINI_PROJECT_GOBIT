package com.gobit.minipj_gobit.noticeDept.entity;

import com.gobit.minipj_gobit.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_BOARD_NOTICE")
public class nBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NBOARD_NUM")
    private Long id;

    @Column(name = "NBOARD_TITLE")
    private String title;

    @Column(name = "NBOARD_CONTENT", length = 1000)
    private String content;

    @Column(name = "NBOARD_REGDATE")
    private LocalDateTime regDate = LocalDateTime.now();

    @Column(name = "NBOARD_UPDATEDATE")
    private LocalDateTime updateDate;

    @Column(name = "NBOARD_CNT")
    private  Integer cnt = 0;

    @ManyToOne
    @JoinColumn(name = "USER_NUM")
    private User user;

    @OneToMany(mappedBy = "board" , cascade = CascadeType.REMOVE)
    private List<nBoardFile> files;

    @Builder
    public nBoard(Long id, String title, String content, LocalDateTime regDate, LocalDateTime updateDate, int cnt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.regDate = regDate;
        this.updateDate = updateDate;
        this.cnt = cnt;
    }
}
