package pl.kelog.ytdownloader;

import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import pl.kelog.ytdownloader.job.DownloadJob;
import pl.kelog.ytdownloader.job.DownloadJobRepository;

import java.nio.charset.Charset;

@Service
@Log
public class YoutubeService {
    
    private final DownloadJobRepository downloadJobRepository;
    private final AsyncYoutubeDownloader asyncYoutubeDownloader;
    private final AppConfiguration appConfiguration;
    
    public YoutubeService(
            DownloadJobRepository downloadJobRepository,
            AsyncYoutubeDownloader asyncYoutubeDownloader,
            AppConfiguration appConfiguration
    ) {
        this.downloadJobRepository = downloadJobRepository;
        this.asyncYoutubeDownloader = asyncYoutubeDownloader;
        this.appConfiguration = appConfiguration;
    }
    
    public String getThumbnailUrl(String youtubeMovieLink) throws Exception {
        Process process = new ProcessBuilder(appConfiguration.getYoutubeDlPath(), "--get-thumbnail", youtubeMovieLink).start();
        process.waitFor();
        ensureSuccessExitCode(process);
        
        return IOUtils.toString(process.getInputStream(), Charset.defaultCharset()).trim();
    }
    
    public String beginDownload(String youtubeMovieLink) throws Exception {
        DownloadJob jobInfo = downloadJobRepository.create();
        jobInfo.setUrl(youtubeMovieLink);
        jobInfo.setStatus(DownloadJob.DownloadStatus.PENDING);
        downloadJobRepository.save(jobInfo);
        
        asyncYoutubeDownloader.beginDownload(jobInfo);
        
        return jobInfo.getId();
    }
    
    private void ensureSuccessExitCode(Process process) {
        if (process.exitValue() != 0) {
            log.severe("youtube-dl execution error - exit code " + process.exitValue());
            throw new RuntimeException();
        }
    }
}
