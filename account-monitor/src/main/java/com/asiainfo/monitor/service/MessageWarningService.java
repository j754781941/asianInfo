package com.asiainfo.monitor.service;

public interface MessageWarningService {
    /**
     * 账单查询告警
     */
    void queryCentralWarning();

    /**
     * 集团直冲、省内卡充、省内直充、集团卡充告警
     */
    void queryGroupWarning();

    /**
     * 账务与CRM对账
     */
    void queryAccountAndCRMCheck();
}
