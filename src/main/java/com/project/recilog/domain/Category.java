package com.project.recilog.domain; // 도메인 엔티티 패키지 위치

import jakarta.persistence.*; // JPA 표준 어노테이션 임포트
import lombok.*; // Lombok 어노테이션 임포트

@Entity // JPA 엔티티 클래스 선언
@Table(name = "categories") // DB의 "categories" 테이블 매핑
@Getter // Getter 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 기본 생성자
@AllArgsConstructor // 전체 필드 생성자
@Builder // 빌더 패턴 적용
public class Category { // 지출 카테고리 정보 엔티티

    @Id // PK 기본키 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    @Column(name = "category_id") // DB 컬럼명 지정
    private Integer categoryId; // 카테고리 PK ID

    @Column(nullable = false, length = 50) // 한글 카테고리명 (식비, 교통 등)
    private String name; // 카테고리 이름

    @Column(nullable = false, unique = true, length = 30) // 영문 코드명 (FOOD, TRANSPORT 등)
    private String code; // 카테고리 식별 코드

    @Builder.Default // 빌더 기본값 적용
    @Column(name = "display_order", nullable = false) // 화면 정렬 순서
    private Integer displayOrder = 0; // 정렬 순서 기본값 0
}