package gui;

import gui.config.WindowStateManager;

import gui.config.ConfigurableWindow;
import log.Logger;
import java.awt.event.KeyEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final WindowStateManager stateManager = new WindowStateManager();
    private final List<ConfigurableWindow> windows = new ArrayList<>();
    
    private LogWindow logWindow;
    private GameWindow gameWindow;
    
    public MainApplicationFrame() {
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width - inset * 2,
            screenSize.height - inset * 2);

        setContentPane(desktopPane);
        
        createWindows();
        
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
    }
    
    private void createWindows() {
        logWindow = new LogWindow(Logger.getDefaultLogSource());
        gameWindow = new GameWindow();
        
        windows.add(logWindow);
        windows.add(gameWindow);
        
        stateManager.loadConfigurationsWithDefaults(windows);
        
        restoreWindowStates();
        
        addWindow(logWindow);
        addWindow(gameWindow);
        
        Logger.debug("Протокол работает");
    }
    
    private void restoreWindowStates() {
        for (ConfigurableWindow window : windows) {
            stateManager.restoreWindowState(window);
        }
    }
    
    private void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    
    private void exitApplication() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Вы действительно хотите выйти из приложения?",
            "Подтверждение выхода",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            // Сохраняем состояния всех окон перед выходом
            stateManager.saveConfigurations(windows);
            dispose();
        }
    }
    
    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createExitMenu());
        return menuBar;
    }
    
    private JMenu createLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        
        JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        });
        lookAndFeelMenu.add(systemLookAndFeel);

        JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_U);
        crossplatformLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        });
        lookAndFeelMenu.add(crossplatformLookAndFeel);
        
        return lookAndFeelMenu;
    }
    
    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        
        JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            Logger.debug("Новая строка");
        });
        testMenu.add(addLogMessageItem);
        
        return testMenu;
    }
    
    private JMenu createExitMenu() {
        JMenu exitMenu = new JMenu("Приложение");
        exitMenu.setMnemonic(KeyEvent.VK_A);
        
        JMenuItem exitItem = new JMenuItem("Выход", KeyEvent.VK_X);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        exitItem.addActionListener((event) -> exitApplication());
        exitMenu.add(exitItem);
        
        return exitMenu;
    }
    
    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}