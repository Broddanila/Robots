package gui.config;
import javax.swing.SwingUtilities;  
import javax.swing.JInternalFrame;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ConfigurableInternalFrame extends JInternalFrame implements ConfigurableWindow {
    private static final Logger LOGGER = Logger.getLogger(ConfigurableInternalFrame.class.getName());
    
    public ConfigurableInternalFrame(String title, boolean resizable, boolean closable, 
                                      boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }
    
    @Override
    public WindowConfig saveConfiguration() {
        Rectangle bounds = getBounds();
        return new WindowConfig(
            getWindowIdentifier(),
            bounds,
            isMaximum(),
            isIcon()
        );
    }
    
    @Override
    public void loadConfiguration(WindowConfig config) {
        if (config == null) {
            return;
        }
        
        Rectangle bounds = config.bounds();
        if (bounds != null && bounds.width > 0 && bounds.height > 0) {
            setBounds(bounds);
        }
        
        try {
            if (isIconifiable() && config.isIconified()) {                
                setIcon(false);            
                SwingUtilities.invokeLater(() -> {
                    try {
                        if (!isClosed()) {
                            setIcon(true);
                        }
                    } catch (PropertyVetoException e) {
                        LOGGER.log(Level.WARNING, "Failed to iconify window: " + getWindowIdentifier(), e);
                    }
                });
            }
            
            if (!config.isIconified() && isMaximizable() && config.isMaximized()) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        if (!isClosed() && !isIcon()) {
                            setMaximum(true);
                        }
                    } catch (PropertyVetoException e) {
                        LOGGER.log(Level.WARNING, "Failed to maximize window: " + getWindowIdentifier(), e);
                    }
                });
            }
        } catch (PropertyVetoException e) {
            LOGGER.log(Level.WARNING, "Failed to set initial window state for: " + getWindowIdentifier(), e);
        }
    }
}