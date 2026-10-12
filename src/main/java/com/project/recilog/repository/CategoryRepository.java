package com.project.recilog.repository; // 리포지토리 패키지 위치 선언

import com.project.recilog.domain.Category; // 카테고리 엔티티 클래스 임포트
import org.springframework.data.jpa.repository.JpaRepository; // Spring Data JPA 기본 인터페이스 임포트

/**
 * [카테고리(Category) 엔티티 전담 JPA 리포지토리 인터페이스]
 * - JpaRepository 상속만으로 공통 CRUD 및 전체 목록 조회(findAll), 단건 조회(findById)가 구현체 없이 자동 준비됨
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> { // 엔티티 타입 Category, PK 타입 Integer 지정
    // 기본으로 제공되는 findAll(), findById() 등을 서비스 레이어에서 활용하므로 별도 커스텀 메서드 정의 없이 빈 관리됨
}