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
 * Query Transition Probability for a next query ID in the Query Transition Table.
 *
 * @author sky
 * @version 02/07/14
 */
public class QueryTransitionProbability {

    /**
     * The next query ID transitional from the current query.
     */
    private String nextQueryId;

    /**
     * The probability of execution of the next query.
     */
    private double probability;

    public String getNextQueryId() {
        return nextQueryId;
    }

    public void setNextQueryId(String nextQueryId) {
        this.nextQueryId = nextQueryId;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    @Override
    public String toString() {
        return "QueryTransitionProbability{" +
                "nextQueryId='" + nextQueryId + '\'' +
                ", probability=" + probability +
                '}';
    }
}
