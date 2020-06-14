package com.asiainfo.monitor.util;

import com.asiainfo.monitor.config.DS;
import com.asiainfo.monitor.constant.Constants;
import com.asiainfo.monitor.entity.CatchFileUpload;
import com.asiainfo.monitor.mapper.AccountMonitorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConnectDBUtil {

    @Autowired
    private AccountMonitorMapper accountMonitorMapper;

    /**
     * 连接汇聚库
     */
    @DS(Constants.GATH_ACCTDB)
    public List<CatchFileUpload> connGathAcctDB(String querySql){
        return accountMonitorMapper.commQuery(querySql);
    }

    /**
     * 连接对端数据库
     */
    @DS(Constants.SR_DB)
    public List<String> connSrDB(String sideQuerySql){
        return accountMonitorMapper.queryFileNumByFileName(sideQuerySql);
    }

    /**
     * 连接ABM汇聚库
     */
    @DS(Constants.ABM_DB)
    public List<CatchFileUpload> connABMDB(String querySql){
        return accountMonitorMapper.commQuery(querySql);
    }

    /**
     * 连接账务teleDB数据源
     */
//    @DS(Constants.ACCOUNT_ACCTDB)
//    public List<String> connAccountDB(String querySql){
//        return accountMonitorMapper.commQuery(querySql);
//    }

    /**
     * 连接佣金系统数据源
     */
    @DS(Constants.COMMISSION)
    public List<CatchFileUpload> connCommission(String querySql){
        return accountMonitorMapper.commQuery(querySql);
    }
}
