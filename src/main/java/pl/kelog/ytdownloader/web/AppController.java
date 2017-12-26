package pl.kelog.ytdownloader.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kelog.ytdownloader.common.DownloadJobDto;
import pl.kelog.ytdownloader.youtube.YoutubeService;

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
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ThumbnailUrlDto {
        String thumbnailUrl;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class YoutubeUrlDto {
        String youtubeUrl;
    }
}
