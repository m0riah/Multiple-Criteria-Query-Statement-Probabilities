package org.mqpbms.preprocessor.service.impl;
/*
 * Created with IntelliJ IDEA.
 * User: sky
 * Date: 2/6/14
 * Time: 11:52 PM
 * To change this template use File | Settings | File Templates.
 */

import org.junit.Before;
import org.junit.Test;
import org.mqpbms.logsender.models.MySqlGeneralLog;
import org.mqpbms.preprocessor.service.Collector;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author sky
 * @version 2/6/14
 */
public class MySqlPreprocessorServiceTest {

    public String STATEMENT_1 = "select hellio.SALARY as d, Hello.Hello as k FROM Zmployee e, adil, Kelo k WHERE DEPT = 'hello.TECHNOLOGY' AND helli.SALARY = 120";

    public static String STATEMENT_2 = "SELECT * FROM employee, Kelo WHERE DEPT = 'hello.TECHNOLOGY' AND helli.SALARY = 120";

    public static String STATEMENT_3 = "INSERT INTO suppliers (supplier_id, supplier_name, city) VALUES (5001, 'Microsoft', 'New York')";

    public static String STATEMENT_4 = "UPDATE suppliers " +
            "SET city = (SELECT customers.city " +
            "FROM customers " +
            "WHERE customers.customer1_name = suppliers.supplier1_name) " +
            "WHERE EXISTS (SELECT customers.city FROM customers WHERE customers.customer_name = suppliers.supplier_name)";

    public static String STATEMENT_5 = "DELETE FROM suppliers " +
            "WHERE EXISTS ( SELECT customers.customer_name FROM customers WHERE customers.customer_id = suppliers.supplier_id AND customers.customer_name = 'IBM' )";
//
    public static String STATEMENT_6 = "SELECT suppliers23.supplier_id, suppliers.supplier_name, orders.order_date " +
            "FROM suppliers INNER JOIN orders ON suppliers.supplier_id = orders.supplier_id";
//
    public static String STATEMENT_7 = "UPDATE suppliers " +
            "SET city = (SELECT customers.city " +
            "FROM customers " +
            "WHERE customers.customer1_name = suppliers.supplier1_name) " +
            "WHERE EXISTS (SELECT customers.city FROM customers WHERE customers.customer_name = suppliers.supplier_name)";

    public static String STATEMENT_8 = "SELECT orders1.order_id, orders2.order_date, suppliers.supplier_name " +
            "FROM suppliers, orders " +
            "WHERE suppliers.supplier_id = orders.supplier_id";
    public static String STATEMENT_9 = "SELECT C2.QUERY_ID, C3.NAME, C.AGE, O.AMOUNT" +
            " FROM CUSTOMERS AS C, ORDERS AS O" +
            " WHERE  C.QUERY_ID = O.CUSTOMER_ID";


    private Collection<Collector> collectorCollection;

    private Collection<MySqlGeneralLog> mySqlGeneralLogs;

    @Before
    public void setUp() throws Exception {
        collectorCollection = new ArrayList<>();
        collectorCollection.add(new ORCNBasedCollector());
        mySqlGeneralLogs = new ArrayList<>();
        MySqlGeneralLog mySqlGeneralLog1 = new MySqlGeneralLog();
        mySqlGeneralLog1.setUserHost("dlp[dlp] @ [192.168.56.1]");
        mySqlGeneralLog1.setEventTime(new Timestamp(new Date().getTime()));
        mySqlGeneralLog1.setArgument(STATEMENT_1);
        mySqlGeneralLogs.add(mySqlGeneralLog1);
        MySqlGeneralLog mySqlGeneralLog2 = new MySqlGeneralLog();
        mySqlGeneralLog2.setEventTime(new Timestamp(new Date().getTime()));
        mySqlGeneralLog2.setUserHost("hello[hello] @ [localhost]");
        mySqlGeneralLog2.setArgument(STATEMENT_2);
        mySqlGeneralLogs.add(mySqlGeneralLog2);
        MySqlGeneralLog mySqlGeneralLog3 = new MySqlGeneralLog();
        mySqlGeneralLog3.setEventTime(new Timestamp(new Date().getTime()));
        mySqlGeneralLog3.setUserHost("hello[hello] @ [localhost]");
        mySqlGeneralLog3.setArgument(STATEMENT_3);
        mySqlGeneralLogs.add(mySqlGeneralLog3);
        MySqlGeneralLog mySqlGeneralLog4 = new MySqlGeneralLog();
        mySqlGeneralLog4.setEventTime(new Timestamp(new Date().getTime()));
        mySqlGeneralLog4.setUserHost("hello[hello] @ [localhost]");
        mySqlGeneralLog4.setArgument(STATEMENT_4);
        mySqlGeneralLogs.add(mySqlGeneralLog4);
        MySqlGeneralLog mySqlGeneralLog5 = new MySqlGeneralLog();
        mySqlGeneralLog5.setEventTime(new Timestamp(new Date().getTime()));
        mySqlGeneralLog5.setUserHost("hello[hello] @ [localhost]");
        mySqlGeneralLog5.setArgument(STATEMENT_5);
        mySqlGeneralLogs.add(mySqlGeneralLog5);
        MySqlGeneralLog mySqlGeneralLog6 = new MySqlGeneralLog();
        mySqlGeneralLog6.setEventTime(new Timestamp(new Date().getTime()));
        mySqlGeneralLog6.setUserHost("hello[hello] @ [localhost]");
        mySqlGeneralLog6.setArgument(STATEMENT_6);
        mySqlGeneralLogs.add(mySqlGeneralLog6);
        MySqlGeneralLog mySqlGeneralLog7 = new MySqlGeneralLog();
        mySqlGeneralLog7.setEventTime(new Timestamp(new Date().getTime()));
        mySqlGeneralLog7.setUserHost("hello[hello] @ [localhost]");
        mySqlGeneralLog7.setArgument(STATEMENT_7);
        mySqlGeneralLogs.add(mySqlGeneralLog7);
        MySqlGeneralLog mySqlGeneralLog8 = new MySqlGeneralLog();
        mySqlGeneralLog8.setEventTime(new Timestamp(new Date().getTime()));
        mySqlGeneralLog8.setUserHost("hello[hello] @ [localhost]");
        mySqlGeneralLog8.setArgument(STATEMENT_8);
        mySqlGeneralLogs.add(mySqlGeneralLog8);
        MySqlGeneralLog mySqlGeneralLog9 = new MySqlGeneralLog();
        mySqlGeneralLog9.setEventTime(new Timestamp(new Date().getTime()));
        mySqlGeneralLog9.setUserHost("hello[hello] @ [localhost]");
        mySqlGeneralLog9.setArgument(STATEMENT_9);
        mySqlGeneralLogs.add(mySqlGeneralLog9);


    }

    @Test
    public void testProcess() throws Exception {
        MySqlPreprocessorService preprocessorService = new MySqlPreprocessorService();
        preprocessorService.afterPropertiesSet();
        preprocessorService.processAndSend(mySqlGeneralLogs);
    }
}
