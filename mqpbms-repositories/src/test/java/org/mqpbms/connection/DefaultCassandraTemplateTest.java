package org.mqpbms.connection;
/*
 * Created with IntelliJ IDEA.
 * User: sky
 * Date: 2/8/14
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */

import com.datastax.driver.core.Session;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 *
 * @author sky
 * @version 2/8/14
 */
public class DefaultCassandraTemplateTest {

    DefaultCassandraTemplate cassandraTemplate;

    @Before
    public void setUp() throws Exception {
        DefaultCassandraFactory defaultCassandraFactory = new DefaultCassandraFactory();
        defaultCassandraFactory.setHostName("192.168.56.101");
        defaultCassandraFactory.setPort(9042);
        defaultCassandraFactory.afterPropertiesSet();
        defaultCassandraFactory.getCluster();
        cassandraTemplate = new DefaultCassandraTemplate();
        cassandraTemplate.setFactory(defaultCassandraFactory);
        cassandraTemplate.setKeyspace("ppc");
        cassandraTemplate.afterPropertiesSet();
    }

    @Test
    public void testGetSession() throws Exception {
        Session session = cassandraTemplate.getSession();
    }
}
