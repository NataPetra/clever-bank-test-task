package by.nata.util;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

@Testcontainers
public class PostgreSQLTestContainer implements BeforeAllCallback, AfterAllCallback {

    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_clever_bank")
            .withUsername("postgres")
            .withPassword("postgres");

    @Override
    public void beforeAll(ExtensionContext context) throws IOException, SQLException {
        postgreSQLContainer.start();

        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        String username = postgreSQLContainer.getUsername();
        String password = postgreSQLContainer.getPassword();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {

            String createSchemaScript = getResourceFileAsString("01_init.sql");
            String createTableClientScript = getResourceFileAsString("02_client.sql");
            String createTableBankScript = getResourceFileAsString("03_bank.sql");
            String createTableAccountScript = getResourceFileAsString("04_account.sql");
            String createTableTransScript = getResourceFileAsString("05_transaction.sql");
            String insertDataSQL = getResourceFileAsString("06_data.sql");

            statement.execute(createSchemaScript);
            statement.execute(createTableClientScript);
            statement.execute(createTableBankScript);
            statement.execute(createTableAccountScript);
            statement.execute(createTableTransScript);
            statement.execute(insertDataSQL);
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        postgreSQLContainer.stop();
    }

    private static String getResourceFileAsString(String resourceName) throws IOException {
        try (InputStream resourceStream = PostgreSQLTestContainer.class.getResourceAsStream("/sql/" + resourceName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream, StandardCharsets.UTF_8))) {

            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

}
