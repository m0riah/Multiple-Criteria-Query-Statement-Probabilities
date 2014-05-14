/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.common.models;

/**
 * Query Statement Pre-process Algorithm enum class
 *
 * @author sky
 * @version 2/18/14
 */
public enum PreprocessAlgorithm {
    /**
     * Query Command and Table Names (QCTN) based query statement pre-processing algorithm.
     */
    QCTN,
    /**
     * Operation Related Column Name (ORCN) based query statement pre-processing algorithm.
     */
    ORCN
}
