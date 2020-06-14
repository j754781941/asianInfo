package com.asiainfo.monitor.entity;

public class ACheckResult {
    private String checkCycle;
    private String srcSumAmount;
    private String dstSumAmount;
    private String srcSumRecord;
    private String dstSumRecord;
    private String correctSumRecord;
    private String diffSumRecord;

    public ACheckResult(String checkCycle, String srcSumAmount, String dstSumAmount, String srcSumRecord, String dstSumRecord, String correctSumRecord, String diffSumRecord) {
        this.checkCycle = checkCycle;
        this.srcSumAmount = srcSumAmount;
        this.dstSumAmount = dstSumAmount;
        this.srcSumRecord = srcSumRecord;
        this.dstSumRecord = dstSumRecord;
        this.correctSumRecord = correctSumRecord;
        this.diffSumRecord = diffSumRecord;
    }

    public ACheckResult() {
    }

    public String getCheckCycle() {
        return checkCycle;
    }

    public void setCheckCycle(String checkCycle) {
        this.checkCycle = checkCycle;
    }

    public String getSrcSumAmount() {
        return srcSumAmount;
    }

    public void setSrcSumAmount(String srcSumAmount) {
        this.srcSumAmount = srcSumAmount;
    }

    public String getDstSumAmount() {
        return dstSumAmount;
    }

    public void setDstSumAmount(String dstSumAmount) {
        this.dstSumAmount = dstSumAmount;
    }

    public String getSrcSumRecord() {
        return srcSumRecord;
    }

    public void setSrcSumRecord(String srcSumRecord) {
        this.srcSumRecord = srcSumRecord;
    }

    public String getDstSumRecord() {
        return dstSumRecord;
    }

    public void setDstSumRecord(String dstSumRecord) {
        this.dstSumRecord = dstSumRecord;
    }

    public String getCorrectSumRecord() {
        return correctSumRecord;
    }

    public void setCorrectSumRecord(String correctSumRecord) {
        this.correctSumRecord = correctSumRecord;
    }

    public String getDiffSumRecord() {
        return diffSumRecord;
    }

    public void setDiffSumRecord(String diffSumRecord) {
        this.diffSumRecord = diffSumRecord;
    }

    @Override
    public String toString() {
        return "ACheckResult{" +
                "checkCycle='" + checkCycle + '\'' +
                ", srcSumAmount=" + srcSumAmount +
                ", dstSumAmount=" + dstSumAmount +
                ", srcSumRecord=" + srcSumRecord +
                ", dstSumRecord=" + dstSumRecord +
                ", correctSumRecord=" + correctSumRecord +
                ", diffSumRecord=" + diffSumRecord +
                '}';
    }
}
