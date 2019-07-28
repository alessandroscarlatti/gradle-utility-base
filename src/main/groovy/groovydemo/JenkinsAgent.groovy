package groovydemo

/**
 * @author Alessandro Scarlatti
 * @since Saturday , 7/27/2019
 */
// todo and rename the other class to JenkinsService
class JenkinsAgent {

    public static void jenkins(String url, @DelegatesTo(JenkinsAgent.class) Closure buildScript) {
        // do stuff then call the closure

    }

    public static void testJenkins(@DelegatesTo(JenkinsAgent.class) Closure closure) {
        // do stuff then call the closure

    }

    void build(String project) {

    }
}
