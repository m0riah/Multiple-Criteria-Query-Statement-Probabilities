/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.models;

import java.util.Set;

/**
 * A query processed by Operation Related Column Name (ORCN) query statement pre-processing algorithm.
 *
 * @author sky
 * @version 1/31/14
 */
public class OrcnBasedQuery {

    /**
     * Query ID.
     */
    private String id;

    /**
     * Column names related to operations in the query statement.
     * The column names are sorted in a set.
     */
    private Set<String> columnNames;

    /**
     * Original query statement.
     */
    private String originalStatement;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(Set<String> columnNames) {
        this.columnNames = columnNames;
    }

    public String getOriginalStatement() {
        return originalStatement;
    }

    public void setOriginalStatement(String originalStatement) {
        this.originalStatement = originalStatement;
    }

    @Override
    public String toString() {
        return "OrcnBasedQuery{" +
                "id='" + id + '\'' +
                ", columnNames=" + columnNames +
                ", originalStatement='" + originalStatement + '\'' +
                '}';
    }

}
