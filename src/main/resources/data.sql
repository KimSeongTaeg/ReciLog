-- [1. 필수 기본 카테고리 데이터 초기화]
INSERT INTO categories (category_id, name, code, display_order) VALUES
                                                                    (1, '식비', 'FOOD', 1),
                                                                    (2, '카페/간식', 'CAFE', 2),
                                                                    (3, '교통/차량', 'TRANSPORT', 3),
                                                                    (4, '쇼핑/생필품', 'SHOPPING', 4),
                                                                    (5, '문화/취미', 'CULTURE', 5),
                                                                    (6, '주거/통신', 'HOUSING', 6),
                                                                    (7, '기타', 'ETC', 7);

-- [2. 시연용 테스트 계정 추가 (비밀번호: 1234)]
-- BCrypt 암호화 처리된 '1234' 비밀번호값: $2a$10$E2UPv7arXnm9ptpD6sJ3UuN4wE94J1iM2cRz1r2hT5a9v0f.E8J
INSERT INTO users (user_id, email, password, username, provider, role, status, created_at) VALUES
    (1, 'test@example.com', '$2a$10$E2UPv7arXnm9ptpD6sJ3UuN4wE94J1iM2cRz1r2hT5a9v0f.E8J', '테스트유저', 'LOCAL', 'ROLE_USER', 'ACTIVE', NOW());

-- [3. 시연용 샘플 지출 데이터 추가]
INSERT INTO expenses (expense_id, user_id, category_id, store_name, total_amount, spent_at, payment_method, memo, is_deleted) VALUES
                                                                                                                                  (1, 1, 1, '스타벅스 신촌점', 14500, NOW(), 'CARD', 'OCR 영수증 스캔 데이터', 0),
                                                                                                                                  (2, 1, 3, '카카오T 택시', 12800, NOW(), 'CARD', '야간 귀가 택시비', 0);

-- [4. 샘플 지출의 세부 품목 데이터 추가]
INSERT INTO expense_items (item_id, expense_id, item_name, quantity, unit_price, subtotal_price) VALUES
                                                                                                     (1, 1, '아이스 아메리카노 T', 2, 4500, 9000),
                                                                                                     (2, 1, '부드러운 생크림 카스테라', 1, 5500, 5500);