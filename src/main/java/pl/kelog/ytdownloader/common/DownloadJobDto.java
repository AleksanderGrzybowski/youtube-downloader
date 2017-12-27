package pl.kelog.ytdownloader.common;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.kelog.ytdownloader.job.DownloadJob;

@Data
@RequiredArgsConstructor
public class DownloadJobDto {
    
    public final int id;
    public final String url;
    public final String filename;
    public final DownloadJob.Status status;
}
