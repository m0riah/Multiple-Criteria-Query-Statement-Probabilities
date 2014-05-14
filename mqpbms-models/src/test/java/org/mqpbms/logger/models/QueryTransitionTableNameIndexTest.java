/*
 * Multiple-Criteria Transaction Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mtpbms-parent
 * User: sky
 * Date: 2/18/14
 */
package org.mqpbms.logger.models;

import org.junit.Test;
import org.mqpbms.common.models.PreprocessAlgorithm;

/**
 * Class description is required.
 *
 * @author sky
 * @version 2/18/14
 */
public class QueryTransitionTableNameIndexTest {


    @Test
    public void testGetObjectHavingRandomTransitionTableName() throws Exception {
        TransitionTableNameIndex orcnTransitionTableNameIndex =
                TransitionTableNameIndex.getObjectHavingRandomTransitionTableName("test", PreprocessAlgorithm.ORCN);
        System.out.println(orcnTransitionTableNameIndex);
        TransitionTableNameIndex signTransitionTableNameIndex =
                TransitionTableNameIndex.getObjectHavingRandomTransitionTableName("test2", PreprocessAlgorithm.QCTN);
        System.out.println(signTransitionTableNameIndex);
    }
}
