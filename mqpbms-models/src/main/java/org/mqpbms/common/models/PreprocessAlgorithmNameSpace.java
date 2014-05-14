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
 * A class having constant fields of predefined query statement pre-processing algorithm names.
 *
 * @author sky
 * @version 2/3/14
 */
public class PreprocessAlgorithmNameSpace {

    /**
     * Query Command and Table Names (QCTN) based query statement pre-processing algorithm.
     */
    public static final String QCTN_BASED = "qctn";

    /**
     * Operation Related Column Name (ORCN) based query statement pre-processing algorithm.
     */
    public static final String ORCN_BASED = "orcn";
}
