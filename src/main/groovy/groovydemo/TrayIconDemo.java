package groovydemo;

import javax.swing.*;
import java.awt.*;
import java.awt.TrayIcon.MessageType;

public class TrayIconDemo extends JFrame {

    public TrayIconDemo() throws HeadlessException {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) throws AWTException {
        // All GUI work should be handled in the EDT.
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                try {
                    if (SystemTray.isSupported()) {
                        TrayIconDemo td = new TrayIconDemo();
                        td.displayTray();
                    } else {
                        System.err.println("System tray not supported!");
                    }
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void displayTray() throws AWTException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);

        trayIcon.displayMessage("Hello, World", "notification demo", MessageType.INFO);
        System.out.println("message dismissed?");
    }
}