package com.asiainfo.monitor.service;

public interface OriginalBillFileService {
    void queryFileDirs();

    /**
     * 结算中心、公安传输文件
     */
    void uploadFile();
}
