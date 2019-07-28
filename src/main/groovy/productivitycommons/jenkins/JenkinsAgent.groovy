package productivitycommons.jenkins

/**
 * @author Alessandro Scarlatti
 * @since Saturday , 7/27/2019
 */
// todo and rename the other class to JenkinsService
class JenkinsAgent {

    JenkinsGui gui

    public static void jenkins(@DelegatesTo(JenkinsAgent.class) Closure buildScript) {
        // do stuff then call the closure
        jenkins("asdf", buildScript)
    }

    public static void jenkins(String url, @DelegatesTo(JenkinsAgent.class) Closure buildScript) {
        // do stuff then call the closure
        JenkinsAgent jenkinsAgent = new JenkinsAgent()
        jenkinsAgent.gui = new JenkinsGui()
        buildScript.setResolveStrategy(Closure.DELEGATE_FIRST)
        buildScript.setDelegate(jenkinsAgent)
        buildScript.call()
        jenkinsAgent.gui.close()
    }

    public static void testJenkins(@DelegatesTo(JenkinsAgent.class) Closure closure) {
        // do stuff then call the closure

    }

    void build(String project) {

    }
}
