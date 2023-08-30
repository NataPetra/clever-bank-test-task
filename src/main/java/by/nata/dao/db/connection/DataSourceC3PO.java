package by.nata.dao.db.connection;

import by.nata.dao.db.connection.api.IDataSourceWrapper;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceC3PO implements IDataSourceWrapper {

    private static final String DRIVER_CLASS_PROPERTY_NAME = "db.driver";
    private static final String URL_PROPERTY_NAME = "db.url";
    private static final String USER_PROPERTY_NAME = "db.user";
    private static final String PASSWORD_PROPERTY_NAME = "db.password";
    private static ComboPooledDataSource dataSource;

    public DataSourceC3PO(Properties properties) throws PropertyVetoException {
        dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(properties.getProperty(DRIVER_CLASS_PROPERTY_NAME));
        dataSource.setJdbcUrl(properties.getProperty(URL_PROPERTY_NAME));
        dataSource.setUser(properties.getProperty(USER_PROPERTY_NAME));
        dataSource.setPassword(properties.getProperty(PASSWORD_PROPERTY_NAME));
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
