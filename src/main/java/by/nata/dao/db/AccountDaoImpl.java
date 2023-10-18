package by.nata.dao.db;

import by.nata.dao.api.IAccountDao;
import by.nata.dao.db.connection.api.IDataSourceWrapper;
import by.nata.dto.AccountDto;
import by.nata.exception.InsufficientFundsException;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class AccountDaoImpl implements IAccountDao {

    public static final String DATABASE_CONNECTION_ERROR = "Database connection error";
    private final String SQL_GET_AMOUNT = "SELECT account_id, account_number, amount FROM clever_bank.account WHERE account_number = ?";
    private final String SQL_UPDATE_AMOUNT = "UPDATE clever_bank.account SET amount = ? WHERE account_id = ?;";
    private final String SQL_IS_CONTAIN = "SELECT account_id FROM clever_bank.account WHERE account_number = ?;";
    private final String SQL_GET_BALANCE = "SELECT amount FROM clever_bank.account WHERE account_number = ?;";
    private final String SQL_GET_ACCOUNTS = "SELECT account_number FROM clever_bank.account;";
    private final IDataSourceWrapper dataSourceWrapper;

    public AccountDaoImpl(IDataSourceWrapper dataSourceWrapper) {
        this.dataSourceWrapper = dataSourceWrapper;
    }

    @Override
    public AccountDto get(String accountNumber) {
        AccountDto accountDto = null;
        try (Connection connection = dataSourceWrapper.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_AMOUNT)
        ) {
            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long accountId = resultSet.getLong("account_id");
                String number = resultSet.getString("account_number");
                BigDecimal amount = resultSet.getBigDecimal("amount");
                accountDto = new AccountDto(accountId, number, amount);
            }
        } catch (SQLException e) {
            throw new RuntimeException(DATABASE_CONNECTION_ERROR, e);
        }
        return accountDto;
    }

    @Override
    public void updateAmount(AccountDto accountDto) {
        BigDecimal amount = accountDto.amount();

        try (Connection connection = dataSourceWrapper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_AMOUNT)) {

            preparedStatement.setBigDecimal(1, amount);
            preparedStatement.setLong(2, accountDto.id());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(DATABASE_CONNECTION_ERROR, e);
        }
    }

    @Override
    public boolean isAccountExists(String accountNumber) {
        boolean result = false;

        try (Connection connection = dataSourceWrapper.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_IS_CONTAIN)) {

            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result = true;
            }
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(DATABASE_CONNECTION_ERROR, e);
        }
        return result;
    }

    @Override
    public BigDecimal checkAccountBalance(String accountNumber) {
        BigDecimal balance = null;
        try (Connection connection = dataSourceWrapper.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_BALANCE)
        ) {
            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                balance = resultSet.getBigDecimal("amount");
            }
        } catch (SQLException e) {
            throw new RuntimeException(DATABASE_CONNECTION_ERROR, e);
        }
        return balance;
    }

    @Override
    public List<String> getAccounts() {
        List<String> accounts = new ArrayList<>();
        try (Connection connection = dataSourceWrapper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ACCOUNTS);
             ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                String account = resultSet.getString("account_number");
                accounts.add(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(DATABASE_CONNECTION_ERROR, e);
        }
        return accounts;
    }

    @Override
    public List<AccountDto> transferWithinDifferentBanks(String sAccount, String bAccount, BigDecimal amount) {
        List<AccountDto> accountDtos = new ArrayList<>();

        try (Connection connection = dataSourceWrapper.getConnection();
        ) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_AMOUNT);
            preparedStatement.setString(1, sAccount);
            ResultSet resultSet = preparedStatement.executeQuery();
            AccountDto sAccountDto = getAccountDto(resultSet);

            preparedStatement = connection.prepareStatement(SQL_GET_AMOUNT);
            preparedStatement.setString(1, bAccount);
            resultSet = preparedStatement.executeQuery();
            AccountDto bAccountDto = getAccountDto(resultSet);

            Savepoint getAccountsSavePoint = connection.setSavepoint("Get accounts");
            log.info("Creating savepoint...");

            if (sAccountDto.amount().compareTo(amount) < 0) {
                throw new InsufficientFundsException("You do not have enough funds to write off");
            }
            BigDecimal sUpdatedAmount = sAccountDto.amount().subtract(amount);
            AccountDto updateSAccountDto = new AccountDto(sAccountDto.id(), sAccountDto.accountNumber(), sUpdatedAmount);
            accountDtos.add(updateSAccountDto);
            BigDecimal bUpdatedAmount = bAccountDto.amount().add(amount);
            AccountDto updateBAccountDto = new AccountDto(bAccountDto.id(), bAccountDto.accountNumber(), bUpdatedAmount);
            accountDtos.add(updateBAccountDto);

            try {
                preparedStatement = connection.prepareStatement(SQL_UPDATE_AMOUNT);
                preparedStatement.setBigDecimal(1, sUpdatedAmount);
                preparedStatement.setLong(2, sAccountDto.id());
                preparedStatement.executeUpdate();

                preparedStatement = connection.prepareStatement(SQL_UPDATE_AMOUNT);
                preparedStatement.setBigDecimal(1, bUpdatedAmount);
                preparedStatement.setLong(2, bAccountDto.id());
                preparedStatement.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                log.info("SQLException. Executing rollback to savepoint...");
                connection.rollback(getAccountsSavePoint);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(DATABASE_CONNECTION_ERROR, e);
        }
        return accountDtos;
    }

    public AccountDto getAccountDto(ResultSet resultSet) throws SQLException {
        AccountDto accountDto = null;
        while (resultSet.next()) {
            long accountId = resultSet.getLong("account_id");
            String accountNumber = resultSet.getString("account_number");
            BigDecimal accountAmount = resultSet.getBigDecimal("amount");
            accountDto = new AccountDto(accountId, accountNumber, accountAmount);
        }
        return accountDto;
    }
}
