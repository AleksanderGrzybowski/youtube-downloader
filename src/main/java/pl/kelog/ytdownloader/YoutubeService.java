package pl.kelog.ytdownloader;

import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;

@Service
@Log
public class YoutubeService {
    
    private final String youtubeDlPath;
    
    public YoutubeService(@Value("${app.youtubeDlPath:#{null}}") String youtubeDlPath) throws Exception {
        this.youtubeDlPath = (youtubeDlPath == null) ? "youtube-dl" : youtubeDlPath;
        log.info("Using youtube-dl tool path: " + this.youtubeDlPath + ", testing if it works...");
        
        checkIfToolIsPresent();
    }
    
    public String getThumbnailUrl(String youtubeMovieLink) throws Exception {
        Process process = new ProcessBuilder(youtubeDlPath, "--get-thumbnail", youtubeMovieLink).start();
        process.waitFor();
        ensureSuccessExitCode(process);
    
        return IOUtils.toString(process.getInputStream(), Charset.defaultCharset()).trim();
    }
    
    private void checkIfToolIsPresent() throws Exception {
        Process process = new ProcessBuilder(youtubeDlPath, "--version").start();
        process.waitFor();
        ensureSuccessExitCode(process);
        
        log.info("youtube-dl works correctly, exit code " + process.exitValue());
    }
    
    private void ensureSuccessExitCode(Process process) {
        if (process.exitValue() != 0) {
            log.severe("youtube-dl execution error - exit code " + process.exitValue());
            throw new RuntimeException();
        }
    }
}
