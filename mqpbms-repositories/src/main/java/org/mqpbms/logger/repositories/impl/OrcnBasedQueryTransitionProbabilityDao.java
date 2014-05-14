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
import org.mqpbms.connection.CassandraTemplate;
import org.mqpbms.logger.models.QueryTransitionProbability;
import org.mqpbms.logger.repositories.QueryTransitionProbabilityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Operation Related Column Names (ORCN) based Query Transition Table data access object.
 *
 * @author sky
 * @version 2/14/14
 */
@Repository("OrcnBasedQueryTransitionProbabilityDao")
public class OrcnBasedQueryTransitionProbabilityDao implements QueryTransitionProbabilityDao {

    /**
     * Decimal place for final presentation.
     * If DECIMAL_PLACE == 10, then 0.1
     * If DECIMAL_PLACE == 100, then 0.01 and so on.
     */
    static final double DECIMAL_PLACE = 10000.0;

    /**
     * A column name for query ID.
     */
    static final String ID = "id";

    /**
     * A column name for count value.
     */
    static final String COUNTER_VALUE = "count";

    /**
     * This is the postfix added to the name of generated Query Transition Table to create
     * a Query Transition Total Hit Table for each query Transition table to speed up calculation of the
     * Query Transition Probability.
     * <p>
     * Example, if the Query Transition Table name is
     * "5badb7a07b4811e3a20b4fbf0ac09a41" and the postfix is "_t", the Query Transition Total Hit Table name is
     * "5badb7a07b4811e3a20b4fbf0ac09a41_t".
     * </p>
     * <p>
     * The limitation of Cassandra : max table name cannot be more than 48 character.
     * </p>
     */
    static final String TOTAL_TABLE_POSTFIX = "_t";

    /**
     * A column name for the total counter number for the generated Query Transition Table.
     */
    static final String TOTAL = "t_count";

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
    public void createTable(String tableName) {
        // "IF NOT EXISTS" keyword is used for safety.
        cassandraTemplate.getSession().execute("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                ID + " text," +
                COUNTER_VALUE + " counter," +
                "PRIMARY KEY (" + ID + "));");
        cassandraTemplate.getSession().execute("CREATE TABLE IF NOT EXISTS " + tableName + TOTAL_TABLE_POSTFIX + " (" +
                TOTAL + " text," +
                COUNTER_VALUE + " counter," +
                "PRIMARY KEY (" + TOTAL + "));");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateQueryTransitionCounts(String previousTransitionTableName, String currentQueryID) {
        cassandraTemplate.getSession().execute("UPDATE " + previousTransitionTableName +
                " SET " + COUNTER_VALUE + " = " + COUNTER_VALUE + " + 1" +
                " WHERE " + ID + " = '" + currentQueryID + "';");
        cassandraTemplate.getSession().execute("UPDATE " + previousTransitionTableName + TOTAL_TABLE_POSTFIX +
                " SET " + COUNTER_VALUE + " = " + COUNTER_VALUE + " + 1" +
                " WHERE " + TOTAL + " = '" + TOTAL + "';");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getQueryTransitionProbability(String previousTransitionTableName, String currentQueryID) {
        ResultSet resultSet = cassandraTemplate.getSession().execute("SELECT " + COUNTER_VALUE +
                " FROM " + previousTransitionTableName +
                " WHERE " + ID + " = '" + currentQueryID + "';");

        double count = 0.0;
        Row row = resultSet.one();
        if (row != null) count = row.getLong(COUNTER_VALUE);

        ResultSet resultSetForTotal = cassandraTemplate.getSession().execute("SELECT " + COUNTER_VALUE +
                " FROM " + previousTransitionTableName + TOTAL_TABLE_POSTFIX + ";");
        Row rowForTotal = resultSetForTotal.one();

        double totalCount = 0.0;
        if (rowForTotal != null) totalCount = rowForTotal.getLong(COUNTER_VALUE);

        // Expecting return value x is always 0 < x <=  1.
        return roundUpForFinalPresentation(count / totalCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<QueryTransitionProbability> getAllQueryTransitionProbabilities(String transitionTableName) {
        Collection<QueryTransitionProbability> queryTransitionProbabilities = new ArrayList<>();

        ResultSet resultSet = cassandraTemplate.getSession().execute("SELECT * " +
                " FROM " + transitionTableName + ";");
        ResultSet resultSetForTotal = cassandraTemplate.getSession().execute("SELECT " + COUNTER_VALUE +
                " FROM " + transitionTableName + TOTAL_TABLE_POSTFIX + ";");

        Row rowForTotal = resultSetForTotal.one();
        if (rowForTotal != null) {
            double totalCount = rowForTotal.getLong(COUNTER_VALUE);

            for (Row row : resultSet) {
                QueryTransitionProbability ttp = new QueryTransitionProbability();
                ttp.setNextQueryId(row.getString(ID));
                ttp.setProbability(roundUpForFinalPresentation((double) row.getLong(COUNTER_VALUE) / totalCount));
                queryTransitionProbabilities.add(ttp);
            }
        }
        return queryTransitionProbabilities;
    }

    /**
     * Round up the double value to the dedicated decimal place to presentation.
     * DECIMAL_PLACE is the place it needs to round up.
     *
     * @param value the value to calculate.
     * @return the rounded up double value.
     */
    private double roundUpForFinalPresentation(double value) {
        return Math.round(value * DECIMAL_PLACE) / DECIMAL_PLACE;
    }

    public void setCassandraTemplate(CassandraTemplate cassandraTemplate) {
        this.cassandraTemplate = cassandraTemplate;
    }
}
