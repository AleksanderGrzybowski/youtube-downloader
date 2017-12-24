package pl.kelog.ytdownloader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    
    @Data
    @AllArgsConstructor
    public class ThumbnailUrlDto {
        final String thumbnailUrl;
    }
}
