/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.repositories;

import org.mqpbms.logger.models.QctnBasedQuery;

import java.util.Collection;

/**
 * Query Command and Table Names (QCTN) based preprocessed query table data access object.
 * <p>
 * Only one Operation Related Column Names based preprocessed query table is supposed to be created in a Logger node.
 * </p>
 *
 * @author sky
 * @version 2/8/14
 */
public interface QctnBasedQueryDao {

    /**
     * Check whether the query ID is in the QCTN based query table.
     *
     * @param queryId a QCTN based query ID.
     * @return return true if it contain.
     */
    boolean contain(String queryId);

    /**
     * Get a {@link org.mqpbms.logger.models.QctnBasedQuery} object from
     * the QCTN based query table with the query ID.
     *
     * @param queryId a QCTN based query ID.
     * @return a {@link org.mqpbms.logger.models.QctnBasedQuery} object matched to the query QUERY_ID.
     */
    QctnBasedQuery getQueryById(String queryId);

    /**
     * Get all {@link org.mqpbms.logger.models.QctnBasedQuery} in the QCTN based query
     * table.
     *
     * @return List of {@link org.mqpbms.logger.models.QctnBasedQuery} objects.
     */
    Collection<QctnBasedQuery> getAllQueries();

    /**
     * Add a {@link org.mqpbms.logger.models.QctnBasedQuery} to the QCTN based query table.
     *
     * @param qctnBasedQuery a {@link org.mqpbms.logger.models.QctnBasedQuery} object.
     */
    void addQuery(QctnBasedQuery qctnBasedQuery);
}
