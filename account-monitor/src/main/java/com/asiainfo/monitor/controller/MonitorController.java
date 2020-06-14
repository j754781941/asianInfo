package com.asiainfo.monitor.controller;

import com.asiainfo.monitor.entity.*;
import com.asiainfo.monitor.service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监控前台
 * @author Dekun Wang
 */
@Controller
public class MonitorController {

    private static final Logger LOG = LoggerFactory.getLogger(MonitorController.class);

    @Autowired
    private MonitorService monitorService;

    //flag:1  直接查数据库
    //flag:2  直接查缓存
    //flag:3  统计信息查数据库
    //flag:4  统计信息查缓存

    /**
     * 曲线图
     */
//  专票发票推送失败异常
    @PostMapping("/queryInvoice")
    @ResponseBody
    public List<InvoiceInfo> queryInvoice(@RequestParam(value = "flag",defaultValue = "2") String flag){
        List<InvoiceInfo> invoiceInfos = monitorService.queryInvoice(flag);
        return invoiceInfos;
    }
//  格式八上传异常
    @RequestMapping("/queryFormat8")
    @ResponseBody
    public List<Format8> queryFormat8(@RequestParam(value = "flag",defaultValue = "2") String flag){
        List<Format8> format8s = monitorService.queryFormat8(flag);
        return format8s;
    }
//  负余额账本异常
    @RequestMapping("/queryNegativeBalance")
    @ResponseBody
    public List<NegativeBalanceInfo> queryNegativeBalance(@RequestParam(value = "flag",defaultValue = "2") String flag){
        List<NegativeBalanceInfo> negativeBalanceInfos = monitorService.queryNegativeBalance(flag);
        return negativeBalanceInfos;
    }
//  预存支付计划未处理
    @RequestMapping("/queryPrepaidPayment")
    @ResponseBody
    public List<PrepaidPayment> queryPrepaidPayment(@RequestParam(value = "flag",defaultValue = "2") String flag) {
        List<PrepaidPayment> prepaidPayments = monitorService.queryPrepaidPayment(flag);
        return prepaidPayments;
    }
//  预存支付计划处理失败
    @RequestMapping("/queryPrepaidPaymentError")
    @ResponseBody
    public List<PrepaidPaymentError> queryPrepaidPaymentError(@RequestParam(value = "flag",defaultValue = "2")String flag){
        List<PrepaidPaymentError> prepaidPaymentErrors = monitorService.queryPrepaidPaymentError(flag);
        return prepaidPaymentErrors;
    }
//  账务直采上传文件正确记录
    @RequestMapping("/queryPaymentComplex")
    @ResponseBody
    public List<PaymentComplex> queryPaymentComplex(@RequestParam(value = "flag",defaultValue = "2") String flag){
        List<PaymentComplex> paymentComplexes = monitorService.queryPaymentComplex(flag);
        return paymentComplexes;
    }

//  账务直采上传文件错误记录
    @RequestMapping("/queryPaymentComplexError")
    @ResponseBody
    public List<PaymentComplexError> queryPaymentComplexError(@RequestParam(value = "flag",defaultValue = "2") String flag){
        List<PaymentComplexError> paymentComplexes = monitorService.queryPaymentComplexError(flag);
        LOG.info("paymentComplexes-->"+paymentComplexes);
        return paymentComplexes;
    }

//  营业前台（CRM）
    @RequestMapping("/queryCRMInfos")
    @ResponseBody
    public List<CRMInfo> queryCRMInfos(@RequestParam(value = "flag",defaultValue = "2") String flag){
        String queryDate = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE).substring(0,6);
        List<CRMInfo> crmInfos = monitorService.queryCRMInfos(queryDate,flag);
        return crmInfos;
    }

//    网厅
    @RequestMapping("/queryNetworkLobbies")
    @ResponseBody
    public List<NetworkLobby> queryNetworkLobbies(@RequestParam(value = "flag",defaultValue = "2") String flag){
        String queryDate = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE).substring(0,6);
        List<NetworkLobby> networkLobbies = monitorService.queryNetworkLobbies(queryDate,flag);
        return networkLobbies;
    }

//    IPTV
    @RequestMapping("/queryIPTVInfos")
    @ResponseBody
    public List<IPTVInfo> queryIPTVInfos(@RequestParam(value = "flag",defaultValue = "2") String flag){
        String queryDate = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE).substring(0,6);
        List<IPTVInfo> iptvInfos = monitorService.queryIPTVInfos(queryDate,flag);
        return iptvInfos;
    }

    //与上周环比趋势
    @RequestMapping("/queryChain")
    @ResponseBody
    public Map<String,String> queryChain(){
        Map<String,String> map = new HashMap<>();
        String invocieTrend = monitorService.queryInvoiceTrend();
        String paymentTrend = monitorService.queryPaymentTrend();
        String prepaidTrend = monitorService.queryPrepaidTrend();
        String negativeTrend = monitorService.queryNegativeTrend();
        String format8Trend = monitorService.queryFormat8Trend();
        String canalTrend = monitorService.queryCanalTrend();
        map.put("invoiceTrend",invocieTrend);
        map.put("paymentTrend",paymentTrend);
        map.put("prepaidTrend",prepaidTrend);
        map.put("negativeTrend",negativeTrend);
        map.put("format8Trend",format8Trend);
        map.put("canalTrend",canalTrend);
        return map;
    }
}
