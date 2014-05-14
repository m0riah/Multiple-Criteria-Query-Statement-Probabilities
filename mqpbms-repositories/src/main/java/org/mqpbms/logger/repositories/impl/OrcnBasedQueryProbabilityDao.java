/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.repositories.impl;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import org.mqpbms.connection.CassandraTemplate;
import org.mqpbms.logger.repositories.QueryProbabilityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.Map;

/**
 * Operation Related Column Names (ORCN) based Query Probability Table data access object.
 *
 * @author sky
 * @version 2/14/14
 */
@Repository("OrcnBasedQueryProbabilityDao")
public class OrcnBasedQueryProbabilityDao implements QueryProbabilityDao {

    /**
     * Table base name.
     */
    private static final String TABLE_BASE_NAME = "_orcn_qp";

    /**
     * A column name for a query ID.
     */
    static final String ID = "id";

    /**
     * A column name for the query probability.
     */
    static final String PROBABILITY = "prob";

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
                ID + " text," +
                PROBABILITY + " double," +
                "PRIMARY KEY (" + ID + "));"
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getQueryProbability(String tableName, String queryId) {
        double result = 0.0;
        ResultSet resultSet = cassandraTemplate.getSession().execute("SELECT " + PROBABILITY +
                " FROM " + tableName +
                " WHERE " + ID + " = '" + queryId + "';");
        Row row = resultSet.one();
        if (row != null) {
            result = row.getDouble(PROBABILITY);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAllQueryProbabilities(String tableName, Map<String, Double> queryProbabilities) {
        // first, remove all data from the Query Probability table.
        cassandraTemplate.getSession().execute("TRUNCATE " + tableName);
        PreparedStatement ps = cassandraTemplate.getSession().prepare("INSERT INTO " + tableName +
                " (" + ID + ", " + PROBABILITY + ") VALUES (?, ?);");
        Iterator<String> iterator = queryProbabilities.keySet().iterator();
        while (iterator.hasNext()) {
            String patternId = iterator.next();
            double probability = queryProbabilities.get(patternId);
            cassandraTemplate.getSession().execute(ps.bind(patternId, probability));
        }
    }

    public void setCassandraTemplate(CassandraTemplate cassandraTemplate) {
        this.cassandraTemplate = cassandraTemplate;
    }

}
