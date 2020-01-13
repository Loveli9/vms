package com.icss.mvp.util;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExcelImportUtils {
	private static final Logger LOG = Logger.getLogger(ExcelUtils.class);

	public String importExcel(InputStream inputStream, String fileName) throws Exception {

		String message = "Import Success";

		boolean isE2007 = false;
		// 判断是否是excel2007格式
		if (fileName.endsWith("xlsx")) {
			isE2007 = true;
		}
		int rowIndex = 0;
		InputStream input = inputStream; // 建立输入流
		Workbook wb = null;
		try {
			// 根据文件格式(2003或者2007)来初始化
			if (isE2007) {
				wb = new XSSFWorkbook(input);
			} else {
				wb = new HSSFWorkbook(input);
			}
			Sheet sheet = wb.getSheetAt(0); // 获得第一个表单
			int rowCount = sheet.getLastRowNum();
			int ColCount = sheet.getRow(0).getLastCellNum();
			// 获取合并单元格信息
			// List<CellRangeAddress> cellRangeAddresses = getCombineCell(sheet);
			// 逐行获取字段
			for (int i = 0; i <= rowCount; i++) {
				rowIndex = i;
				Row row;
				for (int j = 0; j < ColCount; j++) {
					if (isMergedRegion(sheet, i, j)) {
						String value = ExcelUtils.getCellValue(getMergedRegionValue(sheet, i, j));
						System.out.print(value + "\t");
					} else {
						row = sheet.getRow(i);
						String value = ExcelUtils.getCellValue(row.getCell(j));
						System.out.print(value + "\t");
					}
				}
				System.out.print("\n");
			}
		} catch (Exception ex) {
			message = "Import failed, please check the data in " + rowIndex + " rows ";
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					// logger.error("read file failed!" + e.getMessage());
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					// logger.error("read file failed!" + e.getMessage());
				}
			}
		}
		return message;
	}

	/**
	 * 获取表格数据，包含合并单元格情况
	 * @param sheet 数据sheet
	 * @param i 行
	 * @param names 名称集合
	 * @param name 需要的名称
	 * @return
	 */
	public Cell getCellValue(Sheet sheet,int i,Map<String, Integer> names,String name) {
		try {
			return getCellValue(sheet,i,names.get(name.replace(" ", "")));
		} catch (Exception e) {
			LOG.error("字段名未找到:" + name);
			return null;
		}
	}

	/**
	 * 获取表格数据，包含合并单元格情况
	 * @param sheet 数据sheet
	 * @param i 行
	 * @param j 列
	 * @return
	 */
	public Cell getCellValue(Sheet sheet,int i,int j){
		Cell value;
		if (isMergedRegion(sheet, i, j)) {
			value = getMergedRegionValue(sheet, i, j);
		} else {
			Row row = sheet.getRow(i);
			value = row.getCell(j);
		}
		return value;
	}

	/**
	 * 获取单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	public String getCellValue(Cell cell) {
		if (cell == null)
			return "";
		return cell.getStringCellValue();
	}

	/**
	 * 合并单元格处理,获取合并行
	 * 
	 * @param sheet
	 * @return List<CellRangeAddress>
	 */
	public List<CellRangeAddress> getCombineCell(Sheet sheet) {
		List<CellRangeAddress> list = new ArrayList<>();
		// 获得一个 sheet 中合并单元格的数量
		int sheetmergerCount = sheet.getNumMergedRegions();
		// 遍历所有的合并单元格
		for (int i = 0; i < sheetmergerCount; i++) {
			// 获得合并单元格保存进list中
			CellRangeAddress ca = sheet.getMergedRegion(i);
			list.add(ca);
		}
		return list;
	}

	public int getRowNum(List<CellRangeAddress> listCombineCell, Cell cell, Sheet sheet) {
		int xr = 0;
		int firstC = 0;
		int lastC = 0;
		int firstR = 0;
		int lastR = 0;
		for (CellRangeAddress ca : listCombineCell) {
			// 获得合并单元格的起始行, 结束行, 起始列, 结束列
			firstC = ca.getFirstColumn();
			lastC = ca.getLastColumn();
			firstR = ca.getFirstRow();
			lastR = ca.getLastRow();
			if (cell.getRowIndex() >= firstR && cell.getRowIndex() <= lastR) {
				if (cell.getColumnIndex() >= firstC && cell.getColumnIndex() <= lastC) {
					xr = lastR;
				}
			}

		}
		return xr;

	}

	/**
	 * 判断单元格是否为合并单元格，是的话则将单元格的值返回
	 * 
	 * @param listCombineCell
	 *            存放合并单元格的list
	 * @param cell
	 *            需要判断的单元格
	 * @param sheet
	 *            sheet
	 * @return
	 */
	public String isCombineCell(List<CellRangeAddress> listCombineCell, Cell cell, Sheet sheet) throws Exception {
		int firstC = 0;
		int lastC = 0;
		int firstR = 0;
		int lastR = 0;
		String cellValue = null;
		for (CellRangeAddress ca : listCombineCell) {
			// 获得合并单元格的起始行, 结束行, 起始列, 结束列
			firstC = ca.getFirstColumn();
			lastC = ca.getLastColumn();
			firstR = ca.getFirstRow();
			lastR = ca.getLastRow();
			if (cell.getRowIndex() >= firstR && cell.getRowIndex() <= lastR) {
				if (cell.getColumnIndex() >= firstC && cell.getColumnIndex() <= lastC) {
					Row fRow = sheet.getRow(firstR);
					Cell fCell = fRow.getCell(firstC);
					cellValue = getCellValue(fCell);
					break;
				}
			} else {
				cellValue = "";
			}
		}
		return cellValue;
	}

	/**
	 * 获取合并单元格的值
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public Cell getMergedRegionValue(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					Row fRow = sheet.getRow(firstRow);
					return fRow.getCell(firstColumn);
				}
			}
		}
		return null;
	}

	/**
	 * 判断指定的单元格是否是合并单元格
	 * 
	 * @param sheet
	 * @param row
	 *            行下标
	 * @param column
	 *            列下标
	 * @return
	 */
	private boolean isMergedRegion(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		InputStream is = new FileInputStream(
				"C:\\Users\\Administrator\\Desktop\\361MaturityProblem模板20190221102445.xlsx");
		String s = new ExcelImportUtils().importExcel(is, "a.xlsx");
		System.out.println(s);
	}
}