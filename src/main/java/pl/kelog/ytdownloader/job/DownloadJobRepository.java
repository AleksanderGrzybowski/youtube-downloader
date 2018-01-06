package pl.kelog.ytdownloader.job;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class DownloadJobRepository {
    
    private static final int STARTING_ID = 1;
    
    private int nextId = STARTING_ID;
    
    private final Map<Integer, DownloadJob> database = new HashMap<>();
    
    public DownloadJob create() {
        DownloadJob jobInfo = new DownloadJob();
        jobInfo.setId(nextId);
        nextId++;
        return jobInfo;
    }
    
    public void save(DownloadJob info) {
        database.put(info.getId(), info);
    }
    
    public Optional<DownloadJob> findOne(int id) {
        return Optional.ofNullable(database.get(id));
    }
}
