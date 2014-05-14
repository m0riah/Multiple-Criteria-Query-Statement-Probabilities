/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.preprocessor.service;

import org.mqpbms.logsender.models.MySqlGeneralLog;

import java.util.Collection;

/**
 * Preprocess query statement with multiple preprocessing algorithms and send the result to the logging server.
 *
 * @author sky
 * @version 2/3/14
 */
public interface PreprocessorService {

    /**
     * Process a collection of MySqlGeneralLogs having the query statement before sending it to Logging server.
     * Then send a collection of the pre-processed queries to the logging server.
     *
     * @param mySqlGeneralLogs a collection of general logs from MySQL DB.
     */
    void processAndSend(Collection<MySqlGeneralLog> mySqlGeneralLogs);

}
