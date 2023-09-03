package by.nata.dao.db;

import by.nata.dao.db.connection.api.IDataSourceWrapper;
import by.nata.dto.AccountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountDaoImplTest {

    @InjectMocks
    private AccountDaoImpl accountDao;

    @Mock
    private IDataSourceWrapper dataSourceWrapper;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    public static final String ACCOUNT_NUMBER = "1234567890";
    public static final String AMOUNT = "1000.00";
    public static final String ACCOUNT_NUMBER_2 = "2345678901";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGet() throws SQLException {
        String accountNumber = ACCOUNT_NUMBER;
        BigDecimal amount = new BigDecimal(AMOUNT);
        long accountId = 1L;

        when(dataSourceWrapper.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("account_id")).thenReturn(accountId);
        when(resultSet.getString("account_number")).thenReturn(accountNumber);
        when(resultSet.getBigDecimal("amount")).thenReturn(amount);

        AccountDto accountDto = accountDao.get(accountNumber);

        assertNotNull(accountDto);
        assertEquals(accountId, accountDto.id());
        assertEquals(accountNumber, accountDto.accountNumber());
        assertEquals(amount, accountDto.amount());

        verify(resultSet).close();
        verify(preparedStatement).close();
        verify(connection).close();
    }


    @Test
    void testUpdateAmount() throws SQLException {
        AccountDto accountDto = new AccountDto(1L, ACCOUNT_NUMBER, new BigDecimal("500.00"));

        when(dataSourceWrapper.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        accountDao.updateAmount(accountDto);

        verify(preparedStatement).setBigDecimal(1, accountDto.amount());
        verify(preparedStatement).setLong(2, accountDto.id());
        verify(preparedStatement).executeUpdate();

        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test
    void testIsAccountExists() throws SQLException {
        String accountNumber = ACCOUNT_NUMBER;

        when(dataSourceWrapper.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);

        boolean result = accountDao.isAccountExists(accountNumber);

        assertTrue(result);

        verify(resultSet).close();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test
    void testCheckAccountBalance() throws SQLException {
        String accountNumber = ACCOUNT_NUMBER;
        BigDecimal balance = new BigDecimal(AMOUNT);

        when(dataSourceWrapper.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getBigDecimal("amount")).thenReturn(balance);

        BigDecimal result = accountDao.checkAccountBalance(accountNumber);

        assertNotNull(result);
        assertEquals(balance, result);

        verify(resultSet).close();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test
    void testGetAccounts() throws SQLException {
        List<String> accounts = new ArrayList<>();
        accounts.add(ACCOUNT_NUMBER);
        accounts.add(ACCOUNT_NUMBER_2);

        when(dataSourceWrapper.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("account_number")).thenReturn(ACCOUNT_NUMBER, ACCOUNT_NUMBER_2);

        List<String> result = accountDao.getAccounts();

        assertNotNull(result);
        assertEquals(accounts.size(), result.size());
        assertTrue(result.containsAll(accounts));

        verify(resultSet).close();
        verify(preparedStatement).close();
        verify(connection).close();
    }

}