package org.mqpbms.logger.repositories.impl;
/*
 * Created with IntelliJ IDEA.
 * User: sky
 * Date: 2/8/14
 * Time: 10:36 PM
 * To change this template use File | Settings | File Templates.
 */

import org.junit.Before;
import org.junit.Test;
import org.mqpbms.connection.DefaultCassandraFactory;
import org.mqpbms.connection.DefaultCassandraTemplate;
import org.mqpbms.logger.models.OrcnBasedQuery;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author sky
 * @version 2/8/14
 */
public class OrcnBasedQueryDaoImplTest {

    OrcnBasedQueryDaoImpl orcnBasedTransactionDao;

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
        orcnBasedTransactionDao = new OrcnBasedQueryDaoImpl();
        orcnBasedTransactionDao.setCassandraTemplate(cassandraTemplate);
        orcnBasedTransactionDao.afterPropertiesSet();
    }

    @Test
    public void testAddTransaction() throws Exception {
        OrcnBasedQuery orcnBasedQuery = new OrcnBasedQuery();
        orcnBasedQuery.setId("09876");
        Set<String> columnNames = new HashSet<>();
        columnNames.add("col1");
        columnNames.add("col2");
        orcnBasedQuery.setColumnNames(columnNames);
        orcnBasedTransactionDao.addQuery(orcnBasedQuery);
    }

    @Test
    public void testContain() throws Exception {
        System.out.println(orcnBasedTransactionDao.contain("09876"));
    }

    @Test
    public void testGetTransactionById() throws Exception {
        System.out.println(orcnBasedTransactionDao.getQueryById("09876"));
    }

    @Test
    public void testGetAllTransactions() throws Exception {
        System.out.println(orcnBasedTransactionDao.getAllQueries());
    }
}
