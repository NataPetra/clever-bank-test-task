package by.nata.dao.db.connection;

import by.nata.config.Config;
import by.nata.config.ConfigHandler;
import by.nata.dao.db.connection.api.IDataSourceWrapper;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceC3PO implements IDataSourceWrapper {
    private static final ComboPooledDataSource dataSource = new ComboPooledDataSource();

    public DataSourceC3PO(Properties properties) throws PropertyVetoException, FileNotFoundException {
        ConfigHandler handler = ConfigHandler.getInstance();
        Config config = handler.getConfig();
        dataSource.setDriverClass(config.getDbDriver());
        dataSource.setJdbcUrl(config.getDbUrl());
        dataSource.setUser(config.getDbUser());
        dataSource.setPassword(config.getDbPassword());
    }

    private DataSourceC3PO() {
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void close() throws Exception {
        dataSource.close();
    }
}
