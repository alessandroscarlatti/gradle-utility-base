package productivitycommons.git

import productivitycommons.task.ProcessUtils

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

    private ProcessUtils.ExecResult exec(String commandLine, Path workingDir = null) {
        if (workingDir == null)
            workingDir = repository
        ProcessUtils.ExecResult result = ProcessUtils.exec(commandLine, workingDir)
        if (result.exitCode != 0)
            throw new RuntimeException("Error ")

        return result
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

    @Override
    void merge(String from) {
        try {
            exec "git merge origin ${from}"
        } catch (Exception e) {
            try {
                exec "git merge --abort"
            } catch (Exception e2) {
                new RuntimeException("Failed to abort merge!", e2).printStackTrace()
            } finally {
                throw new RuntimeException("Failed to merge!", e)
            }
        }
    }

    @Override
    void fetch(String revision) {
        exec "git fetch ${revision}"
    }

    @Override
    void createBranch(String name) {
        exec "git branch ${name}"
    }

    @Override
    void setBranchUpstream(String upstream, String branch) {
        exec "git branch --set-upstream-to=${upstream} ${branch}"
    }
}
