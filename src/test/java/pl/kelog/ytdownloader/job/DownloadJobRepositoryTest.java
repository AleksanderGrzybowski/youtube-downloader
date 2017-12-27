package pl.kelog.ytdownloader.job;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class DownloadJobRepositoryTest {
    
    private DownloadJobRepository repository;
    
    @Before
    public void setup() {
        this.repository = new DownloadJobRepository();
    }
    
    @Test
    public void should_produce_new_download_job() {
        assertThat(repository.create()).isNotNull();
    }
    
    @Test
    public void should_save_a_job_and_then_allow_to_get_it_back() {
        DownloadJob job = repository.create();
        repository.save(job);
    
        Optional<DownloadJob> result = repository.findOne(job.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(job.getId());
    }
    
    @Test
    public void should_return_empty_optional_if_no_job_of_given_id_exists() {
        assertThat(repository.findOne(123).isPresent()).isFalse();
    }
    
}