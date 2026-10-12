package com.project.recilog.repository; // 리포지토리 패키지 위치 선언

import com.project.recilog.domain.RefreshToken; // 리프레시 토큰 엔티티 클래스 임포트
import org.springframework.data.jpa.repository.JpaRepository; // Spring Data JPA 기본 인터페이스 임포트
import java.util.Optional; // Optional 객체 임포트

/**
 * [JWT 리프레시 토큰(RefreshToken) 엔티티 전담 JPA 리포지토리 인터페이스]
 * - 로그인 토큰 세션 관리, 재발급, 로그아웃 시 토큰 폐기 처리
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> { // 엔티티 타입 RefreshToken, PK 타입 Long 지정

    /**
     * [쿼리 메서드] 회원 ID를 기준으로 DB에 저장된 Refresh Token 조회
     * - 로그인 시 기존 발급된 토큰이 있는지 확인하고 덮어씌우기(Update) 위해 사용됨
     *
     * @param userId 회원 고유 식별자 PK
     * @return 저정된 RefreshToken 엔티티를 담은 Optional 객체
     */
    Optional<RefreshToken> findByUserId(Long userId);

    /**
     * [쿼리 메서드] 전달받은 리프레시 토큰 문자열 자체로 DB 세션 조회
     * - Access Token 재발급 요청 시 클라이언트가 보낸 토큰이 DB에 실제 존재하는지 검증용
     *
     * @param refreshToken 클라이언트가 전달한 JWT Refresh Token 문자열
     * @return DB에서 일치하는 RefreshToken 엔티티
     */
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    /**
     * [쿼리 메서드] 특정 회원의 저장된 Refresh Token 삭제
     * - 로그아웃 수행 시 DB에서 리프레시 토큰을 완전히 제거하여 재발급을 차단함
     *
     * @param userId 로그아웃하는 회원의 PK ID
     */
    void deleteByUserId(Long userId);
}