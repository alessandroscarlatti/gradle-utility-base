package groovydemo

import javax.imageio.ImageIO
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import java.awt.*
import java.awt.TrayIcon.MessageType
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.ItemEvent
import java.awt.event.ItemListener

/**
 * @author Alessandro Scarlatti
 * @since Saturday , 7/27/2019
 */
class JenkinsGui implements AutoCloseable {

    private String url
    private SystemTray tray
    private TrayIcon trayIcon
    private String status

    JenkinsGui(String url) {
        SwingUtilities.invokeAndWait({
            this.url = url

            //get the system tray
            this.tray = SystemTray.getSystemTray()

            // retrieve icon form resources and scale it to fit the system tray
            trayIcon = new TrayIcon(ImageIO.read(getClass().getResourceAsStream("/jenkins.png")), "Jenkins Agent at: " + url)
            trayIcon.setImageAutoSize(true)

            // add to system tray
            tray.add(trayIcon)

            addPopupMenu()
        })
    }

    private void addPopupMenu() {
        PopupMenu popup = new PopupMenu();

        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(null, "About");
            }
        });

        final CheckboxMenuItem cb1 = new CheckboxMenuItem("Show Notifications");
        cb1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                if (cb1.getState()) {
                } else {
                }
            }
        });

        MenuItem exitItem = new MenuItem("Exit");
        //add actionlistner to Exit menu item
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                tray.remove(trayIcon);
            }
        });

        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb1);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);
    }

    void setStatus(String status, String detail, MessageType messageType = null) {
        this.status = status
        trayIcon.setToolTip(status)

        if (messageType != null)
            trayIcon.displayMessage(status, detail, messageType)
    }

    @Override
    void close() {
        tray.remove(trayIcon)
    }
}
