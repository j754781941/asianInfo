package com.asiainfo.monitor.entity;

public class ChargingHostInfo {
    private Integer hostId;
    private String callsId;
    private String hostIp;
    private String username;
    private String password;
    private String hostdepart;

    public ChargingHostInfo(Integer hostId, String callsId, String hostIp, String username, String password, String hostdepart) {
        this.hostId = hostId;
        this.callsId = callsId;
        this.hostIp = hostIp;
        this.username = username;
        this.password = password;
        this.hostdepart = hostdepart;
    }

    public ChargingHostInfo() {
    }

    public Integer getHostId() {
        return hostId;
    }

    public void setHostId(Integer hostId) {
        this.hostId = hostId;
    }

    public String getCallsId() {
        return callsId;
    }

    public void setCallsId(String callsId) {
        this.callsId = callsId;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHostdepart() {
        return hostdepart;
    }

    public void setHostdepart(String hostdepart) {
        this.hostdepart = hostdepart;
    }
}
