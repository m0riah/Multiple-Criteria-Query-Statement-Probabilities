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
import org.springframework.batch.item.ItemProcessor;

/**
 * This processor just pass the MySqlGeneralLog from the reader
 * {@link org.springframework.batch.item.database.JdbcCursorItemReader} to writer {@link JsonGeneralLogWriter}.
 *
 * No logic is inside it. Just pass the value read from the reader to writer.
 *
 * @author sky
 * @version 10/18/13
 * @since 10/18/13
 */
public class DefaultGeneralLogProcessor implements ItemProcessor<MySqlGeneralLog, MySqlGeneralLog> {

    /**
     * {@inheritDoc}
     */
    @Override
    public MySqlGeneralLog process(MySqlGeneralLog item) throws Exception {
        return item;
    }

}
