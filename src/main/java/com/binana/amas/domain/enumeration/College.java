package com.binana.amas.domain.enumeration;

/**
 * The College enumeration.
 * 财经学院:Finance
 * 电气学院:Electrical
 * 管理学院:Management
 * 机械学院:Mechanical
 * 计通学院:Computer
 * 理学院  :Science
 * 汽车学院:Automotive
 * 社科学院:Social
 * 生化学院:Biochemistry
 * 体育学院:Physical
 * 土建学院:Civil
 * 外语学院:Foreign
 * 医学院  :Medical
 * 艺术学院:Art
 * 职教学院:Vocational
 */
public enum College {
    FINANCE("财经学院"),
    ELECTRICAL("电气学院"),
    MANAGEMENT("管理学院"),
    MECHANICAL("机械学院"),
    COMPUTER("计通学院"),
    SCIENCE("理学院"),
    AUTOMOTIVE("汽车学院"),
    SOCIAL("社科学院"),
    BIOCHEMISTRY("生化学院"),
    PHYSICAL("体育学院"),
    CIVIL("土建学院"),
    FOREIGN("外语学院"),
    MEDICAL("医学院"),
    ART("艺术学院"),
    VOCATIONAL("职教学院");
    private String chineseName;
    private College(String chineseName){
    	this.chineseName = chineseName;
    }
    public String getChineseName(){
    	return this.chineseName;
    }
}
