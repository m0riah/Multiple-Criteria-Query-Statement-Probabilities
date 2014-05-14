/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.models;

/**
 * A user (Insider).
 *
 * @author sky
 * @version 1/9/14
 */
public class User {

    /**
     * User name should be unique.
     */
    private String userName;

    /**
     * Previous query IDs the user executed.
     */
    private String previousQueryId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPreviousQueryId() {
        return previousQueryId;
    }

    public void setPreviousQueryId(String previousTransactionId) {
        this.previousQueryId = previousTransactionId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", previousQueryId='" + previousQueryId + '\'' +
                '}';
    }
}
