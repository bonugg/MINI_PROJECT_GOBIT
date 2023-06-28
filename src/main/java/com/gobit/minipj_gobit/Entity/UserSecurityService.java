package com.gobit.minipj_gobit.Entity;

import com.gobit.minipj_gobit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> _user = this.userRepository.findByUSERNAME(username);

        if (_user.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        User user = _user.get();

        List<GrantedAuthority> authorities = new ArrayList<>();
        if(user.getUSER_POSITION().equals("팀장")){
            authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER")); // 권한 부여
        }else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // 권한 부여
        }

        return new org.springframework.security.core.userdetails.User(String.valueOf(user.getUSERENO()), user.getUSER_PWD() ,authorities);
    }
}
