package com.asiainfo.monitor.entity;

public class WarningCallConfig {
    private Long warningId;
    private String warningName;
    private Integer warningFlag;

    public WarningCallConfig() {
    }

    public WarningCallConfig(Long warningId, String warningName, Integer warningFlag) {
        this.warningId = warningId;
        this.warningName = warningName;
        this.warningFlag = warningFlag;
    }

    public Long getWarningId() {
        return warningId;
    }

    public void setWarningId(Long warningId) {
        this.warningId = warningId;
    }

    public String getWarningName() {
        return warningName;
    }

    public void setWarningName(String warningName) {
        this.warningName = warningName;
    }

    public Integer getWarningFlag() {
        return warningFlag;
    }

    public void setWarningFlag(Integer warningFlag) {
        this.warningFlag = warningFlag;
    }
}
