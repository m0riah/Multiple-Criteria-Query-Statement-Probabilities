/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.monitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ORCN based Statistical Query Probabilities Time Series Graph Controller.
 *
 * @author sky
 * @version 3/3/14
 */
@Controller
@RequestMapping("/orcn")
public class OrcnRenderingController {

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Multi-Criteria Query Probabilities Based Monitoring System: Monitor");
        return "orcn";
    }
}
