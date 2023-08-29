package by.nata.db.connection;

import by.nata.db.connection.api.IDataSourceWrapper;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceC3PO implements IDataSourceWrapper {


    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
