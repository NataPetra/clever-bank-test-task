package by.nata.dao.db;

import by.nata.dao.db.connection.api.IDataSourceWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

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

}