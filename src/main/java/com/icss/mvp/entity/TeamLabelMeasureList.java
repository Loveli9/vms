package com.icss.mvp.entity;

import java.util.List;

public class TeamLabelMeasureList {
	private String teamId; //团队编号
	private List<TeamLabelMeasure> teamLabelMeasures;//该流程指标all
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public List<TeamLabelMeasure> getTeamLabelMeasures() {
		return teamLabelMeasures;
	}
	public void setTeamLabelMeasures(List<TeamLabelMeasure> teamLabelMeasures) {
		this.teamLabelMeasures = teamLabelMeasures;
	}

}
