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
import org.mqpbms.logger.models.TransitionTableNameIndex;
import org.mqpbms.logger.repositories.TransitionTableNameIndexDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Operation Related Column Names based Query Transition Table Name Index table data access object.
 *
 * @author sky
 * @version 2/13/14
 */
@Repository("OrcnBasedTransitionTableNameIndexDao")
public class OrcnBasedTransitionTableNameIndexDao implements TransitionTableNameIndexDao {

    /**
     * Table base name.
     */
    private static final String TABLE_BASE_NAME = "_orcn_transition_index";

    /**
     * Column name for a query ID.
     */
    static final String QUERY_ID = "q_id";

    /**
     * Column name for a Query Transition Table name.
     */
    static final String QUERY_TRANSITION_TABLE_NAME = "table_name";

    /**
     * A predefined select all query.
     */
    private static String SELECT_ALL_BASE;

    static {
        SELECT_ALL_BASE = "SELECT * FROM ";
    }

    /**
     * Cassandra template to access the Cassandra DB.
     */
    @Autowired
    @Qualifier("cassandraTemplate")
    private CassandraTemplate cassandraTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateTableName(String userName) {
        return userName + TABLE_BASE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createTable(String tableName) {
        // "IF NOT EXISTS" keyword is used for safety.
        cassandraTemplate.getSession().execute("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                QUERY_ID + " text," +
                QUERY_TRANSITION_TABLE_NAME + " text," +
                "PRIMARY KEY (" + QUERY_ID + "));"
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contain(String tableName, String queryId) {
        ResultSet resultSet = cassandraTemplate.getSession().execute("SELECT *" +
                " FROM " + tableName +
                " WHERE " + QUERY_ID + " = '" + queryId + "';");
        Row row = resultSet.one();
        if (row == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransitionTableNameIndex getTransitionTableIndexById(String tableName, String queryId) {
        ResultSet resultSet = cassandraTemplate.getSession().execute("SELECT *" +
                " FROM " + tableName +
                " WHERE " + QUERY_ID + " = '" + queryId + "';");
        Row row = resultSet.one();
        TransitionTableNameIndex transitionTableNameIndex = null;
        if (row != null) {
            transitionTableNameIndex = new TransitionTableNameIndex();
            transitionTableNameIndex.setQueryId(row.getString(QUERY_ID));
            transitionTableNameIndex.setTransitionTableName(row.getString(QUERY_TRANSITION_TABLE_NAME));
        }
        return transitionTableNameIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<TransitionTableNameIndex> getAllTransitionTableIndexes(String tableName) {
        ResultSet results = cassandraTemplate.getSession().execute(SELECT_ALL_BASE + tableName + ";");
        Collection<TransitionTableNameIndex> transitionTableNameIndexes = new ArrayList<>();
        for (Row row : results) {
            TransitionTableNameIndex transitionTableNameIndex = new TransitionTableNameIndex();
            transitionTableNameIndex.setQueryId(row.getString(QUERY_ID));
            transitionTableNameIndex.setTransitionTableName(row.getString(QUERY_TRANSITION_TABLE_NAME));
            transitionTableNameIndexes.add(transitionTableNameIndex);
        }
        return transitionTableNameIndexes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTransitionTableIndex(String tableName,
                                        TransitionTableNameIndex transitionTableNameIndex) {
        Insert insert = QueryBuilder.insertInto(tableName);
        insert.value(QUERY_ID, transitionTableNameIndex.getQueryId());
        insert.value(QUERY_TRANSITION_TABLE_NAME, transitionTableNameIndex.getTransitionTableName());
        cassandraTemplate.getSession().execute(insert);
    }

    public void setCassandraTemplate(CassandraTemplate cassandraTemplate) {
        this.cassandraTemplate = cassandraTemplate;
    }
}
