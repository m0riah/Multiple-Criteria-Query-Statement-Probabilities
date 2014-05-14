package org.mqpbms.logger.repositories.impl;
/*
 * Created with IntelliJ IDEA.
 * User: sky
 * Date: 2/9/14
 * Time: 1:44 AM
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
 * @version 2/9/14
 */
public class SignatureBasedQueryQueryTransitionProbabilityDaoTest {

    QctnBasedQueryTransitionProbabilityDao signatureBasedTransitionProbabilityDao;

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
        signatureBasedTransitionProbabilityDao = new QctnBasedQueryTransitionProbabilityDao();
        signatureBasedTransitionProbabilityDao.setCassandraTemplate(cassandraTemplate);
    }


    @Test
    public void testCreateTransactionTransitionTable() throws Exception {
        signatureBasedTransitionProbabilityDao.createTable("d222222f");

    }

    @Test
    public void testUpdateTransitionCounts() throws Exception {
        signatureBasedTransitionProbabilityDao.updateQueryTransitionCounts("d222222f", "0s98r76dd");
    }

    @Test
    public void testGetTransactionTransitionProbability() throws Exception {
        System.out.println(signatureBasedTransitionProbabilityDao.getQueryTransitionProbability("d222222f", "098r76d"));
    }

    @Test
    public void testGetAllTransactionTransitionProbabilities() throws Exception {
        System.out.println(signatureBasedTransitionProbabilityDao.getAllQueryTransitionProbabilities("d222222f"));
    }
}
