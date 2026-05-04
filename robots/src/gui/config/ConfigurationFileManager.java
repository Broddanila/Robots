package gui.config;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConfigurationFileManager {
    private static final Logger LOGGER = Logger.getLogger(ConfigurationFileManager.class.getName());
    private static final String CONFIG_FILE_NAME = ".robots-program-config.properties";
    private final Path configPath;
    
    public ConfigurationFileManager() {
        String userHome = System.getProperty("user.home");
        this.configPath = Paths.get(userHome, CONFIG_FILE_NAME);
    }
    
    public void saveProperties(Properties properties) {
        try (FileOutputStream output = new FileOutputStream(configPath.toFile())) {
            properties.store(output, "Robots Program Configuration");
            LOGGER.info("Configuration saved to: " + configPath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save configuration", e);
        }
    }
    
    
    public Properties loadProperties() {
        Properties properties = new Properties();
        
        if (!configPath.toFile().exists()) {
            LOGGER.info("Configuration file not found, using defaults");
            return properties;
        }
        
        try (FileInputStream input = new FileInputStream(configPath.toFile())) {
            properties.load(input);
            LOGGER.info("Configuration loaded from: " + configPath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load configuration", e);
        }
        
        return properties;
    }
    
    
    public boolean configExists() {
        return configPath.toFile().exists();
    }
}