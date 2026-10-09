DROP TABLE IF EXISTS refresh_tokens;
DROP TABLE IF EXISTS recurring_expenses;
DROP TABLE IF EXISTS budgets;
DROP TABLE IF EXISTS expense_items;
DROP TABLE IF EXISTS expenses;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NULL,
                       username VARCHAR(50) NOT NULL,
                       provider VARCHAR(20) NOT NULL DEFAULT 'LOCAL',
                       provider_id VARCHAR(100) NULL,
                       role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
                       status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                       created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE TABLE refresh_tokens (
                                token_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                user_id BIGINT NOT NULL,
                                refresh_token VARCHAR(500) NOT NULL UNIQUE,
                                expiry_date DATETIME NOT NULL,
                                CONSTRAINT fk_refresh_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE categories (
                            category_id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(50) NOT NULL,
                            code VARCHAR(30) NOT NULL UNIQUE,
                            display_order SMALLINT NOT NULL DEFAULT 0
);

CREATE TABLE expenses (
                          expense_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          user_id BIGINT NOT NULL,
                          category_id INT NOT NULL,
                          store_name VARCHAR(100) NOT NULL,
                          total_amount DECIMAL(12, 0) NOT NULL DEFAULT 0,
                          spent_at DATETIME NOT NULL,
                          payment_method VARCHAR(30) NOT NULL DEFAULT 'CARD',
                          memo VARCHAR(500) NULL,
                          is_deleted TINYINT(1) NOT NULL DEFAULT 0,
                          created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                          CONSTRAINT fk_expenses_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
                          CONSTRAINT fk_expenses_category FOREIGN KEY (category_id) REFERENCES categories (category_id)
);

CREATE TABLE expense_items (
                               item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               expense_id BIGINT NOT NULL,
                               item_name VARCHAR(150) NOT NULL,
                               quantity INT NOT NULL DEFAULT 1,
                               unit_price DECIMAL(10, 0) NOT NULL DEFAULT 0,
                               subtotal_price DECIMAL(12, 0) NOT NULL DEFAULT 0,
                               CONSTRAINT fk_items_expense FOREIGN KEY (expense_id) REFERENCES expenses (expense_id) ON DELETE CASCADE
);

CREATE TABLE budgets (
                         budget_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         category_id INT NOT NULL,
                         target_year_month CHAR(7) NOT NULL,
                         target_amount DECIMAL(12, 0) NOT NULL DEFAULT 0,
                         CONSTRAINT fk_budgets_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
                         CONSTRAINT fk_budgets_category FOREIGN KEY (category_id) REFERENCES categories (category_id),
                         CONSTRAINT uq_user_cat_month UNIQUE (user_id, category_id, target_year_month)
);

CREATE TABLE recurring_expenses (
                                    recurring_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    user_id BIGINT NOT NULL,
                                    category_id INT NOT NULL,
                                    store_name VARCHAR(100) NOT NULL,
                                    amount DECIMAL(12, 0) NOT NULL,
                                    payment_day INT NOT NULL,
                                    payment_method VARCHAR(30) NOT NULL DEFAULT 'CARD',
                                    memo VARCHAR(200) NULL,
                                    is_active TINYINT(1) NOT NULL DEFAULT 1,
                                    CONSTRAINT fk_recurring_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
                                    CONSTRAINT fk_recurring_category FOREIGN KEY (category_id) REFERENCES categories (category_id)
);

CREATE INDEX idx_expenses_user_spent ON expenses (user_id, spent_at, is_deleted);