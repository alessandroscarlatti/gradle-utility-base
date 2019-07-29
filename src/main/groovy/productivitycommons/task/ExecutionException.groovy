package productivitycommons.task

/**
 * @author Alessandro Scarlatti
 * @since Sunday , 7/28/2019
 */
class ExecutionException extends RuntimeException {

    private int exitCode

    ExecutionException(int exitCode) {
        this.exitCode = exitCode
    }

    ExecutionException(String var1, int exitCode) {
        super(var1)
        this.exitCode = exitCode
    }

    ExecutionException(String var1, Throwable var2, int exitCode) {
        super(var1, var2)
        this.exitCode = exitCode
    }

    ExecutionException(Throwable var1, int exitCode) {
        super(var1)
        this.exitCode = exitCode
    }

    ExecutionException(String var1, Throwable var2, boolean var3, boolean var4, int exitCode) {
        super(var1, var2, var3, var4)
        this.exitCode = exitCode
    }
}
