package productivitycommons.jenkins

import productivitycommons.gui.TrayWidget

import javax.imageio.ImageIO
import java.awt.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

import static java.time.LocalDateTime.now
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import static productivitycommons.gui.GuiUtils.choice
import static productivitycommons.gui.GuiUtils.setSystemLookAndFeel

/**
 * @author Alessandro Scarlatti
 * @since Sunday, 7/28/2019
 */
class JenkinsGui2 extends TrayWidget {

    static {
        setSystemLookAndFeel()
    }

    @Override
    protected Image getWidgetIcon() {
        ImageIO.read(getClass().getResourceAsStream("/jenkins.png"))
    }

    @Override
    protected String getWidgetName() {
        "Jenkins Agent"
    }

    @Override
    protected String getWidgetAboutHtml() {
        "Notification mechanism to alert a user while a Jenkins Agent script is running.<br>Notifications will appear here as jobs are executed."
    }

    @Override
    protected Runnable getWidgetAction() {
        return {}
    }

    static String now() {
        DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(LocalDateTime.now())
    }

    @Override
    protected void configurePopupMenu(PopupMenu menu) {
        MenuItem stopAllJobsNow = new MenuItem("Stop All Jobs Now")
        stopAllJobsNow.addActionListener({
            boolean choice = choice("Are you sure you want to stop all jobs?")
            if (choice) {
                // try to stop all jobs...
                setStatus("Stopping all jobs.", "Attempting to stop all jobs now.", TrayIcon.MessageType.INFO)

                boolean success = false;

                // if successful or not, show status
                if (success) {
                    setStatus("All jobs stopped.", "All jobs were stopped at ${now()}.\nJenkin Agent will close automatically after a few seconds.", TrayIcon.MessageType.INFO)
                } else {
                    setStatus("Some jobs were not stopped.", "Some jobs already submitted or currently executing were not stopped at ${ISO_LOCAL_DATE_TIME.format(now())}.\n\nHowever, all future jobs were stopped successfully.\n\nJenkin Agent will close automatically after a few seconds.", TrayIcon.MessageType.ERROR)
                }
            }
        });

        //Add menu item to popup menu
        menu.addSeparator();
        menu.add(stopAllJobsNow);
    }

    void buildStarted(String jobName, String executionUrl = null) {
        setStatus("Building job " + jobName, "Building ${jobName} at ${now()}.${executionUrl != null ? "\nClick to go to build." : ""}\n\n", TrayIcon.MessageType.INFO, {
            if (executionUrl != null)
                Desktop.getDesktop().browse(new URI("${executionUrl}/console"))
        })
    }

    void buildFailed(String jobName, String executionUrl = null) {
        setStatus("Build failed for " + jobName, "Build failed for ${jobName} at ${now()}.${executionUrl != null ? "\nClick to go to build." : ""}\n\n", TrayIcon.MessageType.ERROR, {
            if (executionUrl != null)
                Desktop.getDesktop().browse(new URI("${executionUrl}/console"))
        })
    }

    void allExecutionsSuccessful() {
        setStatus("All executions successful.", "Congratulations! All executions successful at ${now()}.\nJenkins Agent will close automatically in a few seconds.", TrayIcon.MessageType.INFO)
    }

    void someExecutionsFailed(String executionUrl = null) {
        setStatus("Some executions failed.", "Oh no! Some executions failed at ${now()}.${executionUrl != null ? "\nClick to go to build." : ""}\n\nJenkins Agent will close automatically in a few seconds.", TrayIcon.MessageType.ERROR, {
            if (executionUrl != null)
                Desktop.getDesktop().browse(new URI("${executionUrl}/console"))
        })
    }
}
