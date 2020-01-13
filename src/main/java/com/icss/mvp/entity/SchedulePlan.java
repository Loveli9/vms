package com.icss.mvp.entity;

public class SchedulePlan {
	private int id;
	private String name;
	private String description;
	private String scheduleIcon;
	private String scheduleState;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScheduleState() {
		return scheduleState;
	}

	public void setScheduleState(String scheduleState) {
		this.scheduleState = scheduleState;
	}

	public String getScheduleIcon() {
		return scheduleIcon;
	}

	public void setScheduleIcon(String scheduleIcon) {
		this.scheduleIcon = scheduleIcon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
