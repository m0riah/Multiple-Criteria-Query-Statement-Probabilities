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
import org.mqpbms.logger.models.OrcnBasedQuery;
import org.mqpbms.logger.repositories.OrcnBasedQueryDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Operation Related Column Names based Transaction table data access object.
 *
 * @author sky
 * @version 2/8/14
 */
@Repository("OrcnBasedQueryDaoImpl")
public class OrcnBasedQueryDaoImpl implements OrcnBasedQueryDao, InitializingBean {

    /**
     * The table base name.
     */
    private static final String ORCN_BASED_QUERY_TABLE_NAME = "orcn_queries";

    /**
     * Column name for a query ID.
     */
    private static final String ID = "id";

    /**
     * Column name for column names.
     */
    private static final String COLUMN_NAMES = "c_names";

    /**
     * Column name for an original query statement.
     */
    private static final String ORIGINAL_QUERY_STATEMENT = "original_stat";

    /**
     * A predefined select all transactions query.
     */
    private static String SELECT_ALL_QUERIES;

    /**
     * A predefined select a transaction by ID.
     */
    private static String SELECT_A_QUERY_BASE;

    static {
        SELECT_ALL_QUERIES = "SELECT *" +
                " FROM " + ORCN_BASED_QUERY_TABLE_NAME + ";";
        SELECT_A_QUERY_BASE = "SELECT *" +
                " FROM " + ORCN_BASED_QUERY_TABLE_NAME +
                " WHERE " + ID + " = '";
    }

    /**
     * Cassandra template to access the Cassandra DB.
     */
    @Autowired
    @Qualifier("cassandraTemplate")
    private CassandraTemplate cassandraTemplate;

    /**
     * Check whether the table exists, if not create a new table.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // "IF NOT EXISTS" keyword is used for safety.
        cassandraTemplate.getSession().execute("CREATE TABLE IF NOT EXISTS " + ORCN_BASED_QUERY_TABLE_NAME + " (" +
                ID + " text, " +
                COLUMN_NAMES + " set<text>, " +
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
    public OrcnBasedQuery getQueryById(String queryId) {
        String select = SELECT_A_QUERY_BASE + queryId + "' ;";
        ResultSet resultSet = cassandraTemplate.getSession().execute(select);
        Row row = resultSet.one();
        OrcnBasedQuery orcnBasedQuery = null;
        if (row != null) {
            orcnBasedQuery = new OrcnBasedQuery();
            orcnBasedQuery.setId(row.getString(ID));
            orcnBasedQuery.setColumnNames(row.getSet(COLUMN_NAMES, String.class));
            orcnBasedQuery.setOriginalStatement(row.getString(ORIGINAL_QUERY_STATEMENT));
        }
        return orcnBasedQuery;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<OrcnBasedQuery> getAllQueries() {
        ResultSet results = cassandraTemplate.getSession().execute(SELECT_ALL_QUERIES);
        Collection<OrcnBasedQuery> orcnBasedQueries = new ArrayList<>();
        for (Row row : results) {
            OrcnBasedQuery orcnBasedQuery = new OrcnBasedQuery();
            orcnBasedQuery.setId(row.getString(ID));
            orcnBasedQuery.setColumnNames(row.getSet(COLUMN_NAMES, String.class));
            orcnBasedQuery.setOriginalStatement(row.getString(ORIGINAL_QUERY_STATEMENT));
            orcnBasedQueries.add(orcnBasedQuery);
        }
        return orcnBasedQueries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addQuery(OrcnBasedQuery orcnBasedQuery) {
        Insert insert = QueryBuilder.insertInto(ORCN_BASED_QUERY_TABLE_NAME);
        insert.value(ID, orcnBasedQuery.getId());
        insert.value(COLUMN_NAMES, orcnBasedQuery.getColumnNames());
        insert.value(ORIGINAL_QUERY_STATEMENT, orcnBasedQuery.getOriginalStatement());
        cassandraTemplate.getSession().execute(insert);
    }

    public void setCassandraTemplate(CassandraTemplate cassandraTemplate) {
        this.cassandraTemplate = cassandraTemplate;
    }

}
