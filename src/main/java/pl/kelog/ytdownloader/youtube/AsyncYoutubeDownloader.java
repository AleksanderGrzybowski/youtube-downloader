package pl.kelog.ytdownloader.youtube;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.kelog.ytdownloader.AppConfiguration;
import pl.kelog.ytdownloader.job.DownloadJob;
import pl.kelog.ytdownloader.job.DownloadJob.DownloadStatus;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Arrays.asList;

/**
 * http://forum.spring.io/forum/spring-projects/container/106565-async-in-the-same-class
 */
@Service
@Log
@RequiredArgsConstructor
public class AsyncYoutubeDownloader {
    
    private final AppConfiguration appConfiguration;
    
    @Async
    public CompletableFuture<Void> beginDownload(DownloadJob jobInfo) {
        try {
            List<String> command = asList(
                    appConfiguration.getYoutubeDlPath(),
                    jobInfo.getUrl(),
                    "-o",
                    jobInfo.getFilename()
            );
            log.info("Starting download process (id " + jobInfo.getId() + "): " + command + "...");
            Process process = new ProcessBuilder(command).start();
            
            process.waitFor();
            ensureSuccessExitCode(process);
            
            log.info("Download successful (id " + jobInfo.getId() + ").");
            jobInfo.setStatus(DownloadStatus.SUCCESS);
        } catch (Exception e) {
            log.info("Download error (id " + jobInfo.getId() + ").");
            jobInfo.setStatus(DownloadStatus.ERROR);
        }
        
        return CompletableFuture.completedFuture(null);
    }
    
    private void ensureSuccessExitCode(Process process) {
        if (process.exitValue() != 0) {
            log.severe("youtube-dl execution error - exit code " + process.exitValue());
            throw new RuntimeException();
        }
    }
}
