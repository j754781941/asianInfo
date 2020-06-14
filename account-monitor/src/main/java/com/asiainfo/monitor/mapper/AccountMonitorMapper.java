package com.asiainfo.monitor.mapper;

import com.asiainfo.monitor.entity.CatchFileUpload;
import com.asiainfo.monitor.entity.ChargingDownloadConfig;
import com.asiainfo.monitor.entity.ChargingHostInfo;
import com.asiainfo.monitor.entity.WarningCallConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountMonitorMapper {
    List<ChargingDownloadConfig> queryChargingDownloadConfigByDay(@Param("uploadFrequency") String uploadFrequency);

    List<CatchFileUpload> queryWhetherHaveFile(@Param("querySql") String querySql);

    List<String> querySideWhetherDifferenceFile(@Param("sideQuerySql") String sideQuerySql);

    Integer updateWaringCallSwitch(@Param("warningId") Long warningId, @Param("warningFlag") Integer warningFlag);

    Integer queryWarningFlag(@Param("waringId") Long waringId);

    List<WarningCallConfig> queryAllWarningFlag();

    List<String> queryAccountWhetherHaveFile(@Param("querySql") String querySql);

    Integer queryFileNum(@Param("querySql") String querySql);

    List<CatchFileUpload> queryMessageProv(@Param("querySql") String querySql);

    List<String> queryGlodDB(@Param("querySql") String querySql);

    List<CatchFileUpload> commQuery(@Param("querySql") String querySql);

    List<String> queryFileNumByFileName(@Param("sideQuerySql") String sideQuerySql);

    List<ChargingDownloadConfig> queryFTPCollectInfo();

    ChargingHostInfo queryChargingHostInfoByHost(@Param("hostCenter") String hostCenter);

    List<ChargingDownloadConfig> queryDBCollectInfo();
}
