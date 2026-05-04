package gui.config;

import java.awt.Rectangle;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WindowStateManager {
    private static final Logger LOGGER = Logger.getLogger(WindowStateManager.class.getName());
    
    private final ConfigurationFileManager fileManager;
    private final Map<String, WindowConfig> configurations;
    
    private static final String KEY_BOUNDS_X = ".bounds.x";
    private static final String KEY_BOUNDS_Y = ".bounds.y";
    private static final String KEY_BOUNDS_WIDTH = ".bounds.width";
    private static final String KEY_BOUNDS_HEIGHT = ".bounds.height";
    private static final String KEY_MAXIMIZED = ".maximized";
    private static final String KEY_ICONIFIED = ".iconified";
    
    public WindowStateManager() {
        this.fileManager = new ConfigurationFileManager();
        this.configurations = new HashMap<>();
    }
    
    public void loadConfigurations() throws IllegalStateException {
        if (!fileManager.configExists()) {
            throw new IllegalStateException("Configuration file not found. Call saveConfigurations first or use defaults.");
        }
        
        Properties props = fileManager.loadProperties();
        configurations.clear();
        
        Set<String> identifiers = extractIdentifiers(props);
        
        for (String identifier : identifiers) {
            WindowConfig config = loadWindowConfig(identifier, props);
            if (config != null) {
                configurations.put(identifier, config);
            }
        }
        
        LOGGER.info("Loaded configurations for " + configurations.size() + " windows");
    }
    
    public void loadConfigurationsWithDefaults(List<? extends ConfigurableWindow> windows) {
        if (!fileManager.configExists()) {
            LOGGER.info("No config file found, using default configurations");
            for (ConfigurableWindow window : windows) {
                configurations.put(window.getWindowIdentifier(), window.getDefaultConfiguration());
            }
            return;
        }
        
        try {
            loadConfigurations();
            
            for (ConfigurableWindow window : windows) {
                String id = window.getWindowIdentifier();
                if (!configurations.containsKey(id)) {
                    configurations.put(id, window.getDefaultConfiguration());
                    LOGGER.info("Using default configuration for window: " + id);
                }
            }
        } catch (IllegalStateException e) {
            LOGGER.log(Level.WARNING, "Could not load configurations, using defaults", e);
            for (ConfigurableWindow window : windows) {
                configurations.put(window.getWindowIdentifier(), window.getDefaultConfiguration());
            }
        }
    }
    
    public void saveConfigurations(List<? extends ConfigurableWindow> windows) {
        Properties props = new Properties();
        
        for (ConfigurableWindow window : windows) {
            try {
                WindowConfig config = window.saveConfiguration();
                configurations.put(window.getWindowIdentifier(), config);
                saveWindowConfig(props, config);
                LOGGER.info("Saved config for window: " + window.getWindowIdentifier() + 
                           ", bounds: " + config.bounds() + 
                           ", iconified: " + config.isIconified());
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to save config for window: " + window.getWindowIdentifier(), e);
            }
        }
        
        fileManager.saveProperties(props);
        LOGGER.info("Saved configurations for " + windows.size() + " windows");
    }
    
    public void restoreWindowState(ConfigurableWindow window) {
        WindowConfig config = configurations.get(window.getWindowIdentifier());
        if (config != null) {
            try {
                window.loadConfiguration(config);
                LOGGER.info("Restored state for window: " + window.getWindowIdentifier() + 
                           ", iconified: " + config.isIconified());
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to restore window state: " + window.getWindowIdentifier(), e);
            }
        } else {
            LOGGER.warning("No configuration found for window: " + window.getWindowIdentifier());
        }
    }
    
    private Set<String> extractIdentifiers(Properties props) {
        Set<String> identifiers = new HashSet<>();
        for (String key : props.stringPropertyNames()) {
            if (key.endsWith(KEY_BOUNDS_X)) {
                String identifier = key.substring(0, key.length() - KEY_BOUNDS_X.length());
                identifiers.add(identifier);
            }
        }
        return identifiers;
    }
    
    private WindowConfig loadWindowConfig(String identifier, Properties props) {
        try {
            int x = Integer.parseInt(props.getProperty(identifier + KEY_BOUNDS_X));
            int y = Integer.parseInt(props.getProperty(identifier + KEY_BOUNDS_Y));
            int width = Integer.parseInt(props.getProperty(identifier + KEY_BOUNDS_WIDTH));
            int height = Integer.parseInt(props.getProperty(identifier + KEY_BOUNDS_HEIGHT));
            boolean maximized = Boolean.parseBoolean(props.getProperty(identifier + KEY_MAXIMIZED));
            boolean iconified = Boolean.parseBoolean(props.getProperty(identifier + KEY_ICONIFIED));
            
            if (width <= 0) width = WindowConfig.DEFAULT_WIDTH;
            if (height <= 0) height = WindowConfig.DEFAULT_HEIGHT;
            
            return new WindowConfig(identifier, new Rectangle(x, y, width, height), maximized, iconified);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid config for window: " + identifier, e);
            return null;
        }
    }
    
    private void saveWindowConfig(Properties props, WindowConfig config) {
        String prefix = config.identifier();
        Rectangle bounds = config.bounds();
        
        props.setProperty(prefix + KEY_BOUNDS_X, String.valueOf(bounds.x));
        props.setProperty(prefix + KEY_BOUNDS_Y, String.valueOf(bounds.y));
        props.setProperty(prefix + KEY_BOUNDS_WIDTH, String.valueOf(bounds.width));
        props.setProperty(prefix + KEY_BOUNDS_HEIGHT, String.valueOf(bounds.height));
        props.setProperty(prefix + KEY_MAXIMIZED, String.valueOf(config.isMaximized()));
        props.setProperty(prefix + KEY_ICONIFIED, String.valueOf(config.isIconified()));
    }
}