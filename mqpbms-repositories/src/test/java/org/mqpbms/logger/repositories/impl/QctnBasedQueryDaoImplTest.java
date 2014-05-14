package org.mqpbms.logger.repositories.impl;
/*
 * Created with IntelliJ IDEA.
 * User: sky
 * Date: 2/8/14
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */

import org.junit.Before;
import org.junit.Test;
import org.mqpbms.connection.DefaultCassandraFactory;
import org.mqpbms.connection.DefaultCassandraTemplate;
import org.mqpbms.logger.models.QctnBasedQuery;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author sky
 * @version 2/8/14
 */
public class QctnBasedQueryDaoImplTest {

    QctnBasedQueryDaoImpl signatureBasedTransactionDao;

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
        signatureBasedTransactionDao = new QctnBasedQueryDaoImpl();
        signatureBasedTransactionDao.setCassandraTemplate(cassandraTemplate);
        signatureBasedTransactionDao.afterPropertiesSet();
    }

    @Test
    public void testAddTransaction() throws Exception {
        QctnBasedQuery qctnBasedQuery = new QctnBasedQuery();
        qctnBasedQuery.setId("1234567");
        qctnBasedQuery.setCrud("SELECT");
        Set<String> tableNames = new HashSet<>();
        tableNames.add("hey");
        tableNames.add("day");
        qctnBasedQuery.setTableNames(tableNames);
        signatureBasedTransactionDao.addQuery(qctnBasedQuery);
    }

    @Test
    public void testContains() throws Exception {
        System.out.println(signatureBasedTransactionDao.contain("123456"));
    }

    @Test
    public void testGetTransactionbyId() throws Exception {
        System.out.println(signatureBasedTransactionDao.getQueryById("123456"));
    }

    @Test
    public void testGetAllTransactions() throws Exception {
        System.out.println(signatureBasedTransactionDao.getAllQueries());
    }
}
