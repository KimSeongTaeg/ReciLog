package com.project.recilog.repository; // 리포지토리 패키지 위치 선언

import com.project.recilog.domain.User; // 회원 엔티티 클래스 임포트
import org.springframework.data.jpa.repository.JpaRepository; // Spring Data JPA 기본 인터페이스 임포트
import java.util.Optional; // Null-Safe한 객체 처리를 위한 Optional 클래스 임포트

/**
 * [회원(User) 엔티티 전담 JPA 리포지토리 인터페이스]
 * - JpaRepository<User, Long> 상속을 통해 기본적인 CRUD(save, findById, delete 등) 메서드가 자동 생성됨
 */
public interface UserRepository extends JpaRepository<User, Long> { // 엔티티 타입 User, PK 타입 Long 지정

    /**
     * [쿼리 메서드] 이메일을 기준으로 사용자 정보를 단건 조회
     * - JPA가 메서드명을 분석하여 "SELECT * FROM users WHERE email = ?" SQL을 자동 생성함
     *
     * @param email 로그인/가입 조회 시 사용할 이메일 주소
     * @return 조회된 회원 엔티티를 담은 Optional 객체 (회원이 없으면 Optional.empty() 반환)
     */
    Optional<User> findByEmail(String email); // 이메일 기준 회원 단건 조회 메서드 명세

    /**
     * [쿼리 메서드] 해당 이메일을 사용하는 회원이 존재하는지 여부 확인
     * - JPA가 "SELECT COUNT(*) > 0 FROM users WHERE email = ?" 쿼리로 변환하여 실행함
     *
     * @param email 중복 검사할 이메일 주소
     * @return 존재하면 true, 존재하지 않으면 false 반환
     */
    boolean existsByEmail(String email); // 이메일 중복 여부 확인 메서드 명세
}