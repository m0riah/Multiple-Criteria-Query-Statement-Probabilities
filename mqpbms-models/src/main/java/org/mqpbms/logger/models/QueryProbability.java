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
 * Query Probability for a query ID.
 *
 * @author sky
 * @version 1/7/14
 */
public class QueryProbability {

    /**
     * Query id.
     */
    private String id;

    /**
     * Probability of the query.
     */
    private double probability;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    @Override
    public String toString() {
        return "QueryProbability{" +
                "id='" + id + '\'' +
                ", probability=" + probability +
                '}';
    }
}
