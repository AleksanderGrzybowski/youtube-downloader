package pl.kelog.ytdownloader.util;

import lombok.extern.java.Log;

@Log
public class Utils {
    
    public static void ensureSuccessExitCode(Process process) {
        if (process.exitValue() != 0) {
            String message = "youtube-dl execution error - exit code " + process.exitValue();
            log.severe(message);
            throw new RuntimeException(message);
        }
    }
}
