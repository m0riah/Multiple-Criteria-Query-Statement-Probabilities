package org.mqpbms.logger.repositories.impl;
/*
 * Created with IntelliJ IDEA.
 * User: sky
 * Date: 2/14/14
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */

import org.junit.Before;
import org.junit.Test;
import org.mqpbms.connection.DefaultCassandraFactory;
import org.mqpbms.connection.DefaultCassandraTemplate;

/**
 * Created with IntelliJ IDEA.
 *
 * @author sky
 * @version 2/14/14
 */
public class ORCNBasedQueryQueryTransitionProbabilityDaoTest {

    OrcnBasedQueryTransitionProbabilityDao orcnBasedTransitionProbabilityDao;

    @Before
    public void setUp() throws Exception {
        DefaultCassandraFactory defaultCassandraFactory = new DefaultCassandraFactory();
        defaultCassandraFactory.setHostName("192.168.56.101");
        defaultCassandraFactory.setPort(9042);
        defaultCassandraFactory.afterPropertiesSet();
        defaultCassandraFactory.getCluster();
        DefaultCassandraTemplate cassandraTemplate = new DefaultCassandraTemplate();
        cassandraTemplate.setFactory(defaultCassandraFactory);
        cassandraTemplate.setKeyspace("mqpbms");
        cassandraTemplate.afterPropertiesSet();
        orcnBasedTransitionProbabilityDao = new OrcnBasedQueryTransitionProbabilityDao();
        orcnBasedTransitionProbabilityDao.setCassandraTemplate(cassandraTemplate);
    }


    @Test
    public void testCreateTransactionTransitionTable() throws Exception {
        orcnBasedTransitionProbabilityDao.createTable("d1234_56df");

    }

    @Test
    public void testUpdateTransitionCounts() throws Exception {
        orcnBasedTransitionProbabilityDao.updateQueryTransitionCounts("d1234_56df", "0s98r76dd");
    }

    @Test
    public void testGetTransactionTransitionProbability() throws Exception {
        System.out.println(orcnBasedTransitionProbabilityDao.getQueryTransitionProbability("d1234_56df", "098r76d"));
    }

    @Test
    public void testGetAllTransactionTransitionProbabilities() throws Exception {
        System.out.println(orcnBasedTransitionProbabilityDao.getAllQueryTransitionProbabilities("d1234_56df"));

    }
}
