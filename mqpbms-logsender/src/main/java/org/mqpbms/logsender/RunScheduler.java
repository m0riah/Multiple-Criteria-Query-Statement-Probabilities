/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logsender;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import java.io.IOException;

/**
 * This is the spring batch job scheduler with the given parameter.
 *
 * @author sky
 * @version 10/26/13
 * @since 10/26/13
 */
public class RunScheduler {

    /**
     * The shell script to refresh the general_log in mysql.
     */
    private String refreshLogCommand;

    /**
     * Necessary to run the job.
     */
    private JobLauncher jobLauncher;

    /**
     * a job to run.
     */
    private Job job;

    /**
     * Runs the job every certain interval, and execute a rotate shell script, which is basically cleaning up
     * the general_log table in mysql system database.
     */
    public void run() {
        try {
            JobParameters param = new JobParametersBuilder().addLong("job",
                    System.currentTimeMillis()).toJobParameters();
            JobExecution execution = jobLauncher.run(job, param);
            Process p = Runtime.getRuntime().exec(refreshLogCommand);
            p.waitFor();
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRefreshLogCommand(String refreshLogCommand) {
        this.refreshLogCommand = refreshLogCommand;
    }

    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
