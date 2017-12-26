package pl.kelog.ytdownloader.youtube;

import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import pl.kelog.ytdownloader.AppConfiguration;
import pl.kelog.ytdownloader.common.DownloadJobDto;
import pl.kelog.ytdownloader.job.DownloadJob;
import pl.kelog.ytdownloader.job.DownloadJob.DownloadStatus;
import pl.kelog.ytdownloader.job.DownloadJobRepository;

import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Optional;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

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
        log.info("Getting thumbnail URL for " + youtubeMovieLink + "...");
        Process process = new ProcessBuilder(
                appConfiguration.getYoutubeDlPath(),
                "--get-thumbnail",
                youtubeMovieLink
        ).start();
        process.waitFor();
        ensureSuccessExitCode(process);
        
        String url = IOUtils.toString(process.getInputStream(), Charset.defaultCharset()).trim();
        log.info("Got thumbnail URL back: " + url + ".");
        return url;
    }
    
    public DownloadJobDto beginDownload(String youtubeMovieLink) {
        DownloadJob job = downloadJobRepository.create();
        job.setUrl(youtubeMovieLink);
        job.setStatus(DownloadStatus.PENDING);
        
        String downloadedVideoFilename = Paths.get(
                appConfiguration.getStoragePath(),
                job.getId() + "-" + randomAlphanumeric(10) + ".mp4"
        ).toAbsolutePath().toString();
        job.setFilename(downloadedVideoFilename);
        
        log.info("Creating new download job: " + job + "...");
        downloadJobRepository.save(job);
        
        asyncYoutubeDownloader.beginDownload(job);
        
        return mapToDto(job);
    }
    
    
    private void ensureSuccessExitCode(Process process) {
        if (process.exitValue() != 0) {
            log.severe("youtube-dl execution error - exit code " + process.exitValue());
            throw new RuntimeException();
        }
    }
    
    private static DownloadJobDto mapToDto(DownloadJob job) {
        return new DownloadJobDto(job.getId(), job.getUrl(), job.getFilename(), job.getStatus());
    }
    
    public Optional<DownloadJobDto> findOne(int id) {
        return downloadJobRepository.findOne(id).map(YoutubeService::mapToDto);
    }
}