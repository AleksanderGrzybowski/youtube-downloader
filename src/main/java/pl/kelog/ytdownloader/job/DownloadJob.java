package pl.kelog.ytdownloader.job;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DownloadJob {
    
    private int id;
    private String url;
    private DownloadJobType type;
    private String filename;
    private DownloadJobStatus status;
}
