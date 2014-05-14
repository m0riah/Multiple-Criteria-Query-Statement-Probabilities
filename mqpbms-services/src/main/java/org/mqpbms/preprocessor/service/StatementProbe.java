/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.preprocessor.service;

import com.akiban.sql.parser.Visitor;

import java.util.Map;

/**
 * To probe a original query statement and produce pre-processed queries using algorithms defined in a collection
 * of {@link Collector}.
 *
 * @author sky
 * @version 2/3/14
 */
public interface StatementProbe extends Visitor {

    /**
     * Add {@link org.mqpbms.preprocessor.service.Collector} to the Probe.
     * Those Collectors will be used for a batch process.
     *
     * @param collectorName a Collector name.
     * @param collector a {@link org.mqpbms.preprocessor.service.Collector}.
     */
    void addCollector(String collectorName, Collector collector);

    /**
     * Get pre-process algorithm name and {@link org.mqpbms.preprocessor.service.Collector} map.
     *
     * @return a pre-process algorithm name and {@link org.mqpbms.preprocessor.service.Collector} map.
     */
    Map<String, Collector> getCollectorMap();

    /**
     * Set all the {@link org.mqpbms.preprocessor.service.Collector}s empty it has, then set
     * new userName, hostName and timeStamp for the new raw query statement.
     *
     * @param userName a user name who executed the query statement.
     * @param hostName a host name where the query was executed.
     * @param timeStamp a timestamp when the query was executed.
     * @param originalStatement a original statement.
     */
    void reset(String userName, String hostName, long timeStamp, String originalStatement);

}
