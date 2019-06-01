package com.nag.kdf.customizeKeywords;

import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.nag.kdf.keyword.BasicKeyword;
import com.nag.kdf.utility.KDFEvent;
import com.nag.kdf.utility.KDFEventResult;
import com.nag.kdf.utility.SharedResource;
import com.nag.kdf.webDriverSupport.KDFWebElement;

public class Navigator_Keywords extends BasicKeyword {
	
	public Navigator_Keywords(){}  //Empty Constructor
	
	public Navigator_Keywords(WebDriver objWebdriver,KDFEvent objAFTEvent)
	{
		super(objWebdriver,objAFTEvent);
	}
	
	//###################################################################################
 	// Function : GetDivTableCellValue()   	
 	// Description : This method is used to select the row whose number is given as parameter.
  	// GetTableCellValue	ParentObject	TableObject	RowNo	ColumnNo	OutputVariable
 	// Parameters : 
 	// Author : 
 	// Date : 
 	// Modified by - 
 	// Modification - 
 	//###################################################################################		
    public KDFEventResult GetDivTableCellValue()
    {        	 
    	try { 				
    		if(strParam1.equals("") || strParam2.equals("")||strParam3.equals(""))
    		{
    			System.out.println("Either Rownumber, Colnumber or Variable name is empty.");
    			this.strActualRes = "Either Rownumber, Colnumber or Variable name is empty.";
				    this.strStatus = "FAIL";
    		}        		
    		int intRow = 0;
    		int intCol = 0;
			try {
				intRow = Integer.valueOf(strParam1);
				intCol = Integer.valueOf(strParam2);
			} catch (NumberFormatException e) {
				System.out.println("row/Col number is not in number format.");
    			this.strActualRes = "row/Col number is not in number format.";
				    this.strStatus = "FAIL";
			}       		
    		WebElement col;
			try {
				
				this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator,this.intIndex);
				List<WebElement> divRows = objWebElement.findElements(By.xpath("./div"));
				WebElement row = divRows.get(intRow - 1);
				List<WebElement> cols;					
				cols = row.findElements(By.xpath("table/tbody/tr/*"));
				col = cols.get(intCol - 1);	
				SharedResource.dictRTVarCollection.put(strParam3, col.getText());
				this.strActualRes = "Cell("+ strParam1 + "," + strParam2+ ") value is : " + col.getText();
				this.strStatus = "PASS";
			} catch (IndexOutOfBoundsException e) {
				this.strActualRes = "Row/Col index is out of bound";
				this.strStatus = "FAIL";
			}         		 				
 	 }
 	 catch (Exception e)
 	 {
 		 this.strActualRes = e.getStackTrace().toString();
			 this.strStatus = "FAIL";
 	 }     
 		return new KDFEventResult(this.strActualRes, this.strStatus);

    }
    
  //###################################################################################
 	// Function : VerifyDivTableCellData()   	
 	// Description : This method is used to verify cell data with expected text      	// 	
 	// Parameters : ParentObject	DivObject	RowNo	ColumnNo	ExpectedText
 	// Author : 
 	// Date : 
 	// Modified by - 
 	// Modification - 
 	//###################################################################################		
    public KDFEventResult VerifyDivTableCellData()
    {        	 
    	try { 				
    		if(strParam1.equals("") || strParam2.equals(""))
    		{
    			System.out.println("Either Rownumber or Colnumber is empty.");
    			this.strActualRes = "Either Rownumber or Colnumber is empty.";
				    this.strStatus = "FAIL";
    		}        		
    		int intRow =0;
    		int intCol=0;
			try {
				intRow = Integer.valueOf(strParam1);
				intCol = Integer.valueOf(strParam2);
			} catch (NumberFormatException e) {
				System.out.println("row/Col number is not in number format.");
    			this.strActualRes = "row/Col number is not in number format.";
				    this.strStatus = "FAIL";
			}       		
    		WebElement col ;
			try {
				this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator,this.intIndex);
				List<WebElement> divRows = objWebElement.findElements(By.xpath("./div"));
				WebElement row = divRows.get(intRow - 1);
				List<WebElement> cols;					
				cols = row.findElements(By.xpath("table/tbody/tr/*"));
				col = cols.get(intCol - 1);					
				if (strParam3.trim().equals(col.getText().trim()))
				{
					this.strActualRes = "Expected text " + strParam3 + " matches with TableCell("+ strParam1 + "," + strParam2+ ") value " + col.getText();
					this.strStatus = "PASS";
				}
				else
				{
					this.strActualRes = "Expected text " + strParam3 + " doesn't matches with TableCell("+ strParam1 + "," + strParam2+ ") value " + col.getText();
					this.strStatus = "FAIL";
				}					
			} catch (IndexOutOfBoundsException e) {
				this.strActualRes = "Row/Col index is out of bound";
				this.strStatus = "FAIL";
			}         		 				
 	 }
 	 catch (Exception e)
 	 {
 		 this.strActualRes = e.getStackTrace().toString();
			 this.strStatus = "FAIL";
 	 }     
 		return new KDFEventResult(this.strActualRes, this.strStatus);

    }
    

    

//Created For SaveAs Dialog box) 

public KDFEventResult NavigatorSaveAsDialog()
    {    	
   	String[] dialog;
   	String strModalDialogExeName = this.strParam3;
    	String strFilePath = this.strParam2;
    	String strWindowTitle = this.strParam1; 
    	
   	try
    	{
   		strModalDialogExeName = SharedResource.strRootFolder + "\\Batch\\" + strModalDialogExeName ;
   		if (new File(strModalDialogExeName).exists())
   		{
   	  		dialog = new String[] { strModalDialogExeName,strWindowTitle,strFilePath };
        		Process p = Runtime.getRuntime().exec(dialog);      		
        		p.waitFor();    		
        		int intExitCode = p.exitValue();    		
        		p.destroy();    		    		
        		if(intExitCode == 0)
        		{
    	    		this.strStatus = "PASS";
    	            this.strActualRes = "File successfully Saved";
        		}
        		else
        		{
        			this.strStatus = "FAIL";
        			this.strActualRes = "Unable to save file.";
        		}	
   		}
   		else
   		{
    			this.strStatus = "FAIL";
    			this.strActualRes = strModalDialogExeName + " doesnot exist.";
    		}	   		
 
    	} catch(Exception err)
    	{
    		this.strActualRes = err.getStackTrace().toString();
    		this.strStatus = "FAIL";
    	}    
	     return new KDFEventResult(this.strActualRes, this.strStatus);
    }

	

}
