package com.icss.mvp.service;

import com.icss.mvp.dao.CodeGainTypeDao;
import com.icss.mvp.entity.CodeGainType;
import com.icss.mvp.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CodeGainTypeService {
	 @Autowired
	 private CodeGainTypeDao dao;

	 /**
	 * @Description: 保存代码统计方式
	 * @author Administrator  
	 * @param textMeanType 
	 * @param online 
	 * @date 2018年6月5日  
	  */
  public int saveCodeGainType(String type, String no, String textMeanType, String online,
      String trusted, String po) throws Exception {
    
    saveCodeGainTypeByParameterId(type, no, 160);
    saveCodeGainTypeByParameterId(textMeanType, no, 161);
    saveCodeGainTypeByParameterId(online, no, 162);

    if ("1".equals(online)) {// 下线
      saveCodeGainTypeByParameterId(online, no, 163);
      saveCodeGainTypeByParameterId(online, no, 164);

      String projectPO = dao.queryProjectPO(no);
      if (StringUtils.isNotBlank(po)) {
        if (StringUtils.isNotBlank(projectPO)) {
          dao.updateProjectPO(no, po);
        } else {
          dao.addProjectPO(no, po);
        }
      }
    } else if ("0".equals(online)) {// 上线
      saveProjectQualityFlag(no);
    }
    /*if ("1".equals(trusted)) {// 非可信项目
      saveCodeGainTypeByParameterId(trusted, no, 164);
    } else if ("0".equals(trusted)) {// 是可信项目
      saveCodeGainTypeByParameterId(dao.getReliableStaticMeasureCount(no) > 0 ? trusted : "1", no,
          164);
    }*/
    return 0;
  }

  public int saveCodeGainTypeByParameterId(String type, String no, int parameterId) {
    // 判断是否存在该记录
    Integer count = dao.getCodeTypeNum(no, parameterId + "");
    CodeGainType cGainType = new CodeGainType();
    if (count > 0) {// 更新记录
      cGainType.setModifyTime(new Date());
      cGainType.setType(type);
      cGainType.setParameterId(parameterId);
      cGainType.setNo(no);
      dao.updateCodeGainType(cGainType);
    } else {// 新增记录
      cGainType.setCreateTime(new Date());
      cGainType.setModifyTime(new Date());
      cGainType.setType(type);
      cGainType.setNo(no);
      cGainType.setParameterId(parameterId);
      dao.saveCodeGainType(cGainType);
    }
    if (parameterId > 161) {
      String date = DateUtils.getLatestCycles(1, true).get(0);
      dao.saveProjectParamterRecord(cGainType);
      cGainType.setModifyTime(DateUtils.parseDate(date, "yyyy-MM-dd"));
      dao.saveProjectParamterRecord(cGainType);
    }
    return 0;
  }
	 /**
	 * @Description: 获取配置方式
	 * @author Administrator  
	 * @date 2018年6月5日  
	  */
	public List<CodeGainType> getCodeTypeByNo(String no) throws Exception{
		List<CodeGainType> codeGainType = dao.getCodeTypeByNo(no,"160,161,162,165");

		// 1-项目结项
		Integer isClose = dao.getClose(no, "1");
		String po = dao.queryProjectPO(no);
		
		if (null != codeGainType && codeGainType.size() > 0) {
			for (CodeGainType codeGT : codeGainType) {
				if (null != isClose) {
					if (codeGT.getParameterId() == 162) {
						codeGT.setCloseStatus(isClose);
					} 
				}
				if(StringUtils.isNotBlank(po)){
					if (codeGT.getParameterId() == 162) {
						codeGT.setPo(po);
					} 
				}
			}
		}

		return codeGainType;
	}

	public List<CodeGainType> getCodeTypeByNoParameterId(String projNo,String parameterId) {
		List<CodeGainType> codeGainType = dao.getCodeTypeByNo(projNo,parameterId);
		return codeGainType;
	}

	public Map<String, Object> emailConfig(String no, String emailUrl, String emailOnOff) {
		//判断是否存在该记录
		List<Map<String,Object>> count = dao.getEmailInfo(no);
		 if(count==null||count.size()<=0) {//新增记录
			 dao.saveEmailInfo(no,emailUrl,emailOnOff);
		 }else {//更新记录
			 dao.updateEmailInfo(no,emailUrl,emailOnOff);
		 }
		return null;
	}

	public Map<String, Object> getEmailConfig(String no) {
		List<Map<String,Object>> count = dao.getEmailInfo(no);
		 if(count==null||count.size()<=0) {
			 return new HashMap<>();
		 }
		return count.get(0);
	}
	public void saveProjectQualityFlag(String proNo) {
	  List<CodeGainType> codeList= getCodeTypeByNoParameterId(proNo, "162");
	  //项目上线为重点项目
	  if(null != codeList && codeList.size()>0) {
      if("0".equals(codeList.get(0).getType())) {
        //是否配置静态代码质量指标
        int num1 = dao.getStaticMeasureCount(proNo);
        String type1 = "1";
        if(num1>0) {
          type1 = "0";
        } 
        saveCodeGainTypeByParameterId(type1, proNo, 163);
      }
      //是否配置可信指标
      int num2 = dao.getReliableStaticMeasureCount(proNo);
      String type2 = "1";
      if(num2>0) {
        type2 = "0";
      } 
      saveCodeGainTypeByParameterId(type2, proNo, 164);
    }
    
	}

}
