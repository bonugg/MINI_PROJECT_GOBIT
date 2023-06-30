package com.gobit.minipj_gobit.boardDept.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class BoardForm {
    public String title;
    public String content;
    public List<MultipartFile> files;
}
