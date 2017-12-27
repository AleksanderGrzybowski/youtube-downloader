package pl.kelog.ytdownloader.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kelog.ytdownloader.common.DownloadJobDto;
import pl.kelog.ytdownloader.job.DownloadJob.Status;
import pl.kelog.ytdownloader.youtube.YoutubeService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Optional;

@RestController
@ResponseBody
@RequestMapping("/api/")
@RequiredArgsConstructor
public class JobController {
    
    private static final String DOWNLOAD_FILENAME = "download.mp4";
    
    private final YoutubeService youtubeService;
    
    @RequestMapping(value = "/jobs/{id}", method = RequestMethod.GET)
    public ResponseEntity<DownloadJobDto> listJob(@PathVariable("id") int id) {
        return youtubeService.findOne(id)
                .map(job -> new ResponseEntity<>(job, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @RequestMapping(value = "/jobs", method = RequestMethod.POST)
    public DownloadJobDto beginDownload(@RequestBody YoutubeUrlDto dto) {
        return youtubeService.beginDownload(dto.youtubeUrl);
    }
    
    @RequestMapping(
            value = "/jobs/{id}/download",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    @ResponseBody
    public ResponseEntity<FileSystemResource> getFile(@PathVariable("id") int id, HttpServletResponse response) {
        Optional<DownloadJobDto> dto = youtubeService.findOne(id);
        
        if (dto.isPresent() && dto.get().status == Status.SUCCESS) {
            response.setHeader("Content-Disposition", "attachment; filename=" + DOWNLOAD_FILENAME);
            return new ResponseEntity<>(toResource(dto.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    private FileSystemResource toResource(DownloadJobDto downloadJobDto) {
        return new FileSystemResource(new File(downloadJobDto.getFilename()));
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class YoutubeUrlDto {
        String youtubeUrl;
    }
}
