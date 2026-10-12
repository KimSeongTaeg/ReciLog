package com.project.recilog.service; // 서비스 인터페이스 패키지 위치

import com.project.recilog.domain.RecurringExpense; // 고정 지출 엔티티 클래스 임포트
import com.project.recilog.dto.RecurringExpenseRequestDto; // 고정 지출 등록 요청 DTO 클래스 임포트

import java.util.List; // 리스트 컬렉션 객체 임포트

/**
 * [매달 나가는 고정 지출/구독 서비스 관리 비즈니스 로직 인터페이스]
 * - 객체지향 다치성 및 추상화를 위해 Service 인터페이스 규격 선언
 */
public interface RecurringExpenseService { // 고정 지출 관리 인터페이스 선언

    /**
     * 회원의 새로운 고정 지출(월세, 넷플릭스 구독료 등)을 신규 등록하는 메서드
     *
     * @param userId 회원 고유 식별자 PK
     * @param dto    카테고리 ID, 결제처, 결제일자, 금액 정보가 담긴 DTO
     */
    void saveRecurringExpense(Long userId, RecurringExpenseRequestDto dto); // 고정 지출 등록 비즈니스 메서드 명세

    /**
     * 특정 회원의 현재 활성화된(isActive = true) 고정 지출 목록을 조회하는 메서드
     *
     * @param userId 회원 고유 식별자 PK
     * @return 활성화 상태의 고정 지출 엔티티 리스트
     */
    List<RecurringExpense> getActiveRecurringExpenses(Long userId); // 활성 고정 지출 목록 조회 비즈니스 메서드 명세
}