package com.project.recilog.service.impl; // 서비스 구현체 패키지 위치

import com.project.recilog.domain.Category; // 카테고리 엔티티 임포트
import com.project.recilog.domain.Expense; // 지출 엔티티 임포트
import com.project.recilog.domain.ExpenseItem; // 세부 품목 엔티티 임포트
import com.project.recilog.domain.User; // 회원 엔티티 임포트
import com.project.recilog.dto.ExpenseRequestDto; // 입력 DTO 임포트
import com.project.recilog.dto.ExpenseResponseDto; // 응답 DTO 임포트
import com.project.recilog.repository.CategoryRepository; // 카테고리 리포지토리 임포트
import com.project.recilog.repository.ExpenseRepository; // 지출 리포지토리 임포트
import com.project.recilog.repository.UserRepository; // 회원 리포지토리 임포트
import com.project.recilog.service.ExpenseService; // 서비스 인터페이스 임포트
import com.project.recilog.util.ExcelUtil; // 엑셀 유틸리티 임포트
import lombok.RequiredArgsConstructor; // 생성자 주입 어노테이션 임포트
import org.springframework.data.domain.Page; // 페이징 객체 임포트
import org.springframework.data.domain.Pageable; // 페이징 요청 임포트
import org.springframework.stereotype.Service; // 서비스 컴포넌트 선언 어노테이션 임포트
import org.springframework.transaction.annotation.Transactional; // 트랜잭션 선언 어노테이션 임포트

import java.time.LocalDateTime; // 날짜 객체 임포트
import java.util.List; // 리스트 객체 임포트

@Service // Spring 컨테이너에 서비스 빈으로 등록
@RequiredArgsConstructor // final 필드 자동 의존성 주입 (DI)
@Transactional(readOnly = true) // 서비스 전체에 읽기 전용 트랜잭션 적용 (DB 성능 및 메모리 최적화)
public class ExpenseServiceImpl implements ExpenseService { // 지출 관리 비즈니스 로직 구현 클래스

    private final ExpenseRepository expenseRepository; // 지출 DB 처리 인터페이스 주입
    private final CategoryRepository categoryRepository; // 카테고리 DB 처리 인터페이스 주입
    private final UserRepository userRepository; // 사용자 DB 처리 인터페이스 주입
    private final ExcelUtil excelUtil; // Apache POI 엑셀 다운로드 유틸리티 주입

    @Override // 인터페이스 메서드 재정의
    @Transactional // 데이터 CUD (생성/수정/삭제) 발생 메서드이므로 쓰기 가능한 트랜잭션 설정 (원자성 및 Rollback 지원)
    public ExpenseResponseDto saveExpense(Long userId, ExpenseRequestDto dto) { // 지출 등록 핵심 비즈니스 로직
        User user = userRepository.findById(userId) // 1. 현재 접속한 사용자 존재 여부 DB 조회
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")); // 예외 발생 처리

        Category category = categoryRepository.findById(dto.getCategoryId()) // 2. 선택된 카테고리 존재 여부 DB 조회
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")); // 예외 발생 처리

        Expense expense = Expense.builder() // 3. 지출 헤더 Entity 객체 구성 시작
                .user(user) // 회원 참조 설정
                .category(category) // 카테고리 참조 설정
                .storeName(dto.getStoreName()) // 가맹점 상호명 설정
                .totalAmount(dto.getTotalAmount()) // 총 금액 설정
                .spentAt(dto.getSpentAt()) // 결제시간 설정
                .paymentMethod(dto.getPaymentMethod() != null ? dto.getPaymentMethod() : "CARD") // 결제수단 지정 (기본값 CARD)
                .memo(dto.getMemo()) // 메모 설정
                .build(); // 엔티티 객체 생성

        if (dto.getItems() != null && !dto.getItems().isEmpty()) { // 4. 세부 품목 리스트가 유효할 경우 반복 처리
            dto.getItems().forEach(itemDto -> { // 각 DTO 항목 반복
                ExpenseItem item = ExpenseItem.builder() // 세부 품목 Entity 생성
                        .itemName(itemDto.getItemName()) // 상품명 매핑
                        .quantity(itemDto.getQuantity()) // 수량 매핑
                        .unitPrice(itemDto.getUnitPrice()) // 단가 매핑
                        .subtotalPrice(itemDto.getSubtotalPrice()) // 소계 매핑
                        .build(); // 생성
                expense.addItem(item); // [1:N Cascade 연동] 헤더에 품목을 추가하면서 편의 메서드로 연관관계 주입
            });
        }

        Expense saved = expenseRepository.save(expense); // 5. DB 영속화 (CascadeType.ALL에 의해 세부 품목들도 함께 자동 INSERT 저장됨)
        return ExpenseResponseDto.fromEntity(saved); // 6. DTO 변환 후 결과를 최종 반환
    }

    @Override // 동적 조회 메서드 구현
    public Page<ExpenseResponseDto> getExpenses(Long userId, Integer categoryId, String keyword, Pageable pageable) {
        return expenseRepository.searchExpenses(userId, categoryId, keyword, pageable) // Repository에서 데이터 조회
                .map(ExpenseResponseDto::fromEntity); // Page 내 엔티티 항목들을 DTO로 1:1 맵핑 변환
    }

    @Override // 엑셀 다운로드 파일 생성 구현
    public byte[] exportToExcel(Long userId) { // 엑셀 바이너리 스트림 반환
        List<Expense> expenses = expenseRepository.findByUserUserIdAndSpentAtBetween( // 사용자의 전 기간 지출 내역을 DB에서 가져옴
                userId,
                LocalDateTime.of(2000, 1, 1, 0, 0), // 범위 시작
                LocalDateTime.of(2099, 12, 31, 23, 59) // 범위 끝
        );
        return excelUtil.createExpenseExcel(expenses); // Apache POI 유틸리티를 활용해 워크북 바이트 배열 생성 및 반환
    }
}