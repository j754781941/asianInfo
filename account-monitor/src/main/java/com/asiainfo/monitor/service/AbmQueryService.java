package com.asiainfo.monitor.service;

import com.asiainfo.monitor.entity.CatchFileUpload;

import java.util.List;

public interface AbmQueryService {
    List<CatchFileUpload> queryMessageProv(String querySql);

    List<String> queryGlodDB(String querySql);

    void srActivationMonitor();
}
