package org.mqpbms.logger.repositories.impl;
/*
 * Created with IntelliJ IDEA.
 * User: sky
 * Date: 2/13/14
 * Time: 9:35 PM
 * To change this template use File | Settings | File Templates.
 */

import org.junit.Before;
import org.junit.Test;
import org.mqpbms.common.models.PreprocessAlgorithm;
import org.mqpbms.connection.DefaultCassandraFactory;
import org.mqpbms.connection.DefaultCassandraTemplate;
import org.mqpbms.logger.models.TransitionTableNameIndex;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 *
 * @author sky
 * @version 2/13/14
 */
public class SignatureBasedQueryTransitionTableNameIndexDaoTest {
    QctnBasedTransitionTableNameIndexDao signatureBasedTransitionTableIndexDao;
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
        signatureBasedTransitionTableIndexDao = new QctnBasedTransitionTableNameIndexDao();
        signatureBasedTransitionTableIndexDao.setCassandraTemplate(cassandraTemplate);
    }

    @Test
    public void testGenerateTableName() throws Exception {
        System.out.println(signatureBasedTransitionTableIndexDao.generateTableName("test"));
    }

    @Test
    public void testCreateTable() throws Exception {
        String tableName = signatureBasedTransitionTableIndexDao.generateTableName("test");
        signatureBasedTransitionTableIndexDao.createTable(tableName);
    }

    @Test
    public void testContain() throws Exception {
        String tableName = signatureBasedTransitionTableIndexDao.generateTableName("test");
        System.out.println(signatureBasedTransitionTableIndexDao.contain(tableName, "12345236"));
    }

    @Test
    public void testGetTransitionTableIndex() throws Exception {
        String tableName = signatureBasedTransitionTableIndexDao.generateTableName("test");
        System.out.println(signatureBasedTransitionTableIndexDao.getTransitionTableIndexById(tableName, "123456"));
    }

    @Test
    public void testGetAllTransitionTableIndexes() throws Exception {
        String tableName = signatureBasedTransitionTableIndexDao.generateTableName("test");
        Collection<TransitionTableNameIndex> transitionTableNameIndexes =
                signatureBasedTransitionTableIndexDao.getAllTransitionTableIndexes(tableName);
        for (TransitionTableNameIndex sti : transitionTableNameIndexes) {
            System.out.println(sti);
        }
    }

    @Test
    public void testAddTransitionTableIndex() throws Exception {
        String tableName = signatureBasedTransitionTableIndexDao.generateTableName("test");
        TransitionTableNameIndex signTransitionTableNameIndex =
                TransitionTableNameIndex.getObjectHavingRandomTransitionTableName("test", PreprocessAlgorithm.QCTN);
        signatureBasedTransitionTableIndexDao.addTransitionTableIndex(tableName, signTransitionTableNameIndex);
    }
}
