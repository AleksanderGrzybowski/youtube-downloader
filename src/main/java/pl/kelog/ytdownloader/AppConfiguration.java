package pl.kelog.ytdownloader;

import lombok.Getter;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Service
@Log
public class AppConfiguration {
    
    private static final String CHECK_STORAGE_TEST_STRING = "test creating file";
    private static final String YOUTUBE_DL_EXECUTABLE = "youtube-dl";
    private static final String DEFAULT_STORAGE_PATH = "/tmp";
    
    @Getter
    private final String youtubeDlPath;
    
    @Getter
    private final String storagePath;
    
    public AppConfiguration(
            @Value("${app.youtubeDlPath:#{null}}") String youtubeDlPath,
            @Value("${app.storagePath:#{null}}") String storagePath
    ) {
        this.youtubeDlPath = (youtubeDlPath == null) ? YOUTUBE_DL_EXECUTABLE : youtubeDlPath;
        ensureYoutubeDlIsPresent();
        
        this.storagePath = (storagePath == null) ? DEFAULT_STORAGE_PATH : storagePath;
        ensureStorageDirectoryIsWritable();
    }
    
    private void ensureStorageDirectoryIsWritable() {
        log.info("Using storage path: " + storagePath + ", testing if it is writable...");
        
        try {
            FileUtils.writeStringToFile(
                    Paths.get(storagePath, randomAlphanumeric(10) + ".mp4").toFile(),
                    CHECK_STORAGE_TEST_STRING,
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            log.severe("Error while writing test file to storage path: " + e.getMessage());
            throw new RuntimeException(e);
        }
        
        log.info("Storage path " + storagePath + " is writable.");
    }
    
    private void ensureYoutubeDlIsPresent() {
        log.info("Using youtube-dl tool path: " + youtubeDlPath + ", testing if it works...");
        
        try {
            Process process = new ProcessBuilder(youtubeDlPath, "--version").start();
            process.waitFor();
            
            if (process.exitValue() != 0) {
                log.severe("Failed to check youtube-dl version - exit code " + process.exitValue() + ".");
                throw new RuntimeException();
            }
        } catch (Exception e) {
            log.severe("Failed to run youtube-dl process: " + e.getMessage());
            throw new RuntimeException(e);
        }
        
        log.info("youtube-dl works correctly.");
    }
}
