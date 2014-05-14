/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.repositories;

import org.mqpbms.logger.models.PbqLog;

import java.util.Collection;

/**
 * Probability Based Query Log table data access object.
 * <p>
 * Probability Based Query Log table is supposed to be created for a specific user and
 * query statement pre-processing algorithm.
 * </p>
 *
 * @author sky
 * @version 2/9/14
 */
public interface PbqLogDao {

    /**
     * Generate a PBQ Log Table name with the user name.
     *
     * @param userName a user name who executed the query.
     * @return a table name related to the user name.
     */
    String generateTableName(String userName);

    /**
     * Create a PBQ Log table with the table name.
     *
     * @param tableName a table name.
     */
    void createTable(String tableName);

    /**
     * Get all the PBQ Logs existing between the starting and end timestamp in the PBQ Log Table.
     *
     * @param tableName      a table name retrieving the data from.
     * @param startTimestamp starting timestamp.
     * @param endTimestamp   end timestamp.
     * @return collection of all {@link org.mqpbms.logger.models.PbqLog} objects existing between the timestamp interval.
     */
    Collection<PbqLog> getLogsBetweenTimestamps(String tableName,
                                                long startTimestamp, long endTimestamp);

    /**
     * Get all PBQ Logs in the PBQ Log Table.
     *
     * @param tableName a table name retrieving the data from.
     * @return collection of all the {@link org.mqpbms.logger.models.PbqLog}.
     */
    Collection<PbqLog> getAllLogs(String tableName);

    /**
     * Add a {@link org.mqpbms.logger.models.PbqLog} into the PBQ Log Table.
     *
     * @param tableName a table name storing the data to.
     * @param PbqLog    a {@link org.mqpbms.logger.models.PbqLog} object.
     */
    void addLog(String tableName, final PbqLog PbqLog);
}
