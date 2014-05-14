/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.repositories;

import org.mqpbms.logger.models.QueryTransitionProbability;

import java.util.Collection;

/**
 * Query Transition Table data access object.
 * <p>
 * This is to calculate the Query Transition Probability.
 * Many Query Transition Tables are supposed to be created.
 * </p>
 * <p>
 * FYI, in Cassandra,
 * Table names shouldn't be more than 48 characters long
 * </p>
 *
 * @author sky
 * @version 2/8/14
 */
public interface QueryTransitionProbabilityDao {

    /**
     * Create a query Transition Table.
     *
     * @param tableName a table name for the Query Transition Table.
     */
    void createTable(String tableName);

    /**
     * Increment a count value of the corresponding query ID from the Query Transition and
     * Transition Total Hit Table.
     *
     * @param previousTransitionTableName a Query Transition Probability Table name for
     *                                    the previous query.
     * @param currentQueryID              a current query ID.
     */
    void updateQueryTransitionCounts(String previousTransitionTableName, String currentQueryID);

    /**
     * Get Query Transition Probability.
     *
     * @param previousTransitionTableName a Query Transition Probability table name for
     *                                    the previous query.
     * @param currentQueryID              a current query ID.
     * @return a probability of the current query execution from the previous query.
     */
    double getQueryTransitionProbability(String previousTransitionTableName,
                                         String currentQueryID);

    /**
     * Get all the {@link org.mqpbms.logger.models.QueryTransitionProbability} from the Query Transition Table.
     *
     * @param transitionTableName a Query Transition table name.
     * @return collection of {@link org.mqpbms.logger.models.QueryTransitionProbability} objects.
     */
    Collection<QueryTransitionProbability> getAllQueryTransitionProbabilities(String transitionTableName);

}
