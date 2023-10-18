package by.nata.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ConfigHandler {

    private static ConfigHandler configHandler;
    private Config config;

    public static ConfigHandler getInstance() throws FileNotFoundException {
        if(configHandler == null) {
            configHandler = new ConfigHandler();
        }
        return configHandler;
    }

    private ConfigHandler() throws FileNotFoundException {
        this.config = loadConfig();
    }

    public Config loadConfig() throws FileNotFoundException {
        try (InputStream inputStream = getClass().getResourceAsStream("/config.yml")) {
            if (inputStream == null) {
                throw new FileNotFoundException("config.yml not found in the classpath.");
            }

            Constructor constructor = new Constructor(Config.class);
            Yaml yaml = new Yaml(constructor);
            return yaml.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Error loading config.yml", e);
        }
    }

    public Config getConfig() {
        return this.config;
    }

}
