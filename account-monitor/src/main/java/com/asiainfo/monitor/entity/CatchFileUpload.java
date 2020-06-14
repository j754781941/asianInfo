package com.asiainfo.monitor.entity;

public class CatchFileUpload {
    private String shouldNum;
    private String mergeFileName;

    public CatchFileUpload(String realNum, String shouldNum, String mergeFileName) {
        this.shouldNum = shouldNum;
        this.mergeFileName = mergeFileName;
    }

    public CatchFileUpload() {
    }


    public String getShouldNum() {
        return shouldNum;
    }

    public void setShouldNum(String shouldNum) {
        this.shouldNum = shouldNum;
    }

    public String getMergeFileName() {
        return mergeFileName;
    }

    public void setMergeFileName(String mergeFileName) {
        this.mergeFileName = mergeFileName;
    }
}
