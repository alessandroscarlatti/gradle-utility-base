package productivitycommons.git

import java.nio.file.Path

/**
 * @author Alessandro Scarlatti
 * @since Sunday , 7/28/2019
 */
class DelegatingGitExecutor implements GitExecutor {

    private GitExecutor gitExecutor

    DelegatingGitExecutor(GitExecutor gitExecutor) {
        this.gitExecutor = gitExecutor
    }

    @Override
    void shallowClone(String revision) {
        gitExecutor.shallowClone(revision)
    }

    @Override
    void checkout(String revision) {
        gitExecutor.checkout(revision)
    }

    @Override
    void pull() {
        gitExecutor.pull()
    }

    @Override
    void reset(boolean hard) {
        gitExecutor.reset(hard)
    }

    @Override
    void stageAll() {
        gitExecutor.stageAll()
    }

    @Override
    void stage(Path file) {
        gitExecutor.stage(file)
    }

    @Override
    void commit(String message) {
        gitExecutor.commit(message)
    }

    @Override
    void push(String upstream = null) {
        gitExecutor.push()
    }
}
