package pl.kelog.ytdownloader.web;


import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.kelog.ytdownloader.common.DownloadJobDto;
import pl.kelog.ytdownloader.job.DownloadJob.DownloadStatus;
import pl.kelog.ytdownloader.youtube.YoutubeService;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        String youtubeLink = "https://www.youtube.com/watch?v=gGQ2xKSF5VA";
        String thumbnailUrl = "https://i.ytimg.com/vi/gGQ2xKSF5VA/maxresdefault.jpg";
        
        when(service.getThumbnailUrl(youtubeLink)).thenReturn(thumbnailUrl);
        
        mockMvc.perform(get("/api/thumbnailUrl").param("url", youtubeLink))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.thumbnailUrl", is(thumbnailUrl)));
    }
    
    @Test
    public void should_list_details_of_a_job() throws Exception {
        DownloadJobDto dto = new DownloadJobDto(1, "http://link.com", "/tmp/abcd.mp4", DownloadStatus.PENDING);
        when(service.findOne(1)).thenReturn(Optional.of(dto));
        
        mockMvc.perform(get("/api/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.url", is("http://link.com")))
                .andExpect(jsonPath("$.filename", is("/tmp/abcd.mp4")))
                .andExpect(jsonPath("$.status", is(DownloadStatus.PENDING.toString())));
    }
    
    @Test
    public void should_return_404_if_no_job_of_such_id_exists() throws Exception {
        when(service.findOne(1)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/jobs/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void should_begin_new_download() throws Exception {
        mockMvc.perform(
                post("/api/jobs")
                        .content("{\"youtubeUrl\": \"http://link.com\"}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        
        verify(service, times(1)).beginDownload("http://link.com");
    }
    
    @Test
    public void should_return_404_if_there_is_no_such_job_for_file_download() throws Exception {
        when(service.findOne(1)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/jobs/1/download"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void should_return_404_if_there_is_no_fully_downloaded_file_yet_or_error() throws Exception {
        when(service.findOne(1)).thenReturn(Optional.empty());
    
        mockMvc.perform(get("/api/jobs/1/download"))
                .andExpect(status().isNotFound());
        
        when(service.findOne(1)).thenReturn(Optional.of(createDownloadJobDto(DownloadStatus.PENDING)));
        
        mockMvc.perform(get("/api/jobs/1/download"))
                .andExpect(status().isNotFound());
        
        when(service.findOne(1)).thenReturn(Optional.of(createDownloadJobDto(DownloadStatus.ERROR)));
    
        mockMvc.perform(get("/api/jobs/1/download"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void should_download_existing_and_downloaded_movie_file_as_attachment() throws Exception {
        when(service.findOne(1)).thenReturn(Optional.of(createDownloadJobDto(DownloadStatus.SUCCESS)));
        
        mockMvc.perform(get("/api/jobs/1/download"))
                .andExpect(status().isOk());
    }
    
    private static DownloadJobDto createDownloadJobDto(DownloadStatus error) {
        return new DownloadJobDto(1, "", "", error);
    }
}