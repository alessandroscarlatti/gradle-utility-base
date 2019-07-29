package productivitycommons.jenkins
/**
 * @author Alessandro Scarlatti
 * @since Saturday , 7/27/2019
 */
// todo and rename the other class to JenkinsService
class JenkinsAgent {

    JenkinsGui2 jenkinsGui

    static void jenkins(String url, @DelegatesTo(JenkinsAgent.class) Closure buildScript) {
        // do stuff then call the closure
        JenkinsAgent jenkinsAgent = new JenkinsAgent()
        jenkinsAgent.jenkinsGui = new JenkinsGui2()
        try {
            jenkinsAgent.jenkinsGui.setStatus("Starting up...")
            sleep(1000)
            buildScript.setResolveStrategy(Closure.DELEGATE_FIRST)
            buildScript.setDelegate(jenkinsAgent)
            buildScript.call()
        } finally {
            jenkinsAgent.jenkinsGui.setStatus("Closing...")
            sleep(3000)
            jenkinsAgent.jenkinsGui.close()
        }
    }
}
