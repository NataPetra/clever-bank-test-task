INSERT INTO clever_bank.client (full_name, phone_number, email)
VALUES
    ('Иванов Иван', '+375291234567', 'ivanov@example.com'),
    ('Петров Петр', '+375291234568', 'petrov@example.com'),
    ('Сидоров Сидор', '+375291234569', 'sidorov@example.com'),
    ('Александров Александр', '+375291234570', 'alex@example.com'),
    ('Егоров Егор', '+375291234571', 'egor@example.com'),
    ('Ольга Смирнова', '+375291234572', 'olga@example.com'),
    ('Анна Иванова', '+375291234573', 'anna@example.com'),
    ('Максим Козлов', '+375291234574', 'max@example.com'),
    ('Дмитрий Жуков', '+375291234575', 'dmitry@example.com'),
    ('Елена Васильева', '+375291234576', 'elena@example.com'),
    ('Светлана Морозова', '+375291234577', 'svetlana@example.com'),
    ('Андрей Павлов', '+375291234578', 'andrey@example.com'),
    ('Ирина Соколова', '+375291234579', 'irina@example.com'),
    ('Виктория Новикова', '+375291234580', 'victoria@example.com'),
    ('Артем Федоров', '+375291234581', 'artem@example.com'),
    ('Екатерина Лебедева', '+375291234582', 'ekaterina@example.com'),
    ('Михаил Степанов', '+375291234583', 'mikhail@example.com'),
    ('Анастасия Ковалева', '+375291234584', 'anastasia@example.com'),
    ('Павел Шишкин', '+375291234585', 'pavel@example.com'),
    ('Наталья Борисова', '+375291234586', 'natalia@example.com'),
    ('Алексей Григорьев', '+375291234587', 'alexey@example.com'),
    ('Валентина Макарова', '+375291234588', 'valentina@example.com'),
    ('Георгий Дмитриев', '+375291234589', 'georgiy@example.com'),
    ('Людмила Королева', '+375291234590', 'ludmila@example.com'),
    ('Людмила Кор', '+375291234590', 'ludmilc@example.com');

INSERT INTO clever_bank.bank (name)
VALUES
    ('Bank of America'),
    ('JPMorgan Chase'),
    ('Clever-Bank'),
    ('Wells Fargo'),
    ('Citibank');

DO $$
DECLARE
bank_ids INT[] := array[1, 2, 3, 4, 5];
    client_ids INT[] := array[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25];
    account_number_prefix VARCHAR := 'Account-';
    account_number_suffix INT;
    unique_account_numbers VARCHAR[] := '{}';
    new_account_number VARCHAR;
BEGIN
FOR i IN 1..40 LOOP
    LOOP
      account_number_suffix := floor(random() * 10000)::int;
      new_account_number := account_number_prefix || account_number_suffix;
      IF new_account_number = ANY(unique_account_numbers) THEN
        CONTINUE;
ELSE
        unique_account_numbers := unique_account_numbers || new_account_number;
        EXIT;
END IF;
END LOOP;

INSERT INTO clever_bank.account (account_number, amount, open_date, bank_id, client_id)
VALUES
    (new_account_number, (1000 + i * 100), CURRENT_DATE, bank_ids[(i % 5) + 1], client_ids[(i % 25) + 1]);
END LOOP;
END $$;