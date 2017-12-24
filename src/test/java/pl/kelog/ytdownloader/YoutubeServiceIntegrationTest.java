package pl.kelog.ytdownloader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YoutubeServiceIntegrationTest {
    
    @Autowired
    private YoutubeService service;
    
    @Test
    public void should_retrieve_thumbnail_link_for_movie() throws Exception {
        assertThat(
                service.getThumbnailUrl("https://www.youtube.com/watch?v=gGQ2xKSF5VA")
        ).isEqualTo("https://i.ytimg.com/vi/gGQ2xKSF5VA/maxresdefault.jpg");
    }
}
