package by.nata.dao.db;

import by.nata.dao.api.ITransactionDao;
import by.nata.dao.db.connection.api.IDataSourceWrapper;
import by.nata.dto.TransactionDto;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


@RequiredArgsConstructor
public class TransactionDaoImpl implements ITransactionDao {

    private final String SQL_CREATE = """ 
            INSERT INTO clever_bank.transaction 
            (transaction_amount, account_s_id, account_b_id, transaction_date, type) 
            VALUES (?, ?, ?, ?, ?);""";

    private final String SQL_GET = """
            SELECT
                t.transaction_id,
                t.transaction_amount,
                t.transaction_date,
                s.account_number AS sender_account_number,
                bs.name AS sender_bank_name,
                b.account_number AS beneficiary_account_number,
                bb.name AS beneficiary_bank_name
            FROM
                clever_bank.transaction t
            JOIN
                clever_bank.account s ON t.account_s_id = s.account_id
            JOIN
                clever_bank.account b ON t.account_b_id = b.account_id
            JOIN
                clever_bank.bank bs ON s.bank_id = bs.bank_id
            JOIN
                clever_bank.bank bb ON b.bank_id = bb.bank_id
            WHERE
                t.transaction_id = (
                    SELECT MAX(transaction_id)
                    FROM clever_bank.transaction
                );
            """;

    private final IDataSourceWrapper dataSourceWrapper;

    @Override
    public void create(BigDecimal amount, Long sId, Long bId, String type) {
        try (Connection connection = dataSourceWrapper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE)) {

            preparedStatement.setBigDecimal(1, amount);
            preparedStatement.setLong(2, sId);
            preparedStatement.setLong(3, bId);
            preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
            preparedStatement.setString(5, type);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);
        }
    }

    @Override
    public TransactionDto getLast() {
        TransactionDto transactionDto = null;

        try (Connection connection = dataSourceWrapper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long transactionId = resultSet.getLong("transaction_id");
                BigDecimal amount = resultSet.getBigDecimal("transaction_amount");
                Date transactionDate = resultSet.getDate("transaction_date");
                String sAccount = resultSet.getString("sender_account_number");
                String sBank = resultSet.getString("sender_bank_name");
                String bAccount = resultSet.getString("beneficiary_account_number");
                String bBank = resultSet.getString("beneficiary_bank_name");
                transactionDto = new TransactionDto(
                        transactionId, amount, transactionDate,
                        sAccount, sBank, bAccount, bBank);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);
        }
        return transactionDto;
    }
}
