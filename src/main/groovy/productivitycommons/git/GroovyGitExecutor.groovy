package productivitycommons.git

import java.nio.file.Path

/**
 * @author Alessandro Scarlatti
 * @since Sunday , 7/28/2019
 */
class GroovyGitExecutor implements GitExecutor {

    private Path repository;
    private String repositoryUrl;

    GroovyGitExecutor(Path repository, String repositoryUrl) {
        this.repository = repository
        this.repositoryUrl = repositoryUrl
    }

    private static List<String> getEnvironment() {
        return System.getenv().collect { k, v -> "$k=$v" }
    }

    private void exec(String commandLine, Path workingDir = null) {
        if (workingDir == null)
            workingDir = repository
        println repository.parent.toString() + ">" + commandLine
        Process process = ("cmd /c " + commandLine).execute(getEnvironment(), workingDir.toFile())

        def out = new StringBuffer()
        def err = new StringBuffer()
        process.consumeProcessOutput(out, err)
        process.waitFor()
        if (out.size() > 0) println out
        if (err.size() > 0) println err

        if (process.exitValue() != 0) {
            throw new RuntimeException("Error running command: " + commandLine)
        }
    }

    @Override
    void shallowClone(String revision) {
        exec("git clone --depth 1 ${repositoryUrl} ${repository.fileName}", repository.parent)
    }

    @Override
    void checkout(String revision) {
        exec "git checkout ${revision}"
    }

    @Override
    void pull() {
        exec "git pull"
    }

    @Override
    void reset(boolean hard) {
        exec "git reset ${hard ? "--hard" : ""}"
    }

    @Override
    void stageAll() {
        exec "git add ."
    }

    @Override
    void stage(Path file) {
        exec "git add ${file}"
    }

    @Override
    void commit(String message) {
        exec "git commit -m \"${message}\""
    }

    @Override
    void push(String upstream = null) {
        if (upstream == null)
            exec "git push"
        else
            exec "git push -u ${upstream}"
    }
}
