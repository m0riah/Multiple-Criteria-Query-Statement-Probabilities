/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.service.impl;

import org.mqpbms.common.models.PreprocessAlgorithm;
import org.mqpbms.logger.models.OrcnBasedQuery;
import org.mqpbms.logger.models.PbqLog;
import org.mqpbms.logger.models.TransitionTableNameIndex;
import org.mqpbms.logger.models.User;
import org.mqpbms.logger.repositories.*;
import org.mqpbms.logger.service.OrcnBasedQueryLoggingService;
import org.mqpbms.preprocessor.models.ExtendedOrcnBasedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

/**
 * Operation Related Column Names based query logging service implementation.
 *
 * @author sky
 * @version 2/12/14
 */
@Service("OrcnBasedQueryLoggingServiceImpl")
public class OrcnBasedQueryLoggingServiceImpl implements OrcnBasedQueryLoggingService {

    @Autowired
    @Qualifier("OrcnBasedPbqLogDao")
    private PbqLogDao PbqLogDao;

    @Autowired
    @Qualifier("OrcnBasedQueryDaoImpl")
    private OrcnBasedQueryDao queryDao;

    @Autowired
    @Qualifier("OrcnBasedQueryProbabilityDao")
    private QueryProbabilityDao queryProbabilityDao;

    @Autowired
    @Qualifier("OrcnBasedQueryTransitionProbabilityDao")
    private QueryTransitionProbabilityDao queryTransitionProbabilityDao;

    @Autowired
    @Qualifier("OrcnBasedTransitionTableNameIndexDao")
    private TransitionTableNameIndexDao transitionTableNameIndexDao;

    @Autowired
    @Qualifier("OrcnBasedUserDao")
    private UserDao userDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public void logOrcnBasedQueries(Collection<ExtendedOrcnBasedQuery> extendedORCNBasedQueries) {

        for (ExtendedOrcnBasedQuery extendedOrcnBasedQuery : extendedORCNBasedQueries) {

            // get a user who executed the query.
            User user = userDao.getUser(extendedOrcnBasedQuery.getUserName());

            // If the user is new,
            if (user == null) {
                user = setUpForNewUser(extendedOrcnBasedQuery.getUserName());
            }
            // log the ORCN query.
            logORCNBasedQuery(extendedOrcnBasedQuery, user);
            // set the previous query ID for the user to the current query ID.
            user.setPreviousQueryId(extendedOrcnBasedQuery.getId());
            // update the user data in the User Table.
            userDao.addUser(user);
        }
    }

    /**
     * Create all the tables needed for the new user.
     *
     * @param userName a new user name.
     * @return a {@link org.mqpbms.logger.models.User} object.
     */
    private User setUpForNewUser(String userName) {
        User user = new User();
        user.setUserName(userName);

        // Add a Query Transition Table Name Index tables.
        String orcnTransitionTableIndexTableName = transitionTableNameIndexDao.generateTableName(userName);
        transitionTableNameIndexDao.createTable(orcnTransitionTableIndexTableName);

        // Add a Query Probability table for the user.
        String orcnQueryProbTableName = queryProbabilityDao.generateTableName(userName);
        queryProbabilityDao.createTable(orcnQueryProbTableName);

        // Add a Probability Based Query Log tables
        String orcnPBTLogTableName = PbqLogDao.generateTableName(userName);
        PbqLogDao.createTable(orcnPBTLogTableName);
        return user;
    }

    /**
     * Log {@link org.mqpbms.preprocessor.models.ExtendedOrcnBasedQuery} to the DB.
     *
     * @param extendedOrcnBasedQuery a {@link org.mqpbms.preprocessor.models.ExtendedOrcnBasedQuery}.
     * @param user                         a user who executed the query.
     */
    private void logORCNBasedQuery(ExtendedOrcnBasedQuery extendedOrcnBasedQuery, User user) {

        String transitionTableIndexTableName = transitionTableNameIndexDao.generateTableName(user.getUserName());
        // If the query ID does not exist in the Query Transition Table Index table,
        if (!transitionTableNameIndexDao.contain(transitionTableIndexTableName, extendedOrcnBasedQuery.getId())) {
            // If the query ID does not even exist in the Query table,
            if (!queryDao.contain(extendedOrcnBasedQuery.getId())) {
                // Add the query to the Query table.
                OrcnBasedQuery orcnBasedQuery = convertToORCNBasedQuery(extendedOrcnBasedQuery);
                queryDao.addQuery(orcnBasedQuery);
            }
            // Get a TransitionTableNameIndex object having a random table name.
            TransitionTableNameIndex transitionTableNameIndex =
                    TransitionTableNameIndex.getObjectHavingRandomTransitionTableName(extendedOrcnBasedQuery.getId(),
                            PreprocessAlgorithm.ORCN);
            // Create the Query Transition table.
            // This should happen ahead of adding the TransitionTableNameIndex to the Transition Table Name Index table
            // to prevent a Query Transition Table not found error while Query Probability calculation.
            queryTransitionProbabilityDao.createTable(transitionTableNameIndex.getTransitionTableName());

            // Add the query to the Query Transition Table Name Index table.
            transitionTableNameIndexDao.addTransitionTableIndex(transitionTableIndexTableName,
                    transitionTableNameIndex);
        }

        double queryTransitionProbability = 0.0;
        // If the user is just added, so the previous query id is null,
        if (user.getPreviousQueryId() == null) {
            // do nothing, the Query Transition Probability will be just 0.0.
        } else {
            String previousTableName =
                    transitionTableNameIndexDao.
                            getTransitionTableIndexById(transitionTableIndexTableName, user.getPreviousQueryId()).
                            getTransitionTableName();

            // update the Query Transition Probability table.
            queryTransitionProbabilityDao.updateQueryTransitionCounts(previousTableName, extendedOrcnBasedQuery.getId());

            // get the Query Transition Probability of the current query.
            queryTransitionProbability =
                    queryTransitionProbabilityDao.getQueryTransitionProbability(previousTableName,
                            extendedOrcnBasedQuery.getId());
        }

        // Get the ORCN Based Query Probability Table name for the specific user.
        String queryProbabilityTableName = queryProbabilityDao.generateTableName(user.getUserName());
        // get the Query Probability of the current query.
        double queryProbability = queryProbabilityDao.getQueryProbability(
                queryProbabilityTableName,
                extendedOrcnBasedQuery.getId());

        // Create a new PbqLog to log.
        PbqLog PbqLog = new PbqLog(new Date().getTime(),
                extendedOrcnBasedQuery.getTimeStamp(),
                extendedOrcnBasedQuery.getUserName(),
                extendedOrcnBasedQuery.getHostName(),
                extendedOrcnBasedQuery.getId(),
                queryTransitionProbability,
                queryProbability);

        // Get the Probability Based Query Log table name for the specific user.
        String pbtLogTableName = PbqLogDao.generateTableName(user.getUserName());
        // Add the PbqLog into the PbqLog table.
        PbqLogDao.addLog(pbtLogTableName, PbqLog);
    }

    /**
     * Convert the {@link org.mqpbms.preprocessor.models.ExtendedOrcnBasedQuery} to
     * {@link org.mqpbms.logger.models.OrcnBasedQuery}.
     *
     * @param extendedOrcnBasedQuery an {@link org.mqpbms.preprocessor.models.ExtendedOrcnBasedQuery} object.
     * @return an {@link org.mqpbms.logger.models.OrcnBasedQuery}.
     */
    private OrcnBasedQuery convertToORCNBasedQuery(ExtendedOrcnBasedQuery extendedOrcnBasedQuery) {
        OrcnBasedQuery orcnBasedQuery = new OrcnBasedQuery();
        orcnBasedQuery.setId(extendedOrcnBasedQuery.getId());
        orcnBasedQuery.setColumnNames(extendedOrcnBasedQuery.getColumnNames());
        orcnBasedQuery.setOriginalStatement(extendedOrcnBasedQuery.getOriginalStatement());
        return orcnBasedQuery;
    }


    public void setPbqLogDao(PbqLogDao pbqLogDao) {
        this.PbqLogDao = pbqLogDao;
    }

    public void setQueryDao(OrcnBasedQueryDao queryDao) {
        this.queryDao = queryDao;
    }

    public void setQueryProbabilityDao(QueryProbabilityDao queryProbabilityDao) {
        this.queryProbabilityDao = queryProbabilityDao;
    }

    public void setQueryTransitionProbabilityDao(QueryTransitionProbabilityDao queryTransitionProbabilityDao) {
        this.queryTransitionProbabilityDao = queryTransitionProbabilityDao;
    }

    public void setTransitionTableNameIndexDao(TransitionTableNameIndexDao transitionTableNameIndexDao) {
        this.transitionTableNameIndexDao = transitionTableNameIndexDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
