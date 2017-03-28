package com.binana.amas.web.rest.util;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import com.binana.amas.domain.Amember;
import com.binana.amas.domain.Association;
import com.binana.amas.domain.enumeration.College;
import com.binana.amas.domain.enumeration.GENDER;
import com.binana.amas.domain.enumeration.POLITICSSTATUS;
@Component
public class ExcelUtil {
	public List<Amember> parseExcel(InputStream excelFile,long id){
			Association assocation = new Association();
			assocation.setId(id);
			Set<Association> assoSet = new HashSet<Association>();
			assoSet.add(assocation);
		 	Workbook  wb = null; 
			try {
				wb = WorkbookFactory.create(excelFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        Sheet sheet = wb.getSheetAt(0);
	        List<Amember> amemberList = new ArrayList<Amember>();
	        int cellNum = 0;
	        sheet.removeRow(sheet.getRow(0));
	        for (Row row : sheet) {
	        	Amember amember = null;
	        	if(row.getCell(1) == null ){
//	        		System.out.println("null");
	        		continue;
	        	}else{
	        		amember = new Amember();
	        	}
	        	cellNum=0;
	        	for(Cell cell:row){
	        		if(cell.getCellType() == cell.CELL_TYPE_BLANK){
	        			cellNum++;
	        			continue;
	        		}
	        		switch (cellNum) {
					case 0:
						amember.setMembName(cell.getStringCellValue());
						break;
					//学号
					case 1:
//						new BigDecimal(cell.getNumericCellValue()).toPlainString();
						amember.setMembNO(new BigDecimal(cell.getNumericCellValue()).toPlainString() );
						break;
					//班级
					case 2:
						amember.setMembClass(cell.getStringCellValue());
						break;
					//手机号
					case 3:
						amember.setMembPhone(new BigDecimal(cell.getNumericCellValue()).toPlainString());
						break;
					//QQ
					case 4:
						amember.setMembQQ(new BigDecimal(cell.getNumericCellValue()).toPlainString());
						break;
					//邮箱
					case 5:
						amember.setMembEmail(cell.getStringCellValue());
						break;
					//加入日期
					case 6:
						amember.setMembJoinDate(cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
						break;
					//性别
					case 7:
						if("男".equals(cell.getStringCellValue()) || "男生".equals(cell.getStringCellValue()) || "M".equals(cell.getStringCellValue())){
							amember.setGender(GENDER.M);
						}else{
							amember.setGender(GENDER.F);
						}
						break;
					//宿舍号	
					case 8:
						amember.setDormNum(cell.getStringCellValue());
						break;
					case 9:
						if("团员".equals(cell.getStringCellValue())||"共青团员".equals(cell.getStringCellValue()) ){
							amember.setPoliticsStatus(POLITICSSTATUS.LEAGUE);
						}else if("党员".equals(cell.getStringCellValue())||"中共党员".equals(cell.getStringCellValue()) ){
							amember.setPoliticsStatus(POLITICSSTATUS.PARTY);
						}else if("群众".equals(cell.getStringCellValue())){
							amember.setPoliticsStatus(POLITICSSTATUS.PUBLIC);
						}else{
							amember.setPoliticsStatus(POLITICSSTATUS.NON);
						}
						break;
					case 10:
						switch (cell.getStringCellValue()) {
						case "财经学院":
							amember.setCollege(College.FINANCE);
							break;
						case "电气学院":
							amember.setCollege(College.ELECTRICAL);
							break;
						case "管理学院":
							amember.setCollege(College.MANAGEMENT);
							break;
						case "机械学院":
							amember.setCollege(College.MECHANICAL);
							break;
						case "计通学院":
							amember.setCollege(College.COMPUTER);
							break;
						case "理学院":
							amember.setCollege(College.SCIENCE);
							break;
						case "汽车学院":
							amember.setCollege(College.AUTOMOTIVE);
							break;
						case "社科学院":
							amember.setCollege(College.SOCIAL);
							break;
						case "生化学院":
							amember.setCollege(College.BIOCHEMISTRY);
							break;
						case "体育学院":
							amember.setCollege(College.PHYSICAL);
							break;
						case "土建学院":
							amember.setCollege(College.CIVIL);
							break;
						case "外语学院":
							amember.setCollege(College.FOREIGN);
							break;
						case "医学院":
							amember.setCollege(College.MEDICAL);
							break;
						case "艺术学院":
							amember.setCollege(College.ART);
							break;
						case "职教学院":
							amember.setCollege(College.VOCATIONAL);
							break;

						default:
							break;
						}
						break;
						
					default:
						break;
					}
	        		cellNum++;
	        	}
	            if(amember!=null){
	            	amember.setAssociations(assoSet);
	            	amemberList.add(amember);
	            }
	        }
	        /*
	        System.out.println(wb.getSheetName(0));
	        for(Amember amemTmp:amemberList){
	        	System.out.println(amemTmp.toString());
	        }
	        */
	       return amemberList;
	    }
	public static void main(String[] args) {
//		ExcelUtil eu = new ExcelUtil();
//		File excelFile = new File("src/main/java/com/binana/amas/web/rest/util/社团成员.xls");
		for(College coll:College.values()){
			System.out.println(coll);
		}
	}
}
