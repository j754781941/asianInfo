package com.asiainfo.monitor.controller;

import com.asiainfo.monitor.entity.ACheckResult;
import com.asiainfo.monitor.mapper.AccountAndCRMMapper;
import com.asiainfo.monitor.service.AccountAndCRMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/check")
public class AccountAndCRMCheckController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountAndCRMCheckController.class);

    @Autowired
    private AccountAndCRMService accountAndCRMService;

    /**
     *
     * @return
     */
    @RequestMapping("/accountCheck")
    @ResponseBody
    public Map<String,String> accountCheck(){
        List<ACheckResult> aCheckResults = accountAndCRMService.queryACheckResults();
        Map<String,String> map = new HashMap<>();
        map.put("checkCycle",aCheckResults.get(0).getCheckCycle());
        return map;
    }
}
