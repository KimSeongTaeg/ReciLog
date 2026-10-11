package com.project.recilog.util;

import com.project.recilog.dto.ExpenseRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class OcrClientUtil {

    public ExpenseRequestDto parseReceiptImage(MultipartFile file) {
        // 영수증 OCR 모킹 로직 (실제 외부 API 연결 포인트)
        return new ExpenseRequestDto();
    }
}