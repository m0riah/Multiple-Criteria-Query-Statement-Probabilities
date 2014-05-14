package org.mqpbms.logger.repositories.impl;
/*
 * Created with IntelliJ IDEA.
 * User: sky
 * Date: 2/10/14
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */

import org.junit.Before;
import org.junit.Test;
import org.mqpbms.connection.DefaultCassandraFactory;
import org.mqpbms.connection.DefaultCassandraTemplate;
import org.mqpbms.logger.models.PbqLog;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 *
 * @author sky
 * @version 2/10/14
 */
public class QctnBasedPbqLogDaoTest {

    QctnBasedPbqLogDao pbtLogDao;

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
        pbtLogDao = new QctnBasedPbqLogDao();
        pbtLogDao.setCassandraTemplate(cassandraTemplate);
    }

    @Test
    public void testGenerateTableName() throws Exception {
//        System.out.println(pbtLogDao.generateTableName("test"));
    }

    @Test
    public void testCreateTable() throws Exception {
//        String tableName = pbtLogDao.generateTableName("test");
//        pbtLogDao.createTable(tableName);
    }

    @Test
    public void testGetLogsAfterTimestamp() throws Exception {
//        String tableName = pbtLogDao.generateTableName("test");
//        Collection<PbqLog> result = pbtLogDao.getLogsAfterTimestamp(tableName, new Date().getTime());
//        Iterator<PbqLog> itr = result.iterator();
//        while (itr.hasNext()) {
//            PbqLog pbtLog = itr.next();
//            System.out.println(pbtLog);
//            System.out.println(new Timestamp(pbtLog.getLoggingTime()));
//        }
    }

    @Test
    public void testGetLogsBeforeTimestamp() throws Exception {
//        String tableName = pbtLogDao.generateTableName("test");
//        Collection<PbqLog> result = pbtLogDao.getLogsBeforeTimestamp(tableName, new Date().getTime());
//        Iterator<PbqLog> itr = result.iterator();
//        while (itr.hasNext()) {
//            PbqLog pbtLog = itr.next();
//            System.out.println(pbtLog);
//            System.out.println(new Timestamp(pbtLog.getLoggingTime()));
//        }
    }

    @Test
    public void testGetLogsBetweenTimestamps() throws Exception {
        String tableName = pbtLogDao.generateTableName("user1");
        Collection<PbqLog> result = pbtLogDao.getLogsBetweenTimestamps(tableName, 1392849760706L, 1392796060714L);
        Iterator<PbqLog> itr = result.iterator();
        while (itr.hasNext()) {
            PbqLog PbqLog = itr.next();
            System.out.println(PbqLog);
            System.out.println(new Timestamp(PbqLog.getLoggingTime()));
        }
    }

    @Test
    public void testGetAllLogs() throws Exception {
//        String tableName = pbtLogDao.generateTableName("user1");
//        Collection<PbqLog> result = pbtLogDao.getAllLogs(tableName);
//        Iterator<PbqLog> itr = result.iterator();
//        while (itr.hasNext()) {
//            PbqLog pbtLog = itr.next();
//            System.out.println(pbtLog);
//            System.out.println(new Timestamp(pbtLog.getLoggingTime()));
//        }
    }

    @Test
    public void testAddLog() throws Exception {
//        String tableName = pbtLogDao.generateTableName("test");
//        long tempTime = new Date().getTime();
//        PbqLog pbtLog = new PbqLog(tempTime, tempTime, "test", "localhost", "1234567", 0.1, 0.3);
//
//        pbtLogDao.addLog(tableName, pbtLog);
//        long tempTime1 = new Date().getTime() + 10;
//        PbqLog pbtLog1 = new PbqLog(tempTime1, tempTime1, "test", "localhost", "1234568", 0.1, 0.3);
//
//        pbtLogDao.addLog(tableName, pbtLog1);
    }
}
