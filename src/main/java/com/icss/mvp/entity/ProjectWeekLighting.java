package com.icss.mvp.entity;

public class ProjectWeekLighting {

	private Integer red; // 上周红灯
	private Integer yellow;
	private Integer green;
	private Integer noPros;
	private Integer lastRed; // 本周红灯
	private Integer lastYellow;
	private Integer lastGreen;
	private Integer lastNoPros;

	public Integer getRed() {
		return red == null ? 0 : red;
	}

	public void setRed(Integer red) {
		this.red = red;
	}

	public Integer getYellow() {
		return yellow == null ? 0 : yellow;
	}

	public void setYellow(Integer yellow) {
		this.yellow = yellow;
	}

	public Integer getGreen() {
		return green == null ? 0 : green;
	}

	public void setGreen(Integer green) {
		this.green = green;
	}

	public Integer getNoPros() {
		return noPros == null ? 0 : noPros;
	}

	public void setNoPros(Integer noPros) {
		this.noPros = noPros;
	}

	public Integer getLastRed() {
		return lastRed == null ? 0 : lastRed;
	}

	public void setLastRed(Integer lastRed) {
		this.lastRed = lastRed;
	}

	public Integer getLastYellow() {
		return lastYellow == null ? 0 : lastYellow;
	}

	public void setLastYellow(Integer lastYellow) {
		this.lastYellow = lastYellow;
	}

	public Integer getLastGreen() {
		return lastGreen == null ? 0 : lastGreen;
	}

	public void setLastGreen(Integer lastGreen) {
		this.lastGreen = lastGreen;
	}

	public Integer getLastNoPros() {
		return lastNoPros == null ? 0 : lastNoPros;
	}

	public void setLastNoPros(Integer lastNoPros) {
		this.lastNoPros = lastNoPros;
	}
}
