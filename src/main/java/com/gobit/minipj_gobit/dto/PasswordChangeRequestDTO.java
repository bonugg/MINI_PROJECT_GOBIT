package com.gobit.minipj_gobit.dto;

import lombok.Data;

@Data
public class PasswordChangeRequestDTO {
    private String originPw;
    private String changePw;

}
