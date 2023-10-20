package by.nata.service;

import by.nata.dao.db.AccountDaoImpl;
import by.nata.dao.db.TransactionDaoImpl;
import by.nata.dao.db.connection.DataSourceC3PO;
import by.nata.dto.AccountDto;
import by.nata.util.PostgreSQLTestContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(PostgreSQLTestContainer.class)
class AccountServiceIntegrationTest {

    private static AccountService accountService;
    private static String jdbcUrl;
    private static String username;
    private static String password;


    @BeforeAll
    static void setUp() throws PropertyVetoException {
        jdbcUrl = PostgreSQLTestContainer.postgreSQLContainer.getJdbcUrl();
        username = PostgreSQLTestContainer.postgreSQLContainer.getUsername();
        password = PostgreSQLTestContainer.postgreSQLContainer.getPassword();

        DataSourceC3PO dataSource = new DataSourceC3PO(jdbcUrl, username, password);

        accountService = new AccountService(
                new AccountDaoImpl(dataSource),
                new TransactionService(new TransactionDaoImpl(dataSource)));
    }

    @Test
    void testRefill() throws SQLException {

        AccountDto refill = accountService.refill("1234567890", 100.0);

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {

            String query = "SELECT amount FROM clever_bank.account WHERE account_number = '1234567890';";
            ResultSet resultSet = statement.executeQuery(query);

            assertTrue(resultSet.next());
            double amount = resultSet.getDouble("amount");
            assertEquals(refill.amount().doubleValue(), amount);
        }
    }
}
