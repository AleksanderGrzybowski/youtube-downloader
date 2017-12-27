package pl.kelog.ytdownloader.web;


import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import pl.kelog.ytdownloader.youtube.YoutubeService;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class ThumbnailControllerTest {
    
    private MockMvc mockMvc;
    private YoutubeService service;
    
    @Before
    public void setup() {
        service = mock(YoutubeService.class);
        mockMvc = standaloneSetup(new ThumbnailController(service)).build();
    }
    
    @Test
    public void should_return_link_to_thumbnail() throws Exception {
        String youtubeLink = "https://www.youtube.com/watch?v=gGQ2xKSF5VA";
        String thumbnailUrl = "https://i.ytimg.com/vi/gGQ2xKSF5VA/maxresdefault.jpg";
        
        when(service.getThumbnailUrl(youtubeLink)).thenReturn(thumbnailUrl);
        
        mockMvc.perform(get("/api/thumbnailUrl").param("url", youtubeLink))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.thumbnailUrl", is(thumbnailUrl)));
    }
    
    @Test
    public void should_return_400_if_there_was_an_error_with_retrieving_thumbnail_link() throws Exception {
        String youtubeLink = "https://www.youtube.com/watch?v=gGQ2xKSF5VA";
        
        when(service.getThumbnailUrl(youtubeLink)).thenThrow(new Exception());
        
        mockMvc.perform(get("/api/thumbnailUrl").param("url", youtubeLink))
                .andExpect(status().isBadRequest());
    }
}