package com.gobit.minipj_gobit.boardUser;

import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDTO getUser(Long id) {
        User user = userRepository.findByUSERNUM(id).get();
        UserDTO userDTO = UserDTO.userToDTO(user);
        return userDTO;
    }
    public List<UserDTO> getList() {
        List<UserDTO> userDTOList = new ArrayList<>();

        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            UserDTO userDTO = UserDTO.userToDTO(user);
            userDTOList.add(userDTO);
        }

        return userDTOList;
    }

    public List<UserDTO> searchUser(String category,String categoryDetail,  String keyword) {
        List<UserDTO> userDTOList =  new ArrayList<>();
        List<User> userList;
        Specification<User> spec = searchAll(keyword);
        switch (category) {
            case "all" :
                break;
            case "dept" :
                spec = searchDept(categoryDetail, keyword);
                break;
            case "position" :
                spec = searchPosition(categoryDetail, keyword);
                break;
            case "name" :
                spec = searchName(keyword);
                break;
        }

        userList = userRepository.findAll(spec);
        for (User user : userList) {
            UserDTO userDTO = UserDTO.userToDTO(user);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }


    private Specification<User> searchAll(String keyword) {
        return (a, query, cb) -> {
            query.distinct(true);
            String likeKeyword = "%" + keyword + "%";
            return cb.or(
                    cb.like(a.get("USERNAME"), likeKeyword),
                    cb.like(a.get("USERDEPT"), likeKeyword),
                    cb.like(a.get("USERPOSITION"), likeKeyword)
            );
        };
    }

    private Specification<User> searchDept(String detail, String keyword) {
        return (a, query, cb) -> {
            query.distinct(true);
            String likeKeyword = "%" + keyword + "%";
            return cb.and(
                    cb.like(a.get("USERDEPT"), detail),
                    cb.like(a.get("USERNAME"), likeKeyword)
            );
        };
    }

    private Specification<User> searchPosition(String detail, String keyword) {
        return (a, query, cb) -> {
            query.distinct(true);
            String likeKeyword = "%" + keyword + "%";
            return cb.and(
                    cb.like(a.get("USERPOSITION"), detail),
                    cb.like(a.get("USERNAME"), likeKeyword)
            );
        };
    }

    private Specification<User> searchName(String keyword) {
        return (a, query, cb) -> {
            query.distinct(true);
            String likeKeyword = "%" + keyword + "%";
            return cb.like(a.get("USERNAME"), likeKeyword);
        };
    }

}
