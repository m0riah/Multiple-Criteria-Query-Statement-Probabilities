/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.repositories.impl;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.mqpbms.connection.CassandraTemplate;
import org.mqpbms.logger.models.User;
import org.mqpbms.logger.repositories.UserDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Operation Related Column Names based User table data access object.
 *
 * @author sky
 * @version 2/14/14
 */
@Repository("OrcnBasedUserDao")
public class OrcnBasedUserDao implements UserDao, InitializingBean {

    /**
     * The User table name.
     */
    private static final String USER_TABLE_NAME = "orcn_users";

    /**
     * User column name.
     */
    static final String USER_NAME = "u_name";

    /**
     * Previous query ID column name.
     */
    static final String PREVIOUS_QUERY_ID = "prev_q_id";

    /**
     * Predefined select all users query.
     */
    private static String SELECT_ALL_USERS;

    /**
     * Predefined select a user by name query.
     */
    private static String SELECT_USER_BASE;

    static {
        SELECT_ALL_USERS = "SELECT *" +
                " FROM " + USER_TABLE_NAME + ";";

        SELECT_USER_BASE ="SELECT *" +
                " FROM " + USER_TABLE_NAME +
                " WHERE " + USER_NAME + " = '";
    }

    /**
     * Cassandra template to get the session for the cassandra DB.
     */
    @Autowired
    @Qualifier("cassandraTemplate")
    private CassandraTemplate cassandraTemplate;

    /**
     * Create a user table if it does not exist.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // "IF NOT EXISTS" keyword is used for safety.
        cassandraTemplate.getSession().execute("CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME + " (" +
                USER_NAME + " text," +
                PREVIOUS_QUERY_ID + " text," +
                "PRIMARY KEY (" + USER_NAME + "));"
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(String userName) {
        String select = SELECT_USER_BASE + userName + "' ;";
        ResultSet resultSet = cassandraTemplate.getSession().execute(select);
        User user = null;
        Row row = resultSet.one();
        if (row != null) {
            user = new User();
            user.setUserName(userName);
            user.setPreviousQueryId(row.getString(PREVIOUS_QUERY_ID));
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<User> getAllUsers() {
        ResultSet resultSet = cassandraTemplate.getSession().execute(SELECT_ALL_USERS);
        Collection<User> users = new ArrayList<>();
        for (Row row : resultSet) {
            User user = new User();
            user.setUserName(row.getString(USER_NAME));
            user.setPreviousQueryId(row.getString(PREVIOUS_QUERY_ID));
            users.add(user);
        }
        return users;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addUser(User user) {
        Insert insert = QueryBuilder.insertInto(USER_TABLE_NAME);
        insert.value(USER_NAME, user.getUserName());
        insert.value(PREVIOUS_QUERY_ID, user.getPreviousQueryId());
        cassandraTemplate.getSession().execute(insert);
    }

    public void setCassandraTemplate(CassandraTemplate cassandraTemplate) {
        this.cassandraTemplate = cassandraTemplate;
    }
}
