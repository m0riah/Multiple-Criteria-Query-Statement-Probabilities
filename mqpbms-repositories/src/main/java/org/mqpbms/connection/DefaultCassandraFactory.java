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
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;

/**
 * Default implementation of the {@link CassandraFactory}.
 *
 * @author sky
 * @version 12/28/13
 */
public class DefaultCassandraFactory implements InitializingBean, DisposableBean, CassandraFactory {

    /**
     * Cassandra DB cluster.
     */
    private Cluster cluster;

    /**
     * Cassandra DB host name.
     */
    private String hostName;

    /**
     * Cassandra DB port.
     */
    private int port;

    /**
     * Build a Cluster to access the Cassandra DB.
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (cluster == null) {
            cluster = Cluster.builder().addContactPoint(hostName).withPort(port).build();
            // .withSSL() // Uncomment if using client to node encryption
        }
    }

    /**
     * Free the Cluster at the destroy time.
     *
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        cluster = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cluster getCluster() throws DataAccessException {
        return cluster;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
