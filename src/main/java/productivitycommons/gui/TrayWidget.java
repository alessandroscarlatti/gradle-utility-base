package productivitycommons.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Alessandro Scarlatti
 * @since Saturday , 7/27/2019
 */
public abstract class TrayWidget implements AutoCloseable {

    private SystemTray tray;
    private TrayIcon trayIcon;
    private String status;
    private Runnable trayIconAction = () -> {
    };
    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private String widgetName;
    private String widgetAboutHtml;

    public TrayWidget() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    widgetName = getWidgetName();
                    widgetAboutHtml = getWidgetAboutHtml();
                    trayIconAction = getWidgetAction();
                    tray = SystemTray.getSystemTray();

                    // retrieve icon form resources and scale it to fit the system tray
                    trayIcon = new TrayIcon(getWidgetIcon(), widgetName);
                    trayIcon.setImageAutoSize(true);
                    trayIcon.addActionListener(e -> {
                        if (trayIconAction != null)
                            executorService.submit(trayIconAction);
                    });

                    // add to system tray
                    tray.add(trayIcon);

                    // Create a pop-up menu components
                    PopupMenu popup = new PopupMenu();
                    MenuItem aboutItem = new MenuItem("About");
                    aboutItem.addActionListener(e -> {
                        JOptionPane.showMessageDialog(null, "<html><b>" + widgetName + "</b><br><br>" + widgetAboutHtml + "</html>", "About " + widgetName + "", JOptionPane.INFORMATION_MESSAGE);
                    });
                    popup.add(aboutItem);

                    // add custom components
                    configurePopupMenu(popup);
                    trayIcon.setPopupMenu(popup);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Error initializing tray widget.", e);
        }
    }

    protected Image getWidgetIcon() {
        try {
            return ImageIO.read(getClass().getResourceAsStream("/clock.png"));
        } catch (Exception e) {
            throw new RuntimeException("Error loading default icon.", e);
        }
    }

    protected abstract String getWidgetName();

    protected abstract String getWidgetAboutHtml();

    protected Runnable getWidgetAction() {
        return () -> {
        };
    }

    ;

    protected void configurePopupMenu(PopupMenu menu) {
    }

    public void setStatus(String status) {
        setStatus(status, "", MessageType.NONE, null);
    }

    public void setStatus(String status, String detail) {
        setStatus(status, detail, MessageType.NONE, null);
    }

    public void setStatus(String status, String detail, MessageType messageType) {
        setStatus(status, detail, messageType, null);
    }

    public void setStatus(String status, String detail, MessageType messageType, Runnable action) {
        Runnable setStatus = () -> {
            this.status = status;
            trayIcon.setToolTip(this.status);

            if (trayIconAction != null)
                trayIconAction = action;

            if (messageType != null)
                trayIcon.displayMessage("" + widgetName + ": " + status, detail, messageType);
        };

        if (SwingUtilities.isEventDispatchThread()) {
            setStatus.run();
        } else {
            SwingUtilities.invokeLater(setStatus);
        }
    }

    @Override
    public void close() {
        if (!executorService.isShutdown())
            executorService.shutdown();

        if (Arrays.asList(tray.getTrayIcons()).contains(trayIcon))
            tray.remove(trayIcon);
    }
}
