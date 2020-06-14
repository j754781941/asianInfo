package com.asiainfo.monitor.service.impl;

import com.alibaba.fastjson.JSON;
import com.asiainfo.monitor.config.DS;
import com.asiainfo.monitor.entity.*;
import com.asiainfo.monitor.mapper.MonitorMapper;
import com.asiainfo.monitor.service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Dekun Wang
 */
@Service
public class MonitorServiceImpl implements MonitorService{

    private static final Logger LOG = LoggerFactory.getLogger(MonitorServiceImpl.class);

    @Autowired
    private MonitorMapper monitorDao;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 专票发票推送失败异常
     * @param flag
     * @return
     */
    @Override
    public List<InvoiceInfo> queryInvoice(String flag) {
        List<InvoiceInfo> invoiceInfos = new ArrayList<>();
        if(flag.equals("1")){//直接查数据库
            invoiceInfos = monitorDao.queryInvoice();
            redisTemplate.opsForHash().put("monitor","invoice",invoiceInfos);
            redisTemplate.expire("monitor",1, TimeUnit.DAYS);
        }else if(flag.equals("3")){//统计信息查数据库
            //查数据库给缓存设置值
            invoiceInfos = monitorDao.queryInvoice();
            for(InvoiceInfo invoiceInfo :invoiceInfos){
                if(redisTemplate.opsForHash().hasKey("invoiceInfos",invoiceInfo.getInvoiceDate())){
                    BigDecimal count = (BigDecimal)redisTemplate.opsForHash().get("invoiceInfos", invoiceInfo.getInvoiceDate());
                    if(count.compareTo(invoiceInfo.getInvoiceCount()) == -1){
                        redisTemplate.opsForHash().put("invoiceInfos", invoiceInfo.getInvoiceDate(),invoiceInfo.getInvoiceCount());
                    }else if(count.compareTo(invoiceInfo.getInvoiceCount()) == 1){
                        invoiceInfo.setInvoiceCount(count);
                    }
                }else{
                    redisTemplate.opsForHash().put("invoiceInfos", invoiceInfo.getInvoiceDate(),invoiceInfo.getInvoiceCount());
                }
            }
            invoiceInfos.clear();
            List<String> days = getDaysBetwwen(7);
            for(String day:days){
                if(redisTemplate.opsForHash().hasKey("invoiceInfos",day)){
                    InvoiceInfo invoiceInfo = new InvoiceInfo();
                    invoiceInfo.setInvoiceDate(day);
                    invoiceInfo.setInvoiceCount((BigDecimal) redisTemplate.opsForHash().get("invoiceInfos",day));
                    invoiceInfos.add(invoiceInfo);
                }
            }
        }else if(flag.equals("4")){//统计信息查缓存
            List<String> days = getDaysBetwwen(7);
            for(String day:days){
                if(redisTemplate.opsForHash().hasKey("invoiceInfos",day)){
                    InvoiceInfo invoiceInfo = new InvoiceInfo();
                    invoiceInfo.setInvoiceDate(day);
                    invoiceInfo.setInvoiceCount((BigDecimal) redisTemplate.opsForHash().get("invoiceInfos",day));
                    invoiceInfos.add(invoiceInfo);
                }
            }
        }else{//直接查缓存
            if(redisTemplate.opsForHash().hasKey("monitor","invoice")){
                invoiceInfos = (List<InvoiceInfo>)redisTemplate.opsForHash().get("monitor","invoice");
            }else{
                invoiceInfos = monitorDao.queryInvoice();
                redisTemplate.opsForHash().put("monitor","invoice",invoiceInfos);
                redisTemplate.expire("monitor",1, TimeUnit.DAYS);
            }
        }
        return invoiceInfos;
    }

    /**
     * 格式八上传异常
     * @param flag
     * @return
     */
    @Override
    public List<Format8> queryFormat8(String flag) {
        List<Format8> format8s = new ArrayList<>();
        if(flag.equals("1")){//直接查数据库
            format8s = monitorDao.queryFormat8();
            redisTemplate.opsForHash().put("monitor","format8",format8s);
            redisTemplate.expire("monitor",1, TimeUnit.DAYS);
        }else if(flag.equals("3")){//统计信息
            format8s = monitorDao.queryFormat8();
            for(Format8 format8 :format8s){
                if(redisTemplate.opsForHash().hasKey("format8s",format8.getFormatDate())){
                    BigDecimal count = (BigDecimal)redisTemplate.opsForHash().get("format8s", format8.getFormatDate());
                    if(count.compareTo(format8.getFormatCount()) == -1){
                        redisTemplate.opsForHash().put("format8s", format8.getFormatDate(),format8.getFormatCount());
                    }else if(count.compareTo(format8.getFormatCount()) == 1){
                        format8.setFormatCount(count);
                    }
                }else{
                    redisTemplate.opsForHash().put("format8s", format8.getFormatDate(),format8.getFormatCount());
                }
            }
            format8s.clear();
            List<String> days = getDaysBetwwen(7);
            for(String day:days){
                if(redisTemplate.opsForHash().hasKey("format8s",day)){
                    format8s.add(new Format8(day,(BigDecimal)redisTemplate.opsForHash().get("format8s",day)));

                }
            }
        }else if(flag.equals("4")){//统计信息查缓存
            List<String> days = getDaysBetwwen(7);
            for(String day:days){
                if(redisTemplate.opsForHash().hasKey("format8s",day)){
                    format8s.add(new Format8(day,(BigDecimal)redisTemplate.opsForHash().get("format8s",day)));

                }
            }
        }else{//直接查缓存
            if(redisTemplate.opsForHash().hasKey("monitor","format8")){
                format8s = (List<Format8>)redisTemplate.opsForHash().get("monitor","format8");
            }else{
                format8s = monitorDao.queryFormat8();
                redisTemplate.opsForHash().put("monitor","format8",format8s);
                redisTemplate.expire("monitor",1, TimeUnit.DAYS);
            }
        }
        return format8s;
    }

    /**
     * 负余额账本异常
     * @param flag
     * @return
     */
    @Override
    public List<NegativeBalanceInfo> queryNegativeBalance(String flag) {
        List<NegativeBalanceInfo> negativeBalanceInfos = new ArrayList<>();
        if(flag.equals("1")){//直接查数据库
            negativeBalanceInfos = monitorDao.queryNegativeBalance();
            redisTemplate.opsForHash().put("monitor","negativeBalanceInfo",negativeBalanceInfos);
            redisTemplate.expire("monitor",1, TimeUnit.DAYS);
        }else if(flag.equals("3")){//统计信息
            negativeBalanceInfos = monitorDao.queryNegativeBalance();
            for(NegativeBalanceInfo negativeBalanceInfo:negativeBalanceInfos){
                if(redisTemplate.opsForHash().hasKey("negativeBalanceInfos",negativeBalanceInfo.getNegativeBalanceDate())){
                    BigDecimal count = (BigDecimal)redisTemplate.opsForHash().get("negativeBalanceInfos", negativeBalanceInfo.getNegativeBalanceDate());
                    if(count.compareTo(negativeBalanceInfo.getNegativeBalanceCount()) == -1){
                        redisTemplate.opsForHash().put("negativeBalanceInfos", negativeBalanceInfo.getNegativeBalanceDate(),negativeBalanceInfo.getNegativeBalanceCount());
                    }else if(count.compareTo(negativeBalanceInfo.getNegativeBalanceCount()) == 1){
                        negativeBalanceInfo.setNegativeBalanceCount(count);
                    }
                }else{
                    redisTemplate.opsForHash().put("negativeBalanceInfos", negativeBalanceInfo.getNegativeBalanceDate(),negativeBalanceInfo.getNegativeBalanceCount());
                }
            }
            //清空容器查缓存
            negativeBalanceInfos.clear();
            List<String> days = getDaysBetwwen(7);
            for(String day:days){
                if(redisTemplate.opsForHash().hasKey("negativeBalanceInfos",day)){
                    negativeBalanceInfos.add(new NegativeBalanceInfo(day,(BigDecimal)redisTemplate.opsForHash().get("negativeBalanceInfos",day)));
                }
            }
        }else if(flag.equals("4")){//统计信息查缓存
            List<String> days = getDaysBetwwen(7);
            for(String day:days){
                if(redisTemplate.opsForHash().hasKey("negativeBalanceInfos",day)){
                    negativeBalanceInfos.add(new NegativeBalanceInfo(day,(BigDecimal)redisTemplate.opsForHash().get("negativeBalanceInfos",day)));
                }
            }
        }else {//直接查缓存
            if(redisTemplate.opsForHash().hasKey("monitor","negativeBalanceInfo")){
                negativeBalanceInfos = (List<NegativeBalanceInfo>)redisTemplate.opsForHash().get("monitor","negativeBalanceInfo");
            }else{
                negativeBalanceInfos = monitorDao.queryNegativeBalance();
                redisTemplate.opsForHash().put("monitor","negativeBalanceInfo",negativeBalanceInfos);
                redisTemplate.expire("monitor",1, TimeUnit.DAYS);
            }
        }
        return negativeBalanceInfos;
    }



    /**
     * 预存支付计划未处理
     * @param flag
     * @return
     */
    @Override
    public List<PrepaidPayment> queryPrepaidPayment(String flag) {
        List<PrepaidPayment> prepaidPayments = new ArrayList<>();
        if(flag.equals("1")){//直接查数据库
            prepaidPayments = monitorDao.queryPrepaidPayment();
            redisTemplate.opsForHash().put("monitor1", "prepaidPayment", prepaidPayments);
            redisTemplate.expire("monitor1", 15, TimeUnit.MINUTES);
        }else if(flag.equals("3")){//统计信息
            prepaidPayments = monitorDao.queryPrepaidPayment();
            for(PrepaidPayment prepaidPayment:prepaidPayments){
                if(redisTemplate.opsForHash().hasKey("prepaidPayments",prepaidPayment.getPrepaidDate())){
                    BigDecimal count = (BigDecimal)redisTemplate.opsForHash().get("prepaidPayments", prepaidPayment.getPrepaidDate());
                    if(count.compareTo(prepaidPayment.getPrepaidCount()) == -1){
                        redisTemplate.opsForHash().put("prepaidPayments", prepaidPayment.getPrepaidDate(),prepaidPayment.getPrepaidCount());
                    }else if(count.compareTo(prepaidPayment.getPrepaidCount()) == 1){
                        prepaidPayment.setPrepaidCount(count);
                    }
                }else{
                    redisTemplate.opsForHash().put("prepaidPayments", prepaidPayment.getPrepaidDate(),prepaidPayment.getPrepaidCount());
                }
            }
            //清空容器查缓存
            prepaidPayments.clear();
            List<String> days = getDaysBetwwen(7);
            for(String day:days){
                if(redisTemplate.opsForHash().hasKey("prepaidPayments",day)){
                    prepaidPayments.add(new PrepaidPayment(day,(BigDecimal)redisTemplate.opsForHash().get("prepaidPayments",day)));
                }
            }
        }else if(flag.equals("4")){//统计信息查缓存
            List<String> days = getDaysBetwwen(7);
            for(String day:days){
                if(redisTemplate.opsForHash().hasKey("prepaidPayments",day)){
                    prepaidPayments.add(new PrepaidPayment(day,(BigDecimal)redisTemplate.opsForHash().get("prepaidPayments",day)));
                }
            }
        }else {//直接查缓存
            if (redisTemplate.opsForHash().hasKey("monitor1", "prepaidPayment")) {
                prepaidPayments = (List<PrepaidPayment>) redisTemplate.opsForHash().get("monitor1", "prepaidPayment");
            } else {
                prepaidPayments = monitorDao.queryPrepaidPayment();
                redisTemplate.opsForHash().put("monitor1", "prepaidPayment", prepaidPayments);
                redisTemplate.expire("monitor1", 15, TimeUnit.MINUTES);
            }
        }
        return prepaidPayments;
    }

    @Override
    public List<PrepaidPaymentError> queryPrepaidPaymentError(String flag) {
        List<PrepaidPaymentError> prepaidPaymentErrors = new ArrayList<>();
        if(flag.equals("1")){//直接查数据库
            prepaidPaymentErrors = monitorDao.queryPrepaidPaymentError();
            redisTemplate.opsForHash().put("monitor1", "prepaidPaymentError", prepaidPaymentErrors);
            redisTemplate.expire("monitor1", 15, TimeUnit.MINUTES);
        }else if(flag.equals("3")){//统计信息
            prepaidPaymentErrors = monitorDao.queryPrepaidPaymentError();
            for(PrepaidPaymentError prepaidPaymentError:prepaidPaymentErrors){
                if(redisTemplate.opsForHash().hasKey("prepaidPaymentErrors",prepaidPaymentError.getPrepaidErrorDate())){
                    BigDecimal count = (BigDecimal)redisTemplate.opsForHash().get("prepaidPaymentErrors", prepaidPaymentError.getPrepaidErrorDate());
                    if(count.compareTo(prepaidPaymentError.getPrepaidErrorCount()) == -1){
                        redisTemplate.opsForHash().put("prepaidPaymentErrors", prepaidPaymentError.getPrepaidErrorDate(),prepaidPaymentError.getPrepaidErrorCount());
                    }else if(count.compareTo(prepaidPaymentError.getPrepaidErrorCount()) == 1){
                        prepaidPaymentError.setPrepaidErrorCount(count);
                    }
                }else{
                    redisTemplate.opsForHash().put("prepaidPaymentErrors",prepaidPaymentError.getPrepaidErrorDate(),prepaidPaymentError.getPrepaidErrorCount());
                }
            }
            //清空容器查缓存
            prepaidPaymentErrors.clear();
            List<String> days = getDaysBetwwen(7);
            for(String day:days){
                if(redisTemplate.opsForHash().hasKey("prepaidPaymentErrors",day)){
                    prepaidPaymentErrors.add(new PrepaidPaymentError(day,(BigDecimal)redisTemplate.opsForHash().get("prepaidPaymentErrors",day)));
                }
            }
        }else if(flag.equals("4")){//统计信息查缓存
            List<String> days = getDaysBetwwen(7);
            for(String day:days){
                if(redisTemplate.opsForHash().hasKey("prepaidPaymentErrors",day)){
                    prepaidPaymentErrors.add(new PrepaidPaymentError(day,(BigDecimal)redisTemplate.opsForHash().get("prepaidPaymentErrors",day)));
                }
            }
        }else {//直接查缓存
            if (redisTemplate.opsForHash().hasKey("monitor1", "prepaidPaymentError")) {
                prepaidPaymentErrors = (List<PrepaidPaymentError>) redisTemplate.opsForHash().get("monitor1", "prepaidPaymentError");
            } else {
                prepaidPaymentErrors = monitorDao.queryPrepaidPaymentError();
                LOG.info("11111111");
                redisTemplate.opsForHash().put("monitor1", "prepaidPaymentError", prepaidPaymentErrors);
                redisTemplate.expire("monitor1", 15, TimeUnit.MINUTES);
            }
        }
        return prepaidPaymentErrors;
    }

    /**
     * 账务直采上传文件正确记录
     * @param flag
     * @return
     */
    @Override
    public List<PaymentComplex> queryPaymentComplex(String flag) {
        List<PaymentComplex> paymentComplexes = new ArrayList<>();
        if(flag.equals("1")){//直接查数据库
            paymentComplexes = monitorDao.queryPaymentComplex();
            redisTemplate.opsForHash().put("monitor1","paymentComplex",paymentComplexes);
            redisTemplate.expire("monitor1",15, TimeUnit.MINUTES);
        }else if(flag.equals("3")){//统计信息
            paymentComplexes = monitorDao.queryPaymentComplex();
            for(PaymentComplex paymentComplex:paymentComplexes){
                if(redisTemplate.opsForHash().hasKey("paymentComplexes",paymentComplex.getComplexDate())){
                    BigDecimal count = (BigDecimal)redisTemplate.opsForHash().get("paymentComplexes", paymentComplex.getComplexDate());
                    if(count.compareTo(paymentComplex.getComplexCount()) == -1){
                        redisTemplate.opsForHash().put("paymentComplexes", paymentComplex.getComplexDate(),paymentComplex.getComplexCount());
                    }else if(count.compareTo(paymentComplex.getComplexCount()) == 1){
                        paymentComplex.setComplexCount(count);
                    }
                }else{
                    redisTemplate.opsForHash().put("paymentComplexes", paymentComplex.getComplexDate(),paymentComplex.getComplexCount());
                }
            }
            //清空容器查缓存
            paymentComplexes.clear();
            List<String> days = getDaysBetwwen(7);
            for(String day:days){
                if(redisTemplate.opsForHash().hasKey("paymentComplexes",day)){
                    paymentComplexes.add(new PaymentComplex(day,(BigDecimal)redisTemplate.opsForHash().get("paymentComplexes",day)));
                }
            }
        }else if(flag.equals("4")){//统计信息查缓存
            List<String> days = getDaysBetwwen(7);
            for(String day:days){
                if(redisTemplate.opsForHash().hasKey("paymentComplexes",day)){
                    paymentComplexes.add(new PaymentComplex(day,(BigDecimal)redisTemplate.opsForHash().get("paymentComplexes",day)));
                }
            }
        }else{//直接查缓存
            if(redisTemplate.opsForHash().hasKey("monitor1","paymentComplex")){
                paymentComplexes = (List<PaymentComplex>)redisTemplate.opsForHash().get("monitor1","paymentComplex");
            }else{
                paymentComplexes = monitorDao.queryPaymentComplex();
                redisTemplate.opsForHash().put("monitor1","paymentComplex",paymentComplexes);
                redisTemplate.expire("monitor1",15, TimeUnit.MINUTES);
            }
        }
        return paymentComplexes;
    }

    /**
     * 账务直采上传文件失败记录
     * @param flag
     * @return
     */
    @Override
    public List<PaymentComplexError> queryPaymentComplexError(String flag) {
        List<PaymentComplexError> paymentComplexes = new ArrayList<>();
        if(flag.equals("1")){//直接查数据库
            paymentComplexes = monitorDao.queryPaymentComplexError();
            redisTemplate.opsForHash().put("monitor1","paymentComplexError",paymentComplexes);
            redisTemplate.expire("monitor1",15, TimeUnit.MINUTES);
        }else if(flag.equals("3")){//统计信息
            paymentComplexes = monitorDao.queryPaymentComplexError();
            for(PaymentComplexError paymentComplex:paymentComplexes){
                if(redisTemplate.opsForHash().hasKey("paymentComplexesError",paymentComplex.getComplexErrorDate())){
                    BigDecimal count = (BigDecimal)redisTemplate.opsForHash().get("paymentComplexesError", paymentComplex.getComplexErrorDate());
                    if(count.compareTo(paymentComplex.getComplexErrorCount()) == -1){
                        redisTemplate.opsForHash().put("paymentComplexesError", paymentComplex.getComplexErrorDate(),paymentComplex.getComplexErrorCount());
                    }else if(count.compareTo(paymentComplex.getComplexErrorCount()) == 1){
                        paymentComplex.setComplexErrorCount(count);
                    }
                }else{
                    redisTemplate.opsForHash().put("paymentComplexesError", paymentComplex.getComplexErrorDate(),paymentComplex.getComplexErrorCount());
                }
            }
            //清空容器查缓存
            paymentComplexes.clear();
            List<String> days = getDaysBetwwen(7);
            for(String day:days){
                if(redisTemplate.opsForHash().hasKey("paymentComplexesError",day)){
                    paymentComplexes.add(new PaymentComplexError(day,(BigDecimal)redisTemplate.opsForHash().get("paymentComplexesError",day)));
                }
            }
        }else if(flag.equals("4")){//统计信息查缓存
            List<String> days = getDaysBetwwen(7);
            for(String day:days){
                if(redisTemplate.opsForHash().hasKey("paymentComplexesError",day)){
                    paymentComplexes.add(new PaymentComplexError(day,(BigDecimal)redisTemplate.opsForHash().get("paymentComplexesError",day)));
                }
            }
        }else{//直接查缓存
            if(redisTemplate.opsForHash().hasKey("monitor1","paymentComplexError")){
                paymentComplexes = (List<PaymentComplexError>)redisTemplate.opsForHash().get("monitor1","paymentComplexError");
            }else{
                paymentComplexes = monitorDao.queryPaymentComplexError();
                redisTemplate.opsForHash().put("monitor1","paymentComplexError",paymentComplexes);
                redisTemplate.expire("monitor1",15, TimeUnit.MINUTES);
            }
        }
        return paymentComplexes;
    }

    /**
     * 各渠道缴费量与入表数据异常
     * @param queryDate
     * @param flag
     * @return
     */
    //CRM
    @Override
    public List<CRMInfo> queryCRMInfos(String queryDate,String flag) {
        List<CRMInfo> crmInfos;
        if(flag.equals("1")){//直接查数据库
            crmInfos = monitorDao.queryCRMInfos(queryDate);
            redisTemplate.opsForHash().put("monitor","crmInfos",crmInfos);
            redisTemplate.expire("monitor",1, TimeUnit.DAYS);
        }else{
            if(redisTemplate.opsForHash().hasKey("monitor","crmInfos")){
                crmInfos = (List<CRMInfo>)redisTemplate.opsForHash().get("monitor","crmInfos");
            }else{
                crmInfos = monitorDao.queryCRMInfos(queryDate);
                redisTemplate.opsForHash().put("monitor","crmInfos",crmInfos);
                redisTemplate.expire("monitor",1, TimeUnit.DAYS);
            }
        }
        return crmInfos;
    }
    //网厅
    @Override
    public List<NetworkLobby> queryNetworkLobbies(String queryDate,String flag) {
        List<NetworkLobby> networkLobbies;
        if(flag.equals("1")){//直接查数据库
            networkLobbies = monitorDao.queryNetworkLobbies(queryDate);
            redisTemplate.opsForHash().put("monitor","networkLobbies",networkLobbies);
            redisTemplate.expire("monitor",1, TimeUnit.DAYS);
        }else{
            if(redisTemplate.opsForHash().hasKey("monitor","networkLobbies")){
                networkLobbies = (List<NetworkLobby>)redisTemplate.opsForHash().get("monitor","networkLobbies");
            }else{
                networkLobbies = monitorDao.queryNetworkLobbies(queryDate);
                redisTemplate.opsForHash().put("monitor","networkLobbies",networkLobbies);
                redisTemplate.expire("monitor",1, TimeUnit.DAYS);
            }
        }

        return networkLobbies;
    }
//  IPTV
    @Override
    public List<IPTVInfo> queryIPTVInfos(String queryDate,String flag) {
        List<IPTVInfo> iptvInfos;
        if(flag.equals("1")){//直接查数据库
            iptvInfos = monitorDao.queryIPTVInfos(queryDate);
            redisTemplate.opsForHash().put("monitor","iptvInfos",iptvInfos);
            redisTemplate.expire("monitor",1, TimeUnit.DAYS);
        }else{
            if(redisTemplate.opsForHash().hasKey("monitor","iptvInfos")){
                iptvInfos = (List<IPTVInfo>)redisTemplate.opsForHash().get("monitor","iptvInfos");
            }else{
                iptvInfos = monitorDao.queryIPTVInfos(queryDate);
                redisTemplate.opsForHash().put("monitor","iptvInfos",iptvInfos);
                redisTemplate.expire("monitor",1, TimeUnit.DAYS);
            }
        }
        return iptvInfos;
    }


    //环比增长率=（本期数-上期数）/上期数×100%
    @Override
    public String queryInvoiceTrend() {
        return getChain("invoiceInfos");
    }

    @Override
    public String queryPaymentTrend() {
        return getChain("paymentComplexes");
    }

    @Override
    public String queryPrepaidTrend() {
        return getChain("prepaidPaymentErrors");
    }

    @Override
    public String queryNegativeTrend() {
        return getChain("negativeBalanceInfos");
    }

    @Override
    public String queryFormat8Trend() {
        return getChain("format8s");
    }

    @Override
    public String queryCanalTrend() {
        return getChain("canals");
    }



    /**
     * 获得前7天的天数
     * @param days
     * @return
     */
    private Date getDateAdd(int days){
        SimpleDateFormat sf = new SimpleDateFormat("MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -days);
        return c.getTime();
    }
    private  List<String> getDaysBetwwen(int days){
        List<String> dayss = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        start.setTime(getDateAdd(days));
        Long startTIme = start.getTimeInMillis();
        Calendar end = Calendar.getInstance();
        end.setTime(new Date());
        Long endTime = end.getTimeInMillis();
        Long oneDay = 1000 * 60 * 60 * 24l;
        Long time = startTIme;
        while (time <= endTime) {
            Date d = new Date(time);
            DateFormat df = new SimpleDateFormat("MM-dd");
            dayss.add(df.format(d));
            time += oneDay;
        }
        return dayss;
    }

    /**
     * 获得环比增长率
     * @param mark
     * @return
     */
    public String getChain(String mark){
        List<String> nows = getDaysBetwwen(13).subList(7,14);
        List<String> lasts = getDaysBetwwen(13).subList(0,7);
        BigDecimal nowNum = new BigDecimal(0);
        BigDecimal lastNum = new BigDecimal(0);
        //获得本期数
        for(String now : nows){
            if(redisTemplate.opsForHash().hasKey(mark,now)){
                nowNum = nowNum.add((BigDecimal)redisTemplate.opsForHash().get(mark,now));
            }
        }
        //获得上期数
        for(String last : lasts){
            if(redisTemplate.opsForHash().hasKey(mark,last)){
                lastNum = lastNum.add((BigDecimal)redisTemplate.opsForHash().get(mark,last));
            }
        }
        String trend = null;
        if(!lastNum.equals(BigDecimal.ZERO)){
            trend = nowNum.subtract(lastNum).multiply(new BigDecimal(100)).divide(lastNum,2,BigDecimal.ROUND_HALF_UP).toString();
        }
        return trend;
    }
}
