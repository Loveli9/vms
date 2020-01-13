package com.icss.mvp.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.stereotype.Service;

@Service
public class ExcelUtils {
	private static final Logger LOG = Logger.getLogger(ExcelUtils.class);

	/**
	 * 传入Excel的sheet参数，标题行的行号
	 * 
	 * @param sheet
	 * @param num
	 *            标题行行号,从1开始，和Excel行号一样
	 * @return 行号，列编号的map
	 */
	public static Map<String, Integer> getMapName2Num(Sheet sheet, int num) {
		Map<String, Integer> res = new HashMap<>();
		Row row = sheet.getRow(num - 1);
		int CellSize = row.getLastCellNum();
		for (int i = 0; i < CellSize; i++) {
			if (getCellFormatValue(row.getCell(i)) == null) {
				continue;
			}
			res.put(getCellFormatValue(row.getCell(i)).replace("\r\n", "").replace("\n", "").replace(" ", ""), i);
		}
		return res;
	}

	/**
	 * 根据标题名称，获取call
	 * 
	 * @param names
	 *            行号，列编号的map
	 * @param row
	 *            当前数据行
	 * @param name
	 *            标题名称
	 * @return call数据，可以使用getCellFormatValue获取里面的值
	 */
	public static Cell getCell(Map<String, Integer> names, Row row, String name) {
		try {
			return row.getCell(names.get(name.replace(" ", "")));
		} catch (Exception e) {
			LOG.error("字段名未找到:" + name);
			return null;
		}
	}

	private static String getCellFormatValue(Cell cell) {
		String strCell = null;
		if (cell == null) {
			return strCell;
		}
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf(cell.getNumericCellValue());
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		default:
			strCell = null;
			break;
		}
		return strCell;
	}

    /**
     * 根据标题行生成列名索引
     * 
     * @param header 标题行 sheet.getRow(0)
     * @return
     */
    public static Map<String, Integer> getHeaderMap(Row header) {
        Map<String, Integer> result = new HashMap<>();
        for (int i = 0; i < header.getLastCellNum(); i++) {
            String title = getCellFormatValue(header.getCell(i));
            if (StringUtils.isBlank(title)) {
                continue;
            }

            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(title);
            result.put(m.replaceAll(""), i);
            // result.put(title.replace("\r\n", "").replace("\n", "").replace(" ", ""), i);
        }
        return result;
    }

    /**
     * @param row 数据行
     * @param headers 标题行索引
     * @param title 列名
     * @return
     */
    public static Cell getCell(Row row, Map<String, Integer> headers, String title) {
        title = title.replace(" ", "");
        if (!headers.containsKey(title)) {
            LOG.error("字段名未找到:" + title);
            return null;
        }

        try {
            return row.getCell(headers.get(title));
        } catch (Exception e) {
            LOG.error("字段名未找到:" + title);
            return null;
        }
    }

    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        String result = null;
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                // result = String.valueOf(cell.getNumericCellValue());
                if (DateUtil.isCellDateFormatted(cell)) {
                    result = DateUtils.STANDARD_FORMAT_GENERAL.format(cell.getDateCellValue()); // 日期型
                } else {
//                    result = new DecimalFormat("0").format(cell.getNumericCellValue()); // 数字型
                    result = new BigDecimal(cell.getNumericCellValue()).toPlainString(); // 科学计数法数字转字符串
                }
                break;
            case STRING:
                result = cell.getStringCellValue();
                break;
            case BOOLEAN:
                result = String.valueOf(cell.getBooleanCellValue());
                break;
            case _NONE:
                break;
            case FORMULA:
                break;
            case BLANK:
                break;
            case ERROR:
                break;
            default:
                break;
        }

        return result;
    }
    
    public static Date convertToDate(Cell cell, SimpleDateFormat format) {
        if (cell == null) {
            return null;
        }

        Date result;
        try {
            result = format.parse(cell.getStringCellValue());
        } catch (ParseException e) {
            result = null;
        }

        return result;
    }
}