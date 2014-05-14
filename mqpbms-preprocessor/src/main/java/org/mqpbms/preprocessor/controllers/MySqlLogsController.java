/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.preprocessor.controllers;

import org.mqpbms.logsender.models.MySqlGeneralLog;
import org.mqpbms.preprocessor.service.PreprocessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

/**
 * This API controller is dedicated only to get General Logs from MySQL DB.
 * <p>
 * This controllers gets a post request with JSON objects having the list of MySQL General Log objects.
 * </p>
 *
 * @author sky
 * @version 1/25/14
 */
@Controller
@RequestMapping("api")
public class MySqlLogsController {

    /**
     * PreprocesserService to process coming mysql log.
     */
    @Autowired
    @Qualifier("MySqlPreprocessorService")
    private PreprocessorService preprocessorService;

    /**
     * MySQL General Logs post controller.
     *
     * @param jsons a collection of {@link org.mqpbms.logsender.models.MySqlGeneralLog}s in json format.
     * @return http 201 "created"
     */
    @RequestMapping(value = "mysqlLogs", method = RequestMethod.POST)
    public ResponseEntity<String> postMySqlLogs(@RequestBody String jsons) {
        Collection<MySqlGeneralLog> mySqlGeneralLogs = MySqlGeneralLog.fromJsonArrayToGeneralLogs(jsons);
        preprocessorService.processAndSend(mySqlGeneralLogs);
//        Debug purpose.
        for (MySqlGeneralLog mySqlGeneralLog : mySqlGeneralLogs) {
            System.out.println(mySqlGeneralLog);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

}
