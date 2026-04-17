package gui;

import javax.swing.UIManager;

public class RussianLocalization {
    
    public static void setupFullRussianLocalization() {
                    
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        UIManager.put("OptionPane.cancelButtonText", "Отмена");
        UIManager.put("OptionPane.okButtonText", "OK");
        
        
        UIManager.put("OptionPane.inputDialogTitle", "Ввод данных");
        UIManager.put("OptionPane.messageDialogTitle", "Сообщение");
        UIManager.put("OptionPane.errorTitle", "Ошибка");
        UIManager.put("OptionPane.warningTitle", "Предупреждение");
        UIManager.put("OptionPane.informationTitle", "Информация");
        
        
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.fileNameLabelText", "Имя файла:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Тип файлов:");
        UIManager.put("FileChooser.acceptAllFileFilterText", "Все файлы");
        
        
        UIManager.put("ColorChooser.okText", "OK");
        UIManager.put("ColorChooser.cancelText", "Отмена");
        UIManager.put("ColorChooser.resetText", "Сброс");
        
        
        UIManager.put("InternalFrame.closeButtonToolTip", "Закрыть");
        UIManager.put("InternalFrame.iconButtonToolTip", "Свернуть");
        UIManager.put("InternalFrame.maxButtonToolTip", "Развернуть");
    }
}