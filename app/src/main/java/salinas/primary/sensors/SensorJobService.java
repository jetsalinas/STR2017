package salinas.primary.sensors;

import android.app.job.JobParameters;
import android.app.job.JobService;

/**
 * Created by Jose Salinas on 3/30/2017.
 */

public class SensorJobService extends JobService {

    public SensorJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
