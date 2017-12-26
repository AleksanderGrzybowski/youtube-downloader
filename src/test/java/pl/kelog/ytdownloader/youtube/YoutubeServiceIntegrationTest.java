package pl.kelog.ytdownloader.youtube;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kelog.ytdownloader.common.DownloadJobDto;
import pl.kelog.ytdownloader.job.DownloadJob.DownloadStatus;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YoutubeServiceIntegrationTest {
    
    private static final String YOUTUBE_TEST_MOVIE_URL = "https://www.youtube.com/watch?v=C0DPdy98e4c";
    private static final String YOUTUBE_TEST_MOVIE_THUMBNAIL_URL = "https://i.ytimg.com/vi/C0DPdy98e4c/hqdefault.jpg";
    
    @Autowired
    private YoutubeService service;
    
    @Test
    public void should_retrieve_thumbnail_link_for_movie() throws Exception {
        assertThat(
                service.getThumbnailUrl(YOUTUBE_TEST_MOVIE_URL)
        ).isEqualTo(YOUTUBE_TEST_MOVIE_THUMBNAIL_URL);
    }
    
    @Test
    public void should_download_a_sample_movie() throws Exception {
        DownloadJobDto jobDto = service.beginDownload(YOUTUBE_TEST_MOVIE_URL);
        
        assertThat(jobDto.getStatus()).isEqualTo(DownloadStatus.PENDING);
        
        int timer = 0;
        while (timer++ < 10 && jobDto.getStatus() == DownloadStatus.PENDING) {
            //noinspection ConstantConditions
            jobDto = service.findOne(jobDto.getId()).get();
            System.out.println(".");
            Thread.sleep(500);
        }
        
        assertThat(jobDto.getStatus() == DownloadStatus.SUCCESS);
        assertThat(new File(jobDto.getFilename()).exists()).isTrue();
    }
}
