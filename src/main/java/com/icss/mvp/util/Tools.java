package com.icss.mvp.util;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.util.ObjectUtils;

public class Tools {
	public static Document getDocumentByXml(String xml, String... charset) {
		try {
			StringBuilder sb = new StringBuilder();
			char[] chs = xml.toCharArray();
			StringBuilder uncheckWord = new StringBuilder();
			Set<String> eqWordSet = new HashSet<>();
			String eqWord = null;
			for (char ch : chs) {
				if (null == eqWord) {
					if (ch != '&' && 0 == uncheckWord.length()) {
						continue;
					}
					uncheckWord.append(ch);
					if (ch != ';') {
						continue;
					}
				}
				eqWord = StringEscapeUtils.unescapeXml(uncheckWord.toString());
				ch = CharUtils.toChar(eqWord);
				if ((ch >= 0x00 && ch <= 0x08) || (ch >= 0x0b && ch <= 0x0c) || (ch >= 0x0e && ch <= 0x1f)) {
					eqWordSet.add(uncheckWord.toString());
				}
				uncheckWord = new StringBuilder();
				eqWord = null;
			}
			for (String ws : eqWordSet) {
				xml = xml.replaceAll(ws, "");
			}
			sb.append(xml);
			if (!ObjectUtils.isEmpty(charset)) {
				String char0 = charset[0];
				if (charset.length == 1) {
					SAXReader sax = new SAXReader();
					return sax.read(new ByteArrayInputStream(sb.toString().getBytes(char0)));
				}
				if (charset.length == 2) {
					String char1 = charset[1];
					SAXReader saxReader = new SAXReader();
					Document document = saxReader.read(new ByteArrayInputStream(sb.toString().getBytes(char0)));
					OutputFormat format = OutputFormat.createPrettyPrint();
					format.setEncoding(char1);
					StringWriter writer = new StringWriter();
					XMLWriter xmlWriter = new XMLWriter(writer, format);
					xmlWriter.write(document);
					sb = new StringBuilder();
					sb.append(writer.toString());
					writer.flush();
					xmlWriter.flush();
					writer.close();
					xmlWriter.close();
					SAXReader sax = new SAXReader();
					return sax.read(new ByteArrayInputStream(sb.toString().getBytes(char1)));
				}
			}
			return DocumentHelper.parseText(sb.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}