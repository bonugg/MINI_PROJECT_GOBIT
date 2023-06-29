package com.gobit.minipj_gobit.boardDept.file;

import com.gobit.minipj_gobit.Entity.User;
import com.gobit.minipj_gobit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class excepController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @GetMapping("/excel")
    public String main() { // 1
        return "boardDept/excel";
    }

    @PostMapping("/excel/read")
    public String readExcel(@RequestParam("file") MultipartFile file)
            throws IOException { // 2

        List<User> dataList = new ArrayList<>();

        String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3

        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new IOException("엑셀파일만 업로드 해주세요.");
        }

        Workbook workbook = null;

        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (extension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }

        Sheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) { // 4

            Row row = worksheet.getRow(i);

            User data = new User();

            String pwd = String.valueOf((int)row.getCell(4).getNumericCellValue());
            Date joinDate = row.getCell(7).getDateCellValue();
            String joinStr = new SimpleDateFormat("yyyy-MM-dd").format(joinDate);



            data.setUSERENO((long) row.getCell(0).getNumericCellValue());
            data.setUSERNAME(row.getCell(1).getStringCellValue());
            data.setUSERDEPT(row.getCell(2).getStringCellValue());
            data.setUSER_POSITION(row.getCell(3).getStringCellValue());
            data.setUSER_PWD(passwordEncoder.encode(pwd));
            data.setUSER_EMAIL(row.getCell(5).getStringCellValue());
            data.setUSER_PHONE(row.getCell(6).getStringCellValue());
            data.setUSER_JOIN(joinStr);
            dataList.add(data);

            System.out.println(data);

            userRepository.save(data);
        }

//        model.addAttribute("datas", dataList); // 5

        return "boardDept/excel";

    }
}
