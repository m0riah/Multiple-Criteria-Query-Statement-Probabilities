/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.connection;

import com.datastax.driver.core.Session;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Default implementation of the CassandraTemplate interface {@link CassandraTemplate}
 *
 * @author sky
 * @version 12/28/13
 */
public class DefaultCassandraTemplate implements CassandraTemplate, InitializingBean, DisposableBean {

    /**
     * CassandraFactory to access the Cassandra DB.
     */
    private CassandraFactory factory;

    /**
     * {@link com.datastax.driver.core.Session} to execute queries on Cassandra DB.
     */
    private Session session;

    /**
     * The name of keyspace using.
     */
    private String keyspace;

    /**
     * Connect to the Cassandra DB and be in the predefined keyspace.
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (session == null) {
            session = factory.getCluster().connect();
        }
        session.execute("CREATE KEYSPACE IF NOT EXISTS " + keyspace + " WITH replication = {'class':'SimpleStrategy', 'replication_factor':3};");
        session.execute("USE " + keyspace + ";");
    }

    /**
     * Shutdown the session at the destroy time.
     *
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        session.shutdown();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session getSession() {
        return session;
    }

    public void setFactory(CassandraFactory factory) {
        this.factory = factory;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    public String getKeyspace() {
        return keyspace;
    }
}
