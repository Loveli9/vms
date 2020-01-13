package com.icss.mvp.entity.index;

import com.icss.mvp.util.StringUtilsLocal;

import java.util.Map;

/**
 * 度量指标模板
 */
public class InTmplIndexEntity extends InTmplLabelEntity {

	private Integer indexId; 		// 主键Id

	private String labId; //流程id
	// 指标名称
	private String name;
	// 单位
	private String unit;
	// 上限值
	private String upper;
	// 下限值
	private String lower;
	// 指标目标值
	private String target;
	// 挑战值
	private String challenge;
	// 计算公式
	private String content;
	// 版本
	private String version;
	// 迭代
	private String iteration;
	// 采集方式
	private String collectType;
	//计算亮灯规则 (11:目标优先12:上限优先13:下限优先)
	private Integer computeRule;

	public InTmplIndexEntity() { }

	/**
	 * measure_range表查询出来的map映射
	 * @param map
	 */
    public InTmplIndexEntity(Map<String, Object> map) {
		this.indexId = Integer.valueOf(StringUtilsLocal.valueOf(map.get("measure_id")));
		this.setLabel(StringUtilsLocal.valueOf(map.get("label")));
		this.setCategory(StringUtilsLocal.valueOf(map.get("category")));
		this.upper = StringUtilsLocal.valueOf(map.get("upper"));
		this.challenge = StringUtilsLocal.valueOf(map.get("challenge"));
		this.target = StringUtilsLocal.valueOf(map.get("target"));
		this.lower = StringUtilsLocal.valueOf(map.get("lower"));
		this.collectType = StringUtilsLocal.valueOf(map.get("collect_type"));
		Object value = map.get("compute_rule");
		if(value instanceof Integer){
			this.computeRule = (Integer) value;
		}
    }

    public Integer getIndexId() {
		return indexId;
	}

	public void setIndexId(Integer indexId) {
		this.indexId = indexId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUpper() {
		return upper;
	}

	public void setUpper(String upper) {
		this.upper = upper;
	}

	public String getLower() {
		return lower;
	}

	public void setLower(String lower) {
		this.lower = lower;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getChallenge() {
		return challenge;
	}

	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getIteration() {
		return iteration;
	}

	public void setIteration(String iteration) {
		this.iteration = iteration;
	}

	public String getCollectType() {
		return collectType;
	}

	public void setCollectType(String collectType) {
		this.collectType = collectType;
	}

	public Integer getComputeRule() {
		return computeRule;
	}

	public void setComputeRule(Integer computeRule) {
		this.computeRule = computeRule;
	}

	public String getLabId() {
		return labId;
	}

	public void setLabId(String labId) {
		this.labId = labId;
	}
}
