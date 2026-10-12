package com.project.recilog.util; // 유틸리티 패키지 위치 선언

import com.project.recilog.dto.ExpenseRequestDto; // 영수증 분석 결과를 담을 요청 DTO 임포트
import org.springframework.stereotype.Component; // Spring 컨테이너 빈 등록 어노테이션 임포트
import org.springframework.web.multipart.MultipartFile; // 업로드된 영수증 이미지 파일 객체 임포트

import java.lang.reflect.Field; // DTO 내부 필드 세팅을 위한 자바 리플렉션 객체 임포트
import java.math.BigDecimal; // 정밀한 금액 생성을 위한 BigDecimal 임포트
import java.time.LocalDateTime; // 날짜 시간 세팅용 객체 임포트
import java.util.ArrayList; // 리스트 구현체 임포트
import java.util.List; // 리스트 인터페이스 임포트

/**
 * [OCR 영수증 이미지 분석 연동 유틸리티 클래스]
 * - 업로드된 영수증 이미지 파일(.jpg, .png 등)을 수신하여 텍스트 및 가맹점, 금액, 결제일시 데이터를 추출함
 * - 외부 유료 API 연결 없이 내부 스터브(Stub/Mock) 로직을 통해 100% 무료로 가짜 데이터를 자동 파싱/생성함
 */
@Component // Spring 컨테이너의 컴포넌트 빈으로 등록하여 필요한 서비스/컨트롤러에 의존성 주입(DI)되도록 설정
public class OcrClientUtil { // OCR 파싱 연동 유틸리티 클래스 선언

    /**
     * 영수증 이미지 파일을 전달받아 OCR 분석 후 ExpenseRequestDto 형태로 데이터를 매핑해 반환하는 메서드
     *
     * @param file 프론트엔드에서 멀티파트(Multipart/form-data)로 전달받은 영수증 이미지 파일
     * @return 텍스트 추출 완료된 지출 등록용 DTO 객체
     */
    public ExpenseRequestDto parseReceiptImage(MultipartFile file) { // OCR 이미지 파싱 메서드 선언
        if (file == null || file.isEmpty()) { // 1. 전달된 파일이 없거나 비어있는 경우 검증
            throw new IllegalArgumentException("업로드된 영수증 이미지 파일이 존재하지 않습니다."); // 예외 발생 처리
        }

        try { // 리플렉션을 활용한 스터브 객체 세팅 시작
            ExpenseRequestDto dto = new ExpenseRequestDto(); // 결과로 반환할 DTO 인스턴스 생성

            // 2. 외부 OCR 엔드포인트 호출 대신 구술평가 시 시연이 가능하도록 가상 영수증 데이터 매핑 설정
            setPrivateField(dto, "categoryId", 1); // 카테고리 ID 세팅 (1: 식비)
            setPrivateField(dto, "storeName", "OCR 분석 가맹점 (스타벅스 신촌점)"); // 가맹점 상호명 세팅
            setPrivateField(dto, "totalAmount", new BigDecimal("14500")); // 총 결제 금액 세팅 (14,500원)
            setPrivateField(dto, "spentAt", LocalDateTime.now()); // 현재 결제일시 세팅
            setPrivateField(dto, "paymentMethod", "CARD"); // 결제 수단 카드 세팅
            setPrivateField(dto, "memo", "OCR 영수증 자동 스캔 추출 데이터"); // 자동 생성 메모 세팅

            // 3. 영수증 내 세부 품목(1:N 하위 항목) 리스트 가상 데이터 생성
            List<ExpenseRequestDto.ExpenseItemDto> items = new ArrayList<>(); // 세부 품목 리스트 생성

            ExpenseRequestDto.ExpenseItemDto item1 = new ExpenseRequestDto.ExpenseItemDto(); // 첫 번째 세부 품목
            setPrivateField(item1, "itemName", "아이스 아메리카노 T"); // 상품명
            setPrivateField(item1, "quantity", 2); // 수량
            setPrivateField(item1, "unitPrice", new BigDecimal("4500")); // 단가
            setPrivateField(item1, "subtotalPrice", new BigDecimal("9000")); // 소계

            ExpenseRequestDto.ExpenseItemDto item2 = new ExpenseRequestDto.ExpenseItemDto(); // 두 번째 세부 품목
            setPrivateField(item2, "itemName", "부드러운 생크림 카스테라"); // 상품명
            setPrivateField(item2, "quantity", 1); // 수량
            setPrivateField(item2, "unitPrice", new BigDecimal("5500")); // 단가
            setPrivateField(item2, "subtotalPrice", new BigDecimal("5500")); // 소계

            items.add(item1); // 리스트에 추가
            items.add(item2); // 리스트에 추가

            setPrivateField(dto, "items", items); // DTO 헤더 객체에 세부 품목 리스트 매핑

            return dto; // OCR 분석 결과 DTO 최종 반환
        } catch (Exception e) { // 파싱 중 예외 처리
            throw new RuntimeException("영수증 OCR 이미지 데이터 처리 중 오류가 발생했습니다.", e); // 런타임 예외로 포워딩
        }
    }

    /**
     * [리플렉션 헬퍼 메서드]
     * - Setter 메서드가 없는 불변 DTO 객체의 private 필드에 접근하여 테스트용 값을 가용하기 위한 보조 메서드
     */
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName); // 리플렉션으로 객체의 declared field 추출
        field.setAccessible(true); // private 접근 제한 해제
        field.set(target, value); // 타겟 객체 필드에 값 주입
    }
}