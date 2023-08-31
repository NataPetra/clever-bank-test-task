package by.nata.dao.db;

import by.nata.dao.api.IAccountDao;
import by.nata.dao.db.connection.api.IDataSourceWrapper;
import by.nata.dto.AccountDto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDaoImpl implements IAccountDao {

    private final String SQL_GET_AMOUNT = "SELECT account_id, account_number, amount FROM account WHERE account_number = ?";
    private final String SQL_UPDATE_AMOUNT = "UPDATE account SET amount = ? WHERE account_id = ?;";
    private final String SQL_IS_CONTAIN = "SELECT account_id FROM account WHERE account_number = ?;";
    private final String SQL_GET_BALANCE = "SELECT amount FROM account WHERE account_number = ?;";
    private final String SQL_GET_ACCOUNTS = "SELECT account_number FROM account;";
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
            statement.setString(1,accountNumber);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                accountDto= new AccountDto(
                        resultSet.getLong("account_id"),
                        resultSet.getString("account_number"),
                        resultSet.getBigDecimal("amount"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);
        }
        return accountDto;
    }

    @Override
    public void updateAmount(AccountDto accountDto) {
        BigDecimal amount = accountDto.amount();

        try(Connection connection = dataSourceWrapper.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_AMOUNT)){

            preparedStatement.setBigDecimal(1, amount);
            preparedStatement.setLong(2, accountDto.id());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException("Database connection error", e);
        }
    }

    @Override
    public boolean isAccountExists(String accountNumber) {
        boolean result = false;

        try(Connection connection = dataSourceWrapper.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_IS_CONTAIN)){

            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                result = true;
            }
            resultSet.close();

        }catch (SQLException e){
            throw new RuntimeException("Database connection error", e);
        }
        return result;
    }

    @Override
    public BigDecimal checkAccountBalance(String accountNumber) {
        BigDecimal balance = null;
        try (Connection connection = dataSourceWrapper.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_BALANCE)
        ) {
            statement.setString(1,accountNumber);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                balance = resultSet.getBigDecimal("amount");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);
        }
        return balance;
    }

    @Override
    public List<String> getAccounts() {
        List<String> accounts= new ArrayList<>();
        try (Connection connection =dataSourceWrapper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ACCOUNTS);
             ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()){
                String account = resultSet.getString("account_number");
                accounts.add(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);
        }
        return accounts;
    }
}
