package productivitycommons.task

import java.nio.file.Path

/**
 * @author Alessandro Scarlatti
 * @since Sunday , 7/28/2019
 */
class ProcessUtils {

    private static List<String> getEnvironment() {
        return System.getenv().collect { k, v -> "$k=$v" }
    }

    static void exec(String commandLine, Path workingDir, boolean ignoreExitValue = false) {
        workingDir = workingDir.toAbsolutePath()
        println workingDir.toString() + ">" + commandLine
        Process process = ("cmd /c " + commandLine).execute(getEnvironment(), workingDir.toFile())
        process.consumeProcessOutput(System.out as OutputStream, System.err as OutputStream)
        process.waitFor()

        int exitCode = process.exitValue()
        if (!ignoreExitValue && exitCode != 0) {
            throw new ExecutionException("Command exited with code ${exitCode}: " + commandLine, exitCode)
        }
    }

}
