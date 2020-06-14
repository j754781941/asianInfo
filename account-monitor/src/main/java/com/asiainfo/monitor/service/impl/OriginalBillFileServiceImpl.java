package com.asiainfo.monitor.service.impl;

import com.asiainfo.monitor.config.DS;
import com.asiainfo.monitor.constant.Constants;
import com.asiainfo.monitor.mapper.AccountMonitorMapper;
import com.asiainfo.monitor.mapper.MonitorMapper;
import com.asiainfo.monitor.service.OriginalBillFileService;
import com.asiainfo.monitor.util.FtpUtil;
import com.asiainfo.monitor.util.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class OriginalBillFileServiceImpl implements OriginalBillFileService {

    private static final Logger LOG = LoggerFactory.getLogger(OriginalBillFileServiceImpl.class);

    @Value("${bill.threshold.num}")
    private String BillThresholdNum;

    @Autowired
    private MonitorMapper monitorMapper;

    @Autowired
    private AccountMonitorMapper accountMonitorMapper;

    /**
     * 采预监控，监控对端主机的挤压量
     */
    @Override
    @DS
    public void queryFileDirs() {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 8, 120, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        Properties properties = new Properties();
        try {
            //读取properties文件
            Integer num = Integer.parseInt(BillThresholdNum);
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("bill.properties"));
            Enumeration<Object> keys = properties.keys();
            while (keys.hasMoreElements()){
                String cfg = String.valueOf(keys.nextElement());
                String[] ftpInfo = String.valueOf(properties.get(cfg)).split("\\+");
                String host = ftpInfo[0];
                String ftpBasePath = ftpInfo[1];
                String regex = ftpInfo[2];
                String username = ftpInfo[3];
                String password = ftpInfo[4];
                //建立异步线程准备开始扫描主机
                threadPool.execute(()->{
                    FtpUtil ftpUtil = FtpUtil.createFtpCli(host,username,password,ftpBasePath);
                    Integer flag = 0;
                    try {
                        ftpUtil.connect();
                        List<String> ftpDirs = ftpUtil.listFileNames(ftpBasePath);
                        for(int i=0;i<ftpDirs.size();i++){
                            String filePath = null;
                            if(ftpBasePath.equals("/")){
                                filePath = "/" + ftpDirs.get(i);
                            }else{
                                filePath = ftpBasePath + "/" + ftpDirs.get(i);
                            }
                            long threshold = 60 * 60 * 1000 * 9;
                            if(host.equals("137.1.11.54")){
                                threshold = 60 * 60 * 1000 * 12;
                                if(ftpUtil.judgeTimeOut(threshold,filePath)){
                                    if(ftpDirs.get(i).matches(regex)) {
                                        flag++;
                                    }
                                }
                            }else{
                                if(ftpUtil.judgeTimeOut(threshold,filePath)){
                                    if(ftpDirs.get(i).matches(regex)) {
                                        flag++;
                                    }
                                }
                                if(ftpUtil.judgeTimeOutSevenHour(filePath)){
                                    if(ftpDirs.get(i).matches(regex)) {
                                        flag++;
                                    }
                                }
                            }
                        }
                        if(ftpUtil.isConnected()){
                            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            LOG.info("时间-->" + date + "-->正在监控主机={"+ host +"}-->话单配置文件为={"+ cfg +"}-->积压文件量={"+ flag + "}");
                        }else{
                            LOG.info("主机={" + host + "}FTP连接异常！");
                            return;
                        }
                        if(flag > num){
                            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            String message = "采预监控告警:采集对端主机文件积压量已超过阈值->" + num + "，请及时查看!\n" +
                                    "时间：" + date + "\n" +
                                    "ip：" + host + "\n" +
                                    "配置文件："+ cfg+ "\n" +
                                    "积压数量："+ flag;
                            LOG.info(message);
                            SendMessage.sendMsg("8",message);
                            LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>短信发送成功");
                        }
                        ftpUtil.disconnect();
                    } catch (ConnectException e){
                        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        String msg = e.getMessage() + "\n" +
                                "cfg文件为：" + cfg + "\n" +
                                "异常时间为：" + date;
                        LOG.info(msg);
                        SendMessage.sendMsg("8",msg);
                        Long waringId = Constants.COLLECTION_WARING_FLAG;
                        Integer waringFlag = accountMonitorMapper.queryWarningFlag(waringId);
                        if(waringFlag == 1){
                            int flags = monitorMapper.insertWarnInfo(15,num,host);
                            if(flags > 0){
                                LOG.info("外呼告警呼叫成功");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 结算中心、公安传输文件监控
     */
    @Override
    public void uploadFile() {
        //公安传输文件
        try {
            FtpUtil ftpUtil = FtpUtil.createFtpCli("137.0.9.28","test","test@813.com","/mobile/cdma/ngn/zte");
            ftpUtil.connect();
            if(ftpUtil.isConnected()){
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                LOG.info( "时间-->" + date + "-->正在监控主机ip={"+ "137.0.9.28" +"}" );
            }else{
                LOG.info("主机={" + "137.0.9.28" + "}FTP连接异常！");
                return;
            }
            ftpUtil.disconnect();
        } catch (ConnectException e){
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String msg = "公安部ip异常告警："+e.getMessage() + "\n" +
                    "异常时间为：" + date;
            LOG.info(msg);
            SendMessage.sendMsg("8",msg);
        }catch (IOException e) {
            e.printStackTrace();
        }
        //送给结算中心
        try {
            FtpUtil ftpUtil = FtpUtil.createFtpCli("137.0.24.36","cjftp","cjftp123","/zhjs/data/pas/infile/ctjs/CVPN");
            ftpUtil.connect();
            if(ftpUtil.isConnected()){
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                LOG.info( "时间-->" + date + "-->正在监控主机ip={"+ "137.0.24.36" +"}" );
            }else{
                LOG.info("主机={" + "137.0.24.36" + "}FTP连接异常！");
                return;
            }
            ftpUtil.disconnect();
        } catch (ConnectException e){
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String msg = "结算中心ip异常告警："+e.getMessage() + "\n" +
                    "异常时间为：" + date;
            LOG.info(msg);
            SendMessage.sendMsg("8",msg);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
