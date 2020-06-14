package com.asiainfo.monitor.service.impl;

import com.asiainfo.monitor.config.DS;
import com.asiainfo.monitor.constant.Constants;
import com.asiainfo.monitor.entity.CatchFileUpload;
import com.asiainfo.monitor.mapper.AccountMonitorMapper;
import com.asiainfo.monitor.service.AbmQueryService;
import com.asiainfo.monitor.util.FtpUtil;
import com.asiainfo.monitor.util.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AbmQueryServiceImpl implements AbmQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(AbmQueryService.class);

    @Autowired
    private AccountMonitorMapper accountMonitorMapper;

    /**
     * 查询abm汇聚库
     */
    @DS("db4")
    @Override
    public List<CatchFileUpload> queryMessageProv(String querySql){
//        LOG.info("querySql---->"+querySql);
        return accountMonitorMapper.queryMessageProv(querySql);
    }

    @DS("db5")
    @Override
    public List<String> queryGlodDB(String querySql){
        return accountMonitorMapper.queryGlodDB(querySql);
    }

    /**
     * sr去激活，请关注！
     */
    public void srActivationMonitor(){
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            LOG.info("正在监控计费的SR网络情况,时间-->" + date);
            FtpUtil ftpUtil = FtpUtil.createFtpCli(Constants.SR_HOST,Constants.SR_USERNAME,Constants.SR_PASSWORD,Constants.SR_DIR1);
            ftpUtil.connect();
            List<String> ftpDirs = ftpUtil.listFileNames(Constants.SR_DIR1);
            Integer num = 0;//异常文件量
            for(int i=0;i<ftpDirs.size();i++){
                if(ftpUtil.judgeTimeTwoMinute(Constants.SR_DIR1 + ftpDirs.get(i))){
                    num++;
                }
            }
            try {
                Thread.sleep(10 * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ftpDirs = ftpUtil.listFileNames(Constants.SR_DIR2);
            for(int i=0;i<ftpDirs.size();i++){
                if(ftpUtil.judgeTimeTwoMinute(Constants.SR_DIR2 + ftpDirs.get(i))){
                    num++;
                }
            }
            LOG.info("num-->"+ num);
            if(num > 0){
                String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String msg = "sr去激活，请关注！\n" +
                        "时间：" + nowDate + "\n";
                SendMessage.sendMsg("29",msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
