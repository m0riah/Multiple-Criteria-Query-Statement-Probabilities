/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.models;

/**
 * Probability Based Query Log.
 *
 * @author sky
 * @version 1/7/14
 */
public class PbqLog {

    /**
     * A timestamp when the query is logged.
     */
    private long loggingTime;

    /**
     * A timestamp when the transaction was executed.
     */
    private long executionTime;

    /**
     * user name.
     */
    private String userName;

    /**
     * hostName the insider accessing the DB from.
     */
    private String hostName;

    /**
     * Query ID.
     */
    private String queryId;

    /**
     * Query Transition Probability
     */
    private double queryTransitionProbability;

    /**
     * Query Probability
     */
    private double queryProbability;

    public PbqLog(long loggingTime,
                  long executionTime,
                  String userName,
                  String hostName,
                  String queryId,
                  double queryTransitionProbability,
                  double queryProbability) {
        this.loggingTime = loggingTime;
        this.executionTime = executionTime;
        this.userName = userName;
        this.hostName = hostName;
        this.queryId = queryId;
        this.queryTransitionProbability = queryTransitionProbability;
        this.queryProbability = queryProbability;
    }

    public long getLoggingTime() {
        return loggingTime;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public String getUserName() {
        return userName;
    }

    public String getHostName() {
        return hostName;
    }

    public String getQueryId() {
        return queryId;
    }

    public double getQueryTransitionProbability() {
        return queryTransitionProbability;
    }

    @Override
    public String toString() {
        return "PbqLog{" +
                "loggingTime=" + loggingTime +
                ", executionTime=" + executionTime +
                ", userName='" + userName + '\'' +
                ", hostName='" + hostName + '\'' +
                ", queryId='" + queryId + '\'' +
                ", queryTransitionProbability=" + queryTransitionProbability +
                ", queryProbability=" + queryProbability +
                '}';
    }

    public double getQueryProbability() {
        return queryProbability;
    }

}
