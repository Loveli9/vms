package com.icss.mvp.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.NetworkSecurity;
import com.icss.mvp.entity.common.request.PageRequest;

public interface NetworkSecurityDao {

	int saveNetworkSecurity(NetworkSecurity netSec);
	
	Integer queryNetworkSecurityCount(@Param("proNos")Set<String> proNos, @Param("date")String date,@Param("username")String username,@Param("coopType")String coopType);

	List<NetworkSecurity> queryNetworkSecurityList(@Param("proNos")Set<String> proNos, @Param("date")String date, @Param("type")String type, @Param("page")PageRequest page,
			@Param("username")String username,@Param("coopType")String coopType);

	NetworkSecurity getNetworkSecurityByKey(@Param("proNo")String proNo, @Param("date")String date);

}
