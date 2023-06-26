package com.gobit.minipj_gobit.repository;//package com.gobit.minipj_gobit.repository;
//
//import com.gobit.minipj_gobit.Entity.Approval;
//import org.springframework.data.domain.Page;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.awt.print.Pageable;
//import java.util.List;
//
//// 2nd (06.21) ApprovalRepository 추가 완료. 페이징까지 완
//@Repository
//public interface ApprovalRepository extends JpaRepository<Approval, Long> {
//    Page<Approval> findbyUserNumandAppNumContaining(Pageable pageable, String searchKeyword);
//
////    query문으로 검색조건을 미리 만들어두기.
////    keyword가 string이니까 문서번호(int)는 제외. +
//    @Query("SELECT ap FROM Aproval ap WHERE ap.a LIKE %:keyword% " +
//            "AND %ap.appNum% LIKE %:keyword% " +
//            "AND u.phoneNumber LIKE %:keyword% ")
//    List<Approval> findByAllContaining(@Param("keyword") String keyword);
//}
//}
