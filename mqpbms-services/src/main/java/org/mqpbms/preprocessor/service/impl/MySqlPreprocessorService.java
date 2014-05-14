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
import com.akiban.sql.parser.SQLParser;
import com.akiban.sql.parser.StatementNode;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.mqpbms.common.models.PreprocessAlgorithmNameSpace;
import org.mqpbms.logsender.models.MySqlGeneralLog;
import org.mqpbms.preprocessor.models.*;
import org.mqpbms.preprocessor.service.PreprocessorService;
import org.mqpbms.preprocessor.service.StatementProbe;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Preprocessor service for MySQL General Log.
 *
 * @author sky
 * @version 2/3/14
 */
@Service("MySqlPreprocessorService")
public class MySqlPreprocessorService implements PreprocessorService, InitializingBean {

    /**
     * Regular expression pattern for user segment
     */
    private static final String USER_SEG_REGEX = "\\[.+@";

    /**
     * Regular expression pattern for host segment.
     */
    private static final String HOST_SEG_REGEX = "@.+]";

    /**
     * Regular expression pattern to trim host segment.
     */
    private static final String HOST_TRIM_REGEX = "[@\\[\\]\\s]";

    /**
     * The user names who are exempt from monitoring.
     */
    private Set<String> trustedUserNames;

    /**
     * It is to send the JSON data to the destination server.
     */
    private CloseableHttpClient httpClient;

    /**
     * To post the Operation Related Column Names (ORCN) based query JSON data.
     */
    private HttpPost orcnHttpPostRequest;

    /**
     * To post the Query Command and Table Name (QCTN) based query JSON data.
     */
    private HttpPost qctnHttpPostRequest;

    /**
     * QCTN based query post url.
     */
    private String qctnPostUrl;

    /**
     * ORCN based query post url.
     */
    private String orcnPostUrl;

    /**
     * Sql Parser.
     */
    private SQLParser sqlParser;

    @Override
    public void afterPropertiesSet() throws Exception {
        trustedUserNames = new HashSet<>();
        trustedUserNames.add("dlp");
        trustedUserNames.add("phpmyadmin");
        httpClient = HttpClients.createDefault();
        qctnPostUrl = "http://localhost:8080/api/qctnBasedQueries";
        orcnPostUrl = "http://localhost:8080/api/orcnBasedQueries";
        qctnHttpPostRequest = new HttpPost(qctnPostUrl);
        orcnHttpPostRequest = new HttpPost(orcnPostUrl);
        sqlParser = new SQLParser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processAndSend(Collection<MySqlGeneralLog> mySqlGeneralLogs) {
        // Prepare two collections to send them after pre-processing the original query statement.
        Collection<ExtendedOrcnBasedQuery> extendedOrcnBasedQueries = new ArrayList<>();
        Collection<ExtendedQctnBasedQuery> extendedQctnBasedQueries = new ArrayList<>();

        // Create a new StatementProbe to produce pre-processed queries.
        StatementProbe statementProbe = new MySqlStatementProbe();
        // Add the Collectors having pre-processing logic.
        statementProbe.addCollector(PreprocessAlgorithmNameSpace.ORCN_BASED, new ORCNBasedCollector());
        statementProbe.addCollector(PreprocessAlgorithmNameSpace.QCTN_BASED, new QCTNBasedCollector());

        for (MySqlGeneralLog log : mySqlGeneralLogs) {
            String userName = getSimpleUserName(log.getUserHost());

            // Check the username to decide whether the user is need to be monitored.
            if (!trustedUserNames.contains(userName)) {

                // If there is any error while parsing the statement, do not add it to the return array.
                try {
                    // parse the original query statement and put each node into the list.
                    List<StatementNode> nodes = sqlParser.parseStatements(log.getArgument());

                    // rest the state of collectors with a new username, hostname and timestamp of the original statement.
                    statementProbe.reset(userName,
                            getSimpleHost(log.getUserHost()), log.getEventTime().getTime(), log.getArgument());

                    // Probe a parsed statement nodes.
                    for (StatementNode node : nodes) {
                        node.accept(statementProbe);
                    }

                    // If the traversal did not stop, update and add the processed queries in the arrays to send.
                    // into the result array.
                    if (!statementProbe.stopTraversal()) {
                        ExtendedOrcnBasedQuery orcn = ((ORCNBasedCollector) statementProbe.getCollectorMap().
                                get(PreprocessAlgorithmNameSpace.ORCN_BASED)).getExtendedOrcnBasedQuery();
                        extendedOrcnBasedQueries.add(orcn);
                        ExtendedQctnBasedQuery qctn = ((QCTNBasedCollector) statementProbe.
                                getCollectorMap().get(PreprocessAlgorithmNameSpace.QCTN_BASED)).
                                getExtendedQctnBasedQuery();
                        extendedQctnBasedQueries.add(qctn);
                    }
                } catch (StandardException e) {
                    // If there is an exception while parsing the original statement, then do nothing.
//                    System.out.println("error happened");
                }
            }
        }
        sendOrcnBasedQueries(extendedOrcnBasedQueries);
        sendQctnBasedQueries(extendedQctnBasedQueries);
    }

    /**
     * Get simple user name from the userHost string from the {@link org.mqpbms.logsender.models.MySqlGeneralLog}.
     *
     * @param getUserHost a userHost string from {@link org.mqpbms.logsender.models.MySqlGeneralLog}.
     * @return a simple user name.
     */
    private final static String getSimpleUserName(final String getUserHost) {
        final Pattern pattern = Pattern.compile(USER_SEG_REGEX);
        final Matcher matcher = pattern.matcher(getUserHost);
        String result = "";
        if (matcher.find()) {
            result = getUserHost.substring(matcher.start() + 1, matcher.end() - 3);
        }
        return result;
    }

    /**
     * Get simple host name from the userHost string from the {@link org.mqpbms.logsender.models.MySqlGeneralLog}.
     *
     * @param getUserHost a userHost string from {@link org.mqpbms.logsender.models.MySqlGeneralLog}.
     * @return a simple host name.
     */
    private final static String getSimpleHost(final String getUserHost) {
        final Pattern pattern = Pattern.compile(HOST_SEG_REGEX);
        final Matcher matcher = pattern.matcher(getUserHost);
        String result = "";
        if (matcher.find()) {
            result = getUserHost.substring(matcher.start(), matcher.end());
        }
        result = result.replaceAll(HOST_TRIM_REGEX, "");
        return result;
    }

    /**
     * Send a collection of {@link org.mqpbms.preprocessor.models.ExtendedOrcnBasedQuery} to the destination
     * server in JSON form.
     *
     * @param extendedORCNBasedQueries a collection of
     *                                      {@link org.mqpbms.preprocessor.models.ExtendedOrcnBasedQuery}.
     */
    private void sendOrcnBasedQueries(Collection<ExtendedOrcnBasedQuery> extendedORCNBasedQueries) {
        // Debugging purpose.
        int i = 0;
        for (ExtendedOrcnBasedQuery query : extendedORCNBasedQueries) {
            System.out.println(i + " : " + query);
            i++;
        }
        try {
            httpClient = HttpClients.createDefault();
            StringEntity input = new StringEntity(ExtendedOrcnBasedQuery.toJsonArray(extendedORCNBasedQueries));
            input.setContentType("application/json");
            orcnHttpPostRequest.setEntity(input);
            httpClient.execute(orcnHttpPostRequest);
            httpClient.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (HttpHostConnectException e) {
            System.err.println("\n Destination server is not responding. " +
                    "Check the log convert server sending the logs to: " + orcnPostUrl + "\n");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Send a collection of {@link org.mqpbms.preprocessor.models.ExtendedQctnBasedQuery} to the destination
     * server in JSON form.
     *
     * @param extendedQctnBasedQueryCollections a collection of
     *                                                     {@link org.mqpbms.preprocessor.models.ExtendedQctnBasedQuery}.
     */
    private void sendQctnBasedQueries(Collection<ExtendedQctnBasedQuery>
                                              extendedQctnBasedQueryCollections) {
        // Debugging purpose.
        int i = 0;
        for (ExtendedQctnBasedQuery query : extendedQctnBasedQueryCollections) {
            System.out.println(i + " : " + query);
            i++;
        }
        try {
            httpClient = HttpClients.createDefault();
            StringEntity input = new StringEntity(ExtendedQctnBasedQuery.
                    toJsonArray(extendedQctnBasedQueryCollections));
            input.setContentType("application/json");
            qctnHttpPostRequest.setEntity(input);
            httpClient.execute(qctnHttpPostRequest);
            httpClient.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (HttpHostConnectException e) {
            System.err.println("\n Destination server is not responding. " +
                    "Check the log convert server sending the logs to: " + qctnPostUrl + "\n");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
