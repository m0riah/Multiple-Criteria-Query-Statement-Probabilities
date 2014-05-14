/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.monitor.controller;

import org.mqpbms.logger.models.PbqLog;
import org.mqpbms.logger.repositories.PbqLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

/**
 * QCTN based Probability Based Query Log API controller
 *
 * @author sky
 * @version 3/3/14
 */
@Controller
@RequestMapping("api/qctn")
public class QctnPbqLogApiController {

    @Autowired
    @Qualifier("QctnBasedPbqLogDao")
    PbqLogDao pbqLogDao;

    @RequestMapping(value = "pbqlogs", method = RequestMethod.GET, produces="application/json;charset=utf-8")
    public @ResponseBody
    Collection<PbqLog> getPbqLogsWithAfterParam(@RequestParam(value = "start") long startTime
            , @RequestParam(value = "end") long endTime, @RequestParam(value = "userName") String userName) {
        System.out.println(startTime + " " + endTime);
        String tableName = pbqLogDao.generateTableName(userName);
        return pbqLogDao.getLogsBetweenTimestamps(tableName, startTime, endTime);
    }
}
