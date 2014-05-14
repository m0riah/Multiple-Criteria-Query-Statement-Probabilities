/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.controller;

import org.mqpbms.logger.service.OrcnBasedQueryLoggingService;
import org.mqpbms.preprocessor.models.ExtendedOrcnBasedQuery;
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
 * Operation Related Column Names (ORCN) based query API controller.
 *
 * @author sky
 * @version 2/12/14
 */
@Controller
@RequestMapping("api")
public class OrcnBasedQueriesApiController {

    /**
     * {@link org.mqpbms.preprocessor.models.ExtendedOrcnBasedQuery}.
     */
    @Autowired
    @Qualifier("OrcnBasedQueryLoggingServiceImpl")
    private OrcnBasedQueryLoggingService orcnBasedQueryLoggingService;

    /**
     * Accept a Collection of {@link org.mqpbms.preprocessor.models.ExtendedOrcnBasedQuery} JSON file and
     * log the data using {@link org.mqpbms.logger.service.OrcnBasedQueryLoggingService}.
     *
     * @return the result of logging.
     */
    @RequestMapping(value = "orcnBasedQueries", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<String> postProcessedLogList(@RequestBody String jsons) {
        Collection<ExtendedOrcnBasedQuery> extendedORCNBasedQueries =
                ExtendedOrcnBasedQuery.toExtendedOrcnBasedQueries(jsons);
        // Debugging purpose.
        int i = 0;
        for (ExtendedOrcnBasedQuery extendedOrcnBasedQuery : extendedORCNBasedQueries) {
            System.out.println(i+ " : " + extendedOrcnBasedQuery);
            i++;
        }
        orcnBasedQueryLoggingService.logOrcnBasedQueries(extendedORCNBasedQueries);
        // To return the "201 Created" response.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }
}
