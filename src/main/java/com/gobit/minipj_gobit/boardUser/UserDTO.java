package com.gobit.minipj_gobit.boardUser;

import com.gobit.minipj_gobit.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private long id;
    private long eno;
    private String name;
    private String dept;
    private String position;
    private String email;
    private String phone;
    private String address;
    private String join;
    private String birth;
    private String exit;
    private char exitChk;
    private String imgPath;
    private String img;
    private byte[] defaultImg;

    public static UserDTO userToDTO(User user) {
        UserDTO userDTO = UserDTO.builder()
                .id(user.getUSERNUM())
                .eno(user.getUSERENO())
                .name(user.getUSERNAME())
                .dept(user.getUSERDEPT())
                .position(user.getUSERPOSITION())
                .email(user.getUSER_EMAIL())
                .phone(user.getUSER_PHONE())
                .address(user.getUSER_ADDRESS())
                .join(user.getUSER_JOIN())
                .birth(user.getUSER_BIRTH())
                .exit(user.getUSER_EXIT())
                .exitChk(user.getUSER_EXIT_CHK())
                .imgPath(user.getImagePath())
                .img(user.getUSERIMAGE())
                .defaultImg(user.getDefaultImage())
                .build();
        return userDTO;
    }
}
