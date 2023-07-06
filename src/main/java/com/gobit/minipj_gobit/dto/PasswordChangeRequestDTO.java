package com.gobit.minipj_gobit.dto;

import com.gobit.minipj_gobit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChangeRequestDTO {
    private long USERNUM;
    private String originPw;
    private String USER_PWD;



    public User DTOToEntity(){
        if (USERNUM < 1 || originPw == null || originPw.equals("")) {
            throw new IllegalArgumentException("Invalid USERNUM or originPw.");
        }

        User user = User.builder()
                .USERNUM(this.USERNUM)
                .USER_PWD(this.originPw)
                .build();

        return user;
    }


}
