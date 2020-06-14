package com.asiainfo.monitor.service;

import com.asiainfo.monitor.entity.ACheckResult;

import java.util.List;

public interface AccountAndCRMService {

    /**
     * 账务与CRM对账
     * @return List<ACheckResult>
     */
    List<ACheckResult> queryACheckResults();

    /**
     * 端到端卡充与直充
     */

}
