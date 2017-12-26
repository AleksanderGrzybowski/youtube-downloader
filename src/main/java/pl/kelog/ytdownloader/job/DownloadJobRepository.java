package pl.kelog.ytdownloader.job;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@Service
public class DownloadJobRepository {
    
    private final Map<Integer, DownloadJob> database = new HashMap<>();
    
    public DownloadJob create() {
        DownloadJob jobInfo = new DownloadJob();
        jobInfo.setId(Integer.parseInt(randomNumeric(3)));
        return jobInfo;
    }
    
    public void save(DownloadJob info) {
        database.put(info.getId(), info);
    }
    
    public Optional<DownloadJob> findOne(int id) {
        return Optional.ofNullable(database.get(id));
    }
}
