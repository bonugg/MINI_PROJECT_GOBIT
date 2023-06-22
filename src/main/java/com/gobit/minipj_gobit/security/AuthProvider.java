package com.gobit.minipj_gobit.security;

import com.gobit.minipj_gobit.Entity.User;
import com.gobit.minipj_gobit.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class AuthProvider implements AuthenticationProvider {
    @Autowired
    private HttpSession httpSession;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String ENO_STRING = (String) authentication.getPrincipal(); // 로그인 창에 입력한 id
            String PWD = (String) authentication.getCredentials(); // 로그인 창에 입력한 password
            System.out.println(ENO_STRING);
            System.out.println(PWD);
            long ENO;
            try {
                ENO = Long.parseLong(ENO_STRING);
            } catch (NumberFormatException e) {
                ENO = 0;
            }
            PasswordEncoder passwordEncoder = passwordEncoder();
            UsernamePasswordAuthenticationToken token;
            User user = userRepository.findByUSERENO(ENO).get();

            if (user != null && passwordEncoder.matches(PWD, user.getUSER_PWD())) { // 일치하는 user 정보가 있는지 확인
                List<GrantedAuthority> roles = new ArrayList<>();
                if(user.getUSER_POSITION().equals("팀장")){
                    roles.add(new SimpleGrantedAuthority("ROLE_MANAGER")); // 권한 부여
                }else {
                    roles.add(new SimpleGrantedAuthority("ROLE_USER")); // 권한 부여
                }
                token = new UsernamePasswordAuthenticationToken(user.getUSERENO(), null, roles);
                httpSession.setAttribute("user", user);

                return token;
            }
        }catch (NoSuchElementException ne){
        }
        throw new BadCredentialsException("");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }
}
