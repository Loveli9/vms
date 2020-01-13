package com.icss.mvp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.dao.NetworkSecurityDao;
import com.icss.mvp.entity.NetworkSecurity;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.util.CookieUtils;
import com.icss.mvp.util.DateUtils;
@Service
public class NetworkSecurityService {

	@Autowired
	private NetworkSecurityDao netSecDao;
	
	@Resource
	private HttpServletRequest request;
	
	@Autowired
	private ProjectInfoService projectInfoService;
	
	public TableSplitResult<List<NetworkSecurity>> queryNetworkSecurity(NetworkSecurity netSec, String date, String type, PageRequest page) {
		TableSplitResult<List<NetworkSecurity>> ret = new TableSplitResult<List<NetworkSecurity>>();
		Set<String> nos = projectInfoService.projectNos(netSec, null);
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		if(nos.size()<=0) {
			ret.setErr(new ArrayList<>(), 1);
		} else {
			if("15".equals(date.substring(8))) {
				date = DateUtils.SHORT_FORMAT_GENERAL.format(DateUtils.getMonthLastDay(date.substring(0,7)));
			}
			Integer count = netSecDao.queryNetworkSecurityCount(nos, date,username,netSec.getCoopType());
			if(count<=0) {
				ret.setErr(new ArrayList<>(), page.getPageNumber());
			} else {
				List<NetworkSecurity> list =netSecDao.queryNetworkSecurityList(nos, date, type, page,username,netSec.getCoopType());
				ret.setRows(list);
				ret.setTotal(count);
			}
		}
		return ret;
	}

	public List<NetworkSecurity> queryNetworkSecurityList(NetworkSecurity netSec, String date, String type) {
		Set<String> nos = projectInfoService.projectNos(netSec, null);
		if (nos.size() <= 0) {
			return new ArrayList<>();
		} else {
			if ("15".equals(date.substring(8))) {
				date = DateUtils.SHORT_FORMAT_GENERAL.format(DateUtils.getMonthLastDay(date.substring(0, 7)));
			}
			return netSecDao.queryNetworkSecurityList(nos, date, type, new PageRequest(),null,null);
		}
	}
	
	public NetworkSecurity getNetworkSecurityByKey(String proNo, String date) {
		return netSecDao.getNetworkSecurityByKey(proNo, date);
	}

	public List<Map<String, String>> getMonths(int num) {
		List<Map<String, String>> months = new ArrayList<Map<String, String>>();
		String[] monthArray = DateUtils.getLatestMonths(num);
		for (int i = monthArray.length; i > 0; i--) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("text", monthArray[i-1]);
			map.put("value", DateUtils.SHORT_FORMAT_GENERAL.format(DateUtils.getMonthLastDay(monthArray[i-1])));
			months.add(map);
		}	
		return months;
	}
	
	public void saveNetworkSecurity(NetworkSecurity netSec) {
		netSecDao.saveNetworkSecurity(netSec);
	}
}
