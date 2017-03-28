package com.binana.amas.domain;

import com.binana.amas.domain.enumeration.College;

/**
 * 存放学院饼图的类
 * @author shihua
 *
 */

public class CollegePie {
	private College college;
	private long value;
	private String name;
	
	public CollegePie(College college, long value) {
		super();
		this.college = college;
		this.value = value;
	}

	public void setChineseName(){
		this.name = this.college.getChineseName();
	}
	public College getCollege() {
		return college;
	}

	public void setCollege(College college) {
		this.college = college;
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
		return "CollegePie [college=" + college + ", value=" + value + ", name=" + name + "]";
	}
	
}
