package groovydemo

import javax.swing.*

/**
 * @author Alessandro Scarlatti
 * @since Saturday , 7/27/2019
 */
class JenkinsGuiTest {

    public static void main(String[] args) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        JenkinsGui jenkinsGui = new JenkinsGui("https://jenkins.asdflkjsadf.com")
//        jenkinsGui.setStatus("Submitted job abcd.", "Job submitted at ${ISO_LOCAL_DATE_TIME.format(now())}")
//        sleep(5000)


        jenkinsGui.buildStarted("asdf")
        sleep(3000)
        jenkinsGui.buildStarted("asdf", "wert")
        sleep(3000)
        jenkinsGui.buildFailed("asdf")
        sleep(3000)
        jenkinsGui.buildFailed("asdf", "qwer")
        sleep(3000)
        jenkinsGui.allExecutionsSuccessful()
        sleep(3000)
        jenkinsGui.someExecutionsFailed()
        sleep(3000)
        jenkinsGui.someExecutionsFailed("QWer")
        sleep(3000)



        sleep(30000)
        jenkinsGui.close()
    }
}
