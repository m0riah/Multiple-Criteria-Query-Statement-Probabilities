package org.mqpbms.logger.repositories.impl;
/*
 * Created with IntelliJ IDEA.
 * User: sky
 * Date: 2/14/14
 * Time: 3:59 PM
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
 * @version 2/14/14
 */
public class OrcnBasedUserDaoTest {

    OrcnBasedUserDao orcnBasedUserDao = new OrcnBasedUserDao();

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
        orcnBasedUserDao = new OrcnBasedUserDao();
        orcnBasedUserDao.setCassandraTemplate(cassandraTemplate);
        orcnBasedUserDao.afterPropertiesSet();
    }

    @Test
    public void testGetUser() throws Exception {
        System.out.println(orcnBasedUserDao.getUser("test"));

    }

    @Test
    public void testAddUser() throws Exception {
        User user = new User();
        user.setUserName("test");
        user.setPreviousQueryId("123456");
        orcnBasedUserDao.addUser(user);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        System.out.println(orcnBasedUserDao.getAllUsers());
    }
}
