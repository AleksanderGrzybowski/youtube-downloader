package pl.kelog.ytdownloader.youtube;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.kelog.ytdownloader.AppConfiguration;
import pl.kelog.ytdownloader.job.DownloadJob;
import pl.kelog.ytdownloader.job.DownloadJob.Status;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Arrays.asList;
import static pl.kelog.ytdownloader.util.Utils.ensureSuccessExitCode;

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
            log.info("Starting download process " + jobInfo + ", command: " + command + "...");
            Process process = new ProcessBuilder(command).start();
            
            process.waitFor();
            ensureSuccessExitCode(process);
            
            jobInfo.setStatus(Status.SUCCESS);
            log.info("Download successful: " + jobInfo + ".");
        } catch (Exception e) {
            jobInfo.setStatus(Status.ERROR);
            log.severe("Download error: " + jobInfo + ". " + e.getMessage());
        }
        
        return CompletableFuture.completedFuture(null);
    }
}
