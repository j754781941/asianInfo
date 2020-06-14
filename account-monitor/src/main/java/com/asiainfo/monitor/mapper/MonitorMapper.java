package com.asiainfo.monitor.mapper;


import com.asiainfo.monitor.config.DS;
import com.asiainfo.monitor.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper

public interface MonitorMapper {
    //查询专票发票推送失败异常

    List<InvoiceInfo> queryInvoice();


    List<Format8> queryFormat8();


    List<NegativeBalanceInfo> queryNegativeBalance();


    List<PrepaidPayment> queryPrepaidPayment();


    List<PaymentComplex> queryPaymentComplex();


    List<CRMInfo> queryCRMInfos(@Param("queryDate") String queryDate);


    List<NetworkLobby> queryNetworkLobbies(@Param("queryDate") String queryDate);


    List<IPTVInfo> queryIPTVInfos(@Param("queryDate") String queryDate);


    List<PrepaidPaymentError> queryPrepaidPaymentError();


    List<PaymentComplexError> queryPaymentComplexError();


    int insertWarnInfo(@Param("type") Integer type, @Param("num") long num, @Param("host") String host);
}
