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
import org.mqpbms.logger.models.QctnBasedQuery;
import org.mqpbms.logger.repositories.QctnBasedQueryDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Signature based Transaction table data access object.
 *
 * @author sky
 * @version 2/8/14
 */
@Repository("QctnBasedQueryDaoImpl")
public class QctnBasedQueryDaoImpl implements QctnBasedQueryDao, InitializingBean {

    /**
     * Table name.
     */
    private static final String QCTN_BASED_QUERY_TABLE_NAME = "qctn_queries";

    /**
     * Column name for a query ID.
     */
    private static final String ID = "id";

    /**
     * Column name for a query command.
     */
    private static final String COMMAND = "command";

    /**
     * Column name for table names.
     */
    private static final String TABLE_NAMES = "t_names";

    /**
     * Column name for the original query statement.
     */
    private static final String ORIGINAL_QUERY_STATEMENT = "original_stat";

    /**
     * Predefined Select all transactions query.
     */
    private static String SELECT_ALL_QUERIES;

    /**
     * Predefined Select a transaction by ID query.
     */
    private static String SELECT_A_QUERY_BASE;

    static {
        SELECT_ALL_QUERIES = "SELECT *" +
                " FROM " + QCTN_BASED_QUERY_TABLE_NAME + ";";
        SELECT_A_QUERY_BASE = "SELECT *" +
                " FROM " + QCTN_BASED_QUERY_TABLE_NAME +
                " WHERE " + ID + " = '";
    }

    /**
     * Cassandra template to access the Cassandra DB.
     */
    @Autowired
    @Qualifier("cassandraTemplate")
    private CassandraTemplate cassandraTemplate;

    /**
     * Check whether the table exists, if not, create a new table.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // "IF NOT EXISTS" keyword is used for safety.
        cassandraTemplate.getSession().execute("CREATE TABLE IF NOT EXISTS " + QCTN_BASED_QUERY_TABLE_NAME + " (" +
                ID + " text, " +
                COMMAND + " text, " +
                TABLE_NAMES + " set<text>, " +
                ORIGINAL_QUERY_STATEMENT + " text, " +
                "PRIMARY KEY (" + ID + "));");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contain(String queryId) {
        String select = SELECT_A_QUERY_BASE + queryId + "' ;";
        ResultSet resultSet = cassandraTemplate.getSession().execute(select);
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
    public QctnBasedQuery getQueryById(String queryId) {
        String select = SELECT_A_QUERY_BASE + queryId + "' ;";
        ResultSet resultSet = cassandraTemplate.getSession().execute(select);
        Row row = resultSet.one();
        QctnBasedQuery qctnBasedQuery = null;
        if (row != null) {
            qctnBasedQuery = new QctnBasedQuery();
            qctnBasedQuery.setId(row.getString(ID));
            qctnBasedQuery.setCrud(row.getString(COMMAND));
            qctnBasedQuery.setTableNames(row.getSet(TABLE_NAMES, String.class));
            qctnBasedQuery.setOriginalStatement(row.getString(ORIGINAL_QUERY_STATEMENT));
        }
        return qctnBasedQuery;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<QctnBasedQuery> getAllQueries() {
        ResultSet results = cassandraTemplate.getSession().execute(SELECT_ALL_QUERIES);
        Collection<QctnBasedQuery> qctnBasedQueries = new ArrayList<>();
        for (Row row : results) {
            QctnBasedQuery qctnBasedQuery = new QctnBasedQuery();
            qctnBasedQuery.setId(row.getString(ID));
            qctnBasedQuery.setCrud(row.getString(COMMAND));
            qctnBasedQuery.setTableNames(row.getSet(TABLE_NAMES, String.class));
            qctnBasedQuery.setOriginalStatement(row.getString(ORIGINAL_QUERY_STATEMENT));
            qctnBasedQueries.add(qctnBasedQuery);
        }
        return qctnBasedQueries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addQuery(QctnBasedQuery qctnBasedQuery) {
        Insert insert = QueryBuilder.insertInto(QCTN_BASED_QUERY_TABLE_NAME);
        insert.value(ID, qctnBasedQuery.getId());
        insert.value(COMMAND, qctnBasedQuery.getCrud());
        insert.value(TABLE_NAMES, qctnBasedQuery.getTableNames());
        insert.value(ORIGINAL_QUERY_STATEMENT, qctnBasedQuery.getOriginalStatement());
        cassandraTemplate.getSession().execute(insert);
    }

    public void setCassandraTemplate(CassandraTemplate cassandraTemplate) {
        this.cassandraTemplate = cassandraTemplate;
    }
}
