package com.project.recilog.util; // 유틸리티 패키지 위치

import com.project.recilog.domain.Expense; // 지출 엔티티 임포트
import org.apache.poi.ss.usermodel.*; // Apache POI 워크북/시트/행/셀 인터페이스 임포트
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // Apache POI .xlsx 워크북 구현체 임포트
import org.springframework.stereotype.Component; // 스프링 빈 등록 어노테이션 임포트

import java.io.ByteArrayOutputStream; // 메모리 바이트 배열 스트림 임포트
import java.util.List; // 리스트 임포트

@Component // Spring의 일반 유틸리티 빈 컴포넌트로 등록
public class ExcelUtil { // 지출 데이터를 기반으로 엑셀 바이너리 스트림을 자동 생성하는 클래스

    public byte[] createExpenseExcel(List<Expense> expenses) { // 지출 리스트를 전달받아 엑셀 byte[] 생성
        // Try-with-resources 구문을 활용하여 사용 완료된 POI Workbook 및 Stream 메모리를 자동 release close 처리
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) { // .xlsx 워크북 객체 및 출력 스트림 생성
            Sheet sheet = workbook.createSheet("지출 내역"); // 1. 워크북 내에 "지출 내역" 이름의 시트 생성

            Row headerRow = sheet.createRow(0); // 2. 0번째 인덱스에 헤더 컬럼 행 생성
            String[] headers = {"ID", "카테고리", "상호명", "금액", "결제일시", "결제수단", "메모"}; // 컬럼 제목 배열
            for (int i = 0; i < headers.length; i++) { // 헤더 길이만큼 반복
                Cell cell = headerRow.createCell(i); // 셀 생성
                cell.setCellValue(headers[i]); // 컬럼명 셀 값 할당
            }

            int rowIdx = 1; // 3. 데이터 입력 시작 행 인덱스 지정 (1번 행부터 데이터 시작)
            for (Expense e : expenses) { // 데이터 리스트 반복
                Row row = sheet.createRow(rowIdx++); // 데이터 행 추가 생성
                row.createCell(0).setCellValue(e.getExpenseId()); // 셀 0: 지출 ID 할당
                row.createCell(1).setCellValue(e.getCategory().getName()); // 셀 1: 카테고리명 할당
                row.createCell(2).setCellValue(e.getStoreName()); // 셀 2: 가맹점 상호명 할당
                row.createCell(3).setCellValue(e.getTotalAmount().doubleValue()); // 셀 3: 결제 금액 할당
                row.createCell(4).setCellValue(e.getSpentAt().toString()); // 셀 4: 결제 일시 할당
                row.createCell(5).setCellValue(e.getPaymentMethod()); // 셀 5: 결제 수단 할당
                row.createCell(6).setCellValue(e.getMemo() != null ? e.getMemo() : ""); // 셀 6: 메모 정보 할당
            }

            workbook.write(out); // 4. 완성된 엑셀 워크북 내용을 메모리 바이트 스트림으로 작성
            return out.toByteArray(); // 5. 생성된 바이트 배열을 최종 결과물로 반환
        } catch (Exception e) { // 엑셀 생성 중 예외 발생 시 처리
            throw new RuntimeException("엑셀 생성 실패", e); // 런타임 예외 재발생
        }
    }
}