package com.binana.amas.domain.enumeration;

/**
 * The GENDER enumeration.
 */
public enum GENDER {
    M("男生"),F("女生");
    private String chineseGender;
	private GENDER(String chineseGender){
		this.chineseGender = chineseGender;
	}
	public String getChinese(){
		return this.chineseGender;
	}
}
