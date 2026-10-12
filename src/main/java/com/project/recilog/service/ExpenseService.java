package com.project.recilog.service; // 서비스 인터페이스 패키지 위치 선언

import com.project.recilog.dto.ExpenseRequestDto; // 지출 등록 요청 DTO 임포트
import com.project.recilog.dto.ExpenseResponseDto; // 지출 응답 DTO 임포트
import org.springframework.data.domain.Page; // 페이징 결과 처리를 위한 Spring Data 인터페이스 임포트
import org.springframework.data.domain.Pageable; // 페이징 요청 객체 임포트

/**
 * [가계부 지출 내역 관리 비즈니스 로직 인터페이스]
 * - 1:N 지출 저장, 동적 조회/페이징, 엑셀 스트림 추출 로직 규격 정의
 */
public interface ExpenseService { // 지출 관리 서비스 인터페이스 선언

    /**
     * 영수증 지출 헤더 및 세부 품목(1:N)을 DB에 저장하는 메서드
     *
     * @param userId 지출을 등록하는 회원의 고유 식별자 PK
     * @param dto    지출 정보(상호명, 금액, 결제일시 등) 및 세부 품목 리스트가 담긴 DTO
     * @return 저장 완료된 지출 상세 데이터 DTO
     */
    ExpenseResponseDto saveExpense(Long userId, ExpenseRequestDto dto); // 지출 저장 비즈니스 메서드 명세

    /**
     * 조건별(카테고리, 검색 키워드) 지출 목록을 오프셋 기반 페이징으로 조회하는 메서드
     *
     * @param userId     현재 로그인한 회원의 PK ID
     * @param categoryId 조회할 카테고리 ID (선택 조건)
     * @param keyword    상호명/메모 검색어 (선택 조건)
     * @param pageable   페이징 및 정렬 요청 객체
     * @return DTO 형태로 가공된 지출 목록 페이징 데이터
     */
    Page<ExpenseResponseDto> getExpenses(Long userId, Integer categoryId, String keyword, Pageable pageable); // 지출 목록 페이징 조회 메서드 명세

    /**
     * 회원의 전체 지출 내역을 Apache POI 기반 엑셀 파일(byte[])로 생성하여 반환하는 메서드
     *
     * @param userId 다운로드를 요청한 회원의 PK ID
     * @return 엑셀 파일 바이너리 스트림 데이터 (byte[])
     */
    byte[] exportToExcel(Long userId); // 엑셀 다운로드 바이너리 생성 메서드 명세
}