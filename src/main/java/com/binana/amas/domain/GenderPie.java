package com.binana.amas.domain;

import com.binana.amas.domain.enumeration.GENDER;

public class GenderPie {
	private GENDER gender;
	private long value;
	private String name;
	public GenderPie(GENDER gender, long value) {
		super();
		this.gender = gender;
		this.value = value;
	}
	//转化为中文
	public void setChinese(){
		this.name = this.gender.getChinese();
	}
	public GENDER getGender() {
		return gender;
	}

	public void setGender(GENDER gender) {
		this.gender = gender;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "GenderPie [gender=" + gender + ", value=" + value + ", name=" + name + "]";
	}
	
}
