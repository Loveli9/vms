package com.icss.mvp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.constant.Constants;
import com.icss.mvp.dao.IDtsTaskDao;
import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.entity.ParameterValueNew;
import com.icss.mvp.entity.ProjectParameterValue;

@SuppressWarnings("deprecation")
@Service
public class ExcelReader
{
	private InputStream is;

	private static final String[] HEADERS = { "参数大类型", "参数小类型", "参数名称", "单位",
			"数据源", "是否显示" };
	private static final Logger LOG = Logger.getLogger(ExcelReader.class);
	
	@Autowired
	private IDtsTaskDao dtsTaskDao;
	public ExcelReader()
	{
	}

	public ExcelReader(InputStream inputStream)
	{
		is = inputStream;
	}

	public Map<String, Object> read(String no) throws IOException,
			ParseException
	{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ParameterValueNew> list = new LinkedList<ParameterValueNew>();
		Workbook workbook = new XSSFWorkbook(is);
		Sheet sheet = workbook.getSheetAt(0);
		int rowSize = sheet.getLastRowNum();
		if (rowSize <= 1)
		{
			resultMap.put(Constants.STATUS, Constants.FAILED);
			resultMap.put(Constants.MESSAGE, "没有要导入的参数！");
			return resultMap;
		}
		Row row = null;
		Cell cell = null;
		int colSize = 0;
		String cell0Val, cellValue, month;
		ParameterValueNew param = null;
		for (int i = 2; i <= rowSize; i++)
		{
			row = sheet.getRow(i);
			colSize = row.getPhysicalNumberOfCells();
			cell0Val = getCellFormatValue(row.getCell(0));
			if (StringUtils.isNotEmpty(cell0Val) && isDouble(cell0Val))
			{
				if (colSize <= 7)
				{
					resultMap.put(Constants.STATUS, Constants.FAILED);
					resultMap.put(Constants.MESSAGE, "没有要导入的参数！");
					return resultMap;
				}
				for (int j = 7; j <= colSize; j++)
				{
					cellValue = getCellFormatValue(row.getCell(j));
					month = getCellFormatValue(sheet.getRow(1)
							.getCell(j));
					if (StringUtils.isEmpty(month))
					{
						continue;
					}
					if (!isDate(month ))
					{
						resultMap.put(Constants.STATUS, Constants.FAILED);
						resultMap.put(Constants.MESSAGE, "月份不符合格式要求！");
						return resultMap;
					}
					if (StringUtils.isEmpty(cellValue))
					{
						continue;
					}
					if (!isDouble(cellValue))
					{
						resultMap.put(Constants.STATUS, Constants.FAILED);
						resultMap.put(Constants.MESSAGE, "指标值需为数值！");
						return resultMap;
					}

					param = new ParameterValueNew();
					param.setNo(no);
					param.setParameterId(Double.valueOf(cell0Val).intValue());
					param.setMonth(str2Date(month));
					param.setValue(Double.parseDouble(cellValue));
					list.add(param);
				}
			}
		}
		resultMap.put(Constants.STATUS, Constants.SUCCESS);
		resultMap.put("DATA", list);
		return resultMap;

	}

	private Date str2Date(String month) throws ParseException
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		Date date = formatter.parse(month);
		return date;
	}

	private boolean isDate(String str)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		try
		{
			Date date = formatter.parse(str);
			return str.equals(formatter.format(date));
		} catch (ParseException e)
		{
			return false;
		}
	}

	private String getCellFormatValue(Cell cell)
	{
		String strCell = "";
		if (cell == null)
		{
			return strCell;
		}
		switch (cell.getCellType())
			{
			case XSSFCell.CELL_TYPE_STRING:
				strCell = cell.getStringCellValue();
				break;
			case XSSFCell.CELL_TYPE_NUMERIC:
				if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式  
	                SimpleDateFormat sdf = null;  
	                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat  
	                        .getBuiltinFormat("h:mm")) {  
	                    sdf = new SimpleDateFormat("HH:mm");  
	                } else {// 日期  
	                    sdf = new SimpleDateFormat("yyyy-MM");  
	                }  
	                Date date = cell.getDateCellValue();  
	                strCell = sdf.format(date);  
	            } else if (cell.getCellStyle().getDataFormat() == 58) {  
	                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)  
	                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");  
	                double value = cell.getNumericCellValue();  
	                Date date = org.apache.poi.ss.usermodel.DateUtil  
	                        .getJavaDate(value);  
	                strCell = sdf.format(date);  
	            } else{
	            	strCell = String.valueOf(cell.getNumericCellValue());
	            }
				break;
			case XSSFCell.CELL_TYPE_BOOLEAN:
				strCell = String.valueOf(cell.getBooleanCellValue());
				break;
			default:
				strCell = "";
				break;
			}
		return strCell;
	}

	// 判断整数（int）
	private boolean isInteger(String str)
	{
		if (StringUtils.isEmpty(str))
		{
			return false;
		}
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	// 判断浮点数（double和float）
	private boolean isDouble(String str)
	{
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*[.]?[\\dE\\d]*$");
		return pattern.matcher(str).matches();
	}

	public byte[] write(List<ProjectParameterValue> paraValueList)
	{
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		Row row = sheet.createRow(0);
		for (int i = 0; i < 8; i++)
		{
			row.createCell(i);
		}
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
		CellStyle cs1 = wb.createCellStyle();
		cs1.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		cs1.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Cell cell = row.getCell(0);
		cell.setCellStyle(cs1);
		cell.setCellValue("说明：灰色部分不可编辑；红色部分填写年月（yyyy-MM，如2017-06）可在右侧追加月份，不可重复；黄色部分填写指标值，指标值需填写数值；");

		setParamNames(wb, sheet, paraValueList);
		ProjectParameterValue paramVlaue = null;
		for (int i = 0; i < paraValueList.size(); i++)
		{
			paramVlaue = paraValueList.get(i);
			row = sheet.createRow(i + 2);
			cell = row.createCell(0);
			CellStyle style = getGrayBkSytle(wb);
			cell.setCellStyle(style);
			cell.setCellValue(paramVlaue.getId());
			cell = row.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue(paramVlaue.getBig_type_value());
			cell = row.createCell(2);
			cell.setCellStyle(style);
			cell.setCellValue(paramVlaue.getSmall_type_value());
			cell = row.createCell(3);
			cell.setCellStyle(style);
			cell.setCellValue(paramVlaue.getName());
			cell = row.createCell(4);
			cell.setCellStyle(style);
			cell.setCellValue(paramVlaue.getUnit());
			cell = row.createCell(5);
			cell.setCellStyle(style);
			cell.setCellValue(paramVlaue.getSource());
			cell = row.createCell(6);
			cell.setCellStyle(style);
			cell.setCellValue("0".equals(paramVlaue.getIsDisplay()) ? "否" : "是");
			cell = row.createCell(7);
			style = getYellowBkSytle(wb);
			cell.setCellStyle(style);
			cell.setCellValue("");
		}
		sheet.setColumnHidden((short)0, true);
		for(int i=1; i<=6; i++)
		{
			sheet.autoSizeColumn(i, true); 
		}
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			wb.write(os);
		} catch (IOException e) {
			LOG.error("export param template failed!", e);
		}
		return os.toByteArray();
	}

	private void setParamNames(Workbook wb, Sheet sheet,
			List<ProjectParameterValue> paraValueList)
	{
		Row row = sheet.createRow(1);
		CellStyle cs = getGrayBkSytle(wb);
		cs.setHidden(true);
		Cell cell0 = row.createCell(0);
		cell0.setCellStyle(cs);
		cell0.setCellValue("id");
		int i = 0;
		CellStyle cs1 = getGrayBkSytle(wb);
		for (; i < HEADERS.length; i++)
		{
			Cell cell = row.createCell(i + 1);
			cell.setCellStyle(cs1);
			cell.setCellValue(HEADERS[i]);
		}
		Cell cellLast = row.createCell(i+1);
		cellLast.setCellStyle(getRedBkSytle(wb));
		if (paraValueList.size() > 0)
		{
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
			String month = sf.format(paraValueList.get(0).getYearMonth());
			cellLast.setCellValue(month);
		} 
		else
		{
			cellLast.setCellValue("yyyy-MM");
		}
	}


	
	private CellStyle getGrayBkSytle(Workbook wb)
	{
		XSSFCellStyle  cs = (XSSFCellStyle) wb.createCellStyle();
		cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cs.setBorderBottom(CellStyle.BORDER_DASHED); //下边框    
		cs.setBottomBorderColor(new XSSFColor(java.awt.Color.WHITE));   
		cs.setBorderLeft(CellStyle.BORDER_DASHED);//左边框    
		cs.setLeftBorderColor(new XSSFColor(java.awt.Color.WHITE));   
		cs.setBorderTop(CellStyle.BORDER_DASHED);//上边框    
		cs.setTopBorderColor(new XSSFColor(java.awt.Color.WHITE));   
		cs.setBorderRight(CellStyle.BORDER_DASHED);//右边框    
		cs.setRightBorderColor(new XSSFColor(java.awt.Color.WHITE));   
		return cs;
	}

	private CellStyle getRedBkSytle(Workbook wb)
	{
		CellStyle cs = wb.createCellStyle();
		cs.setFillForegroundColor(IndexedColors.RED.getIndex());
		cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return cs;
	}
	
	private CellStyle getYellowBkSytle(Workbook wb)
	{
		CellStyle cs = wb.createCellStyle();
		cs.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return cs;
	}

	public void readDts(String fileName, IProjectListDao projectListDao) throws IOException
	{
		boolean isE2007 = false;
		Workbook wb = null;
		if(fileName.endsWith("xlsx"))
		{
            isE2007 = true;  
		}
		if(isE2007)
		{
			wb = new XSSFWorkbook(is);
		}
		else
		{
			wb = new HSSFWorkbook(is);
		}
		Sheet sheet = wb.getSheetAt(0);
		int rowSize = sheet.getLastRowNum();
		if(rowSize < 1)
		{
			return;
		}
		Row row = sheet.getRow(0);
		int colSize = row.getPhysicalNumberOfCells();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	    String currentDate = sdf.format(new Date());
		List<Map<String, String>> dtsList = new LinkedList<Map<String, String>>();
		Map<String, String> dtsMap = null;
		String c_version = "";
		for(int i=1; i<=rowSize; i++)
		{
			row = sheet.getRow(i);
			dtsMap = new HashMap<String, String>();
			for(int j=0; j<colSize; j++)
			{
				dtsMap.put(String.valueOf(j), getHSSFCellFormatValue(row.getCell((short) j + 1)));
			}
			
			dtsMap.put("importDate", currentDate);
			dtsMap.put("updateDate", currentDate);
			//判断c_version在project_info中是否有对应记录，如果没有则忽略此记录
			c_version = dtsMap.get("7");
			if(projectListDao.isExistVersion(c_version).size() > 0)
			{
				dtsList.add(dtsMap);
				if (dtsList.size() >= 100){
					dtsTaskDao.insert(dtsList);
					dtsList.clear();
				}
			}
			dtsMap = null;
		}
		if (dtsList.size() > 0){
			dtsTaskDao.insert(dtsList);
		}
	}
    /** 
     * 根据HSSFCell类型设置数据 
     * @param cell 
     * @return 
     */  
    private String getHSSFCellFormatValue(Cell cell) {  
        String cellvalue = "";  
        if (cell != null) {  
            // 判断当前Cell的Type  
            switch (cell.getCellType()) {  
            // 如果当前Cell的Type为NUMERIC  
            case HSSFCell.CELL_TYPE_NUMERIC:  
            case HSSFCell.CELL_TYPE_FORMULA: {  
                // 判断当前的cell是否为Date  
                if (HSSFDateUtil.isCellDateFormatted(cell)) {  
                    // 如果是Date类型则，转化为Data格式  
                      
                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00  
                    //cellvalue = cell.getDateCellValue().toLocaleString();  
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12  
                    Date date = cell.getDateCellValue();  
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
                    cellvalue = sdf.format(date);  
                }  
                // 如果是纯数字  
                else {  
                    // 取得当前Cell的数值  
                	cellvalue =  String.valueOf(cell.getNumericCellValue());
                }  
                break;  
            }  
            // 如果当前Cell的Type为STRIN  
            case HSSFCell.CELL_TYPE_STRING:  
                // 取得当前的Cell字符串  
                cellvalue = cell.getRichStringCellValue().getString();  
                break;  
            // 默认的Cell值  
            default:  
                cellvalue = "";  
            }  
        } else {  
            cellvalue = "";  
        }  
        return cellvalue;  
    }  
}  
	
