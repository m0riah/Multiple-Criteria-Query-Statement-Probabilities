/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logsender.models;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A general_log row instance in mysql system database.
 *
 * @author sky
 * @version 10/15/13
 * @since 10/15/13
 */
public class MySqlGeneralLog {
    /**
     * A name of the database userHost who and where the query is executed.
     */
    private String userHost;

    /**
     * The time of the query executed.
     */
    private Timestamp eventTime;

    /**
     * connection command (not SELECT, UPDATE, etc.).
     */
    private String command;

    /**
     * A query statement.
     */
    private String argument;


    public String getUserHost() {
        return userHost;
    }

    public void setUserHost(String userHost) {
        this.userHost = userHost;
    }

    public Timestamp getEventTime() {
        return eventTime;
    }

    public void setEventTime(Timestamp eventTime) {
        this.eventTime = eventTime;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getArgument() {
        return argument;
    }


    public void setArgument(String argument) {
        this.argument = argument;
    }

    /**
     * This is for converting the class to Json serialized format.
     * Thanks for the net.sf.flexjson library.
     *
     * @return a serialized MySqlGeneralLog object in JSON format.
     */
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    /**
     * Converter from GneralLog collection to Json strings.
     *
     * @return a serialized MultiCriteriaTransaction objects in JSON format.
     */
    public static String toJsonArray(Collection<MySqlGeneralLog> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static Collection<MySqlGeneralLog> fromJsonArrayToGeneralLogs(String json) {
        return new JSONDeserializer<List<MySqlGeneralLog>>().use(null,
                ArrayList.class).use("values", MySqlGeneralLog.class).deserialize(json);
    }

    /**
     * For test purpose.
     */
    @Override
    public String toString() {
        return "TransactionLog{" +
                "userHost='" + userHost + '\'' +
                ", eventTime=" + eventTime +
                ", command='" + command + '\'' +
                ", argument='" + argument + '\'' +
                '}';
    }
}
