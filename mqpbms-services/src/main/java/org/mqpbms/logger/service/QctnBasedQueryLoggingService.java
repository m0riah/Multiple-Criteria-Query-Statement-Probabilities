/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.service;

import org.mqpbms.preprocessor.models.ExtendedQctnBasedQuery;

import java.util.Collection;

/**
 * Query Command and Table Names (QCTN) based transaction logging service.
 *
 * @author sky
 * @version 2/12/14
 */
public interface QctnBasedQueryLoggingService {

    /**
     * Log a collection of the {@link org.mqpbms.preprocessor.models.ExtendedQctnBasedQuery} objects with
     * Query Transition and Query Probabilities into the Probability Based Query Log table
     * {@link org.mqpbms.logger.models.PbqLog}.
     *
     * @param extendedQctnBasedQueries a collection of
     *                                      {@link org.mqpbms.preprocessor.models.ExtendedQctnBasedQuery}.
     */
    void logQctnBasedQueries(Collection<ExtendedQctnBasedQuery> extendedQctnBasedQueries);
}
