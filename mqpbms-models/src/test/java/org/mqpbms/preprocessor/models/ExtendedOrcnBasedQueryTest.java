package org.mqpbms.preprocessor.models;
/*
 * Created with IntelliJ IDEA.
 * User: sky
 * Date: 2/11/14
 * Time: 1:36 AM
 * To change this template use File | Settings | File Templates.
 */

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 *
 * @author sky
 * @version 2/11/14
 */
public class ExtendedOrcnBasedQueryTest {

    ExtendedOrcnBasedQuery extendedOrcnBasedQuery;
    Collection<ExtendedOrcnBasedQuery> extendedORCNBasedQueries;

    @Before
    public void setUp() throws Exception {
        extendedORCNBasedQueries = new ArrayList<>();
        extendedOrcnBasedQuery =
                new ExtendedOrcnBasedQuery();
        extendedOrcnBasedQuery.setId("1234");
        extendedOrcnBasedQuery.setHostName("localhost");
        extendedOrcnBasedQuery.setUserName("sky");
        extendedOrcnBasedQuery.setTimeStamp(new Date().getTime());
        extendedOrcnBasedQuery.setAlgorithm("orcn");
        TreeSet<String> columnNames = new TreeSet<>();
        columnNames.add("col3");
        columnNames.add("col2");
        extendedOrcnBasedQuery.setColumnNames(columnNames);
        extendedORCNBasedQueries.add(extendedOrcnBasedQuery);
        extendedOrcnBasedQuery.setUserName("sam");
        extendedORCNBasedQueries.add(extendedOrcnBasedQuery);

    }

    @Test
    public void testGenerateId() throws Exception {
        System.out.println("\nTest to generate ID");
        System.out.println(ExtendedOrcnBasedQuery.generateId(extendedOrcnBasedQuery));
    }

    @Test
    public void testToJson() throws Exception {
        System.out.println("\nTest to Json");
        System.out.println(extendedOrcnBasedQuery.toJson());
    }

    @Test
    public void testToJsonArray() throws Exception {
        System.out.println("\ntest to JsonArray");
        System.out.println(ExtendedOrcnBasedQuery.toJsonArray(extendedORCNBasedQueries));
    }

    @Test
    public void testToExtendedORCNBasedTransaction() throws Exception {
        System.out.println("\ntest to ExtendedOrcnBasedQuery");
        String json = extendedOrcnBasedQuery.toJson();
        System.out.println(ExtendedOrcnBasedQuery.toExtendedOrcnBasedQuery(json));
    }

    @Test
    public void testToExtendedORCNBasedTransactions() throws Exception {
        System.out.println("\ntest to ExtendedORCNBasedTransactions");
        String jsons = ExtendedOrcnBasedQuery.toJsonArray(extendedORCNBasedQueries);
        System.out.println(ExtendedOrcnBasedQuery.toExtendedOrcnBasedQueries(jsons));
    }
}
