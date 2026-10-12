package com.project.recilog.controller; // 컨트롤러 패키지 위치

import com.project.recilog.dto.ExpenseRequestDto; // Request DTO 임포트
import com.project.recilog.dto.ExpenseResponseDto; // Response DTO 임포트
import com.project.recilog.service.ExpenseService; // 서비스 인터페이스 임포트
import jakarta.validation.Valid; // 요청 값 유효성 검사 어노테이션 임포트
import lombok.RequiredArgsConstructor; // 생성자 자동 생성 어노테이션 임포트
import org.springframework.data.domain.Page; // 페이징 결과 객체 임포트
import org.springframework.data.domain.Pageable; // 페이징 요청 객체 임포트
import org.springframework.http.HttpHeaders; // HTTP 헤더 객체 임포트
import org.springframework.http.MediaType; // HTTP 미디어타입 객체 임포트
import org.springframework.http.ResponseEntity; // HTTP 응답 엔티티 객체 임포트
import org.springframework.security.core.annotation.AuthenticationPrincipal; // 시큐리티 인증 정보 추출 어노테이션 임포트
import org.springframework.web.bind.annotation.*; // Spring MVC REST 컨트롤러 어노테이션 전체 임포트

@RestController // JSON 데이터를 반환하는 RESTful 컨트롤러임을 선언
@RequestMapping("/api/v1/expenses") // 기본 매핑 API URL 공통 경로 설정
@RequiredArgsConstructor // final 필드 자동 생성자 의존성 주입
public class ExpenseController { // 지출 관리 REST API 컨트롤러

    private final ExpenseService expenseService; // 지출 관련 비즈니스 로직을 수행할 서비스 레이어 주입

    @PostMapping // HTTP POST 방식 요청 매핑 - 신규 지출 생성 API
    public ResponseEntity<ExpenseResponseDto> createExpense(@AuthenticationPrincipal Long userId, // JwtAuthenticationFilter에서 인증 후 Context에 등록한 로그인 사용자 ID 자동 세팅
                                                            @Valid @RequestBody ExpenseRequestDto dto) { // 클라이언트가 보낸 JSON Body 데이터를 DTO로 변환하고 @Valid로 유효성 검증
        return ResponseEntity.ok(expenseService.saveExpense(userId, dto)); // 서비스 호출 후 지출 저장 결과를 200 OK HTTP 응답 데이터로 반환
    }

    @GetMapping // HTTP GET 방식 요청 매핑 - 지출 목록 페이징/검색 조회 API
    public ResponseEntity<Page<ExpenseResponseDto>> getExpenses(@AuthenticationPrincipal Long userId, // 로그인된 사용자 ID
                                                                @RequestParam(required = false) Integer categoryId, // 카테고리 필터링 쿼리 파라미터 (선택값)
                                                                @RequestParam(required = false) String keyword, // 검색어 쿼리 파라미터 (선택값)
                                                                Pageable pageable) { // ?page=0&size=10 등의 페이징 정보를 담은 객체
        return ResponseEntity.ok(expenseService.getExpenses(userId, categoryId, keyword, pageable)); // 검색된 페이징 결과를 200 OK로 반환
    }

    @GetMapping("/excel") // HTTP GET 방식 요청 매핑 - 지출 내역 엑셀 다운로드 API
    public ResponseEntity<byte[]> downloadExcel(@AuthenticationPrincipal Long userId) { // 로그인된 사용자 ID 추출
        byte[] excelBytes = expenseService.exportToExcel(userId); // 서비스 레이어를 통해 생성된 엑셀 바이너리 byte 배열 획득
        return ResponseEntity.ok() // 200 OK 응답 생성 시작
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expenses.xlsx") // 브라우저가 파일을 감지하고 다운로드 창을 띄우도록 헤더 설정
                .contentType(MediaType.APPLICATION_OCTET_STREAM) // 응답 콘텐츠 타입을 바이너리 파일 다운로드 타입으로 지정
                .body(excelBytes); // 엑셀 파일 데이터 바디 세팅 후 반환
    }
}