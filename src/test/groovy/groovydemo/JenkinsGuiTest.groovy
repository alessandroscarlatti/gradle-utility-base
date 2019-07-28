package groovydemo

import static java.awt.TrayIcon.MessageType.INFO
import static java.time.LocalDateTime.now
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

/**
 * @author Alessandro Scarlatti
 * @since Saturday , 7/27/2019
 */
class JenkinsGuiTest {

    public static void main(String[] args) {
        JenkinsGui jenkinsGui = new JenkinsGui("https://jenkins.asdflkjsadf.com")
        jenkinsGui.setStatus("Submitted job abcd.", "Job submitted at ${ISO_LOCAL_DATE_TIME.format(now())}")
        sleep(5000)
        jenkinsGui.setStatus("Building job abcd.", "Build started at ${ISO_LOCAL_DATE_TIME.format(now())}", INFO)
        sleep(5000)
        jenkinsGui.close()
    }
}
