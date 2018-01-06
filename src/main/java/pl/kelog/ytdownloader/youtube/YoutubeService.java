package pl.kelog.ytdownloader.youtube;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import pl.kelog.ytdownloader.AppConfiguration;
import pl.kelog.ytdownloader.common.DownloadJobDto;
import pl.kelog.ytdownloader.job.DownloadJob;
import pl.kelog.ytdownloader.job.DownloadJobRepository;
import pl.kelog.ytdownloader.job.DownloadJobStatus;
import pl.kelog.ytdownloader.job.DownloadJobType;

import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Optional;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static pl.kelog.ytdownloader.util.Utils.ensureSuccessExitCode;

@Service
@Log
@RequiredArgsConstructor
public class YoutubeService {
    
    private final DownloadJobRepository downloadJobRepository;
    private final AsyncYoutubeDownloader asyncYoutubeDownloader;
    private final AppConfiguration appConfiguration;
    
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
    
    public DownloadJobDto beginDownload(String youtubeMovieLink, DownloadJobType type) {
        DownloadJob job = downloadJobRepository.create();
        job.setUrl(youtubeMovieLink);
        job.setType(type);
        job.setStatus(DownloadJobStatus.PENDING);
    
        String downloadedVideoPath = Paths.get(
                appConfiguration.getStoragePath(),
                job.getId() + "-" + randomAlphanumeric(10) + "." + fileExtensionFor(type)
        ).toAbsolutePath().toString();
        job.setFilename(downloadedVideoPath);
        
        log.info("Creating new download job: " + job + "...");
        downloadJobRepository.save(job);
        
        asyncYoutubeDownloader.beginDownload(job);
        
        return mapToDto(job);
    }
    
    public Optional<DownloadJobDto> findOne(int id) {
        return downloadJobRepository.findOne(id).map(YoutubeService::mapToDto);
    }
    
    private String fileExtensionFor(DownloadJobType type) {
        switch (type) {
            case VIDEO:
                return "mp4";
            case AUDIO:
                return "mp3";
            default:
                throw new AssertionError();
        }
    }
    
    private static DownloadJobDto mapToDto(DownloadJob job) {
        return new DownloadJobDto(job.getId(), job.getUrl(), job.getType(), job.getFilename(), job.getStatus());
    }
}
