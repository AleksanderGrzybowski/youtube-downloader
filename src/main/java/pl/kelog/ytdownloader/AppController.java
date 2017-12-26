package pl.kelog.ytdownloader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    
    @RequestMapping(value = "/api/jobs", method = RequestMethod.POST)
    public JobIdDto beginDownload(@RequestBody YoutubeUrlDto dto) throws Exception {
        String jobId = youtubeService.beginDownload(dto.youtubeUrl);
        return new JobIdDto(
                jobId
        );
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
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobIdDto {
        String jobId;
    }
}
