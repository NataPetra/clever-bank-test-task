package by.nata.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ConfigHandler {

    private static ConfigHandler configHandler;
    private final Config config;

    public static ConfigHandler getInstance() {
        if (configHandler == null) {
            configHandler = new ConfigHandler();
        }
        return configHandler;
    }

    private ConfigHandler() {
        this.config = loadConfig();
    }

    public Config loadConfig() {
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
