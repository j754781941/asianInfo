package com.asiainfo.monitor.constant;

public class Constants {

    //每天十点
    public static final String TEN_EVERY_DAY = "0 0 10 * * ?";

    //每天九点
    public static final String NINE_EVERY_DAY = "0 0 9 * * ?";

    //1 BSN端到端监控外呼告警
    public static final Long BSN_WARING_FLAG = 1L;

    //2 账单查询监控外呼告警
    public static final Long BILL_QUERY_WARING_FLAG = 2L;

    //3 采预监控外呼告警
    public static final Long COLLECTION_WARING_FLAG = 3L;

    //充值中心ftp连接信息
    public static final String VC_HOST = "137.0.251.100";

    public static final String VC_USERNAME = "vcapp";

    public static final String VC_PASSWORD = "Sddxvcap@100";

    //物联网类文件ftp连接信息
    public static final String IOT_HOST = "137.0.251.121";

    public static final String IOT_USERNAME = "acct";

    public static final String IOT_PASSWORD = "<act0109<<";

    //SR主机ftp连接信息
    public static final String SR_HOST = "137.0.31.14";

    public static final String SR_USERNAME = "sr1";

    public static final String SR_PASSWORD = "SR!app";

    public static final String SR_DIR1 = "/data_SR1/SR_V3/scripts/active_monitor_sr1/deactive_log/";

    public static final String SR_DIR2 = "/data_SR1/SR_V3/scripts/active_monitor_sr1/deactive_log/";

    //汇聚库数据源
    public static final String GATH_ACCTDB = "db";

    //账务teledb数据源
    public static final String ACCOUNT_ACCTDB = "db2";

    //SR直采主机数据源
    public static final String SR_DB = "db3";

    //abm汇聚库数据源
    public static final String ABM_DB = "db4";

    //佣金系统数据源
    public static final String COMMISSION = "db5";
}
