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
import pl.kelog.ytdownloader.youtube.YoutubeService;

import java.io.File;

@RestController
@ResponseBody
@RequiredArgsConstructor
public class AppController {
    
    private final YoutubeService youtubeService;
    
    @RequestMapping("/api/thumbnailUrl")
    public ThumbnailUrlDto thumbnailUrl(@RequestParam("url") String youtubeUrl) throws Exception {
        return new ThumbnailUrlDto(
                youtubeService.getThumbnailUrl(youtubeUrl)
        );
    }
    
    @RequestMapping(value = "/api/jobs/{id}", method = RequestMethod.GET)
    public ResponseEntity<DownloadJobDto> listJob(@PathVariable("id") int id) {
        return youtubeService.findOne(id)
                .map(job -> new ResponseEntity<>(job, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @RequestMapping(value = "/api/jobs", method = RequestMethod.POST)
    public DownloadJobDto beginDownload(@RequestBody YoutubeUrlDto dto) {
        return youtubeService.beginDownload(dto.youtubeUrl);
    }
    
    @RequestMapping(
            value = "/api/jobs/{id}/download",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    @ResponseBody
    public ResponseEntity<FileSystemResource> getFile(@PathVariable("id") int id) {
        return youtubeService.findOne(id)
                .map(downloadJobDto -> new ResponseEntity<>(toResource(downloadJobDto), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    private FileSystemResource toResource(DownloadJobDto downloadJobDto) {
        return new FileSystemResource(new File(downloadJobDto.getFilename()));
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ThumbnailUrlDto {
        String thumbnailUrl;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class YoutubeUrlDto {
        String youtubeUrl;
    }
}
