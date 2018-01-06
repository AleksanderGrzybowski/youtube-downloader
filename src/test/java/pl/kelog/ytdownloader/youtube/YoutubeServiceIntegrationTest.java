package pl.kelog.ytdownloader.youtube;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;
import pl.kelog.ytdownloader.common.DownloadJobDto;
import pl.kelog.ytdownloader.job.DownloadJobStatus;
import pl.kelog.ytdownloader.job.DownloadJobType;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YoutubeServiceIntegrationTest {
    
    private static final String YOUTUBE_TEST_MOVIE_URL = "https://www.youtube.com/watch?v=C0DPdy98e4c";
    private static final String YOUTUBE_TEST_MOVIE_THUMBNAIL_URL = "https://i.ytimg.com/vi/C0DPdy98e4c/hqdefault.jpg";
    private static final String YOUTUBE_TEST_MOVIE_MD5 = "c91612e975d7a04f9f6f717abb427104";
    private static final String YOUTUBE_TEST_MOVIE_MP3_MD5 = "31e2d1126fe2fc2ef2b4d100515554e3";
     
    
    @Autowired
    private YoutubeService service;
    
    @Test
    public void should_retrieve_thumbnail_link_for_movie() throws Exception {
        assertThat(
                service.getThumbnailUrl(YOUTUBE_TEST_MOVIE_URL)
        ).isEqualTo(YOUTUBE_TEST_MOVIE_THUMBNAIL_URL);
    }
    
    @Test
    public void should_download_a_sample_movie_as_video() throws Exception {
        DownloadJobDto jobDto = service.beginDownload(YOUTUBE_TEST_MOVIE_URL, DownloadJobType.VIDEO);
        
        assertThat(jobDto.getStatus()).isEqualTo(DownloadJobStatus.PENDING);
    
        loopWhileStatusPending(jobDto);
        
        assertThat(jobDto.getStatus() == DownloadJobStatus.SUCCESS);
        assertThat(jobDto.getFilename()).isNotNull();
        assertThat(jobDto.getType()).isEqualTo(DownloadJobType.VIDEO);
        File downloaded = new File(jobDto.getFilename());
        assertThat(downloaded.exists()).isTrue();
        assertThat(DigestUtils.md5DigestAsHex(FileUtils.readFileToByteArray(downloaded)))
                .isEqualTo(YOUTUBE_TEST_MOVIE_MD5);
    }
    
    @Test
    public void should_download_a_sample_movie_as_audio() throws Exception {
        DownloadJobDto jobDto = service.beginDownload(YOUTUBE_TEST_MOVIE_URL, DownloadJobType.AUDIO);
        
        assertThat(jobDto.getStatus()).isEqualTo(DownloadJobStatus.PENDING);
        
        loopWhileStatusPending(jobDto);
        
        assertThat(jobDto.getStatus() == DownloadJobStatus.SUCCESS);
        assertThat(jobDto.getFilename()).isNotNull();
        assertThat(jobDto.getType()).isEqualTo(DownloadJobType.AUDIO);
        File downloaded = new File(jobDto.getFilename());
        assertThat(downloaded.exists()).isTrue();
        assertThat(DigestUtils.md5DigestAsHex(FileUtils.readFileToByteArray(downloaded)))
                .isEqualTo(YOUTUBE_TEST_MOVIE_MP3_MD5);
    }
    
    @Test
    public void should_fail_on_downloading_invalid_link() throws Exception {
        DownloadJobDto jobDto = service.beginDownload("http://google.com", DownloadJobType.VIDEO);
        
        assertThat(jobDto.getStatus()).isEqualTo(DownloadJobStatus.PENDING);
        
        loopWhileStatusPending(jobDto);
        
        assertThat(jobDto.getStatus() == DownloadJobStatus.ERROR);
    }
    
    private void loopWhileStatusPending(DownloadJobDto jobDto) throws InterruptedException {
        int sleepCounter = 0;
        int countLimit = 10;
        while (sleepCounter++ < countLimit && jobDto.getStatus() == DownloadJobStatus.PENDING) {
            //noinspection ConstantConditions
            jobDto = service.findOne(jobDto.getId()).get();
            System.out.println(".");
            Thread.sleep(500);
        }
    }
}
