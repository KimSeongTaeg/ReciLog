package com.project.recilog.domain; // 도메인 엔티티 패키지 위치

import jakarta.persistence.*; // JPA 어노테이션 임포트
import lombok.*; // Lombok 어노테이션 임포트
import java.time.LocalDateTime; // 만료일시 저장용 임포트

@Entity // JPA 관리 엔티티 선언
@Table(name = "refresh_tokens") // DB "refresh_tokens" 테이블 매핑
@Getter // Getter 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자
@AllArgsConstructor // 전체 생성자
@Builder // 빌더 패턴
public class RefreshToken { // Access Token 재발급용 Refresh Token 저장 엔티티

    @Id // PK 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    @Column(name = "token_id") // DB 컬럼명
    private Long tokenId; // 토큰 식별 PK

    @Column(name = "user_id", nullable = false) // 토큰 소유 회원 ID
    private Long userId; // 회원 ID PK 참조

    @Column(name = "refresh_token", nullable = false, unique = true, length = 500) // 토큰 문자열 (중복 불가)
    private String refreshToken; // 실제 JWT 리프레시 토큰 값

    @Column(name = "expiry_date", nullable = false) // 토큰 만료 일시
    private LocalDateTime expiryDate; // 만료 일시

    public void updateToken(String token, LocalDateTime expiryDate) { // 토큰 정보 갱신 비즈니스 메서드
        this.refreshToken = token; // 새 토큰 교체
        this.expiryDate = expiryDate; // 새 만료일시 교체
    }
}