package pl.kelog.ytdownloader.util;

import java.util.logging.Logger;

public class Utils {
    private static final Logger LOG = Logger.getLogger(Utils.class.getName());
    
    public static void ensureSuccessExitCode(Process process) {
        if (process.exitValue() != 0) {
            String message = "youtube-dl execution error - exit code " + process.exitValue();
            LOG.severe(message);
            throw new RuntimeException(message);
        }
    }
}
