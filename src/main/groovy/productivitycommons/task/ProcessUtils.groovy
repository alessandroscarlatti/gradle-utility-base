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

    static ExecResult exec(String commandLine, Path workingDir, boolean ignoreExitValue = false) {
        workingDir = workingDir.toAbsolutePath()
        println workingDir.toString() + ">" + commandLine
        Process process = ("cmd /c " + commandLine).execute(getEnvironment(), workingDir.toFile())
        ByteArrayOutputStream out = new Tee(System.out)
        ByteArrayOutputStream err = new Tee(System.err)
        process.consumeProcessOutput(out, err)
        process.waitFor()

        int exitCode = process.exitValue()
        if (!ignoreExitValue && exitCode != 0) {
            throw new ExecutionException("Command exited with code ${exitCode}: " + commandLine, exitCode)
        }

        return new ExecResult(
                exitCode: exitCode,
                stdOut: out.toString("utf-8"),
                stdErr: err.toString("utf-8"),
        )
    }

    static class Tee extends ByteArrayOutputStream {

        private OutputStream original
        private ByteArrayOutputStream tee

        Tee(OutputStream original) {
            super()
            this.original = original
            tee = new ByteArrayOutputStream()
        }

        @Override
        void write(byte[] b) throws IOException {
            original.write(b)
            tee.write(b)
        }

        @Override
        synchronized void write(byte[] b, int off, int len) throws IOException {
            original.write(b, off, len)
            tee.write(b, off, len)
        }

        @Override
        void flush() throws IOException {
            original.flush()
            tee.flush()
        }

        @Override
        void close() throws IOException {
            tee.close()
        }

        @Override
        synchronized void write(int b) throws IOException {
            original.write(b)
            tee.write(b)
        }
    }


    static class ExecResult {
        int exitCode
        String stdOut
        String stdErr
    }
}
