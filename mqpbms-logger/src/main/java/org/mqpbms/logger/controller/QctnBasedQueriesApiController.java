/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.controller;

import org.mqpbms.logger.service.QctnBasedQueryLoggingService;
import org.mqpbms.preprocessor.models.ExtendedQctnBasedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

/**
 * Query Command and Table Names (QCTN) based query API controller.
 *
 * @author sky
 * @version 2/12/14
 */
@Controller
@RequestMapping("api")
public class QctnBasedQueriesApiController {

    /**
     * {@link org.mqpbms.preprocessor.models.ExtendedQctnBasedQuery}.
     */
    @Autowired
    @Qualifier("QctnBasedQueryLoggingServiceImpl")
    private QctnBasedQueryLoggingService qctnBasedQueryLoggingService;

    /**
     * Accept a Collection of {@link org.mqpbms.preprocessor.models.ExtendedQctnBasedQuery} JSON file and
     * log the data using {@link org.mqpbms.logger.service.QctnBasedQueryLoggingService}.
     *
     * @return the result of logging.
     */
    @RequestMapping(value = "qctnBasedQueries", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<String> postProcessedLogList(@RequestBody String jsons) {
        Collection<ExtendedQctnBasedQuery> extendedQctnBasedQueries =
                ExtendedQctnBasedQuery.toExtendedQctnBasedQueries(jsons);
        // For debugging purpose.
        int i = 0;
        for (ExtendedQctnBasedQuery extendedQctnBasedQuery : extendedQctnBasedQueries) {
            System.out.println(i + " : " + extendedQctnBasedQuery);
            i++;
        }
        qctnBasedQueryLoggingService.logQctnBasedQueries(extendedQctnBasedQueries);
        // To return the "201 Created" response.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }
}
