package com.project.recilog.util;

import com.project.recilog.domain.Expense;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

import java.util.List;

@Component
public class ExcelUtil {

    public byte[] createExpenseExcel(List<Expense> expenses) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("지출 내역");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "카테고리", "상호명", "금액", "결제일시", "결제수단", "메모"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowIdx = 1;
            for (Expense e : expenses) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(e.getExpenseId());
                row.createCell(1).setCellValue(e.getCategory().getName());
                row.createCell(2).setCellValue(e.getStoreName());
                row.createCell(3).setCellValue(e.getTotalAmount().doubleValue());
                row.createCell(4).setCellValue(e.getSpentAt().toString());
                row.createCell(5).setCellValue(e.getPaymentMethod());
                row.createCell(6).setCellValue(e.getMemo() != null ? e.getMemo() : "");
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("엑셀 생성 실패", e);
        }
    }
}