package com.gobit.minipj_gobit.noticeDept.dto;

import com.gobit.minipj_gobit.noticeDept.entity.nBoardNoticeFile;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class nFileDto {

    private Long nfileNo;
    private String nfileOrigin;
    private String nfileName;
    private String nfilePath;

    public nBoardNoticeFile toEntity() {
        nBoardNoticeFile build = nBoardNoticeFile.builder()
                .nfileNo(nfileNo)
                .nfileOrigin(nfileOrigin)
                .nfileName(nfileName)
                .nfilePath(nfilePath)
                .build();
        return build;
    }

    @Builder
    public nFileDto(Long nfileNo, String nfileOrigin, String nfileName, String nfilePath) {
        this.nfileNo = nfileNo;
        this.nfileOrigin = nfileOrigin;
        this.nfileName = nfileName;
        this.nfilePath = nfilePath;
    }
}
