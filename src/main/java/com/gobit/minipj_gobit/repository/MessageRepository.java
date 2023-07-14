//package com.gobit.minipj_gobit.repository;
//
//import com.gobit.minipj_gobit.entity.Message;
//import com.gobit.minipj_gobit.entity.User;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface MessageRepository extends JpaRepository<Message, Long> {
//    List<Message> findByUserAndReceiveUser(User user, User receiveUser);
//    @Query(value = "select count(m.chatCheck) from Message m where m.user = :user and m.receiveUser = :receiveUser and m.chatCheck = 0")
//    int findBycnt(User user, User receiveUser);
//}
