package pl.kelog.ytdownloader.web;


import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.kelog.ytdownloader.common.DownloadJobDto;
import pl.kelog.ytdownloader.job.DownloadJobStatus;
import pl.kelog.ytdownloader.job.DownloadJobType;
import pl.kelog.ytdownloader.youtube.YoutubeService;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class JobControllerTest {
    
    private MockMvc mockMvc;
    private YoutubeService service;
    
    @Before
    public void setup() {
        service = mock(YoutubeService.class);
        mockMvc = standaloneSetup(new JobController(service)).build();
    }
    
    @Test
    public void should_list_details_of_a_job() throws Exception {
        DownloadJobDto dto = new DownloadJobDto(
                1,
                "http://link.com",
                DownloadJobType.VIDEO,
                "/tmp/abcd.mp4",
                DownloadJobStatus.PENDING
        );
        when(service.findOne(1)).thenReturn(Optional.of(dto));
        
        mockMvc.perform(get("/api/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.url", is("http://link.com")))
                .andExpect(jsonPath("$.type", is(DownloadJobType.VIDEO.toString())))
                .andExpect(jsonPath("$.filename", is("/tmp/abcd.mp4")))
                .andExpect(jsonPath("$.status", is(DownloadJobStatus.PENDING.toString())));
    }
    
    @Test
    public void should_return_404_if_no_job_of_such_id_exists() throws Exception {
        when(service.findOne(1)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/jobs/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void should_begin_new_video_download() throws Exception {
        mockMvc.perform(
                post("/api/jobs")
                        .content("{\"youtubeUrl\": \"http://link.com\", \"type\": \"VIDEO\"}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        
        verify(service, times(1)).beginDownload("http://link.com", DownloadJobType.VIDEO);
    }
    
    @Test
    public void should_begin_new_audio_download() throws Exception {
        mockMvc.perform(
                post("/api/jobs")
                        .content("{\"youtubeUrl\": \"http://link.com\", \"type\": \"AUDIO\"}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        
        verify(service, times(1)).beginDownload("http://link.com", DownloadJobType.AUDIO);
    }
    
    @Test
    public void should_return_404_if_there_is_no_fully_downloaded_file_yet_or_error() throws Exception {
        when(service.findOne(1)).thenReturn(Optional.empty());
        getJobAndAssert404();
        
        when(service.findOne(1)).thenReturn(Optional.of(createDownloadJobDto(DownloadJobStatus.PENDING)));
        getJobAndAssert404();
        
        when(service.findOne(1)).thenReturn(Optional.of(createDownloadJobDto(DownloadJobStatus.ERROR)));
        getJobAndAssert404();
    }
    
    private void getJobAndAssert404() throws Exception {
        mockMvc.perform(get("/api/jobs/1/download"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void should_download_existing_and_downloaded_movie_file_as_attachment() throws Exception {
        when(service.findOne(1)).thenReturn(Optional.of(createDownloadJobDto(DownloadJobStatus.SUCCESS)));
        
        mockMvc.perform(get("/api/jobs/1/download"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=abcd.mp4"));
    }
    
    private static DownloadJobDto createDownloadJobDto(DownloadJobStatus error) {
        return new DownloadJobDto(1, "", DownloadJobType.VIDEO, "/tmp/abcd.mp4", error);
    }
}