package com.nag.kdf.utility;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.nag.kdf.drivers.ExcelDriver;

//import com.aonhewitt.KDF.drivers.ExcelDriver;


public class KDFEvent {
	public org.apache.poi.ss.usermodel.Row.MissingCellPolicy NullAsBlank = org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;
	public String strFlag = "";
	public String strAction = "";
	public String strParent = "";
	public String strChild = "";
	public String strParam1 = "";
	public String strParam2 = "";
	public String strParam3 = "";
	public String strParam4 = "";
	public String strParam5 = "";
	public String strParam6 = "";
	public String strCompCallCounter = "";
	public XSSFSheet Sheet;
	public String objProperty;
	
	public KDFEvent( String event, String parent, String child,
			String param1, String param2, String param3, String param4,
			String param5, String param6,String objProperty) {
		super();		
		this.strAction = event;
		this.strParent = parent;
		this.strChild = child;
		this.strParam1 = param1;
		this.strParam2 = param2;
		this.strParam3 = param3;
		this.strParam4 = param4;
		this.strParam5 = param5;
		this.strParam6 = param6;
		this.objProperty=objProperty;
	
	}
	
	public KDFEvent(XSSFRow currentRow)
	{
		super();
		this.Sheet = currentRow.getSheet();
		this.strFlag = GetCellVaue(currentRow, 0);
		this.strAction = GetCellVaue(currentRow, 1);
		this.strParent = GetCellVaue(currentRow, 2);
		this.strChild = GetCellVaue(currentRow, 3);
		this.strParam1 = GetCellVaue(currentRow, 4);
		if(this.strParam1.contains("^")){
			this.strParam1=this.strParam1.replace("^","");
			this.strParam1=SharedResource.dictRTVarCollection.get(this.strParam1);
		}
		this.strParam2 = GetCellVaue(currentRow, 5);
		if(this.strParam2.contains("^")){
			this.strParam2=SharedResource.dictRTVarCollection.get(this.strParam2.replace("^",""));
		}
		this.strParam3 = GetCellVaue(currentRow, 6);
		if(this.strParam3.contains("^")){
			this.strParam3=SharedResource.dictRTVarCollection.get(this.strParam3.replace("^",""));
		}
		this.strParam4 = GetCellVaue(currentRow, 7);
		if(this.strParam4.contains("^")){
			this.strParam4=SharedResource.dictRTVarCollection.get(this.strParam4.replace("^",""));
		}
		this.strParam5 = GetCellVaue(currentRow, 8);
		if(this.strParam5.contains("^")){
			this.strParam5=SharedResource.dictRTVarCollection.get(this.strParam5.replace("^",""));
		}
		this.strParam6 = GetCellVaue(currentRow, 9);
		if(this.strParam6.contains("^")){
			this.strParam6=SharedResource.dictRTVarCollection.get(this.strParam6.replace("^",""));
		}
		this.strCompCallCounter = GetCellVaue(currentRow, 32);
		this.objProperty=GetORProperties(this.strParent,this.strChild);
	}
	
	public KDFEvent(XSSFSheet objScenarioSheet, int rowCounter)
	{
		super();
		XSSFRow currentRow = objScenarioSheet.getRow(rowCounter);
		this.Sheet = objScenarioSheet ;
		this.strFlag = GetCellVaue(currentRow, 0);
		this.strAction = GetCellVaue(currentRow, 1);
		this.strParent = GetCellVaue(currentRow, 2);
		this.strChild = GetCellVaue(currentRow, 3);
		this.strParam1 = GetCellVaue(currentRow, 4);
		this.strParam2 = GetCellVaue(currentRow, 5);
		this.strParam3 = GetCellVaue(currentRow, 6);
		this.strParam4 = GetCellVaue(currentRow, 7);
		this.strParam5 = GetCellVaue(currentRow, 8);
		this.strParam6 = GetCellVaue(currentRow, 9);		
		this.strCompCallCounter = GetCellVaue(currentRow, 32);
		this.objProperty=GetORProperties(this.strParent,this.strChild);
	}
	public String GetCellVaue(XSSFRow currentRow, int Cellcounter)
	{
		String strValue = "";
		try {
			currentRow.getCell(Cellcounter, NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
			strValue = currentRow.getCell(Cellcounter, NullAsBlank).getStringCellValue();
		} catch (Exception e1) {					
			strValue = Double.toString(currentRow.getCell(Cellcounter, NullAsBlank).getNumericCellValue());
		}
    	UtilityClass objUtility = new UtilityClass(this.Sheet);
		strValue = objUtility.handleStringForRuntimeVariable_UpdateObject(strValue);
		return strValue;
	}
	
	
	public KDFEvent(ResultSet objTestCaseRS)
	{			
		try {
			UtilityClass objUtility = new UtilityClass();
			this.strAction = objUtility.handleRuntimeVariable(objTestCaseRS.getString("EVENT"));
			this.strParent = objUtility.handleRuntimeVariable( objTestCaseRS.getString("Parent"));
			this.strChild = objUtility.handleRuntimeVariable( objTestCaseRS.getString("Child"));
			this.strParam1 = objUtility.handleRuntimeVariable( objTestCaseRS.getString("Param1"));
			this.strParam2 = objUtility.handleRuntimeVariable( objTestCaseRS.getString("Param2"));
			this.strParam3 = objUtility.handleRuntimeVariable( objTestCaseRS.getString("Param3"));
			this.strParam4 = objUtility.handleRuntimeVariable( objTestCaseRS.getString("Param4"));
			this.strParam5 = objUtility.handleRuntimeVariable( objTestCaseRS.getString("Param5"));
			this.strParam6 = objUtility.handleRuntimeVariable( objTestCaseRS.getString("Param6"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}
		
	}
	
	public static String GetORProperties(String strParent,String strChild){
		
		String cellContent="";
		int rwNum=0;
		String objProperty="";
		if (strParent.equals("{NA}") || strParent.equals("#1") || strParent.equals("1") ){
			objProperty="";
		}
		else{
			if (strParent.equals(strChild)){
				cellContent="Broswer('" + strParent +"')";}
			else{
				cellContent="Broswer('" + strParent +"').Object('" + strChild +"')";
			}
			
				
			
			for (Row row : ExcelDriver.orSheet) {
		        for (Cell cell : row) {
		            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
		                if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) {
		                	rwNum=row.getRowNum();
		                	objProperty=ExcelDriver.orSheet.getRow(rwNum).getCell(4).getStringCellValue();		                	
		                	break;
		                }		                
		            }		            
		        }
		        if (rwNum != 0)
		        	break;
		    }
		}
		return objProperty;
	}
	
	
}
