package com.project.recilog.controller;

import com.project.recilog.dto.ExpenseRequestDto;
import com.project.recilog.util.OcrClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/receipts")
@RequiredArgsConstructor
public class ReceiptOcrController {

    private final OcrClientUtil ocrClientUtil;

    @PostMapping("/ocr")
    public ResponseEntity<ExpenseRequestDto> uploadReceiptOcr(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 첨부되지 않았습니다.");
        }
        return ResponseEntity.ok(ocrClientUtil.parseReceiptImage(file));
    }
}