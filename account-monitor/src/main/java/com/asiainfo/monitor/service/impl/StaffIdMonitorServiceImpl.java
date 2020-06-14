package com.asiainfo.monitor.service.impl;

import com.asiainfo.monitor.service.MessageWarningService;
import com.asiainfo.monitor.service.StaffIdMonitorService;
import com.asiainfo.monitor.util.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class StaffIdMonitorServiceImpl implements StaffIdMonitorService {

    private static final Logger LOG = LoggerFactory.getLogger(StaffIdMonitorServiceImpl.class);

    @Autowired
    private MessageWarningService messageWarningService;

    @Override
    public void doTask_staffIdInterface(){
        try {
            LOG.info("开始对工号接口进行监控");
            Process process1 = Runtime.getRuntime().exec("/app/ci/StaffIdMonitor.sh");
            String result = getProcessResult(process1);
            LOG.info("执行结果为："+result);
            String msg = "";
            if (!result.contains("76")) {
                if(!result.contains("75")) {
                    msg = "账务监控提醒您，工号接口进程挂了!\n" +
                            "IP地址为:137.0.251.75,端口号为8099;\n" +
                            "IP地址为:137.0.251.76,端口号为8099\n" +
                            "请尽快查看！\n";
                }else{
                    msg = "账务监控提醒您，工号接口进程挂了!\n" +
                            "IP地址为:137.0.251.76,端口号为8099\n" +
                            "请尽快查看！\n";
                }
            } else if(!result.contains("75")){
                if(!result.contains("76")) {
                    msg = "账务监控提醒您，工号接口挂了!\n" +
                            "IP地址为:137.0.251.75,端口号为8099;\n" +
                            "IP地址为:137.0.251.76,端口号为8099\n" +
                            "请尽快查看！\n";
                }else{
                    msg = "账务监控提醒您，工号接口挂了!\n" +
                            "IP地址为:137.0.251.75,端口号为8099\n" +
                            "请尽快查看！\n";
                }
            }else{
                LOG.info("工号接口进程正常运行...");
            }
            if(!"".equals(msg)){
                LOG.info(msg);
                SendMessage.sendMsg("1",msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getProcessResult(Process process1) throws IOException, InterruptedException {
        process1.waitFor();
        InputStream is = process1.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        is.close();
        reader.close();
        process1.destroy();

        return sb.toString();
    }
}
