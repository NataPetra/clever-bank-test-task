CREATE TABLE clever_bank.transaction (
                             transaction_id SERIAL PRIMARY KEY,
                             transaction_amount NUMERIC(19, 2) NOT NULL,
                             account_s_id BIGINT,
                             account_b_id BIGINT,
                             transaction_date DATE NOT NULL,
                             type VARCHAR(255) NOT NULL,
                             FOREIGN KEY (account_s_id) REFERENCES clever_bank.account (account_id),
                             FOREIGN KEY (account_b_id) REFERENCES clever_bank.account (account_id)
);