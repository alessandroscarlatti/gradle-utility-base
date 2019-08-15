package productivitycommons.git

import productivitycommons.task.ProcessUtils

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Consumer
import java.util.regex.Pattern

/**
 * @author Alessandro Scarlatti
 * @since Sunday , 7/28/2019
 */
class GitAgent {

    // this is the space where git agent can clone repositories
    private Path workspaceDir = Paths.get(System.getenv("USERPROFILE")).resolve(".gitagent")

    static void git(@DelegatesTo(GitAgent.class) Closure gitScript) {
        GitAgent gitAgent = new GitAgent()
        gitScript.resolveStrategy = Closure.DELEGATE_FIRST
        gitScript.delegate = gitAgent
        gitScript.call()
    }

    void withLocal(Path repositoryDir, @DelegatesTo(RepositoryHandler.class) Closure repositoryScript) {
        RepositoryHandler repositoryHandler = new RepositoryHandler(repositoryDir.toAbsolutePath(), repositoryDir.toAbsolutePath().toString())
        repositoryScript.resolveStrategy = Closure.DELEGATE_FIRST
        repositoryScript.delegate = repositoryHandler
        repositoryScript.call()
    }

    void withRemote(String repositoryUrl, @DelegatesTo(RepositoryHandler.class) Closure repositoryScript) {
        String repositoryName = repositoryUrl.find(Pattern.compile("[^/]+\$"))
        RepositoryHandler repositoryHandler = new RepositoryHandler(workspaceDir.resolve(repositoryName), repositoryUrl)
        repositoryScript.resolveStrategy = Closure.DELEGATE_FIRST
        repositoryScript.delegate = repositoryHandler
        repositoryScript.call()
    }

    class RepositoryHandler implements GitExecutor {

        private Path repositoryDir
        private GitExecutor gitExecutor

        RepositoryHandler(Path repositoryDir, String url) {
            gitExecutor = new GroovyGitExecutor(repositoryDir, url)
            this.repositoryDir = repositoryDir
            Files.createDirectories(repositoryDir)
        }

        void exec(String commandLine, boolean ignoreExitValue = true) {
            ProcessUtils.exec(commandLine, repositoryDir, ignoreExitValue)
        }

        /**
         * Resolve a file relative to the repository root.
         * @param path relative path
         * @return the file in the repository
         */
        Path file(String path = null) {
            if (path == null)
                return repositoryDir
            return repositoryDir.resolve(path)
        }

        void withFile(String path = null, Consumer<Path> script) {
            if (path == null) {
                script.accept(repositoryDir)
                return
            }

            script.accept(repositoryDir.resolve(path))
        }

        /**
         * Synchronize the repository with the requested revision
         * @param revision
         */
        void useRevisionHard(String revision) {
            if (Files.exists(repositoryDir.resolve(".git"))) {
                reset(true)
                pull()
                checkout(revision)
            } else {
                shallowClone(revision)
            }
        }

        void useRevisionSoft(String revision) {
            if (Files.exists(repositoryDir.resolve(".git"))) {
                reset(false)
                pull()
                checkout(revision)
            } else {
                shallowClone(revision)
            }
        }

        void useNewBranch(String baseBranch, String newBranch) {
            useRevisionHard(baseBranch)
            createBranch(newBranch)
            checkout(newBranch)
            setBranchUpstream("origin", newBranch)
        }

        void commitAndPush(String message) {
            commit(message)
            push()
        }

        void modify(Path file, Consumer<Path> modifyScript) {
            modifyScript.accept(file)
            stage(file)
        }

        void mergeBranches(String from, String to) {
            useRevisionHard(to)
            fetch(from)
            merge()
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
            gitExecutor.push(upstream)
        }

        @Override
        void merge(String from) {
            gitExecutor.merge(from)
        }

        @Override
        void fetch(String revision) {
            gitExecutor.fetch(revision)
        }

        @Override
        void createBranch(String name) {
            gitExecutor.createBranch(name)
        }

        @Override
        void setBranchUpstream(String upstream, String branch) {
            gitExecutor.setBranchUpstream(upstream, branch)
        }
    }
}
