package com.asiainfo.monitor.scheduled;

import com.asiainfo.monitor.constant.Constants;
import com.asiainfo.monitor.service.*;
import com.asiainfo.monitor.service.impl.AbmQueryServiceImpl;
import com.asiainfo.monitor.util.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Calendar;

@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class MonitorScheduleTask {
    @Autowired
    private MonitorService monitorService;

    @Autowired
    private MessageWarningService messageWarningService;

    @Autowired
    private StaffIdMonitorService staffIdMonitorService;

    @Autowired
    private AbmQueryService abmQueryService;

    @Autowired
    private OriginalBillFileService originalBillFileService;

    @Autowired
    private AccountMonitorService accountMonitorService;

    @Autowired
    private CommonCollectMonitorService commonCollectMonitorService;


    //每五十秒触发一次
//    @Scheduled( fixedRate = 50 * 1000 )
//    private void configureTasks() {
//        monitorService.queryFormat8("3");
//        monitorService.queryInvoice("3");
//        monitorService.queryPaymentComplex("3");
//        monitorService.queryPrepaidPayment("3");
//        monitorService.queryPrepaidPaymentError("3");
//        monitorService.queryNegativeBalance("3");
//        System.err.println("执行查询统计信息的时间: " + LocalDateTime.now());
//    }
//
//    //每两分钟触发一次
//    @Scheduled( fixedRate = 60 * 1000 * 2 )
//    private void warningTasks(){
//        messageWarningService.queryCentralWarning();
//    }
//
//    //每五分钟触发有一次
//    @Scheduled( fixedRate = 60 * 1000 * 5 )
//    private void warningssTasks(){
//        messageWarningService.queryGroupWarning();
//    }
//
//    //每十五分钟触发一次
//    @Scheduled( fixedRate = 60 * 1000 * 15 )
//    private void warningsTasks(){
//        staffIdMonitorService.doTask_staffIdInterface();
//        //采预积压量监控
////        originalBillFileService.queryFileDirs();
////       originalBillFileService.uploadFile();
//    }
//
//    //每天九点触发一次
//    @Scheduled( cron = Constants.NINE_EVERY_DAY )
//    private void warningNineEveryDay(){
//        messageWarningService.queryAccountAndCRMCheck();
//    }

    /*  账务监控匹配规范
        a1 ：每天十点扫描
        a2 ：每月 1 号 10 点扫描
        a3 ：每月 6 号 10 点扫描
        a4 ：每月 5 15 25 号 10 点扫描
        a5 ：每月 10 号 9 点扫描
    */
    // 每天 10 点监控
    @Scheduled(cron = "0 0 10 * * ?")
    private void  AccountMonitorTenByDayTask(){
        accountMonitorService.MonitorTenByDay("a1");
    }

    // 每月 1 号 10 点扫描
    @Scheduled(cron = "0 0 10 1 * ?")
    private void  AccountMonitorTenByOneTask(){
        accountMonitorService.MonitorTenByDay("a2");
    }

    // 每月 6 号 10 点扫描
    @Scheduled(cron = "0 0 10 6 * ?")
    private void  AccountMonitorTenBySixTask(){
        accountMonitorService.MonitorTenByDay("a3");
    }

    // 每月 9 号 9 点扫描
    @Scheduled(cron = "0 0 9 9 * ?")
    private void  AccountMonitorNineByTenTask(){
        accountMonitorService.MonitorTenByDay("a5");
    }

    //每月5 15 25 号 9点监控
    @Scheduled(cron = "0 0 7 5,15,25 * ?")
    private void  AccountMonitorIOTByTenTask(){
        accountMonitorService.ftpMonitor("a4");
    }

    //日欠费表每天 15点监控
    @Scheduled(cron = "0 0 15 * * ?")
    private void  AccountMonitorAcctItemDayByTenTask(){
        accountMonitorService.MonitorTenByDay("a6");
    }

    //余额账本表每天 12点监控
    @Scheduled(cron = "0 0 12 * * ?")
    private void  AccountMonitorAcctBalanceByTenTask(){
        accountMonitorService.MonitorTenByDay("a7");
    }

    /**
     * 充值中心监控匹配规范
     * c1：每天零点生成上传
     */
    //每天7点扫描
    @Scheduled( cron = "0 0 7 * * ?" )
    private void VCMonitorBySevenTask(){
        accountMonitorService.ftpMonitor("c1");
    }


    /**
     * 消息省中心匹配规范
     * d3：每月
     */
    @Scheduled( cron = "0 0 12 5 * ?" )
    public void MsgMonthTask(){
        accountMonitorService.proMessageCneter("d3");
    }

    /**
     * 佣金系统监控
     * 每月月底最后一天24点前上传
     * e1：（按月）每月月底最后一天24点前上传
     */
    @Scheduled( cron = "0 0 17 28-31 * ?" )
    public void goldLastDayTask(){
        final Calendar c = Calendar.getInstance();
        if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
            //是最后一天
            accountMonitorService.monitorGoldByDay("e1");
        }
    }

    /**
     * 通用监控实时的，每十五分钟监控一次
     */
    @Scheduled( fixedRate = 60 * 1000 * 15 )
    public void MsgMinuteTask(){
        commonCollectMonitorService.ftpCollectMonitor();
        commonCollectMonitorService.dbCollectMonitor();
    }

    /**
     * 增加kubectl的csr认证过期告警
     */
    @Scheduled( cron = "0 0 8 13 5 ?" )
    public void kubectlYearTask(){
        SendMessage.sendMsg("1","计费kubectl的csr认证过期,请重新认证！");
    }

    /**
     * SR监控
     */
    @Scheduled( fixedRate = 60 * 1000 * 2 )
    public void sRMonitorTask(){
        abmQueryService.srActivationMonitor();
    }
}
