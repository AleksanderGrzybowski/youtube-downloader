package pl.kelog.ytdownloader.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kelog.ytdownloader.youtube.YoutubeService;

@RestController
@ResponseBody
@RequestMapping("/api/")
@CrossOrigin
@RequiredArgsConstructor
public class ThumbnailController {
    
    private final YoutubeService youtubeService;
    
    @RequestMapping("/thumbnailUrl")
    public ResponseEntity<ThumbnailUrlDto> thumbnailUrl(@RequestParam("url") String youtubeUrl) {
        try {
            String thumbnailUrl = youtubeService.getThumbnailUrl(youtubeUrl);
            return new ResponseEntity<>(new ThumbnailUrlDto(thumbnailUrl), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ThumbnailUrlDto {
        String thumbnailUrl;
    }
}
