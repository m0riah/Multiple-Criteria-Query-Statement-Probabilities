/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.preprocessor.service.impl;

import com.akiban.sql.StandardException;
import com.akiban.sql.parser.QueryTreeNode;
import com.akiban.sql.parser.StatementNode;
import com.akiban.sql.parser.Visitable;
import org.mqpbms.preprocessor.service.Collector;

import java.util.HashMap;
import java.util.Map;

/**
 * Probe query statement to let collectors collect useful information to produce the pre-processed queries.
 * <p>
 * This is not thread safe. Be careful to use it.
 * </p>
 *
 * @author sky
 * @version 2/3/14
 */
public class MySqlStatementProbe extends AbstractSqlStatementProbe {

    /**
     * If it is true, stop traversal the sql statement.
     */
    private boolean stopTraversal;

    /**
     * Map of {@link org.mqpbms.preprocessor.service.Collector}s.
     */
    private Map<String, Collector> collectors;

    /**
     * Constructor
     */
    public MySqlStatementProbe() {
        stopTraversal = false;
        collectors = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCollector(String collectorName, Collector collector) {
        collectors.put(collectorName, collector);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Collector> getCollectorMap() {
        return collectors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset(String userName, String hostName, long timeStamp, String originalStatement) {
        stopTraversal = false;
        for (String collector : collectors.keySet()) {
            collectors.get(collector).reset(userName, hostName, timeStamp, originalStatement);
        }
    }

    /**
     * Pass each {@link com.akiban.sql.parser.QueryTreeNode} to the list of {@link Collector} to collect data
     * for pre-processed query generation.
     * <p>FILTERING_SQL_QUERY_STATEMENT_SYNTAX set is used to filter unnecessary sql syntax.
     * If the {@link com.akiban.sql.parser.StatementNode} has the filtering syntax, set the stopTraversal flag to
     * true.
     *
     * @param visitable query statement tree node.
     * @return the parameter node again.
     * @throws StandardException
     */
    @Override
    public Visitable visit(Visitable visitable) throws StandardException {
        QueryTreeNode node = (QueryTreeNode) visitable;
        // If the StatementNode has a filtering string, stop traverse the statement.
        if (node instanceof StatementNode) {
            String statementString = ((StatementNode) node).statementToString();
            if (FILTERING_SQL_QUERY_STATEMENT_SYNTAX.contains(statementString)) {
                stopTraversal = true;
            }
        }
        // collect information following the logic in each Collector.
        for (String collector : collectors.keySet()) {
            if (!collectors.get(collector).stopCollect()) {
                collectors.get(collector).collect(node);
            }
        }
        return node;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean visitChildrenFirst(Visitable node) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopTraversal() {
        return stopTraversal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean skipChildren(Visitable node) throws StandardException {
        return false;
    }
}
