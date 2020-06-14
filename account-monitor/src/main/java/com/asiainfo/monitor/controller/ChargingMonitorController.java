package com.asiainfo.monitor.controller;

import com.asiainfo.monitor.entity.WarningCallConfig;
import com.asiainfo.monitor.service.AccountMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/charging")
public class ChargingMonitorController {

    private static final Logger LOG = LoggerFactory.getLogger(ChargingMonitorController.class);

    @Autowired
    private AccountMonitorService accountMonitorService;

    @RequestMapping("/warningCallSwitch")
    @ResponseBody
    public Map<String,Integer> warningCallSwitch(@RequestBody WarningCallConfig warningCallConfig){
        Map<String,Integer> map = new HashMap<>();
        Integer flag = accountMonitorService.updateWaringCallSwitch(warningCallConfig.getWarningId(), warningCallConfig.getWarningFlag());
        if(flag > 0){
            map.put("flag",200);
        }else{
            map.put("flag",500);
        }
        return map;
    }

    @RequestMapping("/queryAllWarningFlag")
    @ResponseBody
    public List<WarningCallConfig> queryAllWarningFlag(){
        List<WarningCallConfig> warningCallConfigs = accountMonitorService.queryAllWarningFlag();
        return warningCallConfigs;
    }
}
