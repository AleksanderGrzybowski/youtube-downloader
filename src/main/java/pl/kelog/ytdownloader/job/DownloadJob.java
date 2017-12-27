package pl.kelog.ytdownloader.job;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DownloadJob {
    
    private int id;
    private String url;
    private String filename;
    private Status status;
    
    public enum Status {
        PENDING, ERROR, SUCCESS
    }
}
