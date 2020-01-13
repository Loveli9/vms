package com.icss.mvp.entity;

public class IcpConfigInfo {
	private String id;
	private String no;
	private String ip;
	private String port;
	private String noCode;
	private String proCode;//工程能力code
	public String getProCode() {
		return proCode;
	}
	public void setProCode(String proCode) {
		this.proCode = proCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getNoCode() {
		return noCode;
	}
	public void setNoCode(String noCode) {
		this.noCode = noCode;
	}
	

}
