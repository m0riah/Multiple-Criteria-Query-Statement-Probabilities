/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logsender;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.mqpbms.logsender.models.MySqlGeneralLog;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

/**
 * Send a list of GeneralLogs to the log converter server using json format.
 *
 * @author sky
 * @version 01/06/13
 * @since 10/15/13
 */
public class JsonGeneralLogWriter implements ItemWriter<MySqlGeneralLog>, InitializingBean {

    /**
     * HttpClient to send list of logs.
     */
    private CloseableHttpClient httpClient;

    /**
     * Helper class to send http post request.
     */
    private HttpPost httpPostRequest;

    /**
     * The server url to post.
     */
    private String postUrl;

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        httpClient = HttpClients.createDefault();
        httpPostRequest = new HttpPost(postUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(List<? extends MySqlGeneralLog> items) throws Exception {
        sendPost((List<MySqlGeneralLog>) items);
//        Debug purpose
//        Iterator<? extends MySqlGeneralLog> itr = items.iterator();
//        while (itr.hasNext()) {
//            MySqlGeneralLog temp = itr.next();
//            if (temp == null) {
//
//            } else {
//                System.out.println(temp);
//            }
//        }
    }

    /**
     * Send the list of GeneralLogs to the log convert server.
     *
     * @param generalLogs general logs from the {@link DefaultGeneralLogProcessor}
     */
    private void sendPost(List<MySqlGeneralLog> generalLogs) {
        // Create HttpClient for each request. If not, it does not work.
        try {
            httpClient = HttpClients.createDefault();
            StringEntity input = new StringEntity(MySqlGeneralLog.toJsonArray(generalLogs));
            input.setContentType("application/json");
            httpPostRequest.setEntity(input);
            httpClient.execute(httpPostRequest);
            httpClient.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (HttpHostConnectException e) {
            System.err.println("\n Destination server is not responding. " +
                    "Check the log convert server sending the logs to: " + postUrl + "\n");
        } catch (ClientProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

}
