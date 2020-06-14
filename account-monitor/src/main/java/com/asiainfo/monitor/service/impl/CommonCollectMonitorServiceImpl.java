package com.asiainfo.monitor.service.impl;

import com.asiainfo.monitor.constant.Constants;
import com.asiainfo.monitor.entity.CatchFileUpload;
import com.asiainfo.monitor.entity.ChargingDownloadConfig;
import com.asiainfo.monitor.entity.ChargingHostInfo;
import com.asiainfo.monitor.mapper.AccountMonitorMapper;
import com.asiainfo.monitor.service.AccountMonitorService;
import com.asiainfo.monitor.service.CommonCollectMonitorService;
import com.asiainfo.monitor.util.ConnectDBUtil;
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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class CommonCollectMonitorServiceImpl implements CommonCollectMonitorService {

    private static final Logger LOG = LoggerFactory.getLogger(CommonCollectMonitorServiceImpl.class);

    @Autowired
    private AccountMonitorMapper accountMonitorMapper;

    @Autowired
    private ConnectDBUtil connectDBUtil;

    /**
     * ftp方式通用监控
     */
    @Override
    public void ftpCollectMonitor(){
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        LOG.info("正在监控计费的FTP方式上传文件,时间-->" + date);
        List<ChargingDownloadConfig> chargingDownloadConfigs = accountMonitorMapper.queryFTPCollectInfo();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 2, 120, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for(int i=0;i<chargingDownloadConfigs.size();i++){
            int flag = i;
            threadPool.execute(()-> {
                try {
                    ChargingHostInfo chargingHostInfo = accountMonitorMapper.queryChargingHostInfoByHost(chargingDownloadConfigs.get(flag).getHostCenter());
                    int num = 0;//符合时间限制和匹配规则的文件，实际文件的生成量
                    String querySql = chargingDownloadConfigs.get(flag).getQuerySql();
                    String uploadFrequency = chargingDownloadConfigs.get(flag).getUploadFrequency();
                    String hostDir = chargingDownloadConfigs.get(flag).getHostDir();
                    String ip = chargingDownloadConfigs.get(flag).getIp();
                    String sideQuerySql = chargingDownloadConfigs.get(flag).getSideQuerySql();
                    String fileName = chargingDownloadConfigs.get(flag).getFileDescription();
                    String fileRegular = chargingDownloadConfigs.get(flag).getFileNameRegular();
                    String hostCenter = chargingDownloadConfigs.get(flag).getHostCenter();
                    String diffRightNum = chargingDownloadConfigs.get(flag).getSideDatabaseInfo();//差别的合理区间
                    //ftp连接信息
                    String host = chargingHostInfo.getHostIp();
                    String username = chargingHostInfo.getUsername();
                    String password = chargingHostInfo.getPassword();
                    FtpUtil ftpUtil;
                    //连接ftp
                    ftpUtil = FtpUtil.createFtpCli(host, username, password, hostDir);
                    ftpUtil.connect();
                    List<String> ftpDirs = ftpUtil.listFileNames(hostDir);
                    List<String> fileNames = new ArrayList<>();//接收符合条件的文件容器
                    for(int j=0;j<ftpDirs.size();j++){
                        if(ftpDirs.get(j).matches(fileRegular)){//是否符合匹配规则
                            String filePath = hostDir + ftpDirs.get(j);
                            if(uploadFrequency.charAt(0) == 'a'){
                                num++;
                                fileNames.add(ftpDirs.get(j));
                            }else {
                                if (ftpUtil.judgeTimeOutFitteenMinute(filePath)) {//是否符合时间限制
                                    num++;
                                    fileNames.add(ftpDirs.get(j));
                                }
                            }
                        }
                    }
                    Integer fileNum = accountMonitorMapper.queryFileNum(querySql);//应该生成的文件量
                    if(num >= fileNum){
                        List<String> sideFileNames = connectDBUtil.connSrDB(sideQuerySql);
                        List<String> diffFile = new ArrayList<>();//差异文件的容器
                        int diffNum = 0;
                        for(int k=0;k<fileNames.size();k++){
                            if(!sideFileNames.contains(fileNames.get(k))){
                                diffNum++;
                                diffFile.add(fileNames.get(k));
                            }
                        }
                        if(diffNum > Integer.parseInt(diffRightNum)){
                            String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            String msg = "计费监控提醒您："+ hostCenter +"异常！\n" +
                                    "告警内容为：源主机和对端主机文件存在数量差异。\n" +
                                    "时间：" + nowDate + "\n" +
                                    "源主机ip：" + ip + "\n" +
                                    "差异量：" + diffNum + "\n" +
                                    "差异文件：" + fileName + "\n" +
                                    "匹配规则：" + fileRegular + "\n" +
                                    "差异文件:" + diffFile.toString();
                            LOG.info(msg);
//                            SendMessage.sendHostCenter(hostCenter,msg);
                            SendMessage.sendMsg("10",msg);
                        }
                    }else{
                        String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        String msg = "计费监控提醒您："+ hostCenter +"文件生成异常！\n" +
                                "源主机ip：" + ip + "\n" +
                                "差异文件：" + fileName + "\n" +
                                "应生成量：" + fileNum + "\n" +
                                "实际生成量：" + num + "\n" +
                                "匹配规则：" + fileRegular + "\n" +
                                "时间：" + nowDate;
                        LOG.info(msg);
//                        SendMessage.sendHostCenter(hostCenter,msg);
                        SendMessage.sendMsg("10",msg);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 数据库方式通用监控
     */
    @Override
    public void dbCollectMonitor(){
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        LOG.info("正在监控计费的数据库方式上传文件,时间-->" + date);
        List<ChargingDownloadConfig> chargingDownloadConfigs = accountMonitorMapper.queryDBCollectInfo();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 2, 120, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for(int i=0;i<chargingDownloadConfigs.size();i++){
            int flag = i;
            threadPool.execute(()-> {
                int num = 0;//符合时间限制和匹配规则的文件，实际文件的生成量
                String querySql = chargingDownloadConfigs.get(flag).getQuerySql();
                String uploadFrequency = chargingDownloadConfigs.get(flag).getUploadFrequency();
                String hostDir = chargingDownloadConfigs.get(flag).getHostDir();
                String ip = chargingDownloadConfigs.get(flag).getIp();
                String sideQuerySql = chargingDownloadConfigs.get(flag).getSideQuerySql();
                String fileName = chargingDownloadConfigs.get(flag).getFileDescription();
                String fileRegular = chargingDownloadConfigs.get(flag).getFileNameRegular();
                String hostCenter = chargingDownloadConfigs.get(flag).getHostCenter();
                String rightNum = chargingDownloadConfigs.get(flag).getSideDatabaseInfo();
//                LOG.info(querySql);
                List<CatchFileUpload> fileNames = selectDBByHost(hostCenter,querySql);//接收符合条件的文件容器
                Integer fileNum = fileNames.size() > 0?Integer.parseInt(fileNames.get(0).getShouldNum()):0;//应该生成的文件量
                num = fileNames.size();
                if(num > 0){
                    if(num >= fileNum){
                        List<String> sideFileNames = connectDBUtil.connSrDB(sideQuerySql);
                        List<String> diffFile = new ArrayList<>();//差异文件的容器
                        int diffNum = 0;
                        for(int k=0;k<fileNames.size();k++){
                            if(!sideFileNames.contains(fileNames.get(k).getMergeFileName())){
                                diffNum++;
                                diffFile.add(fileNames.get(k).getMergeFileName());
                            }
                        }
                        if(diffNum > Integer.parseInt(rightNum)){
                            String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            String msg = "计费监控提醒您："+ hostCenter +"异常！\n" +
                                    "告警内容为：源主机和对端主机文件存在数量差异。\n" +
                                    "时间：" + nowDate + "\n" +
                                    "源主机ip：" + ip + "\n" +
                                    "差异量：" + diffNum + "\n" +
                                    "差异文件：" + fileName + "\n" +
                                    "匹配规则：" + fileRegular + "\n" +
                                    "差异文件:" + diffFile.toString();
                            LOG.info(msg);
//                            SendMessage.sendHostCenter(hostCenter,msg);
                            SendMessage.sendMsg("10",msg);
                        }
                    }else{
                        String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        String msg = "计费监控提醒您："+ hostCenter +"文件生成异常！\n" +
                                "源主机ip：" + ip + "\n" +
                                "差异文件：" + fileName + "\n" +
                                "应生成量：" + fileNum + "\n" +
                                "实际生成量：" + num + "\n" +
                                "匹配规则：" + fileRegular + "\n" +
                                "时间：" + nowDate;
                        LOG.info(msg);
//                        SendMessage.sendHostCenter(hostCenter,msg);
                        SendMessage.sendMsg("10",msg);
                    }
                }
            });
        }
    }


    /**
     * 根据host选择数据源
     */
    public List<CatchFileUpload> selectDBByHost(String hostCenter,String querySql){
        List<CatchFileUpload> catchFileUploads = new ArrayList<>();
        switch (hostCenter){
            case "采预中心":
                catchFileUploads = connectDBUtil.connGathAcctDB(querySql);
                break;
            case "佣金":
                catchFileUploads = connectDBUtil.connCommission(querySql);
                break;
            case "消息省中心":
                catchFileUploads = connectDBUtil.connABMDB(querySql);
                break;
        }
        return catchFileUploads;
    }
}
