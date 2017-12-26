package pl.kelog.ytdownloader.job;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Service
public class DownloadJobRepository {
    
    private final Map<String, DownloadJob> database = new HashMap<>();
    
    public DownloadJob create() {
        DownloadJob jobInfo = new DownloadJob();
        jobInfo.setId(randomAlphanumeric(5));
        return jobInfo;
    }
    
    public void save(DownloadJob info) {
        database.put(info.getId(), info);
    }
}
