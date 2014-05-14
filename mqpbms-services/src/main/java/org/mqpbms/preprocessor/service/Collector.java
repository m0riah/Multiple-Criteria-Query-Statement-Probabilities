/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.preprocessor.service;

import com.akiban.sql.parser.Visitable;

/**
 * Collect specific information from the original query statement.
 * <p/>
 * <p>Collectors will be used in {@link org.mqpbms.preprocessor.service.StatementProbe}</p>
 *
 * @author sky
 * @version 2/3/14
 */
public interface Collector {

    /**
     * Reset the collector to process a new original query statement.
     *
     * @param userName     the user name who executed the query.
     * @param hostName     the host name where the query executed.
     * @param timeStamp    the long timestamp when the query executed.
     * @param originalStatement a original statement.
     */
    void reset(String userName, String hostName, long timeStamp, String originalStatement);

    /**
     * Collect specific information from each node of parsed query statement.
     *
     * @param visitable a node of parsed statement.
     */
    void collect(Visitable visitable);

    /**
     * Return true if this Collector does not need to collect information.
     *
     * @return true if this Collector does not need to collect information.
     */
    boolean stopCollect();

}
