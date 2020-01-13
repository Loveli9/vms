package com.icss.mvp.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.icss.mvp.dao.*;
import com.icss.mvp.entity.*;
import com.icss.mvp.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@SuppressWarnings("all")
public class VersionStrucService {
	
	@Autowired
	private VersionStrucDao dao;
	@Autowired
	private VerificationCycleService verificationCycleService;
	
	@Autowired
	private IPersonalBulidTimeDao personalBulidTimeDao;
	
	@Resource
	private IProjectParameterValueDao projectParameterValueDao;
	
	@Autowired
	private IProjectSourceConfigDao sourceDao;
	
	@Autowired
    private ScripInfoService InfoService;

	@Autowired
	ISvnTaskDao svnTaskDao;
	
	@Autowired
	private MeasureLoadEveryDayDao measureLoadEveryDaydao;
	
	@Autowired
	private TestMeasuresService testMeasuresService;
	
	private static Logger logger=Logger.getLogger(VersionStrucService.class);

	@Value("${micro_service_icp_url}")
  private String icpUrl;
	
	/**
	* @Description: 查询版本级构建时长指标  (项目级图表)
	* @author Administrator  
	* @date 2018年6月20日  
	 */
	public Map<String, Object> queryVersionStrucInfo(String no,String yearNow) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		if(StringUtils.isEmpty(yearNow)) {
		yearNow = format.format(new Date());
	    }
		Map<String, Object> map = new HashMap<String, Object>();
		String months[] = {"01","02","03","04","05","06","07","08","09","10","11","12"};
		for (String month : months) {
			double value = 0;
			//计算截止当前日期的构建时间
			Double buildTime = dao.queryBuildTimes(yearNow+month,no);
			Double buildCounts = dao.queryBuildCounts(yearNow+month, no);
			if(buildTime*buildCounts > 0) {
				value = buildTime/buildCounts;
			}
            // String month2 = EMonth.fromMonth(month).toString();
            // map.put(month2, StringUtilsLocal.keepTwoDecimals(value));
            map.put(LocalDateUtils.getAbbreviatedMonth(month).toUpperCase(), StringUtilsLocal.keepTwoDecimals(value));
		}
		return map;
	}
	
	
	/**
	* @Description:xml解析
	* @author Administrator  
	* @date 2018年6月21日  
	 */
    public void JDOM(String proNo,String localBaseDir) {  
    	//获取当前项目版本构建文件集合  
    	//List<String> filePaths = getAllFile("D:\\log\\build\\"+ysCode+"\\",false);
    	List<String> filePaths = getAllFile(localBaseDir,false);
    	
    	List<VersionStrucInfo> list = new ArrayList<VersionStrucInfo>();
    	SAXBuilder builder=new SAXBuilder();  
    	 for (String url : filePaths) {//遍历解析文件夹
    		 
    		 //获取构建文件名称
    		 File file = new File(url);
 	         String strParentDirectory = file.getParent();
 	         File file1 = new File(strParentDirectory);
 	         String fileBuildName = file1.getName();
 	         // System.out.println("文件的上级目录为 : " + fileBuildName);
 	         InputStream is = null; 	         
    		 try {
    			 is = new FileInputStream(url);
    			 Document document = builder.build(is);  
                 Element buildSummer = document.getRootElement();  
                  
                 List<Element> bs_list = buildSummer.getChildren();  
                 for (Element buildNode : bs_list) {
                	 VersionStrucInfo vStrucInfo  = new VersionStrucInfo();
                  	 String dName = buildNode.getName();
                  	
                  	 if("data".equals(dName.trim())) {//获取data节点
                  		 List<Element> recordChildren = buildNode.getChildren(); //获取record节点
                  		 for (Element recordNode : recordChildren) {
                  			 List<Attribute> record_attr = recordNode.getAttributes(); 
                  			 for (Attribute attr : record_attr) {  
                                   //System.out.println(attr.getName()+":"+attr.getValue());  
                                   if("ci_build_info".equals(attr.getValue().trim())) {
                                  	 List<Element> rc_list = recordNode.getChildren();
                                  	 for (Element buildDetail : rc_list) {
                                  		 if("start_time".equals(buildDetail.getName())){
                                  			 if(buildDetail.getValue() != null) {
                                  				 vStrucInfo.setStartTime(dateFormateTime(buildDetail.getValue()));  
                                  			 }
                                  		 }else if("end_time".equals(buildDetail.getName())){
                                  			 if(buildDetail.getValue() != null) {
                                  				 vStrucInfo.setEndTime(dateFormateTime(buildDetail.getValue()));  
                                  			 }
                                  		 }else if("build_result".equals(buildDetail.getName())){
                                  			 if(buildDetail.getValue() != null) {
                                  				 vStrucInfo.setBuildResult(buildDetail.getValue());  
                                  			 }
                                  		 }else if("build_type".equals(buildDetail.getName())) {
                                  				 vStrucInfo.setBuildType(buildDetail.getValue());
                                  		 }
  									}
                                   }else {
                                  	 break;
                                   }
                               } 
                  			if(vStrucInfo != null) {
                  				vStrucInfo.setId(UUID.randomUUID().toString());
                  			    //存储时间戳文件夹名称
                              	vStrucInfo.setBuildContent(fileBuildName);
                              	vStrucInfo.setNo(proNo);
                  				list.add(vStrucInfo);  
                  			}
  							vStrucInfo = null;  
  						}
                  		 
                  	}else {
                  		continue;
                  	}
                  } 
                  if(list.size()>0) {
                  	dao.saveVersionBuildInfo(list);
                  }
              }catch (Exception e) { 
              	logger.error(e.getMessage());
              }finally {
      			if(null != is) {
    				try {
    					is.close();
    				} catch (IOException e) {
    					// TODO: handle exception
    					logger.error(e.getMessage());
    				}
    			}
    		}	   	 
		}
    }
    
    /**
     * 质量看板数据获取
     * @param localBaseDir
     */
    public void JDOM2(String projNo,String localBaseDir) {  
    	List<String> filePaths = getAllFile(localBaseDir,false);
    	List<ParameterValueNew> list =new ArrayList<>();
    	SAXBuilder builder=new SAXBuilder();  
    	for (String url : filePaths) {//遍历解析文件夹
    		 //构建文件
			File file = new File(url);
			
			if (file.length() == 0) {
				continue;
			}

			if ("build_summary.xml".equals(file.getName())) {
				JDOMSummary(projNo, list, builder, url);
			} else if ("coverage_result.xml".equals(file.getName())) {
				JDOMCoverage(projNo, list, builder, url);
			} else {
				Date date = getEndTime(builder, file);
				
				if (null == date) {
					continue;
				}
				
				if ("CodeDEX.xml".equals(file.getName())) {
					JDOMCodeDEX(projNo, list, builder, url, date);
				} else if ("UADPGuarding.xml".equals(file.getName())) {
					JDOMUADPGuarding(projNo, list, builder, url, date);
				} else if ("sourcemonitor.xml".equals(file.getName())) {
					JDOMSourcemonitor(projNo, list, builder, url, date);
				} else if ("CsecCheck.xml".equals(file.getName())) {
					JDOMCsecCheck(projNo, list, builder, url, date);
				} else if ("cct.xml".equals(file.getName())) {
					JDOMCct(projNo, list, builder, url, date);
				} else if ("py_complexity.xml".equals(file.getName())) {
					JDOMPyComplexity(projNo, list, builder, url, date);
				} else if ("coverage_result.xml".equals(file.getName())) {
					JDOMCoverage(projNo, list, builder, url);
				} else if (file.getName().startsWith("st_") && file.getName().indexOf("-") == -1
						&& file.getName().endsWith(".xml")) {
					JDOMStXml(projNo, list, builder, file, date);
				}
			}
		}
    	if(list.size()>0) {
    		projectParameterValueDao.insertParams(list);
        }  
    }
    
    private void JDOMCodeDEX(String projNo, List<ParameterValueNew> list, SAXBuilder builder, String url, Date endTime) {
    	InputStream is = null;
    	try {
			is = new FileInputStream(url);
			Document document = builder.build(is);  
			Element buildSummer = document.getRootElement(); 
			List<Element> layers = buildSummer.getChildren("layer");              
			for (Element layerNode : layers) { 
				List<Element> summarys = layerNode.getChildren("summary"); //获取子节点
		 		for (Element summary : summarys) {
		 			if("java".equals(summary.getAttributeValue("language").trim())) {
		 				List<Element> modules = summary.getChildren("module");
		 				if(null != modules.get(0).getAttributeValue("coverity")) {
		 					ParameterValueNew parameterValueNew =new ParameterValueNew();
			 				parameterValueNew.setNo(projNo);
			     			parameterValueNew.setParameterId(4);
			     			parameterValueNew.setValue(Double.parseDouble(modules.get(0).getAttributeValue("coverity")));
			     			parameterValueNew.setMonth(endTime);
			     			list.add(parameterValueNew);
		 				}
		 				
		 				if(null != modules.get(0).getAttributeValue("fortify")) {
		     			ParameterValueNew parameterValueNew =new ParameterValueNew();
		 				parameterValueNew.setNo(projNo);
		     			parameterValueNew.setParameterId(5);
		     			parameterValueNew.setValue(Double.parseDouble(modules.get(0).getAttributeValue("fortify")));
		     			parameterValueNew.setMonth(endTime);
		     			list.add(parameterValueNew);    
		 				}
		 			}
		 		}
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (JDOMException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} finally {
			if(null != is) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO: handle exception
					logger.error(e.getMessage());
				}
			}
		}	   	
    }

    private void JDOMUADPGuarding(String projNo, List<ParameterValueNew> list, SAXBuilder builder, String url, Date endTime) {
    	InputStream is = null;
    	try {
			is = new FileInputStream(url);
			Document document = builder.build(is);  
			Element buildSummer = document.getRootElement();  
			List<Element> layers = buildSummer.getChildren("layer");  
			for (Element layerNode : layers) { 
				List<Element> summarys = layerNode.getChildren("summary"); 
				for (Element summaryNode : summarys) {
					Element version = summaryNode.getChild("version");
					ParameterValueNew parameterValueNew =new ParameterValueNew();
					parameterValueNew.setNo(projNo);
		 			parameterValueNew.setParameterId(6);
		 			parameterValueNew.setValue(Double.parseDouble(version.getAttributeValue("eoa")));//6 SAI（当前值）
		 			parameterValueNew.setMonth(endTime);
		 			list.add(parameterValueNew);
				}
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (JDOMException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} finally {
			if(null != is) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO: handle exception
					logger.error(e.getMessage());
				}
			}
		}	
    }
    
    private void JDOMSourcemonitor(String projNo, List<ParameterValueNew> list, SAXBuilder builder, String url, Date endTime) {
    	InputStream is = null;
    	try {
			is = new FileInputStream(url);
    		double functionCyclomaticDefault = 0.0;//函数圈复杂度违约   		
    		Document document = builder.build(is);
    		Element cruisecontrol = document.getRootElement();  
    		List<Element> layers = cruisecontrol.getChildren("layer");
    		for(Element layer:layers) {
    			Element sourcemonitor = layer.getChild("sourcemonitor_metrics");
    			List<Element> depthComps = sourcemonitor.getChildren("depth_comp");
    			if(depthComps!=null&&depthComps.size()!=0) {
    				for(Element depthComp:depthComps) {
    					Integer value = depthComp.getValue()==null||"".equals(depthComp.getValue())?0:Integer.valueOf(depthComp.getValue());
    					if(value>50) {
    						functionCyclomaticDefault++;
    					}
    				}
    			}
    		}
    		ParameterValueNew parameterValueNew =new ParameterValueNew();
			parameterValueNew.setNo(projNo);
 			parameterValueNew.setParameterId(25);
 			parameterValueNew.setValue(functionCyclomaticDefault);//25 函数圈复杂度违约   	
 			parameterValueNew.setMonth(endTime);
 			list.add(parameterValueNew);   		
		}  catch (FileNotFoundException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (JDOMException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} finally {
			if(null != is) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO: handle exception
					logger.error(e.getMessage());
				}
			}
		}	
    }
    
    private void JDOMCsecCheck(String projNo, List<ParameterValueNew> list, SAXBuilder builder, String url, Date endTime) {
    	InputStream is = null;
    	try {
			is = new FileInputStream(url);
    		Document document = builder.build(is);
    		Element cruisecontrol = document.getRootElement();  
    		List<Element> layers = cruisecontrol.getChildren("layer");
    		double addedHazardFunction = 0;//新增危险函数
    		for(Element layer:layers) {
    			Element csecCheck = layer.getChild("CsecCheck");
    			Element summary = csecCheck.getChild("summary");
    			String newUnsafeFunc=summary.getAttributeValue("new_unsafe_func");
    			String unsafeFunc=summary.getAttributeValue("unsafe_func");
    			if(newUnsafeFunc==null||"".equals(newUnsafeFunc)) {
    				addedHazardFunction+=Double.parseDouble(unsafeFunc);
    			}else {
    				addedHazardFunction+=Double.parseDouble(newUnsafeFunc);
    			}
    		}
    		ParameterValueNew parameterValueNew =new ParameterValueNew();
			parameterValueNew.setNo(projNo);
 			parameterValueNew.setParameterId(26);
 			parameterValueNew.setValue(addedHazardFunction);//26 新增危险函数  	
 			parameterValueNew.setMonth(endTime);
 			list.add(parameterValueNew); 
		} catch (NumberFormatException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (JDOMException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} finally {
			if(null != is) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO: handle exception
					logger.error(e.getMessage());
				}
			}
		}	
    }
    
    private void JDOMCct(String projNo, List<ParameterValueNew> list, SAXBuilder builder, String url, Date endTime) {
    	InputStream is = null;
    	try {
			is = new FileInputStream(url);
			Document document = builder.build(is);  
			Element buildSummer = document.getRootElement();  
			double sum = 0;//代码总量
			double javaProduct = 0;
			double cProduct = 0;
			double pythonProduct = 0;
			List<Element> layers = buildSummer.getChildren("layer"); 
    		for(Element layer:layers) {
    			Element cctresult = getCctresult(layer);
				if(null != cctresult) {
					Element summary = cctresult.getChild("Summary");
	    			Element language = summary.getChild("Language");
	    			sum += Double.parseDouble(language.getAttributeValue("NBNCSize"));
	    			
	    			String name = language.getAttributeValue("Name");
	    			if("JAVA".equals(name)) {
	    				javaProduct += Double.parseDouble(language.getAttributeValue("NBNCSize"));
	    			}else if("C++".equals(name)) {
	    				cProduct += Double.parseDouble(language.getAttributeValue("NBNCSize"));
	    			}else if("PYTHON".equals(name)) {
	    				pythonProduct += Double.parseDouble(language.getAttributeValue("NBNCSize"));
	    			}  	
				}
    			
    		}
    		
    		//获取开发人员数量
			Integer developPerson = measureLoadEveryDaydao.getDevelopPerson(projNo);
			//获取项目开始日期
			String startDate = measureLoadEveryDaydao.getProjectStartDate(projNo);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
			Date end = sdf.parse(sdf.format(endTime));
			Date start = sdf.parse(startDate);
			Double days = DateUtils.differenceTime(startDate, DateUtils.SHORT_FORMAT_GENERAL.format(endTime)) + 1;
			if(developPerson * days > 0){
				double javaRate = javaProduct/(developPerson*days);
				double cRate = cProduct/(developPerson*days);
				double pythonRate  = pythonProduct/(developPerson*days); 
				List<MeasureLoadEveryInfo> measureDetailList = new ArrayList<MeasureLoadEveryInfo>();
				//java ete 生产率
				measureDetailList.add(new MeasureLoadEveryInfo(projNo, endTime,"271", StringUtilsLocal.keepTwoDecimals(
                        javaRate) + ""));
				//java 迭代生产率
				measureDetailList.add(new MeasureLoadEveryInfo(projNo, endTime,"254", StringUtilsLocal.keepTwoDecimals(
                        javaRate) + ""));
				// python ete 生产率
				measureDetailList.add(new MeasureLoadEveryInfo(projNo, endTime,"270", StringUtilsLocal.keepTwoDecimals(
                        pythonRate) + ""));
				//web ete 生产率
				measureDetailList.add(new MeasureLoadEveryInfo(projNo, endTime,"272", StringUtilsLocal.keepTwoDecimals(
                        javaRate) + ""));
				//c ete 生产率
				measureDetailList.add(new MeasureLoadEveryInfo(projNo, endTime,"273", StringUtilsLocal.keepTwoDecimals(
                        cRate) + ""));
				//c 迭代生产率
				measureDetailList.add(new MeasureLoadEveryInfo(projNo, endTime,"255", StringUtilsLocal.keepTwoDecimals(
                        cRate) + ""));
				measureLoadEveryDaydao.insert(measureDetailList);
			}
			
			Double workHours = testMeasuresService.getProjectAllManpower(projNo, start, endTime);
			if(null!=workHours && workHours>0) {
				double javaRate = javaProduct/workHours; 
				double cRate = cProduct/workHours; 
				List<MeasureLoadEveryInfo> measureDetailList = new ArrayList<MeasureLoadEveryInfo>();
				//java 端到端生产率
				measureDetailList.add(new MeasureLoadEveryInfo(projNo, endTime,"266", StringUtilsLocal.keepTwoDecimals(
                        javaRate) + ""));
				//c 端到端生产率
				measureDetailList.add(new MeasureLoadEveryInfo(projNo, endTime,"267", StringUtilsLocal.keepTwoDecimals(
                        cRate) + ""));
				measureLoadEveryDaydao.insert(measureDetailList);
			}
    		ParameterValueNew parameterValueNew =new ParameterValueNew();
			parameterValueNew.setNo(projNo);
 			parameterValueNew.setParameterId(167);
 			parameterValueNew.setValue(sum);//167 代码总量 	
 			parameterValueNew.setMonth(endTime);
 			list.add(parameterValueNew); 
		} catch (NumberFormatException e) {
			// TODO: handle exception
			logger.error("NumberFormatException, error: "+e.getMessage());
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			logger.error("FileNotFoundException, error: "+e.getMessage());
		} catch (JDOMException e) {
			// TODO: handle exception
			logger.error("JDOMException, error: "+e.getMessage());
		} catch (IOException e) {
			// TODO: handle exception
			logger.error("IOException, error: "+e.getMessage());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("ParseException, error: "+e.getMessage());
		} finally {
			if(null != is) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO: handle exception
					logger.error(e.getMessage());
				}
			}
		}	
    }
    
    private Element getCctresult(Element layer) {
		List<Element> list = layer.getChildren();
		if(list.isEmpty()) {
			return null;
		}
		
		Element cctresult = null;
		for(Element child: list) {
			if("cctresult".equalsIgnoreCase(child.getName())) {
				cctresult = child;
				break;
			}
			
			cctresult = getCctresult(child);
			if(cctresult!=null) {
				break;
			}
		}
		
		return cctresult;
	}
    
    private void JDOMPyComplexity(String projNo, List<ParameterValueNew> list, SAXBuilder builder, String url, Date endTime) {
    	InputStream is = null;
    	try {
			is = new FileInputStream(url);
			Document document = builder.build(is);  
			Element buildSummer = document.getRootElement(); 
			Element layer = buildSummer.getChild("layer");
			if(null!=layer) {
				Element py_complexity_result = layer.getChild("py_complexity_result");
				if(null!=py_complexity_result) {
					Element summary = py_complexity_result.getChild("Summary");
					
					ParameterValueNew parameterValueNew1 =new ParameterValueNew();
					parameterValueNew1.setNo(projNo);
		 			parameterValueNew1.setParameterId(27);
		 			parameterValueNew1.setValue(Double.parseDouble(summary.getChildText("FunctionComplexity")));
		 			parameterValueNew1.setMonth(endTime);
		 			list.add(parameterValueNew1);
		 			
		 			ParameterValueNew parameterValueNew2 =new ParameterValueNew();
					parameterValueNew2.setNo(projNo);
		 			parameterValueNew2.setParameterId(3);
		 			parameterValueNew2.setValue(Double.parseDouble(summary.getChildText("AverageFunctionComplexity")));
		 			parameterValueNew2.setMonth(endTime);
		 			list.add(parameterValueNew2); 
				}
			}
			      
    	} catch (FileNotFoundException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (JDOMException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} finally {
			if(null != is) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO: handle exception
					logger.error(e.getMessage());
				}
			}
		}	
    }
    
    private void JDOMCoverage(String projNo, List<ParameterValueNew> list, SAXBuilder builder, String url) {
    	InputStream is = null;
    	try {
			is = new FileInputStream(url);
			Document document = builder.build(is);  
			Element buildSummer = document.getRootElement();
			String percent = buildSummer.getAttributeValue("percent");
			ParameterValueNew parameterValueNew =new ParameterValueNew();
			parameterValueNew.setNo(projNo);
 			parameterValueNew.setParameterId(28);
 			parameterValueNew.setValue(Double.parseDouble(percent.substring(0,percent.indexOf("%"))));//28 新增危险函数  	
 			parameterValueNew.setMonth(new Date());
 			list.add(parameterValueNew); 
    	} catch (FileNotFoundException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (JDOMException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} finally {
			if(null != is) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO: handle exception
					logger.error(e.getMessage());
				}
			}
		}	
    }
    
    private void JDOMStXml(String projNo, List<ParameterValueNew> list, SAXBuilder builder, File file, Date endTime) {
    	File[] files = file.getParentFile().listFiles();
		double rates =0;
		double count=0;
		for (File f : files) {
			if(f.getName().startsWith("st_")&& f.getName().indexOf("-")==-1) {
				rates += JDOMStXml(builder, f);
				count++;
			}
		}
		double rate = rates/count;
		rate = (double) Math.round(rate * 100) / 100;
		ParameterValueNew parameterValueNew =new ParameterValueNew();
		parameterValueNew.setNo(projNo);
		parameterValueNew.setParameterId(31);
		parameterValueNew.setValue(rate);//31 LLT用例自动化通过率
		parameterValueNew.setMonth(endTime);
		list.add(parameterValueNew); 
    }
    
    private double JDOMStXml(SAXBuilder builder, File file) {
    	InputStream is = null;
    	double rate = 0;
    	try {
			is = new FileInputStream(file);
			Document document = builder.build(is);  
			Element buildSummer = document.getRootElement(); 
			String tests = buildSummer.getAttributeValue("st_tests");
			String successes = buildSummer.getAttributeValue("st_successes");
			rate = Double.parseDouble(successes)/Double.parseDouble(tests)*100;
    	} catch (FileNotFoundException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (JDOMException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} finally {
			if(null != is) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO: handle exception
					logger.error(e.getMessage());
				}
			}
		}
		return rate;	  	
    }
    
    private Date getEndTime(SAXBuilder builder, File file) {
		Date endTime = null;
		File summaryFile = null;
		
		if (file.getName().startsWith("st_") && file.getName().indexOf("-") == -1
				&& file.getName().endsWith(".xml")) {
			summaryFile = new File(file.getParentFile().getAbsolutePath()+"\\build_summary.xml");
		} else {
			summaryFile = new File(file.getParentFile().getParentFile().getAbsolutePath()+"\\build_summary.xml");
		}
		
		if(summaryFile.exists() && summaryFile.length()>0) {
			InputStream is = null;
			try {
				is = new FileInputStream(summaryFile);
				Document document = builder.build(is);
				Element buildSummer = document.getRootElement();  
				Element data = buildSummer.getChild("data");
				List<Element> records = data.getChildren();
				for (Element recordNode : records) {				
					if("ci_build_info".equals(recordNode.getAttributeValue("table").trim())) {
						endTime=dateFormateTime(recordNode.getChild("end_time").getValue());
						break;
					}
				}
			} catch (FileNotFoundException e) {
				// TODO: handle exception
				logger.error(e.getMessage());
			} catch (JDOMException e) {
				// TODO: handle exception
				logger.error(e.getMessage());
			} catch (IOException e) {
				// TODO: handle exception
				logger.error(e.getMessage());
			} finally {
				if(null != is) {
					try {
						is.close();
					} catch (IOException e) {
						// TODO: handle exception
						logger.error(e.getMessage());
					}
				}
			}
		}
		return endTime;
	}

	private void JDOMSummary(String projNo, List<ParameterValueNew> list, SAXBuilder builder, String url) {
		InputStream is = null;
		try {
			is = new FileInputStream(url);
			Document document = builder.build(is);  
			Element buildSummer = document.getRootElement();
			Element data = buildSummer.getChild("data");
			List<Element> records = data.getChildren();
			Date endTime=null;
			//获取日期
			for (Element recordNode : records) {				
				if("ci_build_info".equals(recordNode.getAttributeValue("table").trim())) {
					endTime=dateFormateTime(recordNode.getChild("end_time").getValue());
					break;
				}
			}
			//获取指标值
			for (Element recordNode : records) {
				List<Attribute> record_attr = recordNode.getAttributes(); 
				for (Attribute attr : record_attr) {
					if("ci_plugin_metric".equals(attr.getValue().trim())) { //2 代码重复率
						String simianDuplicationRate = recordNode.getChild("simianDuplicationRate").getValue();				
						ParameterValueNew parameterValueNew =new ParameterValueNew();
						parameterValueNew.setNo(projNo);
						parameterValueNew.setParameterId(2);
						double rate = Double.parseDouble(simianDuplicationRate);
						rate = (double) Math.round(rate * 100) / 100;
						parameterValueNew.setValue(rate);
						parameterValueNew.setMonth(endTime);
						list.add(parameterValueNew);
			           /* List<Element> rc_list = recordNode.getChildren();
			            for (Element buildDetail : rc_list) {
							ParameterValueNew parameterValueNew =new ParameterValueNew();
			             	if(buildDetail.getName().equals("simianDuplicationRate")){  //代码重复率 改为2
			             		parameterValueNew.setNo(projNo);
			             		parameterValueNew.setParameterId(2);
			             		parameterValueNew.setValue(Double.parseDouble(buildDetail.getValue()));
			             		parameterValueNew.setMonth(endTime);
			             		list.add(parameterValueNew);
			             	}
							} */
			        }else if("ci_plugin_sourcemonitor".equals(attr.getValue().trim())){//3 平均圈复杂度
						/*List<Element> rc_list = recordNode.getChildren();
			           	for (Element buildDetail : rc_list) {
							ParameterValueNew parameterValueNew =new ParameterValueNew();
			           		if(buildDetail.getName().equals("maxcomplexity")){  //163 代码最大圈复杂度
								parameterValueNew.setNo(projNo);
			           			parameterValueNew.setParameterId(163);
			           			parameterValueNew.setValue(Double.parseDouble(buildDetail.getValue()));
			           			parameterValueNew.setMonth(endTime);
			           			list.add(parameterValueNew);
			           		}
			           		if(buildDetail.getName().equals("complexity")){  //平均圈复杂度改为3
			            		parameterValueNew.setNo(projNo);
			            		parameterValueNew.setParameterId(3);
			            		parameterValueNew.setValue(Double.parseDouble(buildDetail.getValue()));
			            		parameterValueNew.setMonth(new Date());
			            		list.add(parameterValueNew);
			            	}
						} */
			        	String complexity = recordNode.getChild("complexity").getValue();
						String methods = recordNode.getChild("methods").getValue();
						double avg = Double.parseDouble(complexity)/Double.parseDouble(methods);
						avg = (double) Math.round(avg * 100) / 100;
						ParameterValueNew parameterValueNew =new ParameterValueNew();
						parameterValueNew.setNo(projNo);
						parameterValueNew.setParameterId(3);
						parameterValueNew.setValue(avg);
						parameterValueNew.setMonth(endTime);
						list.add(parameterValueNew);                    	
			        }else if("ci_plugin_pclint".equals(attr.getValue().trim())){//1 静态检查Pc-lintError\warning级别清零
						/*List<Element> rc_list = recordNode.getChildren();
			         	for (Element buildDetail : rc_list) {
			         		ParameterValueNew parameterValueNew =new ParameterValueNew();
							if(buildDetail.getName().equals("errors")){  //1  静态检查Pc-lintError\warning级别清零
								parameterValueNew.setNo(projNo);
			         			parameterValueNew.setParameterId(1);
			         			parameterValueNew.setValue(Double.parseDouble(buildDetail.getValue()));
			         			parameterValueNew.setMonth(endTime);
			         			list.add(parameterValueNew);
			         		}
//                 		if(buildDetail.getName().equals("warnings")){  //165  静态检查Pc-lintError\warning级别清零
//                 			parameterValueNew.setNo(projNo);
//                 			parameterValueNew.setParameterId(165);
//                 			parameterValueNew.setValue(Double.parseDouble(buildDetail.getValue()));
//                 			parameterValueNew.setMonth(endTime);
//                 			list.add(parameterValueNew);
//                 		 }
							 } */
			        	String errors = recordNode.getChild("errors").getValue();
						String warnings = recordNode.getChild("warnings").getValue();
						ParameterValueNew parameterValueNew =new ParameterValueNew();
						parameterValueNew.setNo(projNo);
						parameterValueNew.setParameterId(1);
						parameterValueNew.setValue(Double.parseDouble(errors)+Double.parseDouble(warnings));
						parameterValueNew.setMonth(endTime);
						list.add(parameterValueNew);
			        }else if("ci_plugin_findbugs".equals(attr.getValue().trim())){//166静态检查FindBugsHigh级别清零
						/*List<Element> rc_list = recordNode.getChildren();
						for (Element buildDetail : rc_list) {
							ParameterValueNew parameterValueNew =new ParameterValueNew();
							if(buildDetail.getName().equals("priority_1")){  //166静态检查FindBugsHigh级别清零 改为1 
								parameterValueNew.setNo(projNo);
								parameterValueNew.setParameterId(1);
								parameterValueNew.setValue(Double.parseDouble(buildDetail.getValue()));
								parameterValueNew.setMonth(endTime);
								list.add(parameterValueNew);
							}
						} */
			        	String priority1 = recordNode.getChild("priority_1").getValue();
						String priority2 = recordNode.getChild("priority_2").getValue();
						ParameterValueNew parameterValueNew =new ParameterValueNew();
						parameterValueNew.setNo(projNo);
						parameterValueNew.setParameterId(1);
						parameterValueNew.setValue(Double.parseDouble(priority1)+Double.parseDouble(priority2));
						parameterValueNew.setMonth(endTime);
						list.add(parameterValueNew);
			       	}else if("ci_plugin_simian".equals(attr.getValue().trim())) { //2 代码重复率
			       		String duplicateLineCount = recordNode.getChild("duplicateLineCount").getValue();
						String totalSignificantLineCount = recordNode.getChild("totalSignificantLineCount").getValue();
						double rate = Double.parseDouble(duplicateLineCount)/Double.parseDouble(totalSignificantLineCount)*100;
						rate = (double) Math.round(rate * 100) / 100;
						ParameterValueNew parameterValueNew =new ParameterValueNew();
						parameterValueNew.setNo(projNo);
						parameterValueNew.setParameterId(2);
						parameterValueNew.setValue(rate);
						parameterValueNew.setMonth(endTime);
						list.add(parameterValueNew);
			       	}else if("ci_plugin_checkstyle".equals(attr.getValue().trim())) {
			       		ParameterValueNew parameterValueNew =new ParameterValueNew();
						parameterValueNew.setNo(projNo);
						parameterValueNew.setParameterId(29);
						parameterValueNew.setValue(Double.parseDouble(recordNode.getChild("errors").getValue()));
						parameterValueNew.setMonth(endTime);
						list.add(parameterValueNew);
						list.add(parameterValueNew);
			       	}else if("ci_plugin_jshint".equals(attr.getValue().trim())) {
						ParameterValueNew parameterValueNew =new ParameterValueNew();
						parameterValueNew.setNo(projNo);
						parameterValueNew.setParameterId(30);
						parameterValueNew.setValue(Double.parseDouble(recordNode.getChild("error").getValue()));
						parameterValueNew.setMonth(endTime);
						list.add(parameterValueNew);
			       	}else{
			       		break;
			       	}   				 
				}
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (JDOMException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		} finally {
			if(null != is) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO: handle exception
					logger.error(e.getMessage());
				}
			}
		}	
	}

    /**
    * @Description: 日期格式转换  
    * @author Administrator  
    * @date 2018年6月21日  
     */
    public  Date dateFormateTime(String timeStr) {
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	if(StringUtils.isNotEmpty(timeStr)) {
    		try {
				return sdf.parse(timeStr);
			} catch (ParseException e) {
				logger.error("日期格式转换错误");
				logger.error("sdf.parse exception, error: "+e.getMessage());
			}
    	}
    	return null;
    }
    
    /**
     * 获取路径下的所有文件/文件夹
     * @param directoryPath 需要遍历的文件夹路径
     * @param isAddDirectory 是否将子文件夹的路径也添加到list集合中
     * @return
     */
    public static List<String> getAllFile(String directoryPath,boolean isAddDirectory) {
        List<String> list = new ArrayList<String>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if(isAddDirectory){
                    list.add(file.getAbsolutePath());
                }
                list.addAll(getAllFile(file.getAbsolutePath(),isAddDirectory));
            } else {
                list.add(file.getAbsolutePath());
            }
        }
        return list;
    }
	
	
	/**
	* @Description: 定时器（指定每晚12点从ftp服务器获取所有项目版本构建文件并解析）
	* @author Administrator  
	* @date 2018年6月27日  
	 */
//	@Transactional
//	@Scheduled(cron = "${ftp_Task_scheduled}")
	public void downFtpXmlAndParse() {
		//获取所有配置了版本构建参数的数据
		List<RepositoryInfo> infos = dao.queryIcpCiConfig();
		for (RepositoryInfo repositoryInfo : infos) {
		  Map<String, Object> parameter = new HashMap<String, Object>();
		  parameter.put("projectId", repositoryInfo.getNo());
		  parameter.put("repositoryId", repositoryInfo.getId());
		  parameter.put("requestId", System.currentTimeMillis());
		  String response = HttpExecuteUtils.httpGet(icpUrl + "/icpci/collectData", parameter);
		  /*FtpInfo f = new FtpInfo();
			f.setUserName("plugin");//用户名
			f.setPwd("plugin");//密码
			f.setPort(16162);//固定端口号
			f.setIpAddr(repositoryInfo.getUrl());//服务ip
			if(!TextUtils.isEmpty(repositoryInfo.getScope())) {
				try {
					JSONObject jsonObject = JSON.parseObject(repositoryInfo.getScope());
					List<String> bb = (List<String>) jsonObject.get("excludePath");
					List<String> gc = (List<String>) jsonObject.get("excludeRevision");
					String bbName = bb.get(0).trim();//版本构建
					String gcName = gc.get(0).trim();//工程能力
					f.setBbName(bbName);
					f.setGcName(gcName);
					f.setToken(UUIDUtil.getNew());
					f.setNo(repositoryInfo.getNo());
					downFtpXmlAndParse(f);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}*/
		}
	}
	
	/**
	* @Description: 解析文件：版本构建、工程能力
	* @author Administrator  
	* @date 2018年7月2日  
	* @version V1.0
	 */
	public int downFtpXmlAndParse(FtpInfo f) {
		int flag = 0;
		if(f!= null) {
			if(StringUtils.isNotEmpty(f.getBbName())) {
				String localBaseDir=downAndParse(f,f.getBbName(),1);//1:版本构建
				//下载成功后解析文件入库
				if(StringUtils.isNotEmpty(localBaseDir)) {
					JDOM(f.getNo(),localBaseDir);
				}else {
					flag = 1;
				}
			}
			if(StringUtils.isNotEmpty(f.getGcName())) {
				String localBaseDir=downAndParse(f,f.getGcName(),2);//2:工程能力
				//下载成功后解析文件入库
				if(StringUtils.isNotEmpty(localBaseDir)) {
					JDOM2(f.getNo(),localBaseDir);
				}else {
					flag = 1;
				}
			}
		}
		return flag;
	}
	
	/*
	 * 创建本地下载目录并下载文件
	 */
	private String downAndParse(FtpInfo f,String codeUrl,int type) {
		String localBaseDir = "";
		try {
			String strPath = "D:\\mylog\\build\\"+codeUrl;  
			File file = new File(strPath);   
			if(!file.exists()){  
			    file.mkdirs();  
			}  
			
			localBaseDir = "D:/mylog/build/"+codeUrl+"/";
			String remoteBaseDir = "/"+codeUrl+"/logs";

			int a =  FtpUtil.startDown(f, localBaseDir,  remoteBaseDir, type);//去指定ftp服务下载文件
			
			if(a == 1) {
			   ScripInfo scripInfo = new ScripInfo();
			   scripInfo.setMessage("更新ICP-CI数据失败：下载过程出现异常，请检查版本构建文件名称是否存在:"+f.getIpAddr());  
			   scripInfo.setNo(f.getNo());
			   scripInfo.setMesType("icp-ci");
			   scripInfo.setToken(f.getToken());
	    	   InfoService.insertErrorMessage(scripInfo);//5icp-ci采集
	    	   localBaseDir = "";
			}
			if(a == 2) {
			   ScripInfo scripInfo = new ScripInfo();
			   scripInfo.setMessage("更新ICP-CI数据失败：下载过程出现异常，请检查工程能力文件名称是否存在:"+f.getIpAddr());
			   scripInfo.setNo(f.getNo());
			   scripInfo.setMesType("icp-ci");
			   scripInfo.setToken(f.getToken());
	    	   InfoService.insertErrorMessage(scripInfo);//5icp-ci采集
	    	   localBaseDir = "";
			}
			remoteBaseDir = "/"+codeUrl+"/coverage";
			FtpUtil.startDown(f, localBaseDir,  remoteBaseDir, 3);//去指定ftp服务下载文件
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return localBaseDir;
	}


	/**
	* @Description:获取最新指标值(项目气泡)
	* @author Administrator  
	* @date 2018年6月28日  
	* @version V1.0
	 */
	public Double queryVersionStrucInfoNew(String no) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");
		String newDate = sdf.format(new Date());
		double value = 0;
		//计算截止当前日期的构建时间
		Double buildTime = dao.queryBuildTimes(newDate,no);
		Double buildCounts = dao.queryBuildCounts(newDate, no);
		if(buildTime*buildCounts > 0) {
			value = StringUtilsLocal.keepTwoDecimals(buildTime / buildCounts);
		}
		return value;
	}

	/**
	* @Description:查询部门级别的6+1所有指标(首页气泡)
	* @author Administrator  
	* @date 2018年7月3日  
	 */
	public Map<String, Object> queryIndexs(ProjectInfo proj) {
		List<String> prons = new ArrayList<String>();
		Map<String, Object> maps = setMap(proj);
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
		String yearMonth = proj.getYear()+dateFormat.format(new Date());
		Double value = 0.0;
		//查询项目编号
		List<ProjectInfo> listNos = dao.querySelectedNos(maps);
		for (ProjectInfo pros : listNos) {
			prons.add(pros.getNo());
		}
		String sqlStr = StringUtilsLocal.listToSqlIn(prons);
		
		//个人构建时长
		Double personBuildTime = personalBulidTimeDao.personBuildTime(yearMonth, "("+sqlStr+")");
		Double personBuildCount = personalBulidTimeDao.personBuildTimeCounts(yearMonth, "("+sqlStr+")");
		map.put("personBuild", StringUtilsLocal.keepTwoDecimals(
                (personBuildTime * personBuildCount) > 0 ? (personBuildTime / personBuildCount) : 0));
		
		//版本时长
		Double versionTime = dao.queryBuildTimesByNos(yearMonth, "("+sqlStr+")");
		Double versionBuildCounts = dao.queryBuildCountsByNos(yearMonth, "("+sqlStr+")");
		map.put("versionBuild", StringUtilsLocal.keepTwoDecimals(
                (versionTime * versionBuildCounts) > 0 ? (versionTime / versionBuildCounts) : 0));
		
		//回归验证周期
		Double rebackTime = verificationCycleService.getRebackTime("("+sqlStr+")",proj.getYear());
		map.put("rebackTime", StringUtilsLocal.keepTwoDecimals(rebackTime));
		//全量功能验证周期
		Double quanliangTime = verificationCycleService.getQuanliangTime("("+sqlStr+")",proj.getYear());
		map.put("quanliangTime", StringUtilsLocal.keepTwoDecimals(quanliangTime));
		//解决方案验证周期
		map.put("solveTime", StringUtilsLocal.keepTwoDecimals(quanliangTime));
		//特性CycleTime
		map.put("CycleTime",0);
		//story/功能点/特性s
		map.put("story",0);
		return map;
	}
	
	private Map<String, Object> setMap(ProjectInfo proj) {
		Map<String, Object> map = new HashMap<>();
		List<String> list = new ArrayList<>();
		if (proj.getArea() != null && !"".equals(proj.getArea())) {
			String[] arr = proj.getArea().split(",");
			map.put("area", Arrays.asList(arr));
		} else {
			map.put("area", list);
		}
		if (proj.getHwpdu() != null && !"".equals(proj.getHwpdu())) {
			String[] arr = proj.getHwpdu().split(",");
			map.put("hwpdu", Arrays.asList(arr));
		} else {
			map.put("hwpdu", list);
		}
		if (proj.getHwzpdu() != null && !"".equals(proj.getHwzpdu())) {
			String[] arr = proj.getHwzpdu().split(",");
			map.put("hwzpdu", Arrays.asList(arr));
		} else {
			map.put("hwzpdu", list);
		}
		if (proj.getPduSpdt() != null && !"".equals(proj.getPduSpdt())) {
			String[] arr = proj.getPduSpdt().split(",");
			map.put("pduSpdt", Arrays.asList(arr));
		} else {
			map.put("pduSpdt", list);
		}
		if (proj.getBu() != null && !"".equals(proj.getBu())) {
			String[] arr = proj.getBu().split(",");
			map.put("bu", Arrays.asList(arr));
		} else {
			map.put("bu", list);
		}
		if (proj.getPdu() != null && !"".equals(proj.getPdu())) {
			String[] arr = proj.getPdu().split(",");
			map.put("pdu", Arrays.asList(arr));
		} else {
			map.put("pdu", list);
		}
		if (proj.getDu() != null && !"".equals(proj.getDu())) {
			String[] arr = proj.getDu().split(",");
			map.put("du", Arrays.asList(arr));
		} else {
			map.put("du", list);
		}
		return map;
	}

	/**
	* @Description: 首页查询版本时长构建时长
	* @author Administrator  
	* @date 2018年7月4日  
	* @version V1.0
	 */
	public Map<String, Object> queryBbscByNos(ProjectInfo proj) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> prons = new ArrayList<String>();
		//查询项目编号
		Map<String, Object> maps = setMap(proj);
		List<ProjectInfo> listNos = dao.querySelectedNos(maps);
		for (ProjectInfo pros : listNos) {
			prons.add(pros.getNo());
		}
		String sqlStr = StringUtilsLocal.listToSqlIn(prons);
		String months[] = {"01","02","03","04","05","06","07","08","09","10","11","12"};
		for (String month : months) {
			double value = 0;
			//计算每个月多个项目的版本构建时长均值
			Double buildTime = dao.queryBuildTimesByNos(proj.getYear()+month,"("+sqlStr+")");
			Double buildCounts = dao.queryBuildCountsByNos(proj.getYear()+month, "("+sqlStr+")");
			if(buildTime*buildCounts > 0) {
				value = buildTime/buildCounts;
			}
            // String month2 = EMonth.fromMonth(month).toString();
            // map.put(month2, StringUtilsLocal.keepTwoDecimals(value));
            map.put(LocalDateUtils.getAbbreviatedMonth(month).toUpperCase(), StringUtilsLocal.keepTwoDecimals(value));
		}
		return map;
	}


	/*
	 * 从ftp服务采集并解析版本构建及工程能力数据
	 */
	public int icpCiCollect(String no, String token, String id) {
		int fg = 0;
		//获取ftp服务配置
		List<RepositoryInfo> infos = sourceDao.searchRepositoryByNos(no,"5",id);
		if (infos == null || !(infos.size()>0)) {
            return -1;
        }
		for (RepositoryInfo repositoryInfo : infos) {
			//判断ftp配置是否正确
			FtpInfo f = new FtpInfo();
			f.setIpAddr(repositoryInfo.getUrl());//ip
			f.setUserName("plugin");//用户名
			f.setPwd("plugin");//密码
			f.setPort(16162);//固定端口号
			f.setNo(no);
			boolean flag = false;
			flag = FtpUtil.connectFtp(f);
			if(flag) {
				if(StringUtils.isNotEmpty(repositoryInfo.getScope())) {
					JSONObject jsonObject = JSON.parseObject(repositoryInfo.getScope());
					List<String> bb = (List<String>) jsonObject.get("excludePath");
					List<String> gc = (List<String>) jsonObject.get("excludeRevision");
					
					String bbName = bb.get(0).trim();//版本构建
					String gcName = gc.get(0).trim();//工程能力
					f.setBbName(bbName);
					f.setGcName(gcName);
					f.setToken(token);
					fg = downFtpXmlAndParse(f);
				}
				Map<String, Object> map = new HashMap<>();
				map.put("NO", no);
				map.put("lasttime", new Date());
				map.put("id", id);
				svnTaskDao.insertlasttime(map);
			}else {
				   fg = 1;
				   ScripInfo scripInfo = new ScripInfo();
				   scripInfo.setMessage("更新ICP-CI数据失败：服务连接异常,请检查IP配置是否正确:"+repositoryInfo.getUrl());
				   scripInfo.setNo(no);
				   scripInfo.setMesType("error");
				   scripInfo.setToken(token);
		    	   InfoService.insertErrorMessage(scripInfo);//5icp-ci采集
			}
		}
		return fg;
	}
}
