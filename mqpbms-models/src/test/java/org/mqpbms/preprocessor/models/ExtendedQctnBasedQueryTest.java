package org.mqpbms.preprocessor.models;
/*
 * Created with IntelliJ IDEA.
 * User: sky
 * Date: 2/11/14
 * Time: 1:51 AM
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
public class ExtendedQctnBasedQueryTest {

    ExtendedQctnBasedQuery extendedQctnBasedQuery;
    Collection<ExtendedQctnBasedQuery> extendedSignatureBasedQueries;

    @Before
    public void setUp() throws Exception {
        extendedSignatureBasedQueries = new ArrayList<>();
        extendedQctnBasedQuery = new ExtendedQctnBasedQuery();
        extendedQctnBasedQuery.setUserName("sky");
        extendedQctnBasedQuery.setHostName("localhost");
        extendedQctnBasedQuery.setAlgorithm("sign");
        extendedQctnBasedQuery.setTimeStamp(new Date().getTime());
        extendedQctnBasedQuery.setCrud("SELECT");
        extendedQctnBasedQuery.setId("12345");
        TreeSet<String> tableNames = new TreeSet<>();
        tableNames.add("table5");
        tableNames.add("table2");
        extendedQctnBasedQuery.setTableNames(tableNames);
        extendedSignatureBasedQueries.add(extendedQctnBasedQuery);
        extendedQctnBasedQuery.setUserName("sam");
        extendedSignatureBasedQueries.add(extendedQctnBasedQuery);
    }

    @Test
    public void testGenerateId() throws Exception {
        System.out.println("\nTest to generate ID");
        System.out.println(ExtendedQctnBasedQuery.generateId(extendedQctnBasedQuery));
    }

    @Test
    public void testToJson() throws Exception {
        System.out.println("\nTest to Json");
        System.out.println(extendedQctnBasedQuery.toJson());
    }

    @Test
    public void testToJsonArray() throws Exception {
        System.out.println("\ntest to JsonArray");
        System.out.println(ExtendedQctnBasedQuery.toJsonArray(extendedSignatureBasedQueries));
    }

    @Test
    public void testToExtendedSignatureBasedTransaction() throws Exception {
        System.out.println("\ntest to ExtendedOrcnBasedQuery");
        String json = extendedQctnBasedQuery.toJson();
        System.out.println(ExtendedQctnBasedQuery.toExtendedQctnBasedQuery(json));
    }

    @Test
    public void testToExtendedSignatureBasedTransactions() throws Exception {
        System.out.println("\ntest to ExtendedORCNBasedTransactions");
        String jsons = ExtendedQctnBasedQuery.toJsonArray(extendedSignatureBasedQueries);
        System.out.println(ExtendedQctnBasedQuery.toExtendedQctnBasedQueries(jsons));
    }
}
