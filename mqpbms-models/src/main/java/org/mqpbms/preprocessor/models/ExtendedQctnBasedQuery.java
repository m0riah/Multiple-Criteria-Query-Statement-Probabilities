/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.preprocessor.models;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * This is used in the Preprocessor component to send a Query Command and Table Names (QCTN) based pre-processed
 * query to the Logger component.
 *
 * @author sky
 * @version 2/11/14
 */
public class ExtendedQctnBasedQuery {
    /**
     * User name.
     */
    private String userName;

    /**
     * Host name.
     */
    private String hostName;

    /**
     * Time stamp the query was executed.
     */
    private long timeStamp;

    /**
     * Query ID.
     */
    private String id;

    /**
     * Algorithm name used to pre-process the original query statement.
     */
    private String algorithm;

    /**
     * COLUMN_NAMES command.
     */
    private String crud;

    /**
     * Table names in the FROM clause.
     */
    private TreeSet<String> tableNames = new TreeSet<>();

    /**
     * Original query statement.
     */
    private String originalStatement;

    /**
     * Generate an ID of the input object using the field values of the object.
     * <p>The ID is to define the transaction uniquely, and not supposed to be parsed again.</p>
     *
     * @param extendedQctnBasedQuery a {@link org.mqpbms.preprocessor.models.ExtendedQctnBasedQuery} object.
     * @return a String ID.
     */
    public static String generateId(ExtendedQctnBasedQuery extendedQctnBasedQuery) {
        StringBuilder sb = new StringBuilder();
        sb.append(extendedQctnBasedQuery.getAlgorithm());
        sb.append("#");
        sb.append(extendedQctnBasedQuery.getCrud());
        sb.append("(");
        Iterator<String> itr = extendedQctnBasedQuery.getTableNames().iterator();
        if (itr.hasNext()) {
            sb.append(itr.next());
        }
        while (itr.hasNext()) {
            sb.append("&");
            sb.append(itr.next());
        }
        sb.append(")");
        return sb.toString();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getCrud() {
        return crud;
    }

    public void setCrud(String crud) {
        this.crud = crud;
    }

    public TreeSet<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(TreeSet<String> tableNames) {
        this.tableNames = tableNames;
    }

    public String getOriginalStatement() {
        return originalStatement;
    }

    public void setOriginalStatement(String originalStatement) {
        this.originalStatement = originalStatement;
    }

    /**
     * Convert this object to JSON string format.
     *
     * @return JSON string format of this object.
     */
    public String toJson() {
        return new JSONSerializer().include("tableNames").exclude("*.class").serialize(this);
    }

    /**
     * Convert a collection of {@link ExtendedQctnBasedQuery} to
     * JSON string format.
     *
     * @param collection a collection of {@link ExtendedQctnBasedQuery}.
     * @return JSON string format of the collection.
     */
    public static String toJsonArray(Collection<ExtendedQctnBasedQuery> collection) {
        return new JSONSerializer().include("tableNames").exclude("*.class").serialize(collection);
    }

    /**
     * Convert a proper JSON string format to {@link ExtendedQctnBasedQuery}.
     *
     * @param json a JSON string having {@link ExtendedQctnBasedQuery} info.
     * @return {@link ExtendedQctnBasedQuery}.
     */
    public static ExtendedQctnBasedQuery toExtendedQctnBasedQuery(String json) {
        return new JSONDeserializer<ExtendedQctnBasedQuery>().
                use(null, ExtendedQctnBasedQuery.class).deserialize(json);
    }

    /**
     * Convert a proper JSON string format to a collection of
     * {@link ExtendedQctnBasedQuery}.
     *
     * @param jsons a json string having a collection of
     *              {@link ExtendedQctnBasedQuery} information.
     * @return a collection of {@link ExtendedQctnBasedQuery}.
     */
    public static Collection<ExtendedQctnBasedQuery> toExtendedQctnBasedQueries(String jsons) {
        return new JSONDeserializer<Collection<ExtendedQctnBasedQuery>>().use(null, ArrayList.class).
                use("values", ExtendedQctnBasedQuery.class).deserialize(jsons);
    }

    @Override
    public String toString() {
        return "ExtendedQctnBasedQuery{" +
                "userName='" + userName + '\'' +
                ", hostName='" + hostName + '\'' +
                ", timeStamp=" + timeStamp +
                ", id='" + id + '\'' +
                ", algorithm='" + algorithm + '\'' +
                ", crud='" + crud + '\'' +
                ", tableNames=" + tableNames +
                '}';
    }
}
