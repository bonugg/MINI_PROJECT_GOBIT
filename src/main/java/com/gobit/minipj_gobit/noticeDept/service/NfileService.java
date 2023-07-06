package com.gobit.minipj_gobit.noticeDept.service;

import com.gobit.minipj_gobit.noticeDept.dto.nFileDto;
import com.gobit.minipj_gobit.noticeDept.entity.nBoard;
import com.gobit.minipj_gobit.noticeDept.entity.nBoardNoticeFile;
import com.gobit.minipj_gobit.noticeDept.repository.NfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class NfileService {

    @Autowired
    private NfileRepository nfileRepository;

    public NfileService(NfileRepository nfileRepository) {
        this.nfileRepository = nfileRepository;
    }

//    @Transactional
//    public Long saveFile(nFileDto fileDto) {
//        return nfileRepository.save(fileDto.toEntity()).getNfileNo();
//    }
//
//    @Transactional
//    public nFileDto getFile(Long nfileNo) {
//        nBoardNoticeFile file = nfileRepository.findById(id).get();
//
//        nFileDto fileDto = nFileDto.builder()
//                .nfileNo(nfileNo)
//                .nfileOrigin(file.getNfileOrigin())
//                .nfileName(file.getNfileName())
//                .nfilePath(file.getNfilePath())
//                .build();
//        return  fileDto;
//
//    }


//    @Value("${file.dir}")
//    private String fileDir;
//
//    public Long saveFile(MultipartFile files) throws IOException {
//        if (files.isEmpty()) {
//            throw new IllegalArgumentException("업로드된 파일이 없습니다.");
//        }
//
//        String origName = files.getOriginalFilename();
//        String uuid = UUID.randomUUID().toString();
//        String extension = origName.substring(origName.lastIndexOf("."));
//        String savedName = uuid + extension;
//        String savedPath = fileDir + File.separator + savedName;
//
//        nBoardNoticeFile fileInfo = nBoardNoticeFile.builder()
//                .nfileOrigin(origName)
//                .nfileName(savedName)
//                .nfilePath(savedPath)
//                .build();
//
//        try {
//            Files.copy(files.getInputStream(), Paths.get(savedPath), StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            // 파일 저장 실패 처리
//            throw new IOException("파일을 저장하는 동안 오류가 발생했습니다.", e);
//        }
//
//        nBoardNoticeFile savedFile = nfileRepository.save(fileInfo);
//
//        return savedFile.getNfileNo();
//    }

    public List<nBoardNoticeFile> saveFiles(nBoard board, MultipartFile[] files) {

        List<nBoardNoticeFile> boardFiles = new ArrayList<>();

        for(MultipartFile file : files) {
            if(!file.isEmpty()) {
                String fileOrigin = file.getOriginalFilename();
                String fileExt = fileOrigin.substring(fileOrigin.lastIndexOf("."));
                String fileName = UUID.randomUUID().toString() + fileExt;
                String filePath = "C:/tmp/uploads/" + fileName;

                try {

                    file.transferTo(new File(filePath));

                } catch (IOException ie) {
                    ie.getStackTrace();
                }

                nBoardNoticeFile boardfile = nBoardNoticeFile.builder()
                        .board(board)
                        .nfileName(fileName)
                        .nfileOrigin(fileOrigin)
                        .nfilePath(filePath)
                                .build();

                boardFiles.add(boardfile);
            }
        }

        return boardFiles;
    }

}
