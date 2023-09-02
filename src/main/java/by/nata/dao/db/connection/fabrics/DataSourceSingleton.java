package by.nata.dao.db.connection.fabrics;

import by.nata.dao.db.connection.DataSourceC3PO;
import by.nata.dao.db.connection.api.IDataSourceWrapper;
import lombok.extern.log4j.Log4j2;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;
import java.util.Properties;

@Log4j2
public class DataSourceSingleton {
    private static Properties properties;
    private volatile static IDataSourceWrapper instance;

    public DataSourceSingleton() {
    }

    public static void setProperties(Properties properties) {
        synchronized (DataSourceSingleton.class) {
            if (instance != null) {
                throw new IllegalStateException("You can not change the settings when a connection to the database has already been created");
            }
            DataSourceSingleton.properties = properties;
        }
    }

    public static IDataSourceWrapper getInstance() throws PropertyVetoException, FileNotFoundException {
        log.info("Create DataSourceC3PO instance...");
        if (instance == null) {
            synchronized (DataSourceSingleton.class) {
                if (instance == null) {
                    instance = new DataSourceC3PO(properties);
                }
            }
        }
        return instance;
    }
}
