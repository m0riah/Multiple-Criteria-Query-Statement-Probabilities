/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.qpc.controller;

import org.mqpbms.qpc.service.QueryProbabilityCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * A root controller for query probability calculator application.
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    @Qualifier("QctnBasedQueryProbabilityCalculationService")
    QueryProbabilityCalculationService qctnBasedQueryProbabilityCalculationService;

    @Autowired
    @Qualifier("OrcnBasedQueryProbabilityCalculationService")
    QueryProbabilityCalculationService orcnBasedQueryProbabilityCalculationService;

    /**
     * Get Controller to show the index.jsp page.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Multi-Criteria Query Probabilities Based Monitoring System: Query Probability Calculator");
        return "index";
    }

    /**
     * Post Controller to get the query probability calculation request.
     *
     * @return the index.jsp page.
     */
    @RequestMapping(method = RequestMethod.POST)
    public String updateAllGlobalTransactionPatternProbabilities() {
        qctnBasedQueryProbabilityCalculationService.updateQueryProbabilitiesForAllUser();
        orcnBasedQueryProbabilityCalculationService.updateQueryProbabilitiesForAllUser();
        return "index";
    }
}