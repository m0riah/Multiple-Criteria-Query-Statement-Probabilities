/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.preprocessor.service.impl;

import org.mqpbms.preprocessor.service.StatementProbe;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is to define the filtering query statement syntax.
 *
 * @author sky
 * @version 2/7/14
 */
public abstract class AbstractSqlStatementProbe implements StatementProbe {

    /**
     * Set of filtering-out sql command syntax.
     * The sql statements having the sql commands added in the FILTERING_SQL_QUERY_STATEMENT_SYNTAX set
     * will not be processed.
     */
    protected static final Set<String> FILTERING_SQL_QUERY_STATEMENT_SYNTAX = new HashSet<>();

    static {
        FILTERING_SQL_QUERY_STATEMENT_SYNTAX.add("SET sql_mode");
        FILTERING_SQL_QUERY_STATEMENT_SYNTAX.add("SET collation_connection");
    }
}
