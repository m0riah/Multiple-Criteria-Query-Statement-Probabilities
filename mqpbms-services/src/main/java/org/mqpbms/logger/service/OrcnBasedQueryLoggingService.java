/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.service;

import org.mqpbms.preprocessor.models.ExtendedOrcnBasedQuery;

import java.util.Collection;

/**
 * Operation Related Column Names (ORCN) based query logging service.
 *
 * @author sky
 * @version 2/12/14
 */
public interface OrcnBasedQueryLoggingService {

    /**
     * Log a collection of the {@link org.mqpbms.preprocessor.models.ExtendedOrcnBasedQuery} objects with
     * Query Transition and Query Probabilities into the Probability Based Query Log table
     * {@link org.mqpbms.logger.models.PbqLog}.
     *
     * @param extendedORCNBasedQueries a collection of
     *                                      {@link org.mqpbms.preprocessor.models.ExtendedOrcnBasedQuery}.
     */
    void logOrcnBasedQueries(Collection<ExtendedOrcnBasedQuery> extendedORCNBasedQueries);
}
