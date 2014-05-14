/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.repositories;

import java.util.Map;

/**
 * The Query Probability Table data access object.
 * <p>
 * A query Probability table is supposed to be created for a specific user nd
 * query statement pre-processing algorithm.
 * </p>
 *
 * @author sky
 * @version 2/9/14
 */
public interface QueryProbabilityDao {

    /**
     * Generate a Query Probability Table name by a user name.
     *
     * @param userName a user name.
     * @return a Query Probability Table name.
     */
    String generateTableName(String userName);

    /**
     * Create a Query Probability Table with a table name.
     *
     * @param tableName a table name for the Query Probability Table.
     */
    void createTable(String tableName);

    /**
     * Get a query probability, which is indicating the probability of a query executed for a specific user.
     *
     * @param tableName a table name for the Query Probability Table.
     * @param queryId a query id.
     * @return a query probability for the specific user and query.
     */
    double getQueryProbability(String tableName, String queryId);

    /**
     * Update all query probabilities for all the queries in the Query Probability Table.
     *
     * @param tableName a table name for the the Query Probability Table.
     * @param queryProbabilities a map of query probabilities pairs
     */
    void updateAllQueryProbabilities(String tableName, Map<String, Double> queryProbabilities);

}
