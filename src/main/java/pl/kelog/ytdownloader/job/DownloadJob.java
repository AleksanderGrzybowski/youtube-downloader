package pl.kelog.ytdownloader.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadJob {
    
    private int id;
    private String url;
    private String filename;
    private DownloadStatus status;
    
    public enum DownloadStatus {
        PENDING, ERROR, SUCCESS
    }
}
