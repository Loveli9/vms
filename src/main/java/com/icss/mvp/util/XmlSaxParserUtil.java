package com.icss.mvp.util;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.icss.mvp.dao.IDtsTaskDao;

/**
 * XmlSaxParserUtil Sax解析XML
 * 
 * @author Administrator
 * 
 */
public class XmlSaxParserUtil extends DefaultHandler {

	private static Logger logger = Logger.getLogger(XmlSaxParserUtil.class);
	private IDtsTaskDao dtsTaskDao;
	private String no;
	private List<Map<String, String>> contentsList = new ArrayList<Map<String, String>>(0);
	private Map<String, String> contentsMap;
	// 作用是记录解析时的上一个节点名称
	private String preTag = null;
	private String preTag1 = null;
	private String preTag2 = null;

	private int colIndex = 0;
	private int rowIndex = 0;

	public XmlSaxParserUtil(IDtsTaskDao dao, String strNo) {
		dtsTaskDao = dao;
		no = strNo;
	}

	public List<Map<String, String>> getContents(InputStream xmlStream) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(xmlStream, this);
		return this.getContents();
	}

	public List<Map<String, String>> getContents() {
		return contentsList;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if ("Row".equals(qName)) {
			if (rowIndex > 0) {
				contentsMap = new HashMap<String, String>();
				preTag = qName;
			}
		}
		if (preTag != null && "Cell".equals(qName)) {
			preTag1 = qName;
		}
		if (preTag != null && preTag1 != null && "Data".equals(qName)) {
			preTag2 = qName;
			if (colIndex > 0) {
				contentsMap.put(String.valueOf(colIndex - 1), "");
			}
		}
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ("Row".equals(qName)) {
			if (rowIndex > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String currentDate = sdf.format(new Date());
				contentsMap.put("importDate", currentDate);
				contentsMap.put("updateDate", currentDate);
				contentsMap.put("no", no);
				contentsList.add(contentsMap);
				if (contentsList.size() > 100) {
					dtsTaskDao.insert(contentsList);
					contentsList.clear();
				}
				contentsMap = null;
				colIndex = 0;
				preTag = null;
				preTag1 = null;
				preTag2 = null;
			}
			rowIndex++;
		}
		if (preTag != null && preTag1 != null && "Cell".equals(qName)) {
			preTag1 = null;
		}
		if (preTag != null && preTag1 != null && preTag2 != null && "Data".equals(qName)) {
			preTag2 = null;
			colIndex++;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (preTag != null && preTag1 != null && preTag2 != null) {
			String content = new String(ch, start, length);
			if (content.length() > 100) {
				content = "";
			}
			if (colIndex > 0) {
				contentsMap.put(String.valueOf(colIndex - 1), content);
			}
		}
	}
}