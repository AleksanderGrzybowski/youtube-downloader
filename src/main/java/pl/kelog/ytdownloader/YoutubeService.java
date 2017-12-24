package pl.kelog.ytdownloader;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log
public class YoutubeService {
    
    private final String youtubeDlPath;
    
    public YoutubeService(@Value("${app.youtubeDlPath:#{null}}") String youtubeDlPath) throws Exception {
        this.youtubeDlPath = (youtubeDlPath == null) ? "youtube-dl" : youtubeDlPath;
        log.info("Using youtube-dl tool path: " + this.youtubeDlPath + ", testing if it works...");
        
        checkIfToolIsPresent();
    }
    
    private void checkIfToolIsPresent() throws Exception{
        Process process = new ProcessBuilder(youtubeDlPath, "--version").start();
        process.waitFor();
        if (process.exitValue() != 0) {
            throw new RuntimeException("youtube-dl execution error - exit code " + process.exitValue());
        } else {
            log.info("youtube-dl works correctly, exit code " + process.exitValue());
        }
    }
}
