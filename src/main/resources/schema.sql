-- [1. 기존 테이블 존재 시 외래키 제약조건 문제 방지를 위해 역순 삭제]
DROP TABLE IF EXISTS expense_items;
DROP TABLE IF EXISTS expenses;
DROP TABLE IF EXISTS budgets;
DROP TABLE IF EXISTS recurring_expenses;
DROP TABLE IF EXISTS refresh_tokens;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

-- [2. 회원 테이블 (users) 생성]
CREATE TABLE users (
                       user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '회원 고유 식별자 PK',
                       email VARCHAR(100) NOT NULL UNIQUE COMMENT '로그인 이메일 (유니크)',
                       password VARCHAR(255) COMMENT 'BCrypt 암호화된 비밀번호',
                       username VARCHAR(50) NOT NULL COMMENT '회원 이름/닉네임',
                       provider VARCHAR(20) NOT NULL DEFAULT 'LOCAL' COMMENT '로그인 제공자 (LOCAL, GOOGLE)',
                       provider_id VARCHAR(100) COMMENT '소셜 로그인 식별자 ID',
                       role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER' COMMENT '권한 (ROLE_USER, ROLE_ADMIN)',
                       status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '계정 상태 (ACTIVE, BLOCKED)',
                       created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가입 일시'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- [3. 카테고리 테이블 (categories) 생성]
CREATE TABLE categories (
                            category_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '카테고리 PK',
                            name VARCHAR(50) NOT NULL COMMENT '한글 카테고리명 (식비, 교통 등)',
                            code VARCHAR(30) NOT NULL UNIQUE COMMENT '영문 식별 코드 (FOOD, TRANSPORT 등)',
                            display_order INT NOT NULL DEFAULT 0 COMMENT '화면 정렬 순서'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- [4. 지출 헤더 테이블 (expenses) 생성]
CREATE TABLE expenses (
                          expense_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '지출 고유 PK',
                          user_id BIGINT NOT NULL COMMENT '지출 회원 외래키 FK',
                          category_id INT NOT NULL COMMENT '카테고리 외래키 FK',
                          store_name VARCHAR(100) NOT NULL COMMENT '가맹점 상호명',
                          total_amount DECIMAL(12,0) NOT NULL COMMENT '총 결제 금액',
                          spent_at DATETIME NOT NULL COMMENT '영수증 결제 일시',
                          payment_method VARCHAR(30) NOT NULL DEFAULT 'CARD' COMMENT '결제 수단 (CARD, CASH)',
                          memo VARCHAR(500) COMMENT '추가 메모',
                          is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '소프트 삭제 여부 (0:활성, 1:삭제)',
                          CONSTRAINT fk_expenses_user FOREIGN KEY (user_id) REFERENCES users (user_id),
                          CONSTRAINT fk_expenses_category FOREIGN KEY (category_id) REFERENCES categories (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- [5. 영수증 세부 품목 테이블 (expense_items) 생성 - 1:N 관계]
CREATE TABLE expense_items (
                               item_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '세부 품목 PK',
                               expense_id BIGINT NOT NULL COMMENT '부모 지출 헤더 FK',
                               item_name VARCHAR(150) NOT NULL COMMENT '구매 상품명',
                               quantity INT NOT NULL COMMENT '구매 수량',
                               unit_price DECIMAL(10,0) NOT NULL COMMENT '개당 단가',
                               subtotal_price DECIMAL(12,0) NOT NULL COMMENT '수량x단가 소계 금액',
                               CONSTRAINT fk_items_expense FOREIGN KEY (expense_id) REFERENCES expenses (expense_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- [6. 월별 목표 예산 테이블 (budgets) 생성]
CREATE TABLE budgets (
                         budget_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '예산 설정 PK',
                         user_id BIGINT NOT NULL COMMENT '회원 FK',
                         category_id INT NOT NULL COMMENT '카테고리 FK',
                         target_year_month VARCHAR(7) NOT NULL COMMENT '목표 연월 (YYYY-MM)',
                         target_amount DECIMAL(12,0) NOT NULL COMMENT '목표 설정 금액',
                         CONSTRAINT uq_user_cat_month UNIQUE (user_id, category_id, target_year_month),
                         CONSTRAINT fk_budgets_user FOREIGN KEY (user_id) REFERENCES users (user_id),
                         CONSTRAINT fk_budgets_category FOREIGN KEY (category_id) REFERENCES categories (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- [7. 고정 지출/구독 테이블 (recurring_expenses) 생성]
CREATE TABLE recurring_expenses (
                                    recurring_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '고정 지출 PK',
                                    user_id BIGINT NOT NULL COMMENT '회원 FK',
                                    category_id INT NOT NULL COMMENT '카테고리 FK',
                                    store_name VARCHAR(100) NOT NULL COMMENT '지출처 상호명',
                                    amount DECIMAL(12,0) NOT NULL COMMENT '정기 결제 금액',
                                    payment_day INT NOT NULL COMMENT '매달 결제일자 (1~31)',
                                    payment_method VARCHAR(30) NOT NULL DEFAULT 'CARD' COMMENT '결제 수단',
                                    memo VARCHAR(200) COMMENT '메모',
                                    is_active TINYINT(1) NOT NULL DEFAULT 1 COMMENT '구독/활성화 여부 (1:활성, 0:해지)',
                                    CONSTRAINT fk_recurring_user FOREIGN KEY (user_id) REFERENCES users (user_id),
                                    CONSTRAINT fk_recurring_category FOREIGN KEY (category_id) REFERENCES categories (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- [8. JWT 리프레시 토큰 테이블 (refresh_tokens) 생성]
CREATE TABLE refresh_tokens (
                                token_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '토큰 PK',
                                user_id BIGINT NOT NULL COMMENT '회원 PK',
                                refresh_token VARCHAR(500) NOT NULL UNIQUE COMMENT 'JWT Refresh Token 문자열',
                                expiry_date DATETIME NOT NULL COMMENT '토큰 만료일시'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;