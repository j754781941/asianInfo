package com.asiainfo.monitor.entity;

public class ChargingDownloadConfig {
    private Long monitorId;
    private String hostCenter;
    private String ip;
    private String databaseInfo;
    private String querySql;
    private String hostDir;
    private String fileNameRegular;
    private String fileDescription;
    private String sideQuerySql;
    private String sideCenter;
    private String sideIp;
    private String sideDatabaseInfo;
    private String sideHostDir;
    private String uploadFrequency;
    private String deadline;
    private String calls;

    public ChargingDownloadConfig() {
    }

    public ChargingDownloadConfig(Long monitorId, String hostCenter, String ip, String databaseInfo, String querySql, String hostDir, String fileNameRegular, String fileDescription, String sideQuerySql, String sideCenter, String sideIp, String sideDatabaseInfo, String sideHostDir, String uploadFrequency, String deadline, String calls) {
        this.monitorId = monitorId;
        this.hostCenter = hostCenter;
        this.ip = ip;
        this.databaseInfo = databaseInfo;
        this.querySql = querySql;
        this.hostDir = hostDir;
        this.fileNameRegular = fileNameRegular;
        this.fileDescription = fileDescription;
        this.sideQuerySql = sideQuerySql;
        this.sideCenter = sideCenter;
        this.sideIp = sideIp;
        this.sideDatabaseInfo = sideDatabaseInfo;
        this.sideHostDir = sideHostDir;
        this.uploadFrequency = uploadFrequency;
        this.deadline = deadline;
        this.calls = calls;
    }

    public String getSideQuerySql() {
        return sideQuerySql;
    }

    public void setSideQuerySql(String sideQuerySql) {
        this.sideQuerySql = sideQuerySql;
    }

    public Long getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
    }

    public String getHostCenter() {
        return hostCenter;
    }

    public void setHostCenter(String hostCenter) {
        this.hostCenter = hostCenter;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDatabaseInfo() {
        return databaseInfo;
    }

    public void setDatabaseInfo(String databaseInfo) {
        this.databaseInfo = databaseInfo;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public String getHostDir() {
        return hostDir;
    }

    public void setHostDir(String hostDir) {
        this.hostDir = hostDir;
    }

    public String getFileNameRegular() {
        return fileNameRegular;
    }

    public void setFileNameRegular(String fileNameRegular) {
        this.fileNameRegular = fileNameRegular;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    public String getSideCenter() {
        return sideCenter;
    }

    public void setSideCenter(String sideCenter) {
        this.sideCenter = sideCenter;
    }

    public String getSideIp() {
        return sideIp;
    }

    public void setSideIp(String sideIp) {
        this.sideIp = sideIp;
    }

    public String getSideDatabaseInfo() {
        return sideDatabaseInfo;
    }

    public void setSideDatabaseInfo(String sideDatabaseInfo) {
        this.sideDatabaseInfo = sideDatabaseInfo;
    }

    public String getSideHostDir() {
        return sideHostDir;
    }

    public void setSideHostDir(String sideHostDir) {
        this.sideHostDir = sideHostDir;
    }

    public String getUploadFrequency() {
        return uploadFrequency;
    }

    public void setUploadFrequency(String uploadFrequency) {
        this.uploadFrequency = uploadFrequency;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getCalls() {
        return calls;
    }

    public void setCalls(String calls) {
        this.calls = calls;
    }
}
