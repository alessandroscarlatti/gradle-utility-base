package productivitycommons.jenkins

import javax.imageio.ImageIO
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import java.awt.*
import java.awt.TrayIcon.MessageType
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import static java.time.LocalDateTime.now
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

/**
 * @author Alessandro Scarlatti
 * @since Saturday , 7/27/2019
 */
class JenkinsGui implements AutoCloseable {

    private String url
    private SystemTray tray
    private TrayIcon trayIcon
    private String status
    private Runnable trayIconAction = {}
    private ExecutorService executorService = Executors.newFixedThreadPool(1)

    JenkinsGui(String url) {
        SwingUtilities.invokeAndWait({
            this.url = url

            //get the system tray
            this.tray = SystemTray.getSystemTray()

            // retrieve icon form resources and scale it to fit the system tray
            trayIcon = new TrayIcon(ImageIO.read(getClass().getResourceAsStream("/jenkins.png")), "Jenkins Agent at: " + url)
            trayIcon.setImageAutoSize(true)

            trayIcon.addActionListener(new ActionListener() {
                @Override
                void actionPerformed(ActionEvent e) {
                    if (trayIconAction != null)
                        executorService.submit(trayIconAction)
                }
            })

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
                JOptionPane.showMessageDialog(null, "<html><b>Jenkins Agent System Tray Icon</b><br><br>Notification mechanism to alert a user while a Jenkins Agent script is running.<br>Notifications will appear here as jobs are executed.</html>", "About Jenkins Agent", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        MenuItem stopAllJobsNow = new MenuItem("Stop All Jobs Now")
        //add action listener to Exit menu item
        stopAllJobsNow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                boolean choice = choice("Are you sure you want to stop all jobs?")
                if (choice) {
                    // try to stop all jobs...
                    setStatus("Stopping all jobs.", "Attempting to stop all jobs now.", MessageType.INFO)

                    boolean success = false;

                    // if successful or not, show status
                    if (success) {
                        setStatus("All jobs stopped.", "All jobs were stopped at ${now()}.\nJenkin Agent will close automatically after a few seconds.", MessageType.INFO)
                    } else {
                        setStatus("Some jobs were not stopped.", "Some jobs already submitted or currently executing were not stopped at ${ISO_LOCAL_DATE_TIME.format(now())}.\n\nHowever, all future jobs were stopped successfully.\n\nJenkin Agent will close automatically after a few seconds.", MessageType.ERROR)
                    }
                }
            }
        });

        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(stopAllJobsNow);

        trayIcon.setPopupMenu(popup);
    }

    static String now() {
        DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(LocalDateTime.now())
    }

    void buildStarted(String jobName, String executionUrl = null) {
        setStatus("Building job " + jobName, "Building ${jobName} at ${now()}.${executionUrl != null ? "\nClick to go to build." : ""}\n\n", MessageType.INFO, {
            if (executionUrl != null)
                Desktop.getDesktop().browse(new URI("${executionUrl}/console"))
        })
    }

    void buildFailed(String jobName, String executionUrl = null) {
        setStatus("Build failed for " + jobName, "Build failed for ${jobName} at ${now()}.${executionUrl != null ? "\nClick to go to build." : ""}\n\n", MessageType.ERROR, {
            if (executionUrl != null)
                Desktop.getDesktop().browse(new URI("${executionUrl}/console"))
        })
    }

    void allExecutionsSuccessful() {
        setStatus("All executions successful.", "Congratulations! All executions successful at ${now()}.\nJenkins Agent will close automatically in a few seconds.", MessageType.INFO)
    }

    void someExecutionsFailed(String executionUrl = null) {
        setStatus("Some executions failed.", "Oh no! Some executions failed at ${now()}.${executionUrl != null ? "\nClick to go to build." : ""}\n\nJenkins Agent will close automatically in a few seconds.", MessageType.ERROR, {
            if (executionUrl != null)
                Desktop.getDesktop().browse(new URI("${executionUrl}/console"))
        })
    }

    void setStatus(String status, String detail, MessageType messageType = null, Runnable action = null) {
        this.status = status
        trayIcon.setToolTip(this.status)

        if (trayIconAction != null)
            trayIconAction = action

        if (messageType != null)
            trayIcon.displayMessage("Jenkins Agent: " + status, detail, messageType)
    }


    boolean choice(String prompt) {
        int choice = JOptionPane.showConfirmDialog(null, prompt, "Jenkins Agent: Confirm Action", JOptionPane.YES_NO_CANCEL_OPTION)
        return choice == JOptionPane.YES_OPTION
    }

    @Override
    void close() {
        executorService.shutdown()
        tray.remove(trayIcon)
    }
}
