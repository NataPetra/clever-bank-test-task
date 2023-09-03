package by.nata.config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigHandler {

    public static final Path configPath = Paths.get("src/main/resources/config.yml");

    private static ConfigHandler configHandler;

    //for war version
//    static {
//        Path path = null;
//        try {
//            URL configUrl = ConfigHandler.class.getResource("/config.yml");
//            URI configUri = configUrl.toURI();
//            path = Paths.get(configUri);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        configPath = path;
//    }

    Config config;

    public static ConfigHandler getInstance() throws FileNotFoundException {
        return getInstance(configPath);
    }

    public static ConfigHandler getInstance(Path configPath) throws FileNotFoundException {
        if(configHandler == null) {
            configHandler = new ConfigHandler(configPath);
        }
        return configHandler;
    }

    private ConfigHandler(Path configPath) throws FileNotFoundException {
        this.config = loadConfig(configPath);
    }

    public Config loadConfig(Path configPath) throws FileNotFoundException {
        Constructor constructor = new Constructor(Config.class);
        Yaml yaml = new Yaml(constructor);
        return yaml.load(new FileInputStream(configPath.toFile()));
    }

    public void dumpConfig() throws IllegalArgumentException, IOException {
        dumpConfig(this.config, this.configPath);
    }

    public void dumpConfig(Config config, Path configPath) throws IllegalArgumentException, IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yml = new Yaml(options);
        yml.dump(config, new FileWriter(configPath.toFile()));
    }

    public Config getConfig() {
        return this.config;
    }

}
