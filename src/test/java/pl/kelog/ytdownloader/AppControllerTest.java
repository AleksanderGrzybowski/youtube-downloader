package pl.kelog.ytdownloader;


import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class AppControllerTest {
    
    private MockMvc mockMvc;
    private YoutubeService service;
    
    @Before
    public void setup() {
        service = mock(YoutubeService.class);
        mockMvc = standaloneSetup(new AppController(service)).build();
    }
    
    @Test
    public void should_return_link_to_thumbnail() throws Exception {
        when(service.getThumbnailUrl("https://www.youtube.com/watch?v=gGQ2xKSF5VA")).thenReturn("https://i.ytimg.com/vi/gGQ2xKSF5VA/maxresdefault.jpg");
        
        mockMvc.perform(get("/api/thumbnailUrl").param("url", "https://www.youtube.com/watch?v=gGQ2xKSF5VA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.thumbnailUrl", is("https://i.ytimg.com/vi/gGQ2xKSF5VA/maxresdefault.jpg")));
        
    }
}