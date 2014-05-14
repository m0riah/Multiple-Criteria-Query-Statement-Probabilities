/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.repositories;

import org.mqpbms.logger.models.TransitionTableNameIndex;

import java.util.Collection;

/**
 * Query Transition Table Name Index Table data access object.
 * <p>
 * The table keeps tracking of the query IDs a specific user has executed with Query Transition Table name.
 * Query Transition Table Name Index Table is supposed to be created for a specific user and
 * query statement pre-processing algorithm.
 * </p>
 *
 * @author sky
 * @version 2/13/14
 */
public interface TransitionTableNameIndexDao {

    /**
     * Generate a Query Transition Table Name Index Table name for a specific user.
     *
     * @param userName a user name.
     * @return the Query Transition Table Name Index Table name for the user.
     */
    String generateTableName(String userName);

    /**
     * Create a Query Transition Table Name Index Table.
     *
     * @param tableName a query Transition Table Index table name.
     */
    void createTable(String tableName);

    /**
     * Check whether the Query Transition Table Name Index Table has the
     * {@link org.mqpbms.logger.models.TransitionTableNameIndex} with the query ID.
     *
     * @param tableName a Query Transition Table Name Index Table name.
     * @param queryId   a query ID.
     * @return return true if it contain.
     */
    boolean contain(String tableName, String queryId);

    /**
     * Get a {@link org.mqpbms.logger.models.TransitionTableNameIndex} by the table name and query ID from
     * the Query Transition Table Name Index Table.
     *
     * @param tableName a Query Transition Table Name Index Table name.
     * @param queryId   a query ID.
     * @return {@link org.mqpbms.logger.models.TransitionTableNameIndex} object.
     */
    TransitionTableNameIndex getTransitionTableIndexById(String tableName, String queryId);

    /**
     * Get all {@link org.mqpbms.logger.models.TransitionTableNameIndex}s by the table name from
     * the Query Transition Table Name Index Table.
     *
     * @param tableName a Query Transition Table Name Index Table name.
     * @return a collection of {@link org.mqpbms.logger.models.TransitionTableNameIndex} objects.
     */
    Collection<TransitionTableNameIndex> getAllTransitionTableIndexes(String tableName);

    /**
     * Add a {@link org.mqpbms.logger.models.TransitionTableNameIndex} to the Query Transition Table Name Index Table.
     *
     * @param tableName                a Query Transition Table Name Index Table name.
     * @param transitionTableNameIndex a {@link org.mqpbms.logger.models.TransitionTableNameIndex} object.
     */
    void addTransitionTableIndex(String tableName, TransitionTableNameIndex transitionTableNameIndex);

}
