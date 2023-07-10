package com.gobit.minipj_gobit.repository;

import com.gobit.minipj_gobit.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUSERENO(long ENO);
    List<User> findByUSERDEPT(String dept);
    Optional<User> findByUSERNAME(String username);
    Optional<User> findByUSERNUM(long usernum);
    List<User> findByUSERNAMEContainingAndUSERPOSITIONContainingAndUSERDEPT(String searchText,String searchTag, String userdept);
    List<User> findAll(Specification<User> spec);

}
