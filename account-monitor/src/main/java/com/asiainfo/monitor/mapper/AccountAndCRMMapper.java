package com.asiainfo.monitor.mapper;


import com.asiainfo.monitor.entity.ACheckResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AccountAndCRMMapper {
    List<ACheckResult> queryACheckResults();
}
