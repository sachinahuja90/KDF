package com.nag.kdf.keyword;

import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;

import com.nag.kdf.customizeKeywords.EntryClassForCustomizeKeywords;
import com.nag.kdf.timeStamp.TimeStamp;
import com.nag.kdf.utility.KDFEvent;
import com.nag.kdf.utility.KDFEventResult;

public class InvokeKeywordForExecution extends EntryClassForCustomizeKeywords {
	public InvokeKeywordForExecution(WebDriver objWebdriver,KDFEvent objAFTEvent)
	{
		super(objWebdriver,objAFTEvent);
	}
	
	
	//###################################################################################
  	// Function : Executor(String strFileName)	
  	// Description : This method is used to get Keywords list and parameters from Excel and Execute
  	// Parameters : Test Scenario Worksheet Name
  	// Author : 	
  	// Date : 
  	// Modified by - 
  	// Modification - 
  	//###################################################################################
   
	public void performAction()
	{			
		this.strTimeStamp = new TimeStamp().getTimeStamp("dd-MMMMM-yyyy-h:mm:ss a");
		
			//UseSeleniumWebDriver objCmnLib = this;		
			EntryClassForCustomizeKeywords objCmnLib = this;
			
			
			try {
				Method objMethod = objCmnLib.getClass().getMethod(strAction.trim());				
				KDFEventResult objEventResult = (KDFEventResult) objMethod.invoke(objCmnLib);
				this.strActualRes = objEventResult.getStrActualResult();
				this.strStatus = objEventResult.getStrStatus();
				
			} catch (SecurityException e) {
				e.printStackTrace();
				this.strActualRes = "Security Exception in performAction()";
				this.strStatus = "FAIL";
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				this.strActualRes = "Keyword is not defined";
				this.strStatus = "FAIL";
			} catch (Exception e) {
				e.printStackTrace();
				this.strActualRes = "Exception occured in performAction()";
				this.strStatus = "FAIL";
			}	
		}				


}
