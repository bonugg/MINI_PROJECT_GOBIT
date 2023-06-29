package com.gobit.minipj_gobit.boardDept.file;

import com.gobit.minipj_gobit.boardDept.entity.dBoardFile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileHandler {
    String absolutePath = "/Users/minje/Desktop/upload/";
    public List<dBoardFile> parseFileInfo(List<MultipartFile> multipartFiles) throws Exception{

        //반환할 파일 리스트
        List<dBoardFile> fileList = new ArrayList<>();


        //전달되어 온 파일이 존재할 경우
        if (!CollectionUtils.isEmpty(multipartFiles)) {
            // 파일명을 업로드 한 날짜로 변환하여 저장
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter =
                    DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);


            File file = new File(absolutePath);

            // 디렉터리가 존재하지 않을 경우
            if(!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                // 디렉터리 생성에 실패했을 경우
                if(!wasSuccessful)
                    System.out.println("file: was not successful");
            }

            for (MultipartFile multipartFile : multipartFiles) {

                // 파일의 확장자 추출
                String originalFileExtension;
                String contentType = multipartFile.getContentType();

                // 확장자명이 존재하지 않을 경우 처리 x
                if(ObjectUtils.isEmpty(contentType)) {
                    continue;
                } else {// 확장자가 jpeg, png인 파일들만 받아서 처리
                    if(contentType.contains("image/jpeg"))
                        originalFileExtension = ".jpg";
                    else if(contentType.contains("image/png"))
                        originalFileExtension = ".png";
                    else  // 다른 확장자일 경우 처리 x
                        continue;
                }

                // 파일명 중복 피하고자 나노초까지 얻어와 지정변수
                String new_file_name = System.nanoTime() + originalFileExtension;

                dBoardFile boardFile = new dBoardFile();
                boardFile.setOriginalName(multipartFile.getOriginalFilename());
                boardFile.setSaveName(absolutePath + File.separator + new_file_name);
                boardFile.setType(multipartFile.getContentType());
                boardFile.setSize(multipartFile.getSize());

                fileList.add(boardFile);

                file = new File(absolutePath  + File.separator + new_file_name);
                multipartFile.transferTo(file);

                // 파일 권한 설정(쓰기, 읽기)
                file.setWritable(true);
                file.setReadable(true);
            }

        }

        return fileList;
    }
}
