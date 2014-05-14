package org.mqpbms.logger.repositories.impl;
/*
 * Created with IntelliJ IDEA.
 * User: sky
 * Date: 2/9/14
 * Time: 3:27 AM
 * To change this template use File | Settings | File Templates.
 */

import org.junit.Before;
import org.junit.Test;
import org.mqpbms.connection.DefaultCassandraFactory;
import org.mqpbms.connection.DefaultCassandraTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author sky
 * @version 2/9/14
 */
public class QctnBasedQueryProbabilityDaoTest {

    QctnBasedQueryProbabilityDao transactionProbabilityDao;

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
        transactionProbabilityDao = new QctnBasedQueryProbabilityDao();
        transactionProbabilityDao.setCassandraTemplate(cassandraTemplate);
    }

    @Test
    public void testGenerateTableName() throws Exception {
        System.out.println(transactionProbabilityDao.generateTableName("test"));
    }

    @Test
    public void testCreateTable() throws Exception {
        String tableName = transactionProbabilityDao.generateTableName("test");
        transactionProbabilityDao.createTable(tableName);

    }

    @Test
    public void testGetTransactionProbability() throws Exception {
        String tableName = transactionProbabilityDao.generateTableName("test");
        System.out.println(transactionProbabilityDao.getQueryProbability(tableName, "123456"));
    }

    @Test
    public void testUpdateAllTransactionProbabilities() throws Exception {
        String tableName = transactionProbabilityDao.generateTableName("test");
        Map<String, Double> transactionProbabilities = new HashMap<>();
        transactionProbabilities.put("123456", 0.1);
        transactionProbabilities.put("123457", 0.3);
        transactionProbabilities.put("123458", 0.4);
        transactionProbabilityDao.updateAllQueryProbabilities(tableName, transactionProbabilities);
    }
}
