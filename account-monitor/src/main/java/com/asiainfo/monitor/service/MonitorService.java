package com.asiainfo.monitor.service;

import com.asiainfo.monitor.entity.*;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MonitorService {
    List<InvoiceInfo> queryInvoice(String flag);

    List<Format8> queryFormat8(String flag);

    List<NegativeBalanceInfo> queryNegativeBalance(String flag);

    List<PrepaidPayment> queryPrepaidPayment(String flag);

    List<PaymentComplex> queryPaymentComplex(String flag);

    List<PaymentComplexError> queryPaymentComplexError(String flag);

    List<CRMInfo> queryCRMInfos(String queryDate, String flag);

    List<NetworkLobby> queryNetworkLobbies(String queryDate, String flag);

    List<IPTVInfo> queryIPTVInfos(String queryDate, String flag);

    String queryInvoiceTrend();

    String queryPaymentTrend();

    String queryPrepaidTrend();

    String queryNegativeTrend();

    String queryFormat8Trend();

    String queryCanalTrend();

    List<PrepaidPaymentError> queryPrepaidPaymentError(String flag);
}
