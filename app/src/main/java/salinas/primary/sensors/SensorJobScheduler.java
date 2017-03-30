package salinas.primary.sensors;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import java.util.List;

/**
 * Created by Jose Salinas on 3/30/2017.
 */

public class SensorJobScheduler extends JobScheduler {

    @Override
    public int schedule(JobInfo job) {
        return 0;
    }

    @Override
    public void cancel(int jobId) {

    }

    @Override
    public void cancelAll() {

    }

    @Override
    public List<JobInfo> getAllPendingJobs() {
        return null;
    }

    @Override
    public JobInfo getPendingJob(int jobId) {
        return null;
    }
}
