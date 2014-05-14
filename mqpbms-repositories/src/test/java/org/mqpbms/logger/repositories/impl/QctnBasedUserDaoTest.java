package org.mqpbms.logger.repositories.impl;
/*
 * Created with IntelliJ IDEA.
 * User: sky
 * Date: 2/8/14
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */

import org.junit.Before;
import org.junit.Test;
import org.mqpbms.connection.DefaultCassandraFactory;
import org.mqpbms.connection.DefaultCassandraTemplate;
import org.mqpbms.logger.models.User;

/**
 * Created with IntelliJ IDEA.
 *
 * @author sky
 * @version 2/8/14
 */
public class QctnBasedUserDaoTest {

    QctnBasedUserDao qctnBasedUserDao;

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
        qctnBasedUserDao = new QctnBasedUserDao();
        qctnBasedUserDao.setCassandraTemplate(cassandraTemplate);
        qctnBasedUserDao.afterPropertiesSet();
    }

    @Test
    public void testGetUser() throws Exception {
        System.out.println(qctnBasedUserDao.getUser("test"));

    }

    @Test
    public void testAddUser() throws Exception {
        User user = new User();
        user.setUserName("test");
        user.setPreviousQueryId("123456");
        qctnBasedUserDao.addUser(user);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        System.out.println(qctnBasedUserDao.getAllUsers());
    }
}
