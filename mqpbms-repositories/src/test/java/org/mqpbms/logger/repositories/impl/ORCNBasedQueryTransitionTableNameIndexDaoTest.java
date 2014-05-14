package org.mqpbms.logger.repositories.impl;
/*
 * Created with IntelliJ IDEA.
 * User: sky
 * Date: 2/13/14
 * Time: 5:06 PM
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
public class ORCNBasedQueryTransitionTableNameIndexDaoTest {

    OrcnBasedTransitionTableNameIndexDao orcnBasedTransitionTableIndexDao;

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
        orcnBasedTransitionTableIndexDao = new OrcnBasedTransitionTableNameIndexDao();
        orcnBasedTransitionTableIndexDao.setCassandraTemplate(cassandraTemplate);
    }

    @Test
    public void testGenerateTableName() throws Exception {
        System.out.println(orcnBasedTransitionTableIndexDao.generateTableName("test"));
    }

    @Test
    public void testCreateTable() throws Exception {
        String tableName = orcnBasedTransitionTableIndexDao.generateTableName("test");
        orcnBasedTransitionTableIndexDao.createTable(tableName);
    }

    @Test
    public void testContain() throws Exception {
        String tableName = orcnBasedTransitionTableIndexDao.generateTableName("test");
        System.out.println(orcnBasedTransitionTableIndexDao.contain(tableName, "1234563"));
    }

    @Test
    public void testGetTransitionTableIndex() throws Exception {
        String tableName = orcnBasedTransitionTableIndexDao.generateTableName("test");
        System.out.println(orcnBasedTransitionTableIndexDao.getTransitionTableIndexById(tableName, "12s3456"));
    }

    @Test
    public void testGetAllTransitionTableIndexes() throws Exception {
        String tableName = orcnBasedTransitionTableIndexDao.generateTableName("test");
        Collection<TransitionTableNameIndex> orcnBasedTransitionTableNameIndexes =
                orcnBasedTransitionTableIndexDao.getAllTransitionTableIndexes(tableName);
        for (TransitionTableNameIndex ori : orcnBasedTransitionTableNameIndexes) {
            System.out.println(ori);
        }
    }

    @Test
    public void testAddTransitionTableIndex() throws Exception {
        TransitionTableNameIndex orcnBasedTransitionTableNameIndex = new TransitionTableNameIndex();
        orcnBasedTransitionTableNameIndex.setQueryId("1234567");
        orcnBasedTransitionTableNameIndex.setTransitionTableName("transition12");
        String tableName = orcnBasedTransitionTableIndexDao.generateTableName("test");
        TransitionTableNameIndex orcnTransitionTableNameIndex =
                TransitionTableNameIndex.getObjectHavingRandomTransitionTableName("test", PreprocessAlgorithm.ORCN);
        orcnBasedTransitionTableIndexDao.addTransitionTableIndex(tableName, orcnTransitionTableNameIndex);
    }
}
