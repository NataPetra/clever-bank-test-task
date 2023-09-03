package by.nata.dao.db;

import by.nata.dao.db.connection.api.IDataSourceWrapper;
import by.nata.dto.TransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class TransactionDaoImplTest {

    private IDataSourceWrapper dataSourceWrapper;
    private TransactionDaoImpl transactionDao;

    @BeforeEach
    public void setUp() {
        dataSourceWrapper = mock(IDataSourceWrapper.class);
        transactionDao = new TransactionDaoImpl(dataSourceWrapper);
    }

    @Test
    void testCreate() throws SQLException {
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(dataSourceWrapper.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        BigDecimal amount = new BigDecimal("100.00");
        Long sId = 1L;
        Long bId = 2L;
        String type = "TRANSFER";

        transactionDao.create(amount, sId, bId, type);

        verify(preparedStatement).setBigDecimal(1, amount);
        verify(preparedStatement).setLong(2, sId);
        verify(preparedStatement).setLong(3, bId);
        verify(preparedStatement).setDate(4, Date.valueOf(LocalDate.now()));
        verify(preparedStatement).setString(5, type);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testGetLast() throws SQLException {
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(dataSourceWrapper.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("transaction_id")).thenReturn(1L);
        when(resultSet.getBigDecimal("transaction_amount")).thenReturn(new BigDecimal("100.00"));
        when(resultSet.getDate("transaction_date")).thenReturn(Date.valueOf(LocalDate.now()));
        when(resultSet.getString("sender_account_number")).thenReturn("12345");
        when(resultSet.getString("sender_bank_name")).thenReturn("BankA");
        when(resultSet.getString("beneficiary_account_number")).thenReturn("67890");
        when(resultSet.getString("beneficiary_bank_name")).thenReturn("BankB");

        TransactionDto transactionDto = transactionDao.getLast();

        assertNotNull(transactionDto);
        assertEquals(1L, transactionDto.id());
        assertEquals(new BigDecimal("100.00"), transactionDto.amount());
    }
}