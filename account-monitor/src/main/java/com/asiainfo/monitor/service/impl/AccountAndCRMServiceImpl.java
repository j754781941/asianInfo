package com.asiainfo.monitor.service.impl;

import com.asiainfo.monitor.config.DS;
import com.asiainfo.monitor.entity.ACheckResult;
import com.asiainfo.monitor.mapper.AccountAndCRMMapper;
import com.asiainfo.monitor.service.AccountAndCRMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountAndCRMServiceImpl implements AccountAndCRMService {

    @Autowired
    private AccountAndCRMMapper accountAndCRMMapper;



    @Override
    @DS("db2")
    public List<ACheckResult> queryACheckResults() {
        return accountAndCRMMapper.queryACheckResults();
    }


}
