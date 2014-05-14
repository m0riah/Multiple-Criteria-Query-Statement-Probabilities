/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.qpc.service.impl;

import org.mqpbms.logger.models.QueryTransitionProbability;
import org.mqpbms.logger.models.TransitionTableNameIndex;
import org.mqpbms.logger.models.User;
import org.mqpbms.logger.repositories.QueryProbabilityDao;
import org.mqpbms.logger.repositories.QueryTransitionProbabilityDao;
import org.mqpbms.logger.repositories.TransitionTableNameIndexDao;
import org.mqpbms.logger.repositories.UserDao;
import org.mqpbms.qpc.service.QueryProbabilityCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Calculate the Operation Related Column Names (ORCN) based Query Probabilities using Markov chain mathematical model.
 *
 * @author sky
 * @version 2/18/14
 */
@Service("OrcnBasedQueryProbabilityCalculationService")
public class OrcnBasedQueryProbabilityCalculationService implements QueryProbabilityCalculationService {

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

    @Override
    public boolean updateQueryProbabilitiesForAllUser() {
        Iterator<User> userItr = userDao.getAllUsers().iterator();
        // Process through all the users in the ORCN based user table.
        while (userItr.hasNext()) {
            Map<String, Integer> queryIdAndIndexNumberMap = new TreeMap<>();
            Map<String, String> queryIdAndTransitionTableNameMap = new TreeMap<>();
            User user = userItr.next();
            System.out.println(user);
            String tpTableName = transitionTableNameIndexDao.generateTableName(user.getUserName());
            // Get a Collection of the query Transition Probability Table indexes related to a specific user
            Collection<TransitionTableNameIndex> transitionTableNameIndexes =
                    transitionTableNameIndexDao.getAllTransitionTableIndexes(tpTableName);
            Iterator<TransitionTableNameIndex> queryTransitionTableIndexIterator =
                    transitionTableNameIndexes.iterator();
            int i = 0;
            while (queryTransitionTableIndexIterator.hasNext()) {
                TransitionTableNameIndex transitionTableIndex = queryTransitionTableIndexIterator.next();
                queryIdAndTransitionTableNameMap.put(transitionTableIndex.getQueryId(), transitionTableIndex.getTransitionTableName());
                queryIdAndIndexNumberMap.put(transitionTableIndex.getQueryId(), i);
                i++;
            }
            double[][] transitionProbabilityMatrix =
                    produceQueryTransitionProbabilityMatrix(queryIdAndIndexNumberMap,
                            queryIdAndTransitionTableNameMap);
            double[] steadyStateVectors = applyMarkovChain(transitionProbabilityMatrix);
            String probabilityTableName = queryProbabilityDao.generateTableName(user.getUserName());
            updateQueryProbabilitiesTable(probabilityTableName, queryIdAndIndexNumberMap, steadyStateVectors);
        }
        return true;
    }

    /**
     * Produce a Transition Probability Matrix for a user to apply Markov Chain.
     * <p/>
     * delta = (1 - VALUE_P) / (total number of querys executed by the user), which is the probability of a
     * query randomly executed.
     * <p/>
     *
     * @param queryIdAndIndexNumberMap a map having a query ID as a key and index number as a value.
     * @param queryIdAndTransitionTableNameMap a map having a query ID as a key and query Transition
     *                                               Table name as a value.
     * @return a Transition Probability Matrix for the user.
     */
    public double[][] produceQueryTransitionProbabilityMatrix(Map<String, Integer> queryIdAndIndexNumberMap,
                                                              Map<String, String> queryIdAndTransitionTableNameMap) {
        int matrixSize = queryIdAndIndexNumberMap.size();
        // delta = (1 - p) / (total number of queries).
        double delta = roundUpForMiddleSteps((1 - VALUE_P) / (matrixSize));
        // this is for the matrix columns having no possible query transition (no outgoing link).
        double emptyTableValue = roundUpForMiddleSteps(1.0 / matrixSize);
        // query Transition Probability Matrix.
        double[][] queryTransitionProbabilityMatrix = new double[matrixSize][matrixSize];
        // First, initialize all the values in the matrix with the delta value.
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                queryTransitionProbabilityMatrix[i][j] = delta;
            }
        }
        // Update every column of the matrix
        Iterator<String> queryIdIterator = queryIdAndIndexNumberMap.keySet().iterator();
        while (queryIdIterator.hasNext()) {
            // pick up a query ID
            String queryId = queryIdIterator.next();
            // find a column number of the query Transition Probability Matrix corresponding to the query ID.
            int columnNumber = queryIdAndIndexNumberMap.get(queryId);
            String queryTransitionTableName = queryIdAndTransitionTableNameMap.get(queryId);
            // get all the possible query transition probabilities from the selected column.
            Collection<QueryTransitionProbability> queryTransitionProbabilities =
                    queryTransitionProbabilityDao.getAllQueryTransitionProbabilities(queryTransitionTableName);
            if (queryTransitionProbabilities.size() == 0) {
                // if there is nothing, set all the rows of the selected column to 1 / (matrix size).
                for (int i = 0; i < matrixSize; i++) {
                    queryTransitionProbabilityMatrix[i][columnNumber] = emptyTableValue;
                }
            } else {
                // else if there is at least one outgoing query transition probability,
                // assign delta value + p * (a query transition probability to the row) to the rows.
                Iterator<QueryTransitionProbability> transitionProbabilityIterator =
                        queryTransitionProbabilities.iterator();
                while (transitionProbabilityIterator.hasNext()) {
                    QueryTransitionProbability queryTransitionProbability = transitionProbabilityIterator.next();
                    // find a row number of the selected transient query.
                    int rowNumber = queryIdAndIndexNumberMap.get(queryTransitionProbability.getNextQueryId());
                    // add p * (a query transition probability to the row) to each corresponding row.
                    queryTransitionProbabilityMatrix[rowNumber][columnNumber] =
                            roundUpForMiddleSteps((VALUE_P * queryTransitionProbability.getProbability()) + delta);
                }
            }
        }
        return queryTransitionProbabilityMatrix;
    }

    /**
     * Round up the double value to the dedicated decimal place to presentation.
     * DECIMAL_PLACE is the place it needs to round up.
     *
     * @param value the value to calculate.
     * @return the rounded up double value.
     */
    private double roundUpForFinalPresentation(double value) {
        return Math.round(value * DECIMAL_PLACE) / DECIMAL_PLACE;
    }

    /**
     * Round up for middle steps.
     *
     * @param value the value to calculate.
     * @return the rounded up double value.
     */
    private double roundUpForMiddleSteps(double value) {
        return Math.round(value * MIDDLE_STEP_DECIMAL_PLACE) / MIDDLE_STEP_DECIMAL_PLACE;
    }

    /**
     * Apply the Markov chain calculation until it reaches to the steady-state vectors idea to produce
     * the query query Probability Table.
     * <p>
     *     For mitigation, if the iteration is over 100, stop the calculation.
     * </p>
     *
     * @param queryTransitionProbabilityMatrix the query Transition PROBABILITY Matrix.
     * @return the steady-state vector (ideally).
     */
    public double[] applyMarkovChain(double[][] queryTransitionProbabilityMatrix) {

        int matrixLength = queryTransitionProbabilityMatrix.length;
        double[] previousVector = new double[matrixLength];
        // populate the initial value for the vector as 1 / the length of one side of the matrix (the matrix is square).
        // This means that the probabilities to transit to different queries are all the same.
        for (int i = 0; i < matrixLength; i++) {
            previousVector[i] = roundUpForMiddleSteps(1.0 / matrixLength);
        }

        double[] resultVector;
        boolean isSteadyStateVector;
        int k = 0;
        while (true) {
            resultVector = new double[matrixLength];
            isSteadyStateVector = true;
            // a multiplication.
            for (int i = 0; i < matrixLength; i++) {
                for (int j = 0; j < matrixLength; j++) {
                    resultVector[i] += queryTransitionProbabilityMatrix[i][j] * previousVector[j];
                    resultVector[i] = roundUpForMiddleSteps(resultVector[i]);
                }
            }
            k++;
            // check whether the result vector is the same as the previous vector.
            for (int i = 0; i < matrixLength; i++) {
                if (previousVector[i] != resultVector[i]) {
                    isSteadyStateVector = false;
                }
            }
            // If the result vector is the steady state vector, stop calculation.
            if (isSteadyStateVector) {
                break;
            } else {
                previousVector = resultVector;
            }
            // Mitigation, if the markov chain is longer than 100 iteration, stop.
            if (k > 100) {
                break;
            }
            System.out.println(k);
        }
        // Round up at certain decimal point.
        for (int i = 0; i < matrixLength; i++) {
            resultVector[i] = roundUpForFinalPresentation(resultVector[i]);
        }
        System.out.println("Iteration time to calculate the steady vector : " + k);
        return resultVector;
    }

    /**
     * Update the query Probabilities Table for the user.
     *
     * @param tableName                  a query Probability table name related to the user.
     * @param queryIdAndIndexNumberMap a mapper between the steadyStateVector and query IDs.
     * @param steadyStateVector          steady-state vector.
     */
    public void updateQueryProbabilitiesTable(String tableName,
                                              Map<String, Integer> queryIdAndIndexNumberMap,
                                              double[] steadyStateVector) {
        Map<String, Double> queryProbabilities = new TreeMap<>();
        Iterator<String> iterator = queryIdAndIndexNumberMap.keySet().iterator();
        while (iterator.hasNext()) {
            String queryId = iterator.next();
            double probability = steadyStateVector[queryIdAndIndexNumberMap.get(queryId)];
            queryProbabilities.put(queryId, probability);
        }
        queryProbabilityDao.updateAllQueryProbabilities(tableName, queryProbabilities);
    }
}
