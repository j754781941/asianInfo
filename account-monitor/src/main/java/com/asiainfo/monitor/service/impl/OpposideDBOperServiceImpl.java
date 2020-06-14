package com.asiainfo.monitor.service.impl;

import com.asiainfo.monitor.config.DS;
import com.asiainfo.monitor.mapper.AccountMonitorMapper;
import com.asiainfo.monitor.service.OpposideDBOperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OpposideDBOperServiceImpl implements OpposideDBOperService {

    @Autowired
    private AccountMonitorMapper accountMonitorMapper;

    @Override
    @DS("db3")
    public List<String> querySideWhetherDifferenceFile(String sideQuerySql) {
        return accountMonitorMapper.querySideWhetherDifferenceFile(sideQuerySql);
    }
}
