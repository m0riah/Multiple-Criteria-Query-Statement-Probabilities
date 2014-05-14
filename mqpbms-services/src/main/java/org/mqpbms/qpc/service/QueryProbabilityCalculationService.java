/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.qpc.service;

/**
 * Query Probability Calculation Service.
 *
 * @author sky
 * @version 2/17/14
 */
public interface QueryProbabilityCalculationService {

    /**
     * The probability to follow a predefined work sequence (Got idea from Google Page Rank paper).
     * <p>
     * This value is a variable, can be changed in each case.
     * </p>
     */
    static final double VALUE_P = 0.95;

    /**
     * Decimal place for final presentation.
     * If DECIMAL_PLACE == 10, then 0.1
     * If DECIMAL_PLACE == 100, then 0.01 and so on.
     */
    static final double DECIMAL_PLACE = 10000.0;

    /**
     * Decimal place for calculation steps.
     * If DECIMAL_PLACE == 10, then 0.1
     * If DECIMAL_PLACE == 100, then 0.01 and so on.
     */
    static final double MIDDLE_STEP_DECIMAL_PLACE = 1000000.0;

    /**
     * Update the Query Probability tables for all the user.
     *
     * @return
     */
    boolean updateQueryProbabilitiesForAllUser();

}
