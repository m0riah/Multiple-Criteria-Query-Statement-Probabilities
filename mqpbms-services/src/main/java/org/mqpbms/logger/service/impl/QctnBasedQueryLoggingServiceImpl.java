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
import org.mqpbms.logger.models.*;
import org.mqpbms.logger.repositories.*;
import org.mqpbms.logger.service.QctnBasedQueryLoggingService;
import org.mqpbms.preprocessor.models.ExtendedQctnBasedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

/**
 *  Query Command and Table Names (QCTN) based query logging service implementation.
 *
 * @author sky
 * @version 2/16/14
 */
@Service("QctnBasedQueryLoggingServiceImpl")
public class QctnBasedQueryLoggingServiceImpl implements QctnBasedQueryLoggingService {

    @Autowired
    @Qualifier("QctnBasedPbqLogDao")
    private PbqLogDao PbqLogDao;

    @Autowired
    @Qualifier("QctnBasedQueryDaoImpl")
    private QctnBasedQueryDao queryDao;

    @Autowired
    @Qualifier("QctnBasedQueryProbabilityDao")
    private QueryProbabilityDao queryProbabilityDao;

    @Autowired
    @Qualifier("QctnBasedQueryTransitionProbabilityDao")
    private QueryTransitionProbabilityDao queryTransitionProbabilityDao;

    @Autowired
    @Qualifier("QctnBasedTransitionTableNameIndexDao")
    private TransitionTableNameIndexDao transitionTableNameIndexDao;

    @Autowired
    @Qualifier("QctnBasedUserDao")
    private UserDao userDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public void logQctnBasedQueries(Collection<ExtendedQctnBasedQuery> extendedQctnBasedQueries) {
        for (ExtendedQctnBasedQuery extendedQctnBasedQuery : extendedQctnBasedQueries) {

            // get a user who executed the query.
            User user = userDao.getUser(extendedQctnBasedQuery.getUserName());

            // If the user is new,
            if (user == null) {
                user = setUpForNewUser(extendedQctnBasedQuery.getUserName());
            }
            // log the QCTN query.
            logQctnBasedQuery(extendedQctnBasedQuery, user);
            // set the previous query ID for the user to the current query ID.
            user.setPreviousQueryId(extendedQctnBasedQuery.getId());
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

        // Add a query Transition Table Name Index tables.
        String qctnTransitionTableIndexTableName = transitionTableNameIndexDao.generateTableName(userName);
        transitionTableNameIndexDao.createTable(qctnTransitionTableIndexTableName);

        // Add a query Probability table for the user.
        String qctnQueryProbTableName = queryProbabilityDao.generateTableName(userName);
        queryProbabilityDao.createTable(qctnQueryProbTableName);

        // Add a Probability Based Query Log tables
        String qctnPbqLogTableName = PbqLogDao.generateTableName(userName);
        PbqLogDao.createTable(qctnPbqLogTableName);
        return user;
    }

    /**
     * Log {@link org.mqpbms.preprocessor.models.ExtendedQctnBasedQuery} to the DB.
     *
     * @param extendedQctnBasedQuery a {@link org.mqpbms.preprocessor.models.ExtendedQctnBasedQuery}.
     * @param user a user who executed the query.
     */
    private void logQctnBasedQuery(ExtendedQctnBasedQuery extendedQctnBasedQuery, User user) {

        String transitionTableIndexTableName = transitionTableNameIndexDao.generateTableName(user.getUserName());
        // If the query ID does not exist in the Transition Table Name Index table,
        if (!transitionTableNameIndexDao.contain(transitionTableIndexTableName, extendedQctnBasedQuery.getId())) {
            // If the query ID does not even exist in the Query table,
            if (!queryDao.contain(extendedQctnBasedQuery.getId())) {
                // Add the query to the Query table.
                QctnBasedQuery qctnBasedQuery = convertToQctnBasedQuery(extendedQctnBasedQuery);
                queryDao.addQuery(qctnBasedQuery);
            }
            // Get a TransitionTableNameIndex object having a random table name.
            TransitionTableNameIndex transitionTableNameIndex =
                    TransitionTableNameIndex.getObjectHavingRandomTransitionTableName(extendedQctnBasedQuery.getId(),
                            PreprocessAlgorithm.QCTN);

            // Create the Query Transition table.
            // This should happen ahead of adding the TransitionTableNameIndex to the Transition Probability
            // Table Index table to prevent a Query Transition Table not found error while Query Probability
            // calculation.
            queryTransitionProbabilityDao.createTable(transitionTableNameIndex.getTransitionTableName());

            // Add the query to the Query Transition Table Name Index table.
            transitionTableNameIndexDao.addTransitionTableIndex(transitionTableIndexTableName,
                    transitionTableNameIndex);

        }

        double queryTransitionProbability = 0.0;
        // If the user is just added, so the previous query id is null,
        if (user.getPreviousQueryId() == null) {
            // do nothing, the query Transition Probability will be just 0.0.
        } else {
            String previousTableName =
                    transitionTableNameIndexDao.
                            getTransitionTableIndexById(transitionTableIndexTableName, user.getPreviousQueryId()).
                            getTransitionTableName();

            // update the Query Transition Probability table.
            queryTransitionProbabilityDao.updateQueryTransitionCounts(previousTableName, extendedQctnBasedQuery.getId());

            // get the Query Transition Probability of the current query.
            queryTransitionProbability =
                    queryTransitionProbabilityDao.getQueryTransitionProbability(previousTableName,
                            extendedQctnBasedQuery.getId());
        }

        // Get the QCTN Based query Probability Table name for the specific user.
        String queryProbabilityTableName = queryProbabilityDao.generateTableName(user.getUserName());
        // get the Query Probability of the current query.
        double queryProbability = queryProbabilityDao.getQueryProbability(
                queryProbabilityTableName,
                extendedQctnBasedQuery.getId());

        // Create a new PbqLog to log.
        PbqLog PbqLog = new PbqLog(new Date().getTime(),
                extendedQctnBasedQuery.getTimeStamp(),
                extendedQctnBasedQuery.getUserName(),
                extendedQctnBasedQuery.getHostName(),
                extendedQctnBasedQuery.getId(),
                queryTransitionProbability,
                queryProbability);

        // Get the Probability Based Query Log table name for the specific user.
        String pbqLogTableName = PbqLogDao.generateTableName(user.getUserName());
        // Add the PbqLog into the PbqLog table.
        PbqLogDao.addLog(pbqLogTableName, PbqLog);
    }

    /**
     * Convert the {@link org.mqpbms.preprocessor.models.ExtendedQctnBasedQuery} to
     * {@link org.mqpbms.logger.models.QctnBasedQuery}.
     *
     * @param extendedQctnBasedQuery an {@link org.mqpbms.preprocessor.models.ExtendedQctnBasedQuery} object.
     * @return an {@link org.mqpbms.logger.models.QctnBasedQuery}.
     */
    private QctnBasedQuery convertToQctnBasedQuery(ExtendedQctnBasedQuery extendedQctnBasedQuery) {
        QctnBasedQuery qctnBasedQuery = new QctnBasedQuery();
        qctnBasedQuery.setId(extendedQctnBasedQuery.getId());
        qctnBasedQuery.setCrud(extendedQctnBasedQuery.getCrud());
        qctnBasedQuery.setTableNames(extendedQctnBasedQuery.getTableNames());
        qctnBasedQuery.setOriginalStatement(extendedQctnBasedQuery.getOriginalStatement());
        return qctnBasedQuery;
    }


    public void setPbqLogDao(PbqLogDao pbqLogDao) {
        this.PbqLogDao = pbqLogDao;
    }

    public void setQueryDao(QctnBasedQueryDao queryDao) {
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
