package com.gobit.minipj_gobit.noticeDept.dto;

import com.gobit.minipj_gobit.noticeDept.entity.nBoard;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class nBoardDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private int cnt;

    public nBoard toEntity() {
        nBoard build = nBoard.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .regDate(this.regDate)
                .updateDate(this.updateDate)
                .cnt(this.cnt)
                .build();
        return build;
    }

    @Builder
    public nBoardDto(Long id, String title, String content, LocalDateTime regDate, LocalDateTime updateDate, int cnt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.regDate = regDate;
        this.updateDate = updateDate;
        this.cnt = cnt;
    }
}
