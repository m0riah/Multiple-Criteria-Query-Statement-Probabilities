/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.connection;

import com.datastax.driver.core.Cluster;
import org.springframework.dao.DataAccessException;

/**
 * Cassandra Factory to get a Cluster having a information and known state of a Cassandra cluster.
 * <p/>
 * This is the main entry point of the driver. A simple example of access to a Cassandra cluster would be:
 * <pre>
 * Cluster cluster = Cluster.builder().addContactPoint("192.168.0.1").build();
 * Session session = cluster.connect("db1");
 * </pre>
 *
 * @author sky
 * @version 12/28/13
 */
public interface CassandraFactory {

    /**
     * Return the cluster instance for the connection.
     *
     * @return Cluster instance.
     * @throws org.springframework.dao.DataAccessException
     */
    Cluster getCluster() throws DataAccessException;

}
