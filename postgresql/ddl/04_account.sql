CREATE TABLE clever_bank.account (
                         account_id SERIAL PRIMARY KEY,
                         account_number VARCHAR(255) NOT NULL UNIQUE,
                         amount NUMERIC(19, 2) NOT NULL,
                         open_date DATE NOT NULL,
                         bank_id BIGINT NOT NULL,
                         client_id BIGINT NOT NULL,
                         FOREIGN KEY (bank_id) REFERENCES clever_bank.bank (bank_id),
                         FOREIGN KEY (client_id) REFERENCES clever_bank.client (client_id)
);