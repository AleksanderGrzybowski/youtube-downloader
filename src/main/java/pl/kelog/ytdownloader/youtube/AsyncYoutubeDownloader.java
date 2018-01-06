package pl.kelog.ytdownloader.youtube;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.kelog.ytdownloader.AppConfiguration;
import pl.kelog.ytdownloader.job.DownloadJob;
import pl.kelog.ytdownloader.job.DownloadJobStatus;
import pl.kelog.ytdownloader.job.DownloadJobType;

import java.io.File;
import java.util.ArrayList;
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
            List<String> command = createCommand(jobInfo);
            log.info("Starting download process " + jobInfo + ", command: " + command + "...");
            Process process = new ProcessBuilder(command).start();
            
            process.waitFor();
            ensureSuccessExitCode(process);
            
            // youtube-dl duplicates extensions, sorry
            if (jobInfo.getType() == DownloadJobType.AUDIO) {
                //noinspection ResultOfMethodCallIgnored
                new File(jobInfo.getFilename() + ".mp3").renameTo(new File(jobInfo.getFilename()));
            }
            
            jobInfo.setStatus(DownloadJobStatus.SUCCESS);
            log.info("Download successful: " + jobInfo + ".");
        } catch (Exception e) {
            jobInfo.setStatus(DownloadJobStatus.ERROR);
            log.severe("Download error: " + jobInfo + ". " + e.getMessage());
        }
        
        return CompletableFuture.completedFuture(null);
    }
    
    private List<String> createCommand(DownloadJob jobInfo) {
        List<String> command = new ArrayList<>(asList(
                appConfiguration.getYoutubeDlPath(),
                jobInfo.getUrl(),
                "-o",
                jobInfo.getFilename()
        ));
        
        if (jobInfo.getType() == DownloadJobType.AUDIO) {
            command.addAll(asList(
                    "-x",
                    "-f",
                    "bestvideo+bestaudio",
                    "--audio-format=mp3"
            ));
        }
        
        return command;
    }
}
