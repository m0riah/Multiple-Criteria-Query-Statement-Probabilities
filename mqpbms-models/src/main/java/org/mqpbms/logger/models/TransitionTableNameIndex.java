/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.models;

import org.mqpbms.common.models.PreprocessAlgorithm;
import org.mqpbms.common.models.PreprocessAlgorithmNameSpace;

import java.util.UUID;

/**
 * This class is to index the Query Transition Table names mapped to each transaction IDs the user has executed.
 *
 * @author sky
 * @version 2/13/14
 */
public class TransitionTableNameIndex {

    /**
     * Query ID.
     */
    private String queryId;

    /**
     * Query Transition Table name.
     */
    private String transitionTableName;

    /**
     * This is to set random Query Transition Table name for the given transaction ID.
     * <p>The random table name is based on the UUID string</p>
     *
     * @param transactionId a transaction ID.
     * @param preprocessAlgorithm an algorithm to preprocess the transaction.
     * @return
     */
    public static final TransitionTableNameIndex getObjectHavingRandomTransitionTableName(
            String transactionId, PreprocessAlgorithm preprocessAlgorithm) {
        TransitionTableNameIndex transitionTableNameIndex =
                new TransitionTableNameIndex();
        transitionTableNameIndex.setQueryId(transactionId);
        String uuidStringForm = UUID.randomUUID().toString().replace("-", "");
        String transitionTableName;
        if (preprocessAlgorithm.equals(PreprocessAlgorithm.ORCN)) {
            transitionTableName = PreprocessAlgorithmNameSpace.ORCN_BASED + "_" + uuidStringForm;
        } else {
            transitionTableName = PreprocessAlgorithmNameSpace.QCTN_BASED + "_" + uuidStringForm;
        }
        transitionTableNameIndex.setTransitionTableName(transitionTableName);
        return transitionTableNameIndex;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getTransitionTableName() {
        return transitionTableName;
    }

    public void setTransitionTableName(String transitionTableName) {
        this.transitionTableName = transitionTableName;
    }

    @Override
    public String toString() {
        return "TransitionTableNameIndex{" +
                "queryId='" + queryId + '\'' +
                ", transitionTableName='" + transitionTableName + '\'' +
                '}';
    }
}
