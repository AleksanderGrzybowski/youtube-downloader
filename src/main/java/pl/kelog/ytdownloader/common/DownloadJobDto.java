package pl.kelog.ytdownloader.common;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.kelog.ytdownloader.job.DownloadJobStatus;
import pl.kelog.ytdownloader.job.DownloadJobType;

@Data
@RequiredArgsConstructor
public class DownloadJobDto {
    
    public final int id;
    public final String url;
    public final DownloadJobType type;
    public final String filename;
    public final DownloadJobStatus status;
}
