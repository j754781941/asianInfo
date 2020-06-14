package com.asiainfo.monitor.service;

import com.asiainfo.monitor.entity.WarningCallConfig;

import java.util.List;

public interface AccountMonitorService {
    void MonitorTenByDay(String uploadFrequency);

    Integer updateWaringCallSwitch(Long warningId,Integer warningFlag);

    List<WarningCallConfig> queryAllWarningFlag();

    void catchFileMonitor(String uploadFrequency);

    void ftpMonitor(String uploadFrequency);

    void proMessageCneter(String uploadFrequency);

    void monitorGoldByDay(String uploadFrequency);
}
