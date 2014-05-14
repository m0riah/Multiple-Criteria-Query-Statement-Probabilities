/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.preprocessor.service.impl;

import com.akiban.sql.parser.BinaryOperatorNode;
import com.akiban.sql.parser.QueryTreeNode;
import com.akiban.sql.parser.Visitable;
import org.mqpbms.common.models.PreprocessAlgorithmNameSpace;
import org.mqpbms.preprocessor.models.ExtendedOrcnBasedQuery;
import org.mqpbms.preprocessor.service.Collector;

/**
 * Collect necessary information from a parsed query statement nodes {@link com.akiban.sql.parser.SQLParser} to
 * produce Operation Related Column Names (ORCN) based preprocessed query.
 *
 * @author sky
 * @version 2/3/14
 */
public class ORCNBasedCollector implements Collector {

    /**
     * Stop collect flag.
     */
    private boolean stopCollect = false;

    /**
     * Operation Related Column Names based Preprocessed query domain.
     */
    private ExtendedOrcnBasedQuery extendedOrcnBasedQuery;

    @Override
    public void reset(String userName, String hostName, long timeStamp, String originalStatement) {
        extendedOrcnBasedQuery = new ExtendedOrcnBasedQuery();
        extendedOrcnBasedQuery.setUserName(userName);
        extendedOrcnBasedQuery.setHostName(hostName);
        extendedOrcnBasedQuery.setTimeStamp(timeStamp);
        extendedOrcnBasedQuery.setAlgorithm(PreprocessAlgorithmNameSpace.ORCN_BASED);
        extendedOrcnBasedQuery.setOriginalStatement(originalStatement);
        stopCollect = false;
    }

    /**
     * Collect the column names (table name concatenated) related to operations.
     *
     * <p> The table name concatenation is not perfect this code. It cannot properly work if the table name
     * is co-relational name For example,  "SELECT C.QUERY_ID, C.NAME, C.AGE, O.AMOUNT FROM CUSTOMERS AS C, ORDERS AS O
     * WHERE  C.QUERY_ID = O.CUSTOMER_ID", The concatenated column names will be c.id and o.customer_id. </p>
     *
     * @param visitable parsed original query statement node.
     */
    @Override
    public void collect(Visitable visitable) {
        QueryTreeNode node = (QueryTreeNode) visitable;
        if (node instanceof BinaryOperatorNode) {
            if (((BinaryOperatorNode) node).getLeftOperand().getColumnName() != null) {
                String leftColumnName = "";
                if (((BinaryOperatorNode) node).getLeftOperand().getTableName() != null) {
                    leftColumnName += ((BinaryOperatorNode) node).getLeftOperand().getTableName();
                    leftColumnName += ".";
                }
                leftColumnName += ((BinaryOperatorNode) node).getLeftOperand().getColumnName();
                extendedOrcnBasedQuery.getColumnNames().add(leftColumnName);
            }
            if (((BinaryOperatorNode) node).getRightOperand().getColumnName() != null) {
                String rightColumnName = "";
                if (((BinaryOperatorNode) node).getRightOperand().getTableName() != null) {
                    rightColumnName += ((BinaryOperatorNode) node).getRightOperand().getTableName();
                    rightColumnName += ".";
                }
                rightColumnName += ((BinaryOperatorNode) node).getRightOperand().getColumnName();
                extendedOrcnBasedQuery.getColumnNames().add(rightColumnName);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopCollect() {
        return stopCollect;
    }

    public ExtendedOrcnBasedQuery getExtendedOrcnBasedQuery() {
        extendedOrcnBasedQuery.setId(ExtendedOrcnBasedQuery.generateId(extendedOrcnBasedQuery));
        return extendedOrcnBasedQuery;
    }
}
