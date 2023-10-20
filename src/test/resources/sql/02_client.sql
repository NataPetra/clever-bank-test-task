CREATE TABLE clever_bank.client (
                        client_id SERIAL PRIMARY KEY,
                        full_name VARCHAR(255) NOT NULL,
                        phone_number VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL
);