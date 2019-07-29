package productivitycommons.gui;

import jdk.jfr.events.ExceptionThrownEvent;

import javax.swing.*;

/**
 * @author Alessandro Scarlatti
 * @since Sunday , 7/28/2019
 */
public class GuiUtils {

    public static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean choice(String prompt) {
        int choice = JOptionPane.showConfirmDialog(null, prompt, "Jenkins Agent: Confirm Action", JOptionPane.YES_NO_CANCEL_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }
}
