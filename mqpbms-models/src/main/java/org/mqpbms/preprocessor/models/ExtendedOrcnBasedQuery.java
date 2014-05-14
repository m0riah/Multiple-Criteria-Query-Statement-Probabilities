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
 * This is used in the Preprocessor component to send an Operation Related Column Names (ORCN) based pre-processed
 * query to the Logger component.
 *
 * @author sky
 * @version 2/11/14
 */
public class ExtendedOrcnBasedQuery {

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
     * Column names related to operations in the query statement.
     * The column names are sorted in a set.
     */
    private TreeSet<String> columnNames = new TreeSet<>();

    /**
     * Original query statement.
     */
    private String originalStatement;

    /**
     * Generate an ID of the input object using the field values of the object.
     * <p>The ID is to define the transaction uniquely, and not supposed to be parsed again.</p>
     *
     * @param extendedOrcnBasedQuery {@link org.mqpbms.preprocessor.models.ExtendedOrcnBasedQuery} object.
     * @return ID in String form.
     */
    public static String generateId(ExtendedOrcnBasedQuery extendedOrcnBasedQuery) {
        StringBuilder sb = new StringBuilder();
        sb.append(extendedOrcnBasedQuery.algorithm);
        sb.append("#");
        sb.append("(");
        Iterator<String> itr = extendedOrcnBasedQuery.columnNames.iterator();
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

    public TreeSet<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(TreeSet<String> columnNames) {
        this.columnNames = columnNames;
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
        return new JSONSerializer().include("columnNames").exclude("*.class").serialize(this);
    }

    /**
     * Convert a collection of {@link ExtendedOrcnBasedQuery} to
     * JSON string format.
     *
     * @param collection a collection of {@link ExtendedOrcnBasedQuery}.
     * @return JSON string format of the collection.
     */
    public static String toJsonArray(Collection<ExtendedOrcnBasedQuery> collection) {
        return new JSONSerializer().include("columnNames").exclude("*.class").serialize(collection);
    }

    /**
     * Convert a proper JSON string format to {@link ExtendedOrcnBasedQuery}.
     *
     * @param json a JSON string having {@link ExtendedOrcnBasedQuery} info.
     * @return {@link ExtendedOrcnBasedQuery}.
     */
    public static ExtendedOrcnBasedQuery toExtendedOrcnBasedQuery(String json) {
        return new JSONDeserializer<ExtendedOrcnBasedQuery>().
                use(null, ExtendedOrcnBasedQuery.class).deserialize(json);
    }

    /**
     * Convert a proper JSON string format to a collection of
     * {@link ExtendedOrcnBasedQuery}.
     *
     * @param jsons a json string having a collection of
     *              {@link ExtendedOrcnBasedQuery} information.
     * @return a collection of {@link ExtendedOrcnBasedQuery}.
     */
    public static Collection<ExtendedOrcnBasedQuery> toExtendedOrcnBasedQueries(String jsons) {
        return new JSONDeserializer<Collection<ExtendedOrcnBasedQuery>>().use(null, ArrayList.class).
                use("values", ExtendedOrcnBasedQuery.class).deserialize(jsons);
    }

    @Override
    public String toString() {
        return "ExtendedOrcnBasedQuery{" +
                "userName='" + userName + '\'' +
                ", hostName='" + hostName + '\'' +
                ", timeStamp=" + timeStamp +
                ", id='" + id + '\'' +
                ", algorithm='" + algorithm + '\'' +
                ", columnNames=" + columnNames +
                '}';
    }

}
