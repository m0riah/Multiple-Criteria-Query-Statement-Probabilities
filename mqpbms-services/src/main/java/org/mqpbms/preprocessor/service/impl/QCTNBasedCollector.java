/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.preprocessor.service.impl;

import com.akiban.sql.parser.*;
import org.mqpbms.common.models.PreprocessAlgorithmNameSpace;
import org.mqpbms.preprocessor.models.ExtendedQctnBasedQuery;
import org.mqpbms.preprocessor.service.Collector;

/**
 * Collect necessary information from a parsed query statement nodes {@link com.akiban.sql.parser.SQLParser} to
 * Query Command and Table Names (QCTN) based preprocessed query.
 *
 * @author sky
 * @version 2/3/14
 */
public class QCTNBasedCollector implements Collector {

    /**
     * Stop collect flag.
     */
    private boolean stopCollect = false;

    /**
     * Query Command and Table Names (QCTN) based Preprocessed query domain.
     */
    private ExtendedQctnBasedQuery extendedQctnBasedQuery;

    public QCTNBasedCollector() {
        this.extendedQctnBasedQuery = new ExtendedQctnBasedQuery();
        extendedQctnBasedQuery.setAlgorithm(PreprocessAlgorithmNameSpace.QCTN_BASED);
        // TreeSet makes sorted set.
    }

    @Override
    public void reset(String userName, String hostName, long timeStamp, String originalStatement) {
        this.extendedQctnBasedQuery = new ExtendedQctnBasedQuery();
        extendedQctnBasedQuery.setUserName(userName);
        extendedQctnBasedQuery.setHostName(hostName);
        extendedQctnBasedQuery.setTimeStamp(timeStamp);
        extendedQctnBasedQuery.setAlgorithm(PreprocessAlgorithmNameSpace.QCTN_BASED);
        extendedQctnBasedQuery.setOriginalStatement(originalStatement);
        stopCollect = false;
    }

    /**
     * Collect the column names (table name concatenated) related to operations.
     *
     * @param visitable parsed original query statement node.
     */
    @Override
    public void collect(Visitable visitable) {
        QueryTreeNode node = (QueryTreeNode) visitable;
        if (node instanceof StatementNode) {
            String crud = ((StatementNode) node).statementToString();
            extendedQctnBasedQuery.setCrud(crud);
            // If the crud command part is for a table modification: to wit, INSERT, UPDATE or DELETE.
            // add the target table and stop collecting.
            if (node instanceof DMLModStatementNode) {
                extendedQctnBasedQuery.getTableNames().add(((DMLModStatementNode) node).
                        getTargetTableName().getTableName());
                stopCollect = true;
            }
        }
        if (node instanceof FromBaseTable) {
            String tableName = ((FromBaseTable) node).getOrigTableName().toString();
            extendedQctnBasedQuery.getTableNames().add(tableName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopCollect() {
        return stopCollect;
    }

    public ExtendedQctnBasedQuery getExtendedQctnBasedQuery() {
        extendedQctnBasedQuery.setId(ExtendedQctnBasedQuery.generateId(extendedQctnBasedQuery));
        return extendedQctnBasedQuery;
    }
}
