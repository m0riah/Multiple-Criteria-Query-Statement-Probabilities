package org.mqpbms.logger.repositories.impl;
/*
 * Created with IntelliJ IDEA.
 * User: sky
 * Date: 2/14/14
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */

import org.junit.Before;
import org.junit.Test;
import org.mqpbms.connection.DefaultCassandraFactory;
import org.mqpbms.connection.DefaultCassandraTemplate;
import org.mqpbms.logger.models.PbqLog;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 *
 * @author sky
 * @version 2/14/14
 */
public class OrcnBasedPbqLogDaoTest {

    OrcnBasedPbqLogDao orcnBasedPBTLogDao;

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
        orcnBasedPBTLogDao = new OrcnBasedPbqLogDao();
        orcnBasedPBTLogDao.setCassandraTemplate(cassandraTemplate);
    }

    @Test
    public void testGenerateTableName() throws Exception {
        System.out.println(orcnBasedPBTLogDao.generateTableName("test"));
    }

    @Test
    public void testCreateTable() throws Exception {
        String tableName = orcnBasedPBTLogDao.generateTableName("test");
        orcnBasedPBTLogDao.createTable(tableName);
    }

    @Test
    public void testGetLogsBetweenTimestamps() throws Exception {

    }

    @Test
    public void testGetAllLogs() throws Exception {
        String tableName = orcnBasedPBTLogDao.generateTableName("test");
        Collection<PbqLog> result = orcnBasedPBTLogDao.getAllLogs(tableName);
        Iterator<PbqLog> itr = result.iterator();
        while (itr.hasNext()) {
            PbqLog PbqLog = itr.next();
            System.out.println(PbqLog);
            System.out.println(new Timestamp(PbqLog.getLoggingTime()));
        }
    }

    @Test
    public void testAddLog() throws Exception {
        String tableName = orcnBasedPBTLogDao.generateTableName("test");
        long tempTime = new Date().getTime();
        PbqLog PbqLog = new PbqLog(tempTime, tempTime, "test", "localhost", "1234567", 0.1, 0.3);

        orcnBasedPBTLogDao.addLog(tableName, PbqLog);
        long tempTime1 = new Date().getTime() + 10;
        PbqLog PbqLog1 = new PbqLog(tempTime1, tempTime1, "test", "localhost", "1234568", 0.1, 0.3);

        orcnBasedPBTLogDao.addLog(tableName, PbqLog1);
    }
}
