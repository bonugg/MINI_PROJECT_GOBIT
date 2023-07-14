package com.gobit.minipj_gobit.boardDept.file;

import com.gobit.minipj_gobit.boardDept.entity.dBoardFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileUtils {

    public static final  String UPLOAD_PATH = "/Users/minje/Desktop/upload";

    /**
     * 다중 파일 업로드
     * @param multipartFiles - 파일 객체 List
     * @return DB에 저장할 파일 정보 List
     */
    public List<dBoardFile> uploadFiles(final List<MultipartFile> multipartFiles) {
        List<dBoardFile> files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) {
                continue;
            }
            files.add(uploadFile(multipartFile));
        }
        return files;
    }
    /**
     * 단일 파일 업로드
     * @param multipartFile - 파일 객체
     * @return DB에 저장할 파일 정보
     */
    public dBoardFile uploadFile(final MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            return null;
        }

        String saveName = generateSaveFilename(multipartFile.getOriginalFilename());
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")).toString();
        String uploadPath = getUploadPath(today) + File.separator + saveName;
        File uploadFile = new File(uploadPath);

        try {
            multipartFile.transferTo(uploadFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return dBoardFile.builder()
                .originalName(multipartFile.getOriginalFilename())
                .saveName(saveName)
                .size(multipartFile.getSize())
                .build();
    }

    /**
     * 저장 파일명 생성
     * @param filename 원본 파일명
     * @return 디스크에 저장할 파일명
     */
    private String generateSaveFilename(final String filename) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = StringUtils.getFilenameExtension(filename);
        return uuid + "." + extension;
    }


    /**
     * 업로드 경로 반환
     * @param addPath - 추가 경로
     * @return 업로드 경로
     */
    private String getUploadPath(final String addPath) {
        return makeDirectories(UPLOAD_PATH + File.separator + addPath);
    }

    /**
     * 업로드 폴더(디렉터리) 생성
     * @param path - 업로드 경로
     * @return 업로드 경로
     */
    private String makeDirectories(final String path) {
        File dir = new File(path);
        if (dir.exists() == false) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    public Resource readFileAsResource(final dBoardFile file) {
        String uploadedDate = file.getCreateDate().toLocalDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String filename = file.getSaveName();
        Path filePath = Paths.get(UPLOAD_PATH, uploadedDate, filename);
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() == false || resource.isFile() == false) {
                throw new RuntimeException("file not found : " + filePath.toString());
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException("file not found : " + filePath.toString());
        }
    }
}
