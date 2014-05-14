package org.mqpbms.logger.service.impl;
/*
 * Created with IntelliJ IDEA.
 * User: sky
 * Date: 2/10/14
 * Time: 6:38 PM
 * To change this template use File | Settings | File Templates.
 */

import org.junit.Before;
import org.junit.Test;
import org.mqpbms.common.models.PreprocessAlgorithmNameSpace;
import org.mqpbms.connection.DefaultCassandraFactory;
import org.mqpbms.connection.DefaultCassandraTemplate;
import org.mqpbms.logger.repositories.impl.*;
import org.mqpbms.preprocessor.models.ExtendedOrcnBasedQuery;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author sky
 * @version 2/10/14
 */
public class OrcnBasedQueryLoggingServiceImplTest {

    OrcnBasedQueryLoggingServiceImpl loggingService = new OrcnBasedQueryLoggingServiceImpl();

    @Before
    public void setUp() throws Exception {
        DefaultCassandraFactory defaultCassandraFactory = new DefaultCassandraFactory();
        defaultCassandraFactory.setHostName("192.168.56.101");
        defaultCassandraFactory.setPort(9042);
        defaultCassandraFactory.afterPropertiesSet();
        defaultCassandraFactory.getCluster();
        DefaultCassandraTemplate cassandraTemplate = new DefaultCassandraTemplate();
        cassandraTemplate.setFactory(defaultCassandraFactory);
        cassandraTemplate.setKeyspace("mqpbms");
        cassandraTemplate.afterPropertiesSet();

        OrcnBasedQueryDaoImpl orcnBasedQueryDao = new OrcnBasedQueryDaoImpl();
        orcnBasedQueryDao.setCassandraTemplate(cassandraTemplate);
        orcnBasedQueryDao.afterPropertiesSet();

        OrcnBasedPbqLogDao orcnBasedPBTLogDao = new OrcnBasedPbqLogDao();
        orcnBasedPBTLogDao.setCassandraTemplate(cassandraTemplate);

        OrcnBasedQueryProbabilityDao orcnQueryProbabilityDao = new OrcnBasedQueryProbabilityDao();
        orcnQueryProbabilityDao.setCassandraTemplate(cassandraTemplate);

        OrcnBasedQueryTransitionProbabilityDao orcnBasedTransitionProbabilityDao = new OrcnBasedQueryTransitionProbabilityDao();
        orcnBasedTransitionProbabilityDao.setCassandraTemplate(cassandraTemplate);

        OrcnBasedUserDao orcnBasedUserDao = new OrcnBasedUserDao();
        orcnBasedUserDao.setCassandraTemplate(cassandraTemplate);
        orcnBasedUserDao.afterPropertiesSet();

        OrcnBasedTransitionTableNameIndexDao orcnBasedTransitionTableIndexDao = new OrcnBasedTransitionTableNameIndexDao();
        orcnBasedTransitionTableIndexDao.setCassandraTemplate(cassandraTemplate);

        loggingService.setQueryDao(orcnBasedQueryDao);
        loggingService.setPbqLogDao(orcnBasedPBTLogDao);
        loggingService.setQueryProbabilityDao(orcnQueryProbabilityDao);
        loggingService.setQueryTransitionProbabilityDao(orcnBasedTransitionProbabilityDao);
        loggingService.setTransitionTableNameIndexDao(orcnBasedTransitionTableIndexDao);
        loggingService.setUserDao(orcnBasedUserDao);
    }

    @Test
    public void testLogMultiCriteriaQueries() throws Exception {
        ExtendedOrcnBasedQuery extendedOrcnBasedQuery = new ExtendedOrcnBasedQuery();
        extendedOrcnBasedQuery.setUserName("test");
        extendedOrcnBasedQuery.setHostName("localhost");
        extendedOrcnBasedQuery.setTimeStamp(new Date().getTime());
        extendedOrcnBasedQuery.setAlgorithm(PreprocessAlgorithmNameSpace.ORCN_BASED);
        TreeSet<String> columnNames = new TreeSet<>();
        columnNames.add("col3");
        columnNames.add("col4");
        extendedOrcnBasedQuery.setColumnNames(columnNames);
        extendedOrcnBasedQuery.setId(ExtendedOrcnBasedQuery.generateId(extendedOrcnBasedQuery));

        Collection<ExtendedOrcnBasedQuery> extendedORCNBasedQueries = new ArrayList<>();
        extendedORCNBasedQueries.add(extendedOrcnBasedQuery);
        loggingService.logOrcnBasedQueries(extendedORCNBasedQueries);
    }

}
