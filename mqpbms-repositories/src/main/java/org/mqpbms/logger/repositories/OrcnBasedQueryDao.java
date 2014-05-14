/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.repositories;

import org.mqpbms.logger.models.OrcnBasedQuery;

import java.util.Collection;

/**
 * Operation Related Column Names (ORCN) based preprocessed query table data access object.
 * <p>
 * Only one Operation Related Column Names based preprocessed query table is supposed to be created in a Logger node.
 * </p>
 *
 * @author sky
 * @version 2/8/14
 */
public interface OrcnBasedQueryDao {

    /**
     * Check whether query ID is in the ORCN based Query Table.
     *
     * @param queryId a query ID.
     * @return return true if it contain.
     */
    boolean contain(String queryId);

    /**
     * Get a {@link org.mqpbms.logger.models.OrcnBasedQuery} object from
     * the ORCN based query table in the DB with the query ID.
     *
     * @param queryId a query ID.
     * @return a {@link org.mqpbms.logger.models.OrcnBasedQuery} object matched to the query ID.
     */
    OrcnBasedQuery getQueryById(String queryId);

    /**
     * Get all {@link org.mqpbms.logger.models.OrcnBasedQuery} in the ORCN based query table.
     *
     * @return List of {@link org.mqpbms.logger.models.OrcnBasedQuery} objects.
     */
    Collection<OrcnBasedQuery> getAllQueries();

    /**
     * Add a {@link org.mqpbms.logger.models.OrcnBasedQuery} to the ORCN based query table.
     *
     * @param orcnBasedQuery a {@link org.mqpbms.logger.models.OrcnBasedQuery} object.
     */
    void addQuery(OrcnBasedQuery orcnBasedQuery);
}
