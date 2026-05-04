package gui.config;

import java.awt.Rectangle;

/**
 * Record для хранения состояния окна
 * @param identifier уникальный идентификатор окна (не зависит от локализации)
 * @param bounds границы окна (x, y, width, height)
 * @param isMaximized развернуто ли окно на весь экран
 * @param isIconified свернуто ли окно
 */
public record WindowConfig(
    String identifier,
    Rectangle bounds,
    boolean isMaximized,
    boolean isIconified
) {
    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 400;
    public static final int DEFAULT_X = 100;
    public static final int DEFAULT_Y = 100;
    
    public static WindowConfig createDefault(String identifier) {
        return new WindowConfig(
            identifier,
            new Rectangle(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT),
            false,
            false
        );
    }
}