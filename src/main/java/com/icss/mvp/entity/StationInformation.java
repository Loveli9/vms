package com.icss.mvp.entity;

import java.util.Date;

/**
 * @author 安瑜东
 */
public class StationInformation {

	/**主键id*/
	private Integer id;

	/**发送人*/
	private String sender;

	/**接收人*/
	private String receiver;

	/**发送时间*/
	private Date sendTime;

	/**消息内容*/
	private String information;

	/**接收时间*/
	private Date receivingTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getReceivingTime() {
		return receivingTime;
	}

	public void setReceivingTime(Date receivingTime) {
		this.receivingTime = receivingTime;
	}
}
