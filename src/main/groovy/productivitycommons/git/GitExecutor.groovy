package productivitycommons.git

import java.nio.file.Path

/**
 * @author Alessandro Scarlatti
 * @since Sunday , 7/28/2019
 */
interface GitExecutor {

    void shallowClone(String revision)

    void checkout(String revision)

    void pull()

    void reset(boolean hard)

    void stageAll()

    void stage(Path file)

    void commit(String message)

    void push(String upstream)

    void merge(String from)

    void fetch(String revision)

    void createBranch(String name)

    void setBranchUpstream(String upstream, String branch)
}
