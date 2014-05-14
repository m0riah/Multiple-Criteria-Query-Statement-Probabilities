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

/**
 * Interface that specifies a basic set of Cassandra DB operations.
 * <p/>
 * One template uses only one session which is accessing only one keyspace.
 * Session instances are thread-safe and usually a single instance is enough per application.
 * <p/>
 * However, a given session can only be set to one keyspace at a time, so one instance per keyspace is necessary
 * (http://www.datastax.com/drivers/java/1.0/com/datastax/driver/core/Session.html).
 *
 * @author sky
 * @version 12/28/13
 */
public interface CassandraTemplate {

    /**
     * Return {@link com.datastax.driver.core.Session}.
     *
     * @return {@link com.datastax.driver.core.Session}.
     */
    Session getSession();


}
