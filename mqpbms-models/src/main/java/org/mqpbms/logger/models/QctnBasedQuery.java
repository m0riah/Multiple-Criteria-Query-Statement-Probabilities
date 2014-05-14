/*
 * Multiple-Criteria Transaction Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mtpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.models;

import java.util.Set;

/**
 * A query processed by Query Command and Table Names (QCTN) query statement pre-processing algorithm.
 * @author sky
 * @version 12/30/13
 */
public class QctnBasedQuery {

    /**
     * Query ID.
     */
    private String id;

    /**
     * Query command.
     */
    private String crud;

    /**
     * Table names in the FROM clause.
     */
    private Set<String> tableNames;

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

    public String getCrud() {
        return crud;
    }

    public void setCrud(String crud) {
        this.crud = crud;
    }

    public Set<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(Set<String> tableNames) {
        this.tableNames = tableNames;
    }

    public String getOriginalStatement() {
        return originalStatement;
    }

    public void setOriginalStatement(String originalStatement) {
        this.originalStatement = originalStatement;
    }

    @Override
    public String toString() {
        return "QctnBasedQuery{" +
                "id='" + id + '\'' +
                ", crud='" + crud + '\'' +
                ", tableNames=" + tableNames +
                ", originalStatement='" + originalStatement + '\'' +
                '}';
    }
}

