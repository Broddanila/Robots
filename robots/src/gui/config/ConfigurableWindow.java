package gui.config;

public interface ConfigurableWindow {
    
    String getWindowIdentifier();
    
    
    WindowConfig saveConfiguration();
    
    
    void loadConfiguration(WindowConfig config);
    

    default WindowConfig getDefaultConfiguration() {
        return WindowConfig.createDefault(getWindowIdentifier());
    }
}