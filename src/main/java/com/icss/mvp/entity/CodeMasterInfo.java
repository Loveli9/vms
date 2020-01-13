package com.icss.mvp.entity;

public class CodeMasterInfo {
    
	private String codekey;
	
	private String name;
	
	private String value;
	
	private String note;

	public CodeMasterInfo(){}
	
	public CodeMasterInfo(String codekey, String name, String value){
		this.codekey = codekey;
		this.name = name;
		this.value = value;
	}
	
	public String getCodekey() {
		return codekey;
	}

	public void setCodekey(String codekey) {
		this.codekey = codekey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
}
