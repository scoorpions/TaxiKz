package kz.taxikz.job;

import android.content.Context;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration.Builder;
import com.birbit.android.jobqueue.log.CustomLogger;

public class MyJobManager extends JobManager {

    public MyJobManager(Context context) {
        super(new Builder(context).customLogger(new CustomLogger() {
            @Override
            public boolean isDebugEnabled() {
                return false;
            }

            @Override
            public void d(String text, Object... args) {

            }

            @Override
            public void e(Throwable t, String text, Object... args) {

            }

            @Override
            public void e(String text, Object... args) {

            }

            @Override
            public void v(String text, Object... args) {

            }
        }).minConsumerCount(1).maxConsumerCount(7).loadFactor(3).consumerKeepAlive(15).build());
    }
}
