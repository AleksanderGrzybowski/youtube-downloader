package pl.kelog.ytdownloader.job;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class DownloadJobRepository {
    
    private static final int TOP_ID = 1000;
    
    private final Map<Integer, DownloadJob> database = new HashMap<>();
    
    public DownloadJob create() {
        DownloadJob jobInfo = new DownloadJob();
        jobInfo.setId(RandomUtils.nextInt(1, TOP_ID));
        return jobInfo;
    }
    
    public void save(DownloadJob info) {
        database.put(info.getId(), info);
    }
    
    public Optional<DownloadJob> findOne(int id) {
        return Optional.ofNullable(database.get(id));
    }
}
