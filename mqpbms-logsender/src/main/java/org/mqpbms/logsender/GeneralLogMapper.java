/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logsender;

import org.mqpbms.logsender.models.MySqlGeneralLog;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper to make a mapping between the gneral_log table in the mysql system db and
 * MySqlGeneralLog class.
 *
 * @author sky
 * @version 10/18/13
 * @since 10/18/13
 */
public class GeneralLogMapper implements RowMapper<MySqlGeneralLog> {

    /**
     * {@inheritDoc}
     */
    @Override
    public MySqlGeneralLog mapRow(ResultSet rs, int rowNum) throws SQLException {
        MySqlGeneralLog log = new MySqlGeneralLog();
        log.setEventTime(rs.getTimestamp("event_time"));
        log.setUserHost(rs.getString("user_host"));
        log.setCommand(rs.getString("command_type"));
        log.setArgument(rs.getString("argument"));
        return log;
    }
}
