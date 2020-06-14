package com.asiainfo.monitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.monitor.config.DS;
import com.asiainfo.monitor.constant.Constants;
import com.asiainfo.monitor.controller.MonitorController;
import com.asiainfo.monitor.entity.CatchFileUpload;
import com.asiainfo.monitor.entity.ChargingDownloadConfig;
import com.asiainfo.monitor.entity.WarningCallConfig;
import com.asiainfo.monitor.mapper.AccountMonitorMapper;
import com.asiainfo.monitor.service.AbmQueryService;
import com.asiainfo.monitor.service.AccountMonitorService;
import com.asiainfo.monitor.service.OpposideDBOperService;
import com.asiainfo.monitor.util.FtpUtil;
import com.asiainfo.monitor.util.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 账务直采监控
 */
@Service
public class AccountMonitorServiceImpl implements AccountMonitorService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountMonitorServiceImpl.class);

    @Autowired
    private AccountMonitorMapper accountMonitorMapper;

    @Autowired
    private OpposideDBOperService opposideDBOperService;

    @Autowired
    private AbmQueryService abmQueryService;



    /*匹配规范
    账务监控
    a1 ：每天十点扫描
    a2 ：每月 1 号 10 点扫描
    a3 ：每月 6 号 10 点扫描
    a4 ：每月 5 15 25 号 10 点扫描
    a5 ：每月 10 号9 点扫描
    */
    @DS("db")
    @Override
    public void MonitorTenByDay(String uploadFrequency){
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        LOG.info("正在监控计费的账务直采,时间-->" + date);
        List<ChargingDownloadConfig> chargingDownloadConfigs = accountMonitorMapper.queryChargingDownloadConfigByDay(uploadFrequency);
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 8, 120, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for(int i=0;i<chargingDownloadConfigs.size();i++){
            int flag = i;
            threadPool.execute(()->{
                String querySql = chargingDownloadConfigs.get(flag).getQuerySql();
                String ip = chargingDownloadConfigs.get(flag).getIp();
                String sideQuerySql = chargingDownloadConfigs.get(flag).getSideQuerySql();
                String fileName = chargingDownloadConfigs.get(flag).getFileDescription();
                String fileRegular = chargingDownloadConfigs.get(flag).getFileNameRegular();
                String hostCenter = chargingDownloadConfigs.get(flag).getHostCenter();
                String diffRightNum = chargingDownloadConfigs.get(flag).getSideDatabaseInfo();//差别的合理区间
                List<String> fileNum = accountMonitorMapper.queryAccountWhetherHaveFile(querySql);
                if( fileNum.size() > 0 ){
                    List<String> sideFileNum = opposideDBOperService.querySideWhetherDifferenceFile(sideQuerySql);
                    if((fileNum.size() + 1) != sideFileNum.size()){
                        int diffNum;
                        if("a3".equals(uploadFrequency)){
                            diffNum = Math.abs( sideFileNum.size() - fileNum.size());
                        }else{
                            diffNum = Math.abs( sideFileNum.size() - fileNum.size() + 1);
                        }
                        if(diffNum > Integer.parseInt(diffRightNum)){
                            String msg = "计费监控提醒您："+ hostCenter +"异常！\n" +
                                    "告警内容为：源主机和对端主机文件存在数量差异。\n" +
                                    "源主机ip：" + ip + "\n" +
                                    "差异量：" + diffNum + "\n" +
                                    "差异文件：" + fileName + "\n" +
                                    "匹配规则：" + fileRegular;
                            LOG.info(msg);
//                            SendMessage.sendHostCenter(hostCenter,msg);
                            SendMessage.sendMsg("10",msg);
                        }
                    }
                }else{
                    String msg = "计费监控提醒您：账务直采异常！\n" +
                            "告警内容为：源主机文件生成异常。\n" +
                            "ip:" + ip + "\n" +
                            "差异文件：" + fileName + "\n" +
                            "匹配规则：" + fileRegular;
                    LOG.info(msg);
//                    SendMessage.sendHostCenter(hostCenter,msg);
                    SendMessage.sendMsg("10",msg);
                }
            });
        }
    }

    /**
     * 充值中心和物联网FTP监控
     */
    @DS
    @Override
    public void ftpMonitor(String uploadFrequency){
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        LOG.info("正在监控计费的充值中心和账务中心物联网上传文件,时间-->" + date);
        List<ChargingDownloadConfig> chargingDownloadConfigs = accountMonitorMapper.queryChargingDownloadConfigByDay(uploadFrequency);
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 2, 120, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for(int i=0;i<chargingDownloadConfigs.size();i++){
            int flag = i;
            threadPool.execute(()-> {
                try {
                    int num = 0;//符合时间限制和匹配规则的文件，实际文件的生成量
                    String querySql = chargingDownloadConfigs.get(flag).getQuerySql();
                    String hostDir = chargingDownloadConfigs.get(flag).getHostDir();
                    String ip = chargingDownloadConfigs.get(flag).getIp();
                    String sideQuerySql = chargingDownloadConfigs.get(flag).getSideQuerySql();
                    String fileName = chargingDownloadConfigs.get(flag).getFileDescription();
                    String fileRegular = chargingDownloadConfigs.get(flag).getFileNameRegular();
                    String hostCenter = chargingDownloadConfigs.get(flag).getHostCenter();
                    String diffRightNum = chargingDownloadConfigs.get(flag).getSideDatabaseInfo();//差别的合理区间
                    FtpUtil ftpUtil;
                    //物联网和充值中心的开关
                    if(uploadFrequency.charAt(0) == 'c'){
                        ftpUtil = FtpUtil.createFtpCli(Constants.VC_HOST,Constants.VC_USERNAME,Constants.VC_PASSWORD,hostDir);
                    }else{
                        ftpUtil = FtpUtil.createFtpCli(Constants.IOT_HOST,Constants.IOT_USERNAME,Constants.IOT_PASSWORD,hostDir);
                    }
                    ftpUtil.connect();
                    List<String> ftpDirs = ftpUtil.listFileNames(hostDir);
                    for(int j=0;j<ftpDirs.size();j++){
//                        LOG.info("a---->"+ftpDirs.get(j));
                        if(ftpDirs.get(j).matches(fileRegular)){//是否符合匹配规则
                            String filePath = hostDir + ftpDirs.get(j);
//                            LOG.info("b---->"+uploadFrequency.charAt(0));
                            if(uploadFrequency.charAt(0) == 'a'){
                                num++;
                            }else {
                                if (ftpUtil.judgeTimeOutFitteenMinute(filePath)) {//是否符合时间限制
                                    num++;
                                }
                            }
                        }
                    }
                    Integer fileNum = accountMonitorMapper.queryFileNum(querySql);//应该生成的文件量
                    if(num >= fileNum){
                        List<String> sideFileNum = opposideDBOperService.querySideWhetherDifferenceFile(sideQuerySql);
                        if(sideFileNum.size() != num){
                            int diffNum = Math.abs( sideFileNum.size() - num );
                            if(diffNum > Integer.parseInt(diffRightNum)){
                                String msg = "计费监控提醒您："+ hostCenter +"异常！\n" +
                                        "告警内容为：源主机和对端主机文件存在数量差异。\n" +
                                        "源主机ip：" + ip + "\n" +
                                        "差异量：" + diffNum + "\n" +
                                        "差异文件：" + fileName + "\n" +
                                        "匹配规则：" + fileRegular;
                                LOG.info(msg);
//                                SendMessage.sendHostCenter(hostCenter,msg);
                                SendMessage.sendMsg("10",msg);
                            }
                        }
                    }else{
                        String msg = "计费监控提醒您："+ hostCenter +"文件生成异常！\n" +
                                "源主机ip：" + ip + "\n" +
                                "差异文件：" + fileName + "\n" +
                                "应生成量：" + fileNum + "\n" +
                                "实际生成量：" + num + "\n" +
                                "匹配规则：" + fileRegular;
                        LOG.info(msg);
//                        SendMessage.sendHostCenter(hostCenter,msg);
                        SendMessage.sendMsg("10",msg);
                    }
                    ftpUtil.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    /**
     * 外呼告警开关
     */
    @Override
    @DS
    public Integer updateWaringCallSwitch(Long warningId,Integer warningFlag){
        Integer flag = accountMonitorMapper.updateWaringCallSwitch(warningId,warningFlag);
        return flag;
    }

    @Override
    @DS
    public List<WarningCallConfig> queryAllWarningFlag() {
        return accountMonitorMapper.queryAllWarningFlag();
    }




    /**
     * 采预监控
     * b1 : 每10 分钟扫描
     * b2 : 每 5 分钟扫描
     * b3 ：每 15 分钟扫描
     */
    @DS
    @Override
    public void catchFileMonitor(String uploadFrequency){
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        LOG.info("正在监控计费的采预中心文件上传,时间-->" + date);
        List<ChargingDownloadConfig> chargingDownloadConfigs = accountMonitorMapper.queryChargingDownloadConfigByDay(uploadFrequency);
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 8, 120, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
//        LOG.info("ChargingDownloadConfig的大小：" + chargingDownloadConfigs.size());
        for(int i=0;i<chargingDownloadConfigs.size();i++){
            int flag = i;
            threadPool.execute(()->{
                String querySql = chargingDownloadConfigs.get(flag).getQuerySql();
                String ip = chargingDownloadConfigs.get(flag).getIp();
                String sideQuerySql = chargingDownloadConfigs.get(flag).getSideQuerySql();
                String fileName = chargingDownloadConfigs.get(flag).getFileDescription();
                String fileRegular = chargingDownloadConfigs.get(flag).getFileNameRegular();
                String hostCenter = chargingDownloadConfigs.get(flag).getHostCenter();
                String diffRightNum = chargingDownloadConfigs.get(flag).getSideDatabaseInfo();//差别的合理区间
                List<CatchFileUpload> fileNum = accountMonitorMapper.queryWhetherHaveFile(querySql);
                if(fileNum.size() > 0){
                    if(fileNum.size() >= Integer.parseInt(fileNum.get(0).getShouldNum())){
                        List<String> sideFileNum = opposideDBOperService.querySideWhetherDifferenceFile(sideQuerySql);
                        if(fileNum.size() != sideFileNum.size()){
                            int diffNum = Math.abs(sideFileNum.size() - fileNum.size());
                            if(diffNum > Integer.parseInt(diffRightNum)){
                                if(!"漫入地入网话单".equals(fileName)){
                                    String msg = "计费监控提醒您："+ hostCenter +"异常！\n" +
                                            "告警内容为：源主机和对端主机文件存在数量差异。\n" +
                                            "源主机ip：" + ip + "\n" +
                                            "差异量：" + diffNum + "\n" +
                                            "差异文件：" + fileName + "\n" +
                                            "匹配规则：" + fileRegular;
                                    LOG.info(msg);
//                                    SendMessage.sendHostCenter(hostCenter,msg);
                                    SendMessage.sendMsg("10",msg);
                                }
                            }
                        }
                    }else{
                        String msg = "计费监控提醒您：采预文件生成异常！\n" +
                                "源主机ip：" + ip + "\n" +
                                "差异文件：" + fileName + "\n" +
                                "应生成量：" + fileNum.get(0).getShouldNum() + "\n" +
                                "实际生成量：" + fileNum.size() + "\n" +
                                "匹配规则：" + fileRegular;
                        LOG.info(msg);
//                        SendMessage.sendHostCenter(hostCenter,msg);
                        SendMessage.sendMsg("10",msg);
                    }
                }
            });
        }
    }

    /**
     * 消息省中心匹配规范
     * d1：每小时一个
     * d2：每十五分钟一个
     * d3：每月
     */
    @DS
    @Override
    public void proMessageCneter(String uploadFrequency){
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        LOG.info("正在监控计费的消息省中心文件上传,时间-->" + date);
        List<ChargingDownloadConfig> chargingDownloadConfigs = accountMonitorMapper.queryChargingDownloadConfigByDay(uploadFrequency);
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3, 5, 120, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for(int i=0;i<chargingDownloadConfigs.size();i++){
            int flag = i;
            threadPool.execute(()->{
                String querySql = chargingDownloadConfigs.get(flag).getQuerySql();
                String ip = chargingDownloadConfigs.get(flag).getIp();
                String sideQuerySql = chargingDownloadConfigs.get(flag).getSideQuerySql();
                String fileName = chargingDownloadConfigs.get(flag).getFileDescription();
                String fileRegular = chargingDownloadConfigs.get(flag).getFileNameRegular();
                String hostCenter = chargingDownloadConfigs.get(flag).getHostCenter();
                String diffRightNum = chargingDownloadConfigs.get(flag).getSideDatabaseInfo();//差别的合理区间
                List<CatchFileUpload> fileNum = abmQueryService.queryMessageProv(querySql);
                if(fileNum.size() > 0){
                    if(fileNum.size() >= Integer.parseInt(fileNum.get(0).getShouldNum())){
                        List<String> sideFileNum = opposideDBOperService.querySideWhetherDifferenceFile(sideQuerySql);
                        if(fileNum.size() != sideFileNum.size()){
                            int diffNum = Math.abs(sideFileNum.size() - fileNum.size());
                            if(diffNum > Integer.parseInt(diffRightNum)){
                                String msg = "计费监控提醒您："+ hostCenter +"异常！\n" +
                                        "告警内容为：源主机和对端主机文件存在数量差异。\n" +
                                        "源主机ip：" + ip + "\n" +
                                        "差异量：" + diffNum + "\n" +
                                        "差异文件：" + fileName + "\n" +
                                        "匹配规则：" + fileRegular;
                                LOG.info(msg);
//                                SendMessage.sendHostCenter(hostCenter,msg);
                                SendMessage.sendMsg("10",msg);
                            }
                        }
                    }else{
                        int diffNum = Math.abs(fileNum.size() - Integer.parseInt(fileNum.get(0).getShouldNum()));
                        if(diffNum > 1){
                            String msg = "计费监控提醒您：消息省中心文件生成异常！\n" +
                                    "源主机ip：" + ip + "\n" +
                                    "差异文件：" + fileName + "\n" +
                                    "应生成量：" + fileNum.get(0).getShouldNum() + "\n" +
                                    "实际生成量：" + fileNum.size() + "\n" +
                                    "匹配规则：" + fileRegular;
                            LOG.info(msg);
//                            SendMessage.sendHostCenter(hostCenter,msg);
                            SendMessage.sendMsg("10",msg);
                        }
                    }
                }
            });
        }
    }

    /**
     * 佣金系统监控
     * 每月月底最后一天24点前上传
     * e1：（按月）每月月底最后一天24点前上传
     */
    @DS("db")
    @Override
    public void monitorGoldByDay(String uploadFrequency){
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        LOG.info("正在监控计费的佣金系统上传文件,时间-->" + date);
        List<ChargingDownloadConfig> chargingDownloadConfigs = accountMonitorMapper.queryChargingDownloadConfigByDay(uploadFrequency);
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 2, 120, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for(int i=0;i<chargingDownloadConfigs.size();i++){
            int flag = i;
            threadPool.execute(()->{
                String querySql = chargingDownloadConfigs.get(flag).getQuerySql();
                String ip = chargingDownloadConfigs.get(flag).getIp();
                String sideQuerySql = chargingDownloadConfigs.get(flag).getSideQuerySql();
                String fileName = chargingDownloadConfigs.get(flag).getFileDescription();
                String fileRegular = chargingDownloadConfigs.get(flag).getFileNameRegular();
                String hostCenter = chargingDownloadConfigs.get(flag).getHostCenter();
                String diffRightNum = chargingDownloadConfigs.get(flag).getSideDatabaseInfo();//差别的合理区间
                List<String> fileNum = abmQueryService.queryGlodDB(querySql);
                if( fileNum.size() > 0 ){
                    List<String> sideFileNum = opposideDBOperService.querySideWhetherDifferenceFile(sideQuerySql);
                    if((fileNum.size() + 1) != sideFileNum.size()){
                        int diffNum = Math.abs( sideFileNum.size() - fileNum.size() + 1);
                        if(diffNum > Integer.parseInt(diffRightNum)){
                            String msg = "计费监控提醒您："+ hostCenter +"异常！\n" +
                                    "告警内容为：源主机和对端主机文件存在数量差异。\n" +
                                    "源主机ip：" + ip + "\n" +
                                    "差异量：" + diffNum + "\n" +
                                    "差异文件：" + fileName + "\n" +
                                    "匹配规则：" + fileRegular;
                            LOG.info(msg);
//                            SendMessage.sendHostCenter(hostCenter,msg);
                            SendMessage.sendMsg("10",msg);
                        }
                    }
                }else{
                    String msg = "计费监控提醒您：佣金系统上传文件异常！\n" +
                            "告警内容为：源主机文件未生成。\n" +
                            "ip:" + ip + "\n" +
                            "差异文件：" + fileName + "\n" +
                            "匹配规则：" + fileRegular;
                    LOG.info(msg);
//                    SendMessage.sendHostCenter(hostCenter,msg);
                    SendMessage.sendMsg("10",msg);
                }
            });
        }
    }
}
