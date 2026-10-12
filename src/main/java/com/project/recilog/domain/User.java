package com.project.recilog.domain; // 도메인 엔티티 패키지 위치

import jakarta.persistence.*; // JPA 영속성 어노테이션 임포트
import lombok.*; // Lombok 어노테이션 임포트
import java.time.LocalDateTime; // 생성일시 저장용 날짜 객체 임포트

@Entity // JPA가 관리하는 DB 테이블 매핑 엔티티 클래스 선언
@Table(name = "users") // DB의 "users" 테이블과 직접 매핑
@Getter // 모든 필드의 Getter 메서드 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 생성 (외부 객체 직접 생성을 제한하기 위해 PROTECTED)
@AllArgsConstructor // 모든 필드를 인자로 받는 전체 생성자 생성
@Builder // 안전한 객체 생성을 위한 빌더 디자인 패턴 적용
public class User { // 사용자 정보를 저장하는 엔티티

    @Id // PK(Primary Key) 기본키 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB Auto Increment 전략 사용
    @Column(name = "user_id") // DB 컬럼명 지정
    private Long userId; // 회원 고유 식별자 ID

    @Column(nullable = false, unique = true, length = 100) // 필수값, 중복 불가, 최대 100자
    private String email; // 로그인 계정 이메일

    @Column(length = 255) // 암호화된 비밀번호 저장용 컬럼
    private String password; // BCrypt 암호화 처리된 비밀번호

    @Column(nullable = false, length = 50) // 필수값, 최대 50자
    private String username; // 사용자 이름(닉네임)

    @Builder.Default // 빌더 기본값 명시
    @Column(nullable = false, length = 20) // 로그인 제공자 (LOCAL, GOOGLE 등)
    private String provider = "LOCAL"; // 기본값 LOCAL 지정

    @Column(name = "provider_id", length = 100) // 소셜 로그인 사용자 식별 ID (일반 로그인은 NULL)
    private String providerId; // 소셜 제공자 고유 ID

    @Builder.Default // 빌더 기본값 명시
    @Column(nullable = false, length = 20) // 사용자 권한 (ROLE_USER, ROLE_ADMIN)
    private String role = "ROLE_USER"; // 기본 사용자 권한 지정

    @Builder.Default // 빌더 기본값 명시
    @Column(nullable = false, length = 20) // 계정 상태 (ACTIVE, BLOCKED 등)
    private String status = "ACTIVE"; // 기본 활성화 상태 지정

    @Column(name = "created_at", nullable = false, updatable = false) // 생성일시 (수정 불가)
    private LocalDateTime createdAt; // 회원가입 일시

    @PrePersist // DB INSERT 직전에 자동으로 실행되는 JPA 훅 메서드
    public void prePersist() { // 저장 전 처리 메서드
        this.createdAt = LocalDateTime.now(); // 현재 일시로 가입시간 자동 채우기
    }
}