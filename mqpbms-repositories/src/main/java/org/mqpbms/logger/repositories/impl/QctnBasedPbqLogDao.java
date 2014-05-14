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
import org.mqpbms.logger.models.PbqLog;
import org.mqpbms.logger.repositories.PbqLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Signature based Probability Based Query Log data access object.
 *
 * @author sky
 * @version 2/10/14
 */
@Repository("QctnBasedPbqLogDao")
public class QctnBasedPbqLogDao implements PbqLogDao {

    /**
     * Table base name.
     */
    private static final String TABLE_BASE_NAME = "_qctn_pbq";

    /**
     * A column name for the date of logging for the query happened.
     * <p>
     * This column will be the partition key in Cassandra DB.
     * </p>
     */
    private static final String DATE = "date";

    /**
     * A column name for the logging time.
     */
    private static final String LOGGING_TIME = "l_time";

    /**
     * A column name for the query execution time.
     */
    private static final String EXECUTION_TIME = "e_time";

    /**
     * A column name for the user name who executed the query.
     */
    private static final String USER_NAME = "u_name";

    /**
     * A column name for the host name where the query executed.
     */
    private static final String HOST_NAME = "h_name";

    /**
     * A column name for the query ID.
     */
    private static final String QUERY_ID = "q_id";

    /**
     * A column name for the Query Transition Probability.
     */
    private static final String QUERY_TRANSITION_PROB = "qt_prob";

    /**
     * A column name for the Query Probability.
     */
    private static final String QUERY_PROB = "q_prob";

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
                DATE + " text, " +
                LOGGING_TIME + " timestamp, " +
                EXECUTION_TIME + " timestamp, " +
                USER_NAME + " text, " +
                HOST_NAME + " text, " +
                QUERY_ID + " text, " +
                QUERY_TRANSITION_PROB + " double, " +
                QUERY_PROB + " double, " +
                "PRIMARY KEY (" + DATE + ", " + LOGGING_TIME + ")" +
                ");"
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<PbqLog> getLogsBetweenTimestamps(String tableName, long startTimestamp, long endTimestamp) {
        List<PbqLog> PbqLogs = new ArrayList<>();
        if (startTimestamp < endTimestamp) {
            Date startDate = new Date(startTimestamp);
            Date endDate = new Date(endTimestamp);
            String startDateString = new SimpleDateFormat("yyyyMMdd").format(startDate);
            String endDateString = new SimpleDateFormat("yyyyMMdd").format(endDate);
            ResultSet resultSet;
            if (startDateString.equals(endDateString)) {
                resultSet = cassandraTemplate.getSession().execute("SELECT * " +
                        "FROM " + tableName + " WHERE " + DATE + "='" + startDateString + "' AND " +
                        LOGGING_TIME + " > " + startTimestamp + " AND " +
                        LOGGING_TIME + " <= " + endTimestamp + ";");
                for (Row row : resultSet) {
                    PbqLog PbqLog = new PbqLog(row.getDate(LOGGING_TIME).getTime(),
                            row.getDate(EXECUTION_TIME).getTime(),
                            row.getString(USER_NAME),
                            row.getString(HOST_NAME),
                            row.getString(QUERY_ID),
                            row.getDouble(QUERY_TRANSITION_PROB),
                            row.getDouble(QUERY_PROB));
                    PbqLogs.add(PbqLog);
                }
            } else {
                resultSet = cassandraTemplate.getSession().execute("SELECT * " +
                        "FROM " + tableName + " WHERE " + DATE + "='" + startDateString + "' AND " +
                        LOGGING_TIME + " > " + startTimestamp + ";");
                for (Row row : resultSet) {
                    PbqLog PbqLog = new PbqLog(row.getDate(LOGGING_TIME).getTime(),
                            row.getDate(EXECUTION_TIME).getTime(),
                            row.getString(USER_NAME),
                            row.getString(HOST_NAME),
                            row.getString(QUERY_ID),
                            row.getDouble(QUERY_TRANSITION_PROB),
                            row.getDouble(QUERY_PROB));
                    PbqLogs.add(PbqLog);
                }
                resultSet = cassandraTemplate.getSession().execute("SELECT * " +
                        "FROM " + tableName + " WHERE " + DATE + "='" + endDateString + "' AND " +
                        LOGGING_TIME + " <= " + endTimestamp + ";");
                for (Row row : resultSet) {
                    PbqLog PbqLog = new PbqLog(row.getDate(LOGGING_TIME).getTime(),
                            row.getDate(EXECUTION_TIME).getTime(),
                            row.getString(USER_NAME),
                            row.getString(HOST_NAME),
                            row.getString(QUERY_ID),
                            row.getDouble(QUERY_TRANSITION_PROB),
                            row.getDouble(QUERY_PROB));
                    PbqLogs.add(PbqLog);
                }
            }
        }
        return PbqLogs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<PbqLog> getAllLogs(String tableName) {
        ResultSet results = cassandraTemplate.getSession().execute("SELECT * " +
                "FROM " + tableName + ";");
        List<PbqLog> PbqLogs = new ArrayList<>();
        for (Row row : results) {
            PbqLog PbqLog = new PbqLog(row.getDate(LOGGING_TIME).getTime(),
                    row.getDate(EXECUTION_TIME).getTime(),
                    row.getString(USER_NAME),
                    row.getString(HOST_NAME),
                    row.getString(QUERY_ID),
                    row.getDouble(QUERY_TRANSITION_PROB),
                    row.getDouble(QUERY_PROB));
            PbqLogs.add(PbqLog);
        }
        return PbqLogs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLog(String tableName, PbqLog PbqLog) {
        Insert insert = QueryBuilder.insertInto(tableName);
        Date date = new Date(PbqLog.getLoggingTime());
        String dateString = new SimpleDateFormat("yyyyMMdd").format(date);
        insert.value(DATE, dateString);
        insert.value(LOGGING_TIME, PbqLog.getLoggingTime());
        insert.value(EXECUTION_TIME, PbqLog.getExecutionTime());
        insert.value(USER_NAME, PbqLog.getUserName());
        insert.value(HOST_NAME, PbqLog.getHostName());
        insert.value(QUERY_ID, PbqLog.getQueryId());
        insert.value(QUERY_TRANSITION_PROB, PbqLog.getQueryTransitionProbability());
        insert.value(QUERY_PROB, PbqLog.getQueryProbability());
        cassandraTemplate.getSession().execute(insert);
    }

    public void setCassandraTemplate(CassandraTemplate cassandraTemplate) {
        this.cassandraTemplate = cassandraTemplate;
    }

}
