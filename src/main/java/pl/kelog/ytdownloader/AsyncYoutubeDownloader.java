package pl.kelog.ytdownloader;

import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.kelog.ytdownloader.job.DownloadJob;

import java.util.concurrent.CompletableFuture;

/**
 * http://forum.spring.io/forum/spring-projects/container/106565-async-in-the-same-class 
 */
@Service
@Log
public class AsyncYoutubeDownloader {
    
    private final AppConfiguration appConfiguration;
    
    public AsyncYoutubeDownloader(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }
    
    @Async
    public CompletableFuture<Void> beginDownload(DownloadJob jobInfo) throws Exception {
        Process process = new ProcessBuilder(
                appConfiguration.getYoutubeDlPath(),
                jobInfo.getUrl(),
                "-o",
                "/tmp/abcd.mp4"
        ).start();
        
        process.waitFor();
        ensureSuccessExitCode(process);
        
        jobInfo.setStatus(DownloadJob.DownloadStatus.SUCCESS);
        jobInfo.setFilename("/tmp/abcd.mp4");
        
        return CompletableFuture.completedFuture(null);
    }
    
    private void ensureSuccessExitCode(Process process) {
        if (process.exitValue() != 0) {
            log.severe("youtube-dl execution error - exit code " + process.exitValue());
            throw new RuntimeException();
        }
    }
}
