package com.nag.kdf.drivers;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriverService;

import com.nag.kdf.excel.CellStyles;
import com.nag.kdf.keyword.InvokeKeywordForExecution;
import com.nag.kdf.resultLogging.ConsolidatedTestResults;
import com.nag.kdf.resultLogging.ResultLog;
import com.nag.kdf.resultLogging.TestCaseResults;
import com.nag.kdf.resultLogging.TestStepResults;
import com.nag.kdf.resultLogging.TestSuiteResults;
import com.nag.kdf.utility.KDFEvent;
import com.nag.kdf.utility.PrintMessage;
import com.nag.kdf.utility.SharedResource;
import com.nag.kdf.utility.UtilityClass;
import com.nag.kdf.webDriverSupport.CaptureScreenShot;
import com.sun.org.apache.bcel.internal.generic.Select;

public class ExcelDriver 
{
	//Event Input Parameters
	public String strLocator, strParent;
	public String strAction = "", strChild = "", strParam1 = "" , strParam2 = "", strParam3 = "",
				  strParam4 = "" , strParam5 = "", strParam6 = "",objProperty="";
	
	// Event Result Params
	public String strResult = "", strResultMsg = "", errormsg = null;
	
	
	public String strTestCaseResult="";
	public String strScenarioName = "";
	public String strTestCaseName = "";
	public String strTCFileName;
	public String DB_ProjectName;
	public String strScreenshotPath = "";
	public int intDBTesCaseExecuted = 0, intDBTesCasePassed = 0,
			intDBTesCaseFailed = 0;
	public int strTestStepID=0, strTestCaseID=0,strTestSuiteID=0;
	
	
	
	public boolean boolTCFlag = true;
	public int iRow, intAutomated_Scenario_Count, iTCRowCounter;
	public String Logic_IF_return;
	public int stepcount;
	public String[] sArr;
	public int sCount;
	public String sValue;
	public int intTestStepCnt;
	public int Run_time_row_no;
	public String strProjectLocation;
	public String strBrowser = ""; // For handling Chrome Browser - special case
	public String strContinueExecution = "TRUE";	
	public WebElement objWebElement;
	public WebDriver objWebDriver;
	public ChromeDriverService service = null;
	public org.openqa.selenium.support.ui.Wait<WebDriver> wait;
	public SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public SimpleDateFormat displayDateFormat = new SimpleDateFormat("MM-dd-yyyy");
	public String strORPath = "";
	public String strWorkBookName = "";
	public Hashtable<Integer, String> dictStatusMsg;
	public Hashtable<Integer, String> dictStatus;
	public Hashtable<Integer, String> dictSnapshotPath;	//For Showing Snapshot path in col V // For HRBPO Only
	public String strSnapshotPathArray = "";	
	public int forLoopIterationCounter = 0 ;
	public int forLoopDepth = 0 ;
	public int primaryForLoopIterCounter=0;
	public int primaryForLoop_startLoop =0;
	public int primaryForLoop_endLoop =0;
	
	
	public ConsolidatedTestResults testResult;
	public HashMap<Integer,TestStepResults> strHashMapTestStepResults;
	public HashMap<Integer,TestStepResults> strHashMapCombinedKeywordResults;
	public HashMap<Integer,TestCaseResults> strHashMapTestCaseResults;
	public HashMap<Integer,TestSuiteResults> strHashMapTestSuiteResults;
	
	
	
	int strTestStepPassCnt,strTestStepFailCnt,strTestCasePassCnt,strTestCaseFailCnt,strTestCaseSkippedCnt;
	org.apache.poi.ss.usermodel.Row.MissingCellPolicy NullAsBlank = org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;
	FileInputStream inp;
	FileOutputStream fos;
	public XSSFWorkbook workbook;
	public static XSSFSheet orSheet;
	XSSFSheet objScenarioSheet;
	Hashtable<String, String> dictOrCollection = new Hashtable<String, String>();
	Hashtable<String, String[]> dictCompParameterValues = new Hashtable<String, String[]>();
	Map<String, CellStyle> styles;
	
	XSSFSheet sheet;
	XSSFRow row;
	
	public void attachShutdownHook()
	{
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				WriteOutputStream();
			}
		});	
	}
	
	//create an object of SingleObject
	private static ExcelDriver instance = new ExcelDriver();

	//make the constructor private so that this class cannot be
	//instantiated
	private ExcelDriver(){}

	//Get the only object available
	public static ExcelDriver getInstance(){
	    return instance;
	}

		
	
	
	// Get Workbook Object for Output Stream [Write]
	public void WriteOutputStream()
	{
		try
		{
			fos = new FileOutputStream(new File(strTCFileName));
			workbook.write(fos);	
			if(inp != null)	inp.close();	
			if (fos != null) fos.close();
			workbook = null;
			sheet = null;
			row = null;
		}
		catch (Exception e)
		{
			workbook = null;
			sheet = null;
			row = null;
		}
	}
	
	// Exit Execution
	protected void finalize()
    {
		System.exit(0);
    }
	
	
	// Get Workbook Object for Input Stream	[Read]
	public void refreshWorkbookObject(String strWBName)
	{
		try {
			
			inp = new FileInputStream(strWBName);			
			workbook = new XSSFWorkbook(inp);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
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
   
	public void Executor(String strFileName)	
	{
		SharedResource.strExcelPath = strFileName;
		sheet = null;
		row = null;
		inp= null;
		fos = null;
		workbook = null;
		Date TestStartDate=new Date();
		strHashMapTestSuiteResults=new HashMap<Integer,TestSuiteResults>();
		SharedResource.strTestSuitePassCount=0;
        SharedResource.strTestSuiteFailCount=0;
        
		try 
		{
			System.out.println("Welcome to KDF 1.0 : Excel Driver");			
			inp = new FileInputStream(strFileName);			
			workbook = new XSSFWorkbook(inp);			
			//workbook = getWorkbookObject(strFileName);			
			sheet = workbook.getSheet("TestPlan");			
			orSheet= workbook.getSheet("Object_Mapping");
			styles = new CellStyles().createStyles(workbook);
			getDesignVariables();
						
			
			int iRow = 1;			
			row = sheet.getRow(iRow);	
			
			row.getCell(0,NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
			String strScenarioFlag = row.getCell(0,NullAsBlank).getStringCellValue().toString();
			
			row.getCell(1,NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
			strScenarioName = row.getCell(1,NullAsBlank).getStringCellValue().toString();			
			
			//getForLoopVariables();
			SharedResource.ResultFolderPath=sheet.getRow(11).getCell(12,NullAsBlank).getStringCellValue().toString(); 
			String arr[]=SharedResource.ResultFolderPath.split("");
			int len=arr.length;
			String st=arr[len-1];
			if (!(st.equals("\\"))){
				SharedResource.ResultFolderPath=SharedResource.ResultFolderPath +"\\";
			}
			
			Path path=Paths.get(strFileName);
			String tempFile = path.getFileName().toString();
			strTCFileName = tempFile.replace(".xlsm","").replace(".xls", "");
			
			Date date=new Date();
			
			
			SharedResource.ResultFolderPath=SharedResource.ResultFolderPath + strTCFileName + SharedResource.dateTimeFormat.format(date).replace(" ", "").replace(":","-");
			date=null;
			UtilityClass.createFolder(SharedResource.ResultFolderPath);
			
			
			
			DB_ProjectName = row.getCell(7,NullAsBlank).getStringCellValue().toString();
			
			while(!(strScenarioName.equals(""))&& !(strScenarioName.equals(null)))
			{
				if (strScenarioFlag.equals("Y"))
				{
					
					date = new Date();					
					row.getCell(5,NullAsBlank).setCellValue(SharedResource.dateTimeFormat.format(date));
					this.iTCRowCounter = 1;
					SharedResource.SuiteResultFolderPath=SharedResource.ResultFolderPath +"\\"+ strScenarioName;
					UtilityClass.createFolder(SharedResource.SuiteResultFolderPath);
					
					TestCaseProcessor(strScenarioName);		 		

					row.getCell(6,NullAsBlank).setCellValue(SharedResource.dateTimeFormat.format(date));
					date = null;
					
					
					//fos.flush();
				}
				if(!(SharedResource.strContinueExecution.equalsIgnoreCase("TRUE")))
				{				
					return;
				}    
				iRow++;		
				row = sheet.getRow(iRow);
				row.getCell(1,NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
				strScenarioName = row.getCell(1,NullAsBlank).getStringCellValue().toString();
				row.getCell(0,NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
				strScenarioFlag = row.getCell(0,NullAsBlank).getStringCellValue().toString();
			}		
			attachShutdownHook();
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
     	}		
		
		Date TestEndDate=new Date();
		testResult = new ConsolidatedTestResults(TestStartDate,TestEndDate,strTCFileName,SharedResource.strTestSuitePassCount,SharedResource.strTestSuiteFailCount,strHashMapTestSuiteResults);
		ResultLog.ConsolidatedTestReport(testResult);
	}
	
	
	//###################################################################################
  	// Function : getDesignVariables()
  	// Description : This method is used to get Variable Value form Variable Value Excel WorkSheet.
  	// Parameters : Test Scenario Worksheet Name
  	// Author : 	
  	// Date : 
  	// Modified by - 
  	// Modification - 
  	//###################################################################################
   
	

	
	public void getDesignVariables()
	{
		int intVariableCounter = 1;
		XSSFSheet objVariableSheet;
		//String strVaribleWorkbook = "";		
		try 
		{			
			XSSFRow varRow = null;
			String strDsgnVarName = "";
			String strDsgnVarValue = "";
			
			objVariableSheet = null;
			intVariableCounter = 1;
			objVariableSheet = workbook.getSheet("Variable_Values");
			varRow = objVariableSheet.getRow(intVariableCounter);
			varRow.getCell(0,NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
			strDsgnVarName = varRow.getCell(0,NullAsBlank).getStringCellValue();
			while(!(strDsgnVarName.equals("")) && !(strDsgnVarName.equals(null)))
			{
				Cell cell = varRow.getCell(1,NullAsBlank);
				if(cell.getCellType()==Cell.CELL_TYPE_FORMULA){
					switch(cell.getCachedFormulaResultType()) {
		            case Cell.CELL_TYPE_NUMERIC:
		                System.out.println("Last evaluated as: " + cell.getNumericCellValue());
		                strDsgnVarValue = String.valueOf(cell.getNumericCellValue());
		                break;
		            case Cell.CELL_TYPE_STRING:
		                strDsgnVarValue = cell.getRichStringCellValue().getString();
		                break;
		        }
				}else{
					varRow.getCell(1,NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
					strDsgnVarValue = varRow.getCell(1,NullAsBlank).getStringCellValue();
				}
				
				SharedResource.dictRTVarCollection.put(strDsgnVarName, strDsgnVarValue);
				intVariableCounter ++;
				varRow = objVariableSheet.getRow(intVariableCounter);
				if(varRow == null)
					break;
				varRow.getCell(0,NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
				strDsgnVarName = varRow.getCell(0, NullAsBlank).getStringCellValue();
			}
			varRow = null;
		} 
		catch (Exception e) 
		{	
			System.out.println("Exception in getting Design Time Variable");
		}	
		objVariableSheet = null;	
	}
	
	

	//###################################################################################
  	// Function : getForLoopVariables()
  	// Description : This method is used to get Itaration Value form Variable Value Excel WorkSheet.
  	// Parameters : Test Scenario Worksheet Name
  	// Author : 	
  	// Date : 
  	// Modified by - 
  	// Modification - 
  	//###################################################################################
   
	
	public void getForLoopVariables()
	{
		XSSFSheet objVariableSheet;
		XSSFRow varRow, VarRowForValues ;
		int column = 7;
		int intVariableCounter = 0;
		String ForLoopParameterName = "";
		String ForLoopParametervalue = "";
		int numberofValues = 1;
		int rowCounter = 50;
		try
		{
			objVariableSheet = workbook.getSheet("Variable_Values");
			varRow = objVariableSheet.getRow(intVariableCounter);
			varRow.getCell(column, NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
			ForLoopParameterName = varRow.getCell(column,NullAsBlank).getStringCellValue();
			intVariableCounter++;
			while(!(ForLoopParameterName.equals("")) && !(ForLoopParameterName.equals(null)))
			{				
				List<String> Values = new ArrayList<String>();
				VarRowForValues = objVariableSheet.getRow(numberofValues);
				VarRowForValues.getCell(column, NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
				ForLoopParametervalue = VarRowForValues.getCell(column,NullAsBlank).getStringCellValue();
				
				
				int counter = 1;
                while((counter != rowCounter) && (!(ForLoopParametervalue.equals(""))))
                {
                      Values.add(ForLoopParametervalue);
                      numberofValues++;
                      VarRowForValues = objVariableSheet.getRow(numberofValues);
                      VarRowForValues.getCell(column, NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
                      ForLoopParametervalue = VarRowForValues.getCell(column,NullAsBlank).getStringCellValue();
                      counter ++;
                }

                
				numberofValues = 1;
				SharedResource.dictForLoopVariables.put(ForLoopParameterName,Values.toArray(new String[0]));
				column++;
				varRow.getCell(column, NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
				ForLoopParameterName = varRow.getCell(column,NullAsBlank).getStringCellValue();				
			}			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}		
	}
	
	// To Read Value from Excel Cell
	public String GetExcelCellValue(XSSFRow ObjRow, int columnNumber)
	{
		String strCellValue = "";
		try {
				ObjRow.getCell(columnNumber, NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
				strCellValue = ObjRow.getCell(columnNumber, NullAsBlank).getStringCellValue();
			}
		catch (Exception e1) 
		{					
			strCellValue = Double.toString(ObjRow.getCell(columnNumber, NullAsBlank).getNumericCellValue());
		}		
		return strCellValue;
	}
	
	
	
	//###################################################################################
  	// Function : TestCaseProcessor(String strSheetName)
  	// Description : This method is used to Execute All Test Case written with in a Test Scenario Worksheet
  	// Parameters : Test Scenario Worksheet Name
  	// Author : 	
  	// Date : 
  	// Modified by - 
  	// Modification - 
  	//###################################################################################
    

	public void TestCaseProcessor(String strSheetName)
	{
		
		
		//Reporting Variable
        Date tcStart=null, tcEnd= null, testSuiteStartDate=null, testSuiteEndDate=null;
        
		objScenarioSheet = workbook.getSheet(strSheetName);
        XSSFRow rowScen = objScenarioSheet.getRow(this.iTCRowCounter);
        rowScen.getCell(0, NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
        KDFEvent objKDFEvent = new KDFEvent(objScenarioSheet, this.iTCRowCounter);     
        strHashMapTestCaseResults = new HashMap<Integer, TestCaseResults>();
        
        TestSuiteResults tsResult=null;
        
        testSuiteStartDate = new Date();					
		//this.iTCRowCounter = 1;
		strTestCasePassCnt=0;
        strTestCaseFailCnt=0;
        strTestCaseSkippedCnt=0;
        strTestSuiteID++;
        
        while(!(objKDFEvent.strFlag.equals("")) && !(objKDFEvent.strFlag.equals(null)))
        {        	
        	//objKDFEvent = new KDFEvent(objScenarioSheet, this.iTCRowCounter);
        	if(objKDFEvent.strFlag.toUpperCase().equals("Y"))
        	{     		
        		strAction=objKDFEvent.strAction;
        		//If Action is not equal to TestCase means If Action is other than TestCase.
        		
        		
        		if (!(objKDFEvent.strAction.toUpperCase().equals("TESTCASE")))
        		{
        			strResultMsg = "";
                    strResult = "";
                    errormsg = null;
                    
                    switch (objKDFEvent.strAction.toUpperCase()) 
                    {
	    				case "END_LOOP":
	    				{
	    					forLoopDepth=0;
	    					break;
	    				}
	    				case "MOVETONEXTITERATION":
	    					break;
	    				case "EXITTESTCASE":
	    				{
	    					while(!objKDFEvent.strAction.equalsIgnoreCase("Testcase")){
	                    		if(objKDFEvent.strAction.isEmpty()){
	                    			break;
	                    		}
	                    		this.iTCRowCounter ++; 
	                    		objKDFEvent = new KDFEvent(objScenarioSheet, this.iTCRowCounter);
	                    	}
	                    	this.iTCRowCounter = this.iTCRowCounter-1 ;
	    				}
    					
	    				case "ENDTESTCASE" :
	    				{
	    					//PrintMessage.printStepNumber(objKDFEvent.strParent);
		        			tcEnd= Calendar.getInstance().getTime();
		        			
		        			TestCaseResults tcResult=new TestCaseResults(tcStart,tcEnd,strSheetName,strTestCaseName,strTestStepPassCnt,strTestStepFailCnt,strHashMapTestStepResults);
		        			ResultLog.TestCaseReport(tcResult);
		        			strHashMapTestCaseResults.put(strTestCaseID, tcResult);
		        			tcResult=null;
		        			tcStart=null;
		        			tcEnd=null;
		        			strTestCaseName="";
		        			strHashMapTestStepResults=null;
		        			if (strTestCaseResult=="Pass") 
		        				strTestCasePassCnt++;
		        			else
		        				strTestCaseFailCnt++;
		        			
		        			break;
	    				}
	
	    				
		        		case "BLOCK":
							break;
						case "STEP":
		                {
		        			PrintMessage.printStepNumber(objKDFEvent.strParent);
		        			break;
		                }                    
						case "BEGIN_LOOP":
		                {   
		                	handleFORLoop_new(rowScen);
		                	break;
		                }
		                
		               
						case "CALL_COMBINE_KEYWORDS":
						{
		                	handleCombinedKeyword(rowScen);
		                	break;
		                }
						
						
						default:
	        			{   
	        				PrintMessage.printKDFEvent(objKDFEvent);
	        				InvokeKeywordForExecution obj = new InvokeKeywordForExecution(objWebDriver, objKDFEvent);
							obj.performAction();
							strScreenshotPath = new CaptureScreenShot(objWebDriver, objKDFEvent.strAction).CaptureResultScreenshot(strBrowser);

	        				
	        				switch (objKDFEvent.strAction.toUpperCase()) {
								case "LOGIC_IF":
								{
		        					//Add LogicIf Status to Hash table
		        		      		SharedResource.dictLogcIfStatusCollection.put(SharedResource.LogicIfDepth, obj.strActualRes);
		        					SharedResource.Logic_If = obj.strActualRes;
		        					strResultMsg = "Condition is "+ obj.strActualRes;
		        					if (SharedResource.Logic_If.equalsIgnoreCase("FAIL")){
		        						do {
		        							updateScenarioResult(objScenarioSheet, this.iTCRowCounter, 15);
		        							this.iTCRowCounter ++;
		        	                    	rowScen = objScenarioSheet.getRow(this.iTCRowCounter);
		        	                    	objKDFEvent = new KDFEvent(rowScen);
		        	                    	strAction=objKDFEvent.strAction;
		        	                    	strResultMsg="Event|Statement Skipped";
		        						}while(!objKDFEvent.strAction.equalsIgnoreCase("ELSE")&&(!objKDFEvent.strAction.equalsIgnoreCase("ELSE_IF"))&&(!objKDFEvent.strAction.equalsIgnoreCase("END_IF")));
		        						this.iTCRowCounter--;
		        					}
		        					break;
		        				}
	        				
								case "ELSE_IF" :
		        				{
		        					//Add LogicIf Status to Hash table
		        		      		SharedResource.dictLogcIfStatusCollection.put(SharedResource.LogicIfDepth, obj.strActualRes);
		        					//SharedResource.Else_If = obj.strStatus;
		        					strResultMsg = "Condition is "+ obj.strActualRes;
		        					if (SharedResource.Else_If.equalsIgnoreCase("FAIL")){
		        						do {
		        							
		        							updateScenarioResult(objScenarioSheet, this.iTCRowCounter, 15);
		        							
		        							this.iTCRowCounter ++;
		        	                    	rowScen = objScenarioSheet.getRow(this.iTCRowCounter);
		        	                    	objKDFEvent = new KDFEvent(rowScen);
		        	                    	strAction=objKDFEvent.strAction;
		        	                    	strResultMsg="Event|Statement Skipped";
		        						}while(!objKDFEvent.strAction.equalsIgnoreCase("ELSE")&&(!objKDFEvent.strAction.equalsIgnoreCase("ELSE_IF"))&&(!objKDFEvent.strAction.equalsIgnoreCase("END_IF")));
		        						this.iTCRowCounter--;
		        					}
		        					break;
		        					
		        				}
								case "ELSE":
		        				{
		        					SharedResource.dictLogcIfStatusCollection.put(SharedResource.LogicIfDepth, obj.strActualRes);
		        					//SharedResource.Else = obj.strActualRes;
		        					if (SharedResource.Else.equalsIgnoreCase("FAIL")){
		        						do {
		        							updateScenarioResult(objScenarioSheet, this.iTCRowCounter, 15);
		        							this.iTCRowCounter ++;
		        							
		        	                    	rowScen = objScenarioSheet.getRow(this.iTCRowCounter);
		        	                    	objKDFEvent = new KDFEvent(rowScen);
		        	                    	strAction=objKDFEvent.strAction;
		        	                    	strResultMsg="Event|Statement Skipped";
		        						}while(!objKDFEvent.strAction.equalsIgnoreCase("END_IF"));
		        						this.iTCRowCounter--;
		        					}
		        					SharedResource.Logic_If="";
		        			    	SharedResource.Else_If="";
		        			    	SharedResource.Else="";
		        			    	break;
		        				}
		        				
		        				
								case "SELECTCASE":
		                         {  
		             				do {
		             					updateScenarioResult(objScenarioSheet, this.iTCRowCounter, 15);
		     							this.iTCRowCounter ++;
		     	                    	rowScen = objScenarioSheet.getRow(this.iTCRowCounter);
		     	                    	objKDFEvent = new KDFEvent(rowScen);
		     	                    	strResultMsg="Switch/Select case begins";
		     	                    	strAction=objKDFEvent.strAction;
		     						}while(!objKDFEvent.strAction.equalsIgnoreCase("CaseValue")&&(!objKDFEvent.strAction.equalsIgnoreCase("EndSelect")));
		     						this.iTCRowCounter--;
		     						break;
		                         }
		        				
								case "CASEVALUE":
		                         {   
		                         	
		                         	if(SharedResource.SelectCaseStatus.equalsIgnoreCase("")){
		                         		do {
		                         			updateScenarioResult(objScenarioSheet, this.iTCRowCounter, 15);
		     								this.iTCRowCounter ++;
		     		                    	rowScen = objScenarioSheet.getRow(this.iTCRowCounter);
		     		                    	objKDFEvent = new KDFEvent(rowScen);
		     		                    	strResultMsg="Event|Statement Skipped";
		     		                    	strAction=objKDFEvent.strAction;
		     							}while(!objKDFEvent.strAction.equalsIgnoreCase("CaseValue")&&(!objKDFEvent.strAction.equalsIgnoreCase("EndSelect")));
		                         		this.iTCRowCounter--;
		                         		}
		                         	break;
		                         }
		              

								case "END_IF":
									break;
								case "ENDSELECT":
									break;	
								default:{
								}
							}
	        				
	        				
	        				
	        				
	        				
	        				if(objKDFEvent.strAction.equalsIgnoreCase("LAUNCH"))
	        				{
	        					objWebDriver = obj.objWebDriver;
	        					strBrowser = obj.strBrowser;
	        				}
	        				
	        				
					        strResult = obj.strStatus;
					        strResultMsg = obj.strActualRes;               	
				
			                
	        				try
	                        {
								Thread.sleep(100);
							} 
	                        catch (InterruptedException e)
	                        {
								e.printStackTrace();
							}
					
	        				if(forLoopDepth==0){
	        					updateScenarioResult(objScenarioSheet, this.iTCRowCounter, 15);  // iTCRowCounter : current row number
	        																		// 15 : column in normal scenario
	        				}else{  
	        					String strmsg = "";
	        					String iterationNumber = "" ;
	        						if(forLoopDepth==1){
	        							iterationNumber = String.valueOf(forLoopIterationCounter);
	        						}else{
	        							iterationNumber = primaryForLoopIterCounter + "," + forLoopIterationCounter ;
	        						}
	        						
		        					if (dictStatusMsg.containsKey(this.iTCRowCounter))
		    						{
		    							strmsg = dictStatusMsg.get(this.iTCRowCounter);
		    							strmsg = strmsg + "\n" + "Itr#" + iterationNumber + " " + strResultMsg;
		    							dictStatusMsg.put(this.iTCRowCounter, strmsg);
		    						}
		    						else{
		    						dictStatusMsg.put(this.iTCRowCounter, "Itr#" + iterationNumber + " " + strResultMsg);
		    						}
		    						strmsg = "";
		    						if (dictStatus.containsKey(this.iTCRowCounter))
		    						{
		    							strmsg = dictStatus.get(this.iTCRowCounter);
		    							strmsg = strmsg + "\n" + "Itr#" + iterationNumber + " " + strResult;
		    							dictStatus.put(this.iTCRowCounter,  strmsg);
		    						}
		    						else
		    						{
		    						dictStatus.put(this.iTCRowCounter, "Itr#" + iterationNumber + " "+  strResult);
		    						}
		    						
		    					
		    						strmsg = ""; 
		    						int row=24;
		    						String[] IterArr = iterationNumber.split(",");
		    						iterationNumber = IterArr[0];
	    							int Num=row + Integer.parseInt(iterationNumber);
		    						if (dictSnapshotPath.containsKey(this.iTCRowCounter) )
		    						{
		    							strmsg = dictSnapshotPath.get(this.iTCRowCounter);
		    						
		    						   createHyperLinkAlternate(objScenarioSheet.getRow(this.iTCRowCounter).getCell(Num, NullAsBlank));
		    							strmsg = strmsg + "\n" + "Itr#" + iterationNumber + " " + strScreenshotPath;
		    							dictSnapshotPath.put(this.iTCRowCounter,  strmsg);
		    						}
		    						else
		    						{
		    							dictSnapshotPath.put(this.iTCRowCounter, "Itr#" + iterationNumber + " "+  strScreenshotPath);
		    							 createHyperLinkAlternate(objScenarioSheet.getRow(this.iTCRowCounter).getCell(Num, NullAsBlank));
		    						}
	        					}
	        				
	        				//Move to Next Testcase if Result Msg is Exit Testcase
	        				if(strResultMsg!=null){
		        				 if(strResultMsg.equalsIgnoreCase("EXIT TESTCASE")){ 
			        					 while(!objKDFEvent.strAction.equalsIgnoreCase("Testcase")){
				                         		if(objKDFEvent.strAction.isEmpty()){
				                         			break;
				                         			
				                         		}
				                         		this.iTCRowCounter ++; 
				                         		objKDFEvent = new KDFEvent(objScenarioSheet, this.iTCRowCounter);
			                         	}
		                         	this.iTCRowCounter = this.iTCRowCounter-1 ;
		        				 }
		        				 
		        				//Move to Next Testcase if Result Msg is Exit Scenario
		        				 if(strResultMsg.equalsIgnoreCase("EXIT SCENARIO")){
			        					 while(!objKDFEvent.strAction.isEmpty()){
				                         		this.iTCRowCounter ++; 
				                         		objKDFEvent = new KDFEvent(objScenarioSheet, this.iTCRowCounter);
			                         	}
		        					 this.iTCRowCounter = this.iTCRowCounter-1 ;
		        				 }
	        				}
	        				
	                        if(SharedResource.isVerificationKeyword){
	                        	enterDetailsForImprovedReporting(SharedResource.expectedResult,objScenarioSheet,this.iTCRowCounter);
	                        	SharedResource.isVerificationKeyword = false ;
	                        }
	                    }
                    } 		
        		}

        		else
        		{
        			
        			
        			PrintMessage.printTestCase(strSheetName, objKDFEvent);        			
        			strTestCaseName = objKDFEvent.strChild;
        			strTestCaseResult="Pass";
					if (intDBTesCaseExecuted > 0)
					{
					     if (!boolTCFlag)
					     {
					         intDBTesCaseFailed++;
					     }
					     else
					     {
					         intDBTesCasePassed++;
					     }
					}
					boolTCFlag = true;     
					strHashMapTestStepResults=new HashMap<Integer,TestStepResults>();
		        	tcStart= Calendar.getInstance().getTime();
		        	strTestStepID=1;
		        	strTestCaseID++;
		        	tcEnd=null;
		        	strTestStepPassCnt=0;
		        	strTestStepFailCnt=0;
        			
        			SharedResource.TestCaseResultFolderPath=SharedResource.SuiteResultFolderPath +"\\"+strTestCaseName;
        			UtilityClass.createFolder(SharedResource.TestCaseResultFolderPath);
        			SharedResource.TestCaseScreenShotPath=SharedResource.TestCaseResultFolderPath+"\\ScreenShots";
        			UtilityClass.createFolder(SharedResource.TestCaseScreenShotPath);
        			
        			//intDBTesCaseExecuted++;
        		}         		
        	}
        	else if(objKDFEvent.strFlag.toUpperCase().equals("N")  && (objKDFEvent.strAction.toUpperCase().equals("TESTCASE")))
        	{   
        		
        	}
			else
        	{
        		 intAutomated_Scenario_Count = intAutomated_Scenario_Count + 1;
        	}
        	if(!(SharedResource.strContinueExecution.equalsIgnoreCase("TRUE")))
			{
        		//return;
        		if(SharedResource.strSkipExecution.equalsIgnoreCase("STEP"))
        		{       			
        			do
        			{
        				this.iTCRowCounter ++;
                    	rowScen = objScenarioSheet.getRow(this.iTCRowCounter);
                    	objKDFEvent = new KDFEvent(rowScen);
        			} while(!objKDFEvent.strAction.equals("")&&(!objKDFEvent.strAction.equalsIgnoreCase("STEP"))&&(!objKDFEvent.strAction.equalsIgnoreCase("TestCase")));
        			this.iTCRowCounter --;
        			SharedResource.strContinueExecution = "TRUE";
        			SharedResource.strSkipExecution = "";
        		}
        		else if(SharedResource.strSkipExecution.equalsIgnoreCase("TestCase") || SharedResource.strSkipExecution.equalsIgnoreCase("TC")) 
        		{
        			do
        			{
        				this.iTCRowCounter ++;
                    	rowScen = objScenarioSheet.getRow(this.iTCRowCounter);
                    	objKDFEvent = new KDFEvent(rowScen);
        			}while(!objKDFEvent.strAction.equals("") && (!objKDFEvent.strAction.equalsIgnoreCase("TestCase")));
        			this.iTCRowCounter --;
        			SharedResource.strContinueExecution = "TRUE";
        			SharedResource.strSkipExecution = "";
        		}  
        		else if(SharedResource.strSkipExecution.equalsIgnoreCase("") || SharedResource.strSkipExecution.equalsIgnoreCase("Execution"))
        		{
        			return;
        		}        		
			}
        	
        	this.iTCRowCounter ++;
        	
        	rowScen = objScenarioSheet.getRow(this.iTCRowCounter);
        	if(rowScen!= null){
        		objKDFEvent = new KDFEvent(rowScen);
        	}else{
        		break;
        	}
        } 
        testSuiteEndDate=new Date();
        String suiteURl=SharedResource.SuiteResultFolderPath +"\\" +"Result_"+ strSheetName +".html";
        tsResult=new TestSuiteResults(testSuiteStartDate,testSuiteEndDate,strSheetName,strTCFileName,strTestCasePassCnt,strTestCaseFailCnt,suiteURl,strHashMapTestCaseResults);
        ResultLog.TestSuiteReport(tsResult);
        strHashMapTestSuiteResults.put(strTestSuiteID, tsResult);
        strTestSuiteID++;
        
        
	}
	
	
	

    public boolean handleCombinedKeyword(XSSFRow currentRow)
    {	
    	
    	String combinationName="",combinationDescr="",sheetName=""; 
    	Date CombinedTCEnd,CombinedTCStart=new Date();
    	
		SharedResource.CombinedKeywordResultFolderPath=SharedResource.TestCaseResultFolderPath +"\\"+combinationName;
		UtilityClass.createFolder(SharedResource.CombinedKeywordResultFolderPath);
		SharedResource.CombinedKeywordScreenShotPath=SharedResource.CombinedKeywordResultFolderPath +"\\ScreenShots";
		UtilityClass.createFolder(SharedResource.CombinedKeywordScreenShotPath);
		
		strHashMapCombinedKeywordResults=new HashMap<Integer,TestStepResults>();
		
		int combinationStartID=strTestStepID;
		
		int combinedTestStepID=1;
		String combinedTCStatus="PASS", combinedTCActualRes="Combined Keyword executed successfully";
		
		List<Integer> CombinedKeywordStepsList=new ArrayList<Integer>();
		
		CombinedKeywordStepsList.add(strTestStepID);
		
    	
    	Boolean status = false;
    	
    	KDFEvent objKDFEvent = new KDFEvent(currentRow);
    	combinationName=objKDFEvent.strParam1;
    	combinationDescr=objKDFEvent.strParam2;
    	sheetName=objKDFEvent.strParam3;
    	
    	XSSFSheet combinedKeywordSheet=null;
    	try 
		{
			System.out.println("Combined Keyword : "+combinationName);
			combinedKeywordSheet= workbook.getSheet(sheetName);	
		 
			int iRow = 1;			
			row = combinedKeywordSheet.getRow(iRow);	
			
			row.getCell(0,NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
			String stepFlag = row.getCell(0,NullAsBlank).getStringCellValue().toString();
			
			row.getCell(1,NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
			String eventName = row.getCell(1,NullAsBlank).getStringCellValue().toString();			
			
			row.getCell(4,NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
			String stepName = row.getCell(4,NullAsBlank).getStringCellValue().toString();		
			
			
			do{
				if ((eventName.equals("Create_Combine_Keywords"))&& (stepName.equals(combinationName))){
					if (stepFlag.equals("Y") )
					{	
						iRow++;
						row = combinedKeywordSheet.getRow(iRow);
						objKDFEvent = new KDFEvent(row);
						do{
							if  (stepFlag.equals("Y") )
							{
								
								PrintMessage.printKDFEvent(objKDFEvent);
								
		        				InvokeKeywordForExecution obj = new InvokeKeywordForExecution(objWebDriver, objKDFEvent);
		        				obj.performAction();
		        				
		        				if(objKDFEvent.strAction.equalsIgnoreCase("LAUNCH"))
		        				{
		        					objWebDriver = obj.objWebDriver;
		        					strBrowser = obj.strBrowser;
		        				}
		        				
		        				if(objKDFEvent.strAction.toUpperCase().contains("LAUNCH") | objKDFEvent.strAction.toUpperCase().contains("VERIFY") | objKDFEvent.strAction.toUpperCase().contains("SET"))
		        				{
		        					strScreenshotPath = new CaptureScreenShot(objWebDriver, objKDFEvent.strAction).CaptureResultScreenshot(strBrowser);
		        				}
		        				
		        				
		        				strResult = obj.strStatus;
		        				strResultMsg = obj.strActualRes;
		        				
		        				if (strResult=="FAIL" | strResult=="Fail") {
		        					combinedTCStatus="Fail";
		        				}
		        				
		        				updateScenarioResult(combinedKeywordSheet, iRow, 15);
		        				iRow++;
		        				
		        				row = combinedKeywordSheet.getRow(iRow);
								objKDFEvent = new KDFEvent(row);
	
		        				row.getCell(1,NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
		        				eventName = row.getCell(1,NullAsBlank).getStringCellValue().toString();
		        				
		        				row.getCell(0,NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
		        				stepFlag = row.getCell(0,NullAsBlank).getStringCellValue().toString();
		        			}
							if (eventName.equals("End_Combination")){
								status=true;
								break;
							}
						}while(true);
        			
					}
					else{
						break;
					}
				}
				if (status==true){
					break;
				}
				
				iRow++;		
				CombinedKeywordStepsList.add(strTestCaseID);
				
				row = combinedKeywordSheet.getRow(iRow);
				row.getCell(1,NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
				eventName = row.getCell(1,NullAsBlank).getStringCellValue().toString();
				row.getCell(0,NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
				stepFlag = row.getCell(0,NullAsBlank).getStringCellValue().toString();
				TestStepResults tsResult=new TestStepResults(combinedTestStepID,strAction,strResultMsg,"",strResultMsg,strResult, strScreenshotPath);
				strHashMapCombinedKeywordResults.put(combinedTestStepID, tsResult);
				combinedTestStepID++;
				
			}while(true);
						
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			combinedTCActualRes="Combined keyword execution failed";
     	}		
    	
    	CombinedTCEnd=new Date();
    	
    	
    	int ln=CombinedKeywordStepsList.size();
    	for (int i=0;i<ln;i++) {
    		strHashMapTestCaseResults.remove(CombinedKeywordStepsList.get(i));
    	}
    	
    	
    	TestCaseResults tcResult=new TestCaseResults(CombinedTCStart,CombinedTCEnd,sheetName,"Combine_Keywords_"+combinationName,strTestStepPassCnt,strTestStepFailCnt,strHashMapTestStepResults);
		ResultLog.TestCaseReport(tcResult);
		strHashMapTestCaseResults.put(strTestCaseID, tcResult);
		tcResult=null;
		strTestCaseName="";
		
    	
    	
    	TestStepResults tsResult=new TestStepResults(combinationStartID,"Combine_Keywords_"+combinationName,combinedTCActualRes,"",combinedTCActualRes,combinedTCStatus, "");
    	strHashMapTestStepResults.put(combinationStartID, tsResult);
    	return status;
    	
    }
    

	//###################################################################################
	// Function : updateScenarioResult(XSSFSheet objTargetSheet, int intRowNum, int intColumnNum)   	
	// Description : This method is used to update the result status of the action performed.
	// Parameters : XSSFSheet objTargetSheet - represents the Scenario Sheet under execution.
	//				int intRowNum - represents the row number of the excel sheet.
	//				int intColumnNum - represents the column number from where we need to start adding results.
	// Author : 
	// Date : 
	//###################################################################################		
	
	//updated for handling multiple iteration for call page component
	
	public void updateScenarioResult(XSSFSheet objTargetSheet, int intRowNum, int intColumnNum)
	{
		 try {			
			 //String strScreenShotlink = CaptureResultScreenshot();
			 
			
			createHyperLinkAlternate(objTargetSheet.getRow(intRowNum).getCell(intColumnNum, NullAsBlank));
			
			
			
			objTargetSheet.getRow(intRowNum).getCell(intColumnNum + 1, NullAsBlank).setCellValue(strResultMsg); // Sets result Message.
			
			
			DateFormat objdateFormat = new SimpleDateFormat("dd-MMMMM-yyyy-h:mm:ss a");
			Date objdate = new Date();			
			objTargetSheet.getRow(intRowNum).getCell(intColumnNum + 3, NullAsBlank).setCellValue(objdateFormat.format(objdate)); // Sets Execution time
			
			
			objTargetSheet.getRow(intRowNum).getCell(intColumnNum + 4, NullAsBlank).setCellValue(strResult); // Sets Status as PASS/FAIL.
			if(!(strResult.toUpperCase().contains("FAIL")))
	        {
				objTargetSheet.getRow(intRowNum).getCell(intColumnNum + 4, NullAsBlank).setCellStyle(styles.get("Pass"));	
				strTestStepPassCnt=strTestStepPassCnt+1;
	        }
	        else
	        {
	        	objTargetSheet.getRow(intRowNum).getCell(intColumnNum + 4, NullAsBlank).setCellStyle(styles.get("Fail"));
	        	strTestStepFailCnt=strTestStepFailCnt+1;
	        	strTestCaseResult="Fail";
	        }
			
			//For Showing Snapshot Path in Column V 
			objTargetSheet.getRow(intRowNum).getCell(intColumnNum + 6, NullAsBlank).setCellValue(strSnapshotPathArray);
			TestStepResults tsResult=new TestStepResults(strTestStepID,strAction,strResultMsg,"",strResultMsg,strResult, strScreenshotPath);
			strHashMapTestStepResults.put(strTestStepID, tsResult);
			tsResult=null;
			strTestStepID++;
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
	}	



	
	/*

	public void TestCaseProcessor(String strSheetName)
	{
		
		
		//Reporting Variable
        Date tcStart=null, tcEnd= null, testSuiteStartDate=null, testSuiteEndDate=null;
        
		objScenarioSheet = workbook.getSheet(strSheetName);
        XSSFRow rowScen = objScenarioSheet.getRow(iTCRowCounter);
        rowScen.getCell(0, NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
        KDFEvent objKDFEvent = new KDFEvent(objScenarioSheet, iTCRowCounter);     
        strHashMapTestCaseResults = new HashMap<Integer, TestCaseResults>();
        
        TestSuiteResults tsResult=null;
        
        testSuiteStartDate = new Date();					
		iTCRowCounter = 1;
		strTestCasePassCnt=0;
        strTestCaseFailCnt=0;
        strTestCaseSkippedCnt=0;
        strTestSuiteID++;
        
        while(!(objKDFEvent.strFlag.equals("")) && !(objKDFEvent.strFlag.equals(null)))
        {        	
        	//objKDFEvent = new KDFEvent(objScenarioSheet, iTCRowCounter);
        	if(objKDFEvent.strFlag.toUpperCase().equals("Y"))
        	{     		
        		strAction=objKDFEvent.strAction;
        		//If Action is not equal to TestCase means If Action is other than TestCase.
        		if (!(objKDFEvent.strAction.toUpperCase().equals("TESTCASE")))
        		{
        			strResultMsg = "";
                    strResult = "";
                    errormsg = null;
                    
                    if(objKDFEvent.strAction.toUpperCase().contains("END_LOOP"))
                    {
                    	break;
                    }
                    else if(objKDFEvent.strAction.toUpperCase().contains("MOVETONEXTITERATION"))
                    {
                    	break;
                    }
                    else if (objKDFEvent.strAction.toUpperCase().equals("EXITTESTCASE"))
                    {                   	
                    	while(!objKDFEvent.strAction.equalsIgnoreCase("Testcase")){
                    		if(objKDFEvent.strAction.isEmpty()){
                    			break;
                    		}
                    		iTCRowCounter ++; 
                    		objKDFEvent = new KDFEvent(objScenarioSheet, iTCRowCounter);
                    	}
                    	iTCRowCounter = iTCRowCounter-1 ;
                    }
                    else if (objKDFEvent.strAction.toUpperCase().equalsIgnoreCase( "EndTestCase"))
                    {
	        			//PrintMessage.printStepNumber(objKDFEvent.strParent);
	        			tcEnd= Calendar.getInstance().getTime();
	        			
	        			TestCaseResults tcResult=new TestCaseResults(tcStart,tcEnd,strSheetName,strTestCaseName,strTestStepPassCnt,strTestStepFailCnt,strHashMapTestStepResults);
	        			ResultLog.TestCaseReport(tcResult);
	        			strHashMapTestCaseResults.put(strTestCaseID, tcResult);
	        			tcResult=null;
	        			tcStart=null;
	        			tcEnd=null;
	        			strTestCaseName="";
	        			strHashMapTestStepResults=null;
	        			if (strTestCaseResult=="Pass") 
	        				strTestCasePassCnt++;
	        			else
	        				strTestCaseFailCnt++;
	        			
	        			
	        		}        
	                
                    else if (objKDFEvent.strAction.toUpperCase().contains("BLOCK"))
                    {
                    }
                    else if (objKDFEvent.strAction.toUpperCase().contains("STEP"))
                    {
            			PrintMessage.printStepNumber(objKDFEvent.strParent);
                    }                    
                    else if (objKDFEvent.strAction.equalsIgnoreCase("Begin_Loop"))
                    {   
                    	handleFORLoop_new(rowScen);
                    }
                    
                   
                    else if (objKDFEvent.strAction.equalsIgnoreCase("Call_Combine_Keywords")){
                    	handleCombinedKeyword(rowScen);
                    }
                    
                    else
                    {   
        				PrintMessage.printKDFEvent(objKDFEvent);
        				InvokeKeywordForExecution obj = new InvokeKeywordForExecution(objWebDriver, objKDFEvent);
        				obj.performAction();
        				
        				if(objKDFEvent.strAction.equalsIgnoreCase("LAUNCH"))
        				{
        					objWebDriver = obj.objWebDriver;
        					strBrowser = obj.strBrowser;
        				}
        				
        				strScreenshotPath = new CaptureScreenShot(objWebDriver, objKDFEvent.strAction).CaptureResultScreenshot(strBrowser);
				        strResult = obj.strStatus;
				        strResultMsg = obj.strActualRes;               	
			
		                        
				        if(objKDFEvent.strAction.equalsIgnoreCase("Logic_IF"))  //Condition is True
        				{
        					//Add LogicIf Status to Hash table
        		      		SharedResource.dictLogcIfStatusCollection.put(SharedResource.LogicIfDepth, obj.strActualRes);
        					SharedResource.Logic_If = obj.strActualRes;
        					strResultMsg = "Condition is "+ obj.strActualRes;
        					if (SharedResource.Logic_If.equalsIgnoreCase("FAIL")){
        						do {
        							updateScenarioResult(objScenarioSheet, iTCRowCounter, 15);
        							iTCRowCounter ++;
        	                    	rowScen = objScenarioSheet.getRow(iTCRowCounter);
        	                    	objKDFEvent = new KDFEvent(rowScen);
        	                    	strResultMsg="Event|Statement Skipped";
        						}while(!objKDFEvent.strAction.equalsIgnoreCase("ELSE")&&(!objKDFEvent.strAction.equalsIgnoreCase("ELSE_IF"))&&(!objKDFEvent.strAction.equalsIgnoreCase("END_IF")));
        						iTCRowCounter--;
        					}
        					
        				}
        				
        				
				        else if(objKDFEvent.strAction.equalsIgnoreCase("ELSE_IF"))  //
        				{
        					//Add LogicIf Status to Hash table
        		      		SharedResource.dictLogcIfStatusCollection.put(SharedResource.LogicIfDepth, obj.strActualRes);
        					//SharedResource.Else_If = obj.strStatus;
        					strResultMsg = "Condition is "+ obj.strActualRes;
        					if (SharedResource.Else_If.equalsIgnoreCase("FAIL")){
        						do {
        							
        							updateScenarioResult(objScenarioSheet, iTCRowCounter, 15);
        							
        							iTCRowCounter ++;
        	                    	rowScen = objScenarioSheet.getRow(iTCRowCounter);
        	                    	objKDFEvent = new KDFEvent(rowScen);
        	                    	strResultMsg="Event|Statement Skipped";
        						}while(!objKDFEvent.strAction.equalsIgnoreCase("ELSE")&&(!objKDFEvent.strAction.equalsIgnoreCase("ELSE_IF"))&&(!objKDFEvent.strAction.equalsIgnoreCase("END_IF")));
        						iTCRowCounter--;
        					}
        					
        				}
				        else if(objKDFEvent.strAction.equalsIgnoreCase("Else"))  //
        				{
        					SharedResource.dictLogcIfStatusCollection.put(SharedResource.LogicIfDepth, obj.strActualRes);
        					//SharedResource.Else = obj.strActualRes;
        					if (SharedResource.Else.equalsIgnoreCase("FAIL")){
        						do {
        							updateScenarioResult(objScenarioSheet, iTCRowCounter, 15);
        							iTCRowCounter ++;
        							
        	                    	rowScen = objScenarioSheet.getRow(iTCRowCounter);
        	                    	objKDFEvent = new KDFEvent(rowScen);
        	                    	strResultMsg="Event|Statement Skipped";
        						}while(!objKDFEvent.strAction.equalsIgnoreCase("END_IF"));
        						iTCRowCounter--;
        					}
        					SharedResource.Logic_If="";
        			    	SharedResource.Else_If="";
        			    	SharedResource.Else="";
        				}
        				
        				
        				 else if (objKDFEvent.strAction.equalsIgnoreCase("SelectCase"))
                         {  
             				do {
             					updateScenarioResult(objScenarioSheet, iTCRowCounter, 15);
     							iTCRowCounter ++;
     	                    	rowScen = objScenarioSheet.getRow(iTCRowCounter);
     	                    	objKDFEvent = new KDFEvent(rowScen);
     						}while(!objKDFEvent.strAction.equalsIgnoreCase("CaseValue")&&(!objKDFEvent.strAction.equalsIgnoreCase("EndSelect")));
     						iTCRowCounter--;
                         }
        				
        				 else if (objKDFEvent.strAction.equalsIgnoreCase("CaseValue"))
                         {   
                         	
                         	if(SharedResource.SelectCaseStatus.equalsIgnoreCase("")){
                         		do {
                         			updateScenarioResult(objScenarioSheet, iTCRowCounter, 15);
     								iTCRowCounter ++;
     		                    	rowScen = objScenarioSheet.getRow(iTCRowCounter);
     		                    	objKDFEvent = new KDFEvent(rowScen);
     		                    	strResultMsg="Event|Statement Skipped";
     							}while(!objKDFEvent.strAction.equalsIgnoreCase("CaseValue")&&(!objKDFEvent.strAction.equalsIgnoreCase("EndSelect")));
                         		iTCRowCounter--;
                         		}
                         }
              
                         else if (objKDFEvent.strAction.equalsIgnoreCase("EndSelect")){
                         	
                         }
              
        				
        				//## Handling Goto_True and Goto_False sections... Start
        				if(objKDFEvent.strAction.equalsIgnoreCase("Goto_True") && SharedResource.dictLogcIfStatusCollection.get(SharedResource.LogicIfDepth).equalsIgnoreCase("FAIL"))			
        				{
        					int currentLogicIfDepth = SharedResource.LogicIfDepth;
    			    		iTCRowCounter ++;
    			    		rowScen = objScenarioSheet.getRow(iTCRowCounter);
    			    		objKDFEvent = new KDFEvent(rowScen);
    			            while(!(objKDFEvent.strFlag.equals("")) && !(objKDFEvent.strFlag.equals(null)))
    			            {   
    			            	if(objKDFEvent.strAction.equals("Logic_IF"))
    			            	{
    			            		currentLogicIfDepth = currentLogicIfDepth +1;
    			            	}
    			            	if(objKDFEvent.strAction.equals("End_True") && SharedResource.LogicIfDepth==currentLogicIfDepth )
    			            	{
    			            		break;
    			            	}else if(objKDFEvent.strAction.equals("End_False")){
    			            		currentLogicIfDepth = currentLogicIfDepth -1 ;
    			            	}
    			            	iTCRowCounter ++;  
    			            	rowScen = objScenarioSheet.getRow(iTCRowCounter);
    			            	objKDFEvent = new KDFEvent(rowScen);
    			            }       						
        				}
        				else if(objKDFEvent.strAction.equalsIgnoreCase("Goto_False") && SharedResource.dictLogcIfStatusCollection.get(SharedResource.LogicIfDepth).equalsIgnoreCase("PASS"))
        				{
        					int currentLogicIfDepth = SharedResource.LogicIfDepth;
        			   		//iTCRowCounter ++;
        		    		rowScen = objScenarioSheet.getRow(iTCRowCounter);
        		    		objKDFEvent = new KDFEvent(rowScen);
        		            while(!(objKDFEvent.strFlag.equals("")) && !(objKDFEvent.strFlag.equals(null)))
        		            {   
        		            	if(objKDFEvent.strAction.equals("Logic_IF"))
    			            	{
    			            		currentLogicIfDepth = currentLogicIfDepth +1;
    			            	}
        		            	if(objKDFEvent.strAction.equals("End_False") && SharedResource.LogicIfDepth==currentLogicIfDepth)
        		            	{
            		            	//iTCRowCounter ++;  
        		            		break;
        		            	}else if(objKDFEvent.strAction.equals("End_False")){
        		            		currentLogicIfDepth = currentLogicIfDepth -1  ;
        		            	}
        		            	iTCRowCounter ++;  
        		            	rowScen = objScenarioSheet.getRow(iTCRowCounter);
        		            	objKDFEvent = new KDFEvent(rowScen);
        		            }       		 
        					SharedResource.Logic_If = "";
        				}	
        				//## Handling Goto_True and Goto_False sections... ends       				
        				
        				try
                        {
							Thread.sleep(100);
						} 
                        catch (InterruptedException e)
                        {
							e.printStackTrace();
						}
				
        				if(forLoopDepth==0){
        					updateScenarioResult(objScenarioSheet, iTCRowCounter, 15);  // iTCRowCounter : current row number
        																		// 15 : column in normal scenario
        				}else{  
        					String strmsg = "";
        					String iterationNumber = "" ;
        						if(forLoopDepth==1){
        							iterationNumber = String.valueOf(forLoopIterationCounter);
        						}else{
        							iterationNumber = primaryForLoopIterCounter + "," + forLoopIterationCounter ;
        						}
        						
	        					if (dictStatusMsg.containsKey(iTCRowCounter))
	    						{
	    							strmsg = dictStatusMsg.get(iTCRowCounter);
	    							strmsg = strmsg + "\n" + "Itr#" + iterationNumber + " " + strResultMsg;
	    							dictStatusMsg.put(iTCRowCounter, strmsg);
	    						}
	    						else{
	    						dictStatusMsg.put(iTCRowCounter, "Itr#" + iterationNumber + " " + strResultMsg);
	    						}
	    						strmsg = "";
	    						if (dictStatus.containsKey(iTCRowCounter))
	    						{
	    							strmsg = dictStatus.get(iTCRowCounter);
	    							strmsg = strmsg + "\n" + "Itr#" + iterationNumber + " " + strResult;
	    							dictStatus.put(iTCRowCounter,  strmsg);
	    						}
	    						else
	    						{
	    						dictStatus.put(iTCRowCounter, "Itr#" + iterationNumber + " "+  strResult);
	    						}
	    						
	    					
	    						strmsg = ""; 
	    						int row=24;
	    						String[] IterArr = iterationNumber.split(",");
	    						iterationNumber = IterArr[0];
    							int Num=row + Integer.parseInt(iterationNumber);
	    						if (dictSnapshotPath.containsKey(iTCRowCounter) )
	    						{
	    							strmsg = dictSnapshotPath.get(iTCRowCounter);
	    						
	    						   createHyperLinkAlternate(objScenarioSheet.getRow(iTCRowCounter).getCell(Num, NullAsBlank));
	    							strmsg = strmsg + "\n" + "Itr#" + iterationNumber + " " + strScreenshotPath;
	    							dictSnapshotPath.put(iTCRowCounter,  strmsg);
	    						}
	    						else
	    						{
	    							dictSnapshotPath.put(iTCRowCounter, "Itr#" + iterationNumber + " "+  strScreenshotPath);
	    							 createHyperLinkAlternate(objScenarioSheet.getRow(iTCRowCounter).getCell(Num, NullAsBlank));
	    						}
        					}
        				
        				//Move to Next Testcase if Result Msg is Exit Testcase
        				if(strResultMsg!=null){
	        				 if(strResultMsg.equalsIgnoreCase("EXIT TESTCASE")){ 
		        					 while(!objKDFEvent.strAction.equalsIgnoreCase("Testcase")){
			                         		if(objKDFEvent.strAction.isEmpty()){
			                         			break;
			                         			
			                         		}
			                         		iTCRowCounter ++; 
			                         		objKDFEvent = new KDFEvent(objScenarioSheet, iTCRowCounter);
		                         	}
	                         	iTCRowCounter = iTCRowCounter-1 ;
	        				 }
	        				 
	        				//Move to Next Testcase if Result Msg is Exit Scenario
	        				 if(strResultMsg.equalsIgnoreCase("EXIT SCENARIO")){
		        					 while(!objKDFEvent.strAction.isEmpty()){
			                         		iTCRowCounter ++; 
			                         		objKDFEvent = new KDFEvent(objScenarioSheet, iTCRowCounter);
		                         	}
	        					 iTCRowCounter = iTCRowCounter-1 ;
	        				 }
        				}
        				
                        if(SharedResource.isVerificationKeyword){
                        	enterDetailsForImprovedReporting(SharedResource.expectedResult,objScenarioSheet,iTCRowCounter);
                        	SharedResource.isVerificationKeyword = false ;
                        }
            } 		
		}

        		else
        		{
        			
        			
        			PrintMessage.printTestCase(strSheetName, objKDFEvent);        			
        			strTestCaseName = objKDFEvent.strChild;
        			strTestCaseResult="Pass";
					if (intDBTesCaseExecuted > 0)
					{
					     if (!boolTCFlag)
					     {
					         intDBTesCaseFailed++;
					     }
					     else
					     {
					         intDBTesCasePassed++;
					     }
					}
					boolTCFlag = true;     
					strHashMapTestStepResults=new HashMap<Integer,TestStepResults>();
		        	tcStart= Calendar.getInstance().getTime();
		        	strTestStepID=1;
		        	strTestCaseID++;
		        	tcEnd=null;
		        	strTestStepPassCnt=0;
		        	strTestStepFailCnt=0;
        			
        			SharedResource.TestCaseResultFolderPath=SharedResource.SuiteResultFolderPath +"\\"+strTestCaseName;
        			UtilityClass.createFolder(SharedResource.TestCaseResultFolderPath);
        			SharedResource.TestCaseScreenShotPath=SharedResource.TestCaseResultFolderPath+"\\ScreenShots";
        			UtilityClass.createFolder(SharedResource.TestCaseScreenShotPath);
        			
        			//intDBTesCaseExecuted++;
        		}         		
        	}
        	else if(objKDFEvent.strFlag.toUpperCase().equals("N")  && (objKDFEvent.strAction.toUpperCase().equals("TESTCASE")))
        	{   
        		
        	}
			else
        	{
        		 intAutomated_Scenario_Count = intAutomated_Scenario_Count + 1;
        	}
        	if(!(SharedResource.strContinueExecution.equalsIgnoreCase("TRUE")))
			{
        		//return;
        		if(SharedResource.strSkipExecution.equalsIgnoreCase("STEP"))
        		{       			
        			do
        			{
        				iTCRowCounter ++;
                    	rowScen = objScenarioSheet.getRow(iTCRowCounter);
                    	objKDFEvent = new KDFEvent(rowScen);
        			} while(!objKDFEvent.strAction.equals("")&&(!objKDFEvent.strAction.equalsIgnoreCase("STEP"))&&(!objKDFEvent.strAction.equalsIgnoreCase("TestCase")));
        			iTCRowCounter --;
        			SharedResource.strContinueExecution = "TRUE";
        			SharedResource.strSkipExecution = "";
        		}
        		else if(SharedResource.strSkipExecution.equalsIgnoreCase("TestCase") || SharedResource.strSkipExecution.equalsIgnoreCase("TC")) 
        		{
        			do
        			{
        				iTCRowCounter ++;
                    	rowScen = objScenarioSheet.getRow(iTCRowCounter);
                    	objKDFEvent = new KDFEvent(rowScen);
        			}while(!objKDFEvent.strAction.equals("") && (!objKDFEvent.strAction.equalsIgnoreCase("TestCase")));
        			iTCRowCounter --;
        			SharedResource.strContinueExecution = "TRUE";
        			SharedResource.strSkipExecution = "";
        		}  
        		else if(SharedResource.strSkipExecution.equalsIgnoreCase("") || SharedResource.strSkipExecution.equalsIgnoreCase("Execution"))
        		{
        			return;
        		}        		
			}
        	
        	iTCRowCounter ++;
        	
        	rowScen = objScenarioSheet.getRow(iTCRowCounter);
        	if(rowScen!= null){
        		objKDFEvent = new KDFEvent(rowScen);
        	}else{
        		break;
        	}
        } 
        testSuiteEndDate=new Date();
        String suiteURl=SharedResource.SuiteResultFolderPath +"\\" +"Result_"+ strSheetName +".html";
        tsResult=new TestSuiteResults(testSuiteStartDate,testSuiteEndDate,strSheetName,strTCFileName,strTestCasePassCnt,strTestCaseFailCnt,suiteURl,strHashMapTestCaseResults);
        ResultLog.TestSuiteReport(tsResult);
        strHashMapTestSuiteResults.put(strTestSuiteID, tsResult);
        strTestSuiteID++;
        
        
	}
	
	*/
	
	
	
		
	//This function is used to update Test Scenario Flag to false in Test Plan Sheet
	
	public void updateTestPlan(int intRow)
	{
		workbook.getSheet("Test_Plan").getRow(intRow).getCell(0, NullAsBlank).setCellValue("N");				
	}	
	
	
	
	//This function is used to create Link of snapshot images and update link in Excel.
	
	public void createHyperLinkAlternate(XSSFCell LinkCell)
	{		
		String strHyperlink = "HYPERLINK(" + "\""+  strScreenshotPath  + "\"" + "," + "\"" + "Link\"" + ")";   
		LinkCell.setCellFormula(strHyperlink);
		LinkCell.setCellStyle(styles.get("Link"));
	}
		


    public Boolean ResultVerification(String errormsg, String sResult, String output, String sExpected, String Resultmsg)
	{
		if (!(errormsg == null) && (errormsg.equals(""))) {
			if ((sExpected.toUpperCase()).equals(sResult.toUpperCase())) { // Actual output = Fail 
                strResult = "PASS"; //actual = expected
                strResultMsg = Resultmsg;
                return true;
            } else {
                strResult = "FAIL";
                strResultMsg = Resultmsg;
                return false;
            }			
		} else { //no error message        
			if (!(sExpected.equals("")) && (sExpected != null)) {  //  expected value is not blank
                 if (sExpected.toUpperCase().equals(sResult.toUpperCase())) {//expected n actual are same
                     strResult = "PASS";
                     strResultMsg = Resultmsg;
                 } else {//expected and actual are not same
                     strResult = "FAIL";
                     strResultMsg = Resultmsg;
                 }
             } else {//Expected not required              
                 strResult = "PASS"; 
                 strResultMsg = sResult;
             }
             if (output != null) {
                 //SharedResource.dictRTVarCollection[output] = sResult;
                 //Store_EnvVar(output, sResult);
             }
             return true;
        }
	}
	
    
  //###################################################################################
  	// Function : Logic_IF()   	
  	// Description : This method is used to update the result status of the action performed.
  	// Parameters : XSSFSheet objTargetSheet - represents the Scenario Sheet under execution.
  	//				int intRowNum - represents the row number of the excel sheet.
  	//				int intColumnNum - represents the column number from where we need to start adding results.
  	// Author : 
  	// Date : 
  	//###################################################################################		
  	
    
    public Boolean Logic_IF()
    {
        Double val1, val2;
        String str1 = strParam1;
        String str2 = strParam2;
        String str3 = strParam3;
        int result = 0;
        int loc = strParam1.indexOf("^");
        if (loc != -1)
        {
            str1 = strParam1.replace("^", "");
            if (SharedResource.dictRTVarCollection.containsKey(str1))
            {
                str1 = SharedResource.dictRTVarCollection.get(str1);
            }
        }
        else
        {
            str1 = strParam1;
        }

        loc = strParam2.indexOf("^");
        if (loc != -1)
        {
            str2 = strParam2.replace("^", "");
            if (SharedResource.dictRTVarCollection.containsKey(str2))
            {
                str2 = SharedResource.dictRTVarCollection.get(str2);
            }
        }
        else
        {
            str2 = strParam2;
        }
        try
        {
            val1 = Double.parseDouble(str1);
            val2 = Double.parseDouble(str2);
            //val3 = Int32.Parse(str3);   
            //result = val1 - val2;
            // string sResult = result.ToString();

            if (((str3.equals("0")) & (val1.compareTo(val2) == 0)) || ((str3.equals("-1")) & (val1 > val2)) || ((str3.equals("1")) & (val1 < val2)))
            {
                //Console.WriteLine("true");
                Logic_IF_return = "TRUE";
                strResultMsg = "Condition is True";
                return true;
            }
            else
            {
                //Console.WriteLine("false");
                Logic_IF_return = "FALSE";
                strResultMsg = "Condition is False";
                return false;
            }
        } 
        catch (Exception e)
        {
            result = str1.toUpperCase().compareTo(str2.toUpperCase());
            String sResult = Integer.toString(result);            
            if ((sResult.toUpperCase()).equals((strParam3).toUpperCase()))
            {
                Logic_IF_return = "TRUE";
                return true;
            }
            else
            {
                Logic_IF_return = "FALSE";
                return false;
            }
            //Console.WriteLine(e.Message);
        }
    } 
    
  //###################################################################################
  	// Function : Goto_True()  	
  	// Description : This method is used in combination with Logic If - If statement in logic If is true control will go to Goto_True method
  	// Parameters : 
  	// Author : 
  	// Date : 
  	//###################################################################################		
  	
    
    public Boolean Goto_True()
    {
    	if (Logic_IF_return.equals("TRUE"))
    	{
    		strResult = "PASS";
            return true;
    	}
    	else
    	{    		
    		iTCRowCounter ++;
    		XSSFRow rowScen = objScenarioSheet.getRow(iTCRowCounter);
            rowScen.getCell(1, NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
            String strFlagAction = rowScen.getCell(1, NullAsBlank).getStringCellValue();  
            while(!(strFlagAction.equals("")) && !(strFlagAction.equals(null)))
            {            	          	
            	if(strFlagAction.equals("End_True"))
            	{
            		break;
            	}
            	iTCRowCounter ++;  
            	rowScen = objScenarioSheet.getRow(iTCRowCounter);
            	rowScen.getCell(1, NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
                strFlagAction = rowScen.getCell(1, NullAsBlank).getStringCellValue();
            }            
            return true;    		
    	}
    }
    
    
    // Description : This method is used in combination with Logic If & Goto_True - End_Ture will end the Goto_True block.
    public Boolean End_True()
    {
    	if (Logic_IF_return.equals("TRUE"))
    	{
       		strResult = "PASS";
    	}
    	return true;
    }
    
    

    //###################################################################################
	// Function : Goto_False()  	
	// Description : This method is used in combination with Logic If - If logical statement in logic If is false control will go to Goto_False method
	// Parameters : 
	// Author : 
	// Date : 
	//###################################################################################		
	
    
    public Boolean Goto_False()
    {
    	if (Logic_IF_return.equals("FALSE"))
    	{
    		strResult = "PASS";
            return true;
    	}
    	else
    	{    		
    		iTCRowCounter ++;
    		XSSFRow rowScen = objScenarioSheet.getRow(iTCRowCounter);
            rowScen.getCell(1, NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
            String strFlagAction = rowScen.getCell(1, NullAsBlank).getStringCellValue();  
            while(!(strFlagAction.equals("")) && !(strFlagAction.equals(null)))
            {            	          	
            	if(strFlagAction.equals("End_False"))
            	{
            		break;
            	}
            	iTCRowCounter ++;  
            	rowScen = objScenarioSheet.getRow(iTCRowCounter);
            	rowScen.getCell(1, NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
                strFlagAction = rowScen.getCell(1, NullAsBlank).getStringCellValue();
            }            
            return true;    		
    	}
    }
    
    // Description : This method is used in combination with Logic If & Goto_True - End_Ture will end the Goto_True block.
    
    public Boolean End_False()
    {
    	if (Logic_IF_return.equals("FALSE"))
    	{
       		strResult = "PASS";
    	}
    	return true;    	
    }
    
        
    
    public Boolean handleFORLoop_new(XSSFRow currentRow)
    {	
    	forLoopDepth = forLoopDepth+1 ;  	
    	
    	Boolean boolForLoopStatus = true;
    	XSSFSheet CurrentSheet = currentRow.getSheet();
    	int currentRowCntr = currentRow.getRowNum();
    	KDFEvent objKDFEvent = new KDFEvent(currentRow);
    	//int startLoop,endLoop;
    	try {
    		SharedResource.startLoop_BeginLoop = Integer.valueOf(objKDFEvent.strParent);
    		SharedResource.endLoop_BeginLoop = Integer.valueOf(objKDFEvent.strChild);
		} 
    	catch (NumberFormatException e) {
			e.printStackTrace();
			do{
				currentRow = CurrentSheet.getRow(currentRowCntr + 1);
				objKDFEvent = new KDFEvent(currentRow);
				currentRowCntr++;
			} while (!(objKDFEvent.strAction == "End_Loop"));
			this.iTCRowCounter = currentRowCntr;
			return false;
		}
    	
    	if(forLoopDepth == 1){
    		forLoopIterationCounter=0;
    		primaryForLoop_startLoop=SharedResource.startLoop_BeginLoop;
    		primaryForLoop_endLoop=SharedResource.endLoop_BeginLoop;
    		dictStatusMsg = new Hashtable<Integer, String>() ;
    		dictStatus = new Hashtable<Integer, String>();
    		dictSnapshotPath = new Hashtable<Integer, String>();	//For Showing Snapshot path in col V // For HRBPO Only
    	}else{
    		 primaryForLoopIterCounter = forLoopIterationCounter;
    		 forLoopIterationCounter=0;
    	}
    	
    	currentRowCntr++;
    	System.out.println("For Loop Started from Row Number : " + (this.iTCRowCounter + 1));
    	//for(int i = startLoop;startLoop<=endLoop;startLoop++)
    	while(SharedResource.startLoop_BeginLoop <= SharedResource.endLoop_BeginLoop)
    	{    
    		SharedResource.currentIterationNumber = SharedResource.startLoop_BeginLoop-1;
    		forLoopIterationCounter = forLoopIterationCounter +1 ;
    		this.iTCRowCounter = currentRowCntr;
    		
    		if(!objKDFEvent.strParam1.equals("")){
    			SharedResource.dictRTVarCollection.put(objKDFEvent.strParam1,String.valueOf(SharedResource.startLoop_BeginLoop));
    		}
    		
    		TestCaseProcessor(strScenarioName);
    		
    		System.out.println("IN Loop");
    		SharedResource.startLoop_BeginLoop++;
    		if(forLoopDepth == 1){
    			primaryForLoop_startLoop = SharedResource.startLoop_BeginLoop;
    		}
    	}
    	
    	forLoopDepth = forLoopDepth -1;
    	// If Explicitly Loop ends then handel counter
    	if(this.iTCRowCounter<=currentRowCntr){
    		while(!objKDFEvent.strAction.equalsIgnoreCase("End_Loop")){
        		if(objKDFEvent.strAction.isEmpty()){
        			break;
        		}
        		this.iTCRowCounter ++; 
        		objKDFEvent = new KDFEvent(objScenarioSheet, iTCRowCounter);
        	}
    	}
    	
    	if(forLoopDepth==0){
	    	Enumeration<Integer> keys = dictStatus.keys();    	
	    	while(keys.hasMoreElements())    		
	    	{    		
	    		int i = (int)keys.nextElement();
	    		strResultMsg = dictStatusMsg.get(i);
				strResult = dictStatus.get(i);
				//For Showing Snapshot in Col V //For HRBPO only
				strSnapshotPathArray = dictSnapshotPath.get(i);
				updateScenarioResult(CurrentSheet, i, 15);
	    	}   
    	}else if(forLoopDepth>0){
    		//Reverse values of primary loop
    		forLoopIterationCounter = primaryForLoopIterCounter;
    		SharedResource.startLoop_BeginLoop=primaryForLoop_startLoop;
    		SharedResource.endLoop_BeginLoop=primaryForLoop_endLoop;
    	}
	    	
    	//currentRow.getCell(0, NullAsBlank).setCellValue("N");  // Sets Flag to N.
    	System.out.println("For Loop Ends");
    	return boolForLoopStatus;
    }
        
    public Boolean handleReusableComponent(String strComponentName)
    {
        int intRowNumber = 1;
        boolean boolComponentStatus = true;
        boolean boolStartExec = false;
        int intCallCtr = 0;
        if(SharedResource.dictReusableComponentCallCounter.containsKey(strComponentName.trim()))
        {
        	intCallCtr = SharedResource.dictReusableComponentCallCounter.get(strComponentName) + 1;
        }
        SharedResource.dictReusableComponentCallCounter.put(strComponentName, intCallCtr);
    	XSSFSheet objComponentLibSheet = workbook.getSheet("ComponentLibrary");
        XSSFRow objRow = objComponentLibSheet.getRow(intRowNumber);
        KDFEvent objKDFEvent = new KDFEvent(objComponentLibSheet, intRowNumber);
        objRow.getCell(0, NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
        
        //String strFlagAction = objRow.getCell(0, NullAsBlank).getStringCellValue();
        while(!(objKDFEvent.strFlag.equals("")) && !(objKDFEvent.strFlag.equals(null)))
        { 
        	if(objKDFEvent.strFlag.toUpperCase().equals("Y"))
        	{ 
        		
    			if ((strAction.equalsIgnoreCase("Block_Begin")) && (strParent.equalsIgnoreCase(strComponentName)))
				{
					boolStartExec = true;
					
					objRow.getCell(61 + (5*intCallCtr),NullAsBlank).setCellValue("TestPlan : " + strScenarioName);
					objRow.getCell(61 + (5*intCallCtr), NullAsBlank).setCellStyle(styles.get("Normal"));
					objRow.getCell(61 + (5*intCallCtr) + 1,NullAsBlank).setCellValue("Testcase : "+strTestCaseName);
					objRow.getCell(61 + (5*intCallCtr)+ 1, NullAsBlank).setCellStyle(styles.get("Normal"));
					objRow.getCell(61 + (5*intCallCtr) + 2,NullAsBlank).setCellValue("Call# : " + Integer.toString(intCallCtr + 1));
					objRow.getCell(61 + (5*intCallCtr)+ 2, NullAsBlank).setCellStyle(styles.get("Normal"));
					objRow.getCell(61 + (5*intCallCtr) + 3,NullAsBlank).setCellValue("ScenrioRow# : " + Integer.toString(this.iTCRowCounter + 1));
					objRow.getCell(61 + (5*intCallCtr)+ 3, NullAsBlank).setCellStyle(styles.get("Normal"));
					
					intRowNumber++;
					objRow = objComponentLibSheet.getRow(intRowNumber);
					objKDFEvent = new KDFEvent(objRow);
			        
			        
					continue;
				}				
				if ((objKDFEvent.strAction.equalsIgnoreCase("Block_End")) && (objKDFEvent.strParent.equalsIgnoreCase(strComponentName)))
				{
					boolStartExec = false;
					return boolComponentStatus;
				}
				if(boolStartExec)					
				{
                	//UtilityClass objUtility = new UtilityClass();				
    				//TestObject objTestObject = objUtility.getORProperties(strORPath, objKDFEvent.strParent, objKDFEvent.strChild);    				
    
    				if(objKDFEvent.strAction.contains("????")) {
    					objKDFEvent.strAction = SharedResource.dictResuableParameter.get(objKDFEvent.strAction);
    				}
    				if(objKDFEvent.strParent.contains("????")) {
    					objKDFEvent.strParent = SharedResource.dictResuableParameter.get(objKDFEvent.strParent);
    				}
    				if(objKDFEvent.strChild.contains("????")) {
    					objKDFEvent.strChild = SharedResource.dictResuableParameter.get(objKDFEvent.strChild);
    				}
    				if(objKDFEvent.strParam1.contains("????")) {
    					objKDFEvent.strParam1 = SharedResource.dictResuableParameter.get(objKDFEvent.strParam1);
    				}
    				if(objKDFEvent.strParam2.contains("????")) {
    					objKDFEvent.strParam2 = SharedResource.dictResuableParameter.get(objKDFEvent.strParam2);
    				}
    				if(objKDFEvent.strParam3.contains("????")) {
    					objKDFEvent.strParam3 = SharedResource.dictResuableParameter.get(objKDFEvent.strParam3);
    				}
    				if(objKDFEvent.strParam4.contains("????")) {
    					objKDFEvent.strParam4 = SharedResource.dictResuableParameter.get(objKDFEvent.strParam4);
    				}
    				if(objKDFEvent.strParam5.contains("????")) {
    					objKDFEvent.strParam5 = SharedResource.dictResuableParameter.get(objKDFEvent.strParam5);
    				}
    				if(objKDFEvent.strParam6.contains("????")) {
    					objKDFEvent.strParam6 = SharedResource.dictResuableParameter.get(objKDFEvent.strParam6);
    				}    				

    				System.out.println(">>> " + objKDFEvent.strAction + " - "+ objKDFEvent.strParent + " - "  + objKDFEvent.strChild + " - " + objKDFEvent.strParam1 + " - " + objKDFEvent.strParam2+ " - " + objKDFEvent.strParam3+ " - " + objKDFEvent.strParam4+ " - " + objKDFEvent.strParam5+ " - " + objKDFEvent.strParam6);
    				
    
    				InvokeKeywordForExecution obj = new InvokeKeywordForExecution(objWebDriver, objKDFEvent);
    				obj.performAction();
    				
    				
    				if(objKDFEvent.strAction.equalsIgnoreCase("LAUNCH"))
    				{
    					objWebDriver = obj.objWebDriver;
    					strBrowser = obj.strBrowser;
    				}
    				strScreenshotPath = new CaptureScreenShot(objWebDriver, strAction).CaptureResultScreenshot(strBrowser);			
    				strResult = obj.strStatus;
    				if(strResult.equalsIgnoreCase("FAIL"))
    				{
    					boolComponentStatus = false;
    				}
    				strResultMsg = obj.strActualRes;               	
    				
    				if(objKDFEvent.strAction.equalsIgnoreCase("Logic_IF"))  //Condition is True
    				{
    					SharedResource.Comp_Logic_If = obj.strActualRes;
    					strResultMsg = "Condition is "+ obj.strActualRes;
    				}
    				
    				if(objKDFEvent.strAction.equalsIgnoreCase("Goto_True") && SharedResource.Comp_Logic_If.equalsIgnoreCase("FAIL"))			
    				{
    					intRowNumber ++;
    					objRow = objComponentLibSheet.getRow(intRowNumber);
    					objKDFEvent = new KDFEvent(objRow);
    		            while(!(objKDFEvent.strAction.equals("")) && !(objKDFEvent.strAction.equals(null)))
			            {            	          	
			            	if(objKDFEvent.strAction.equals("End_True"))
			            	{
			            		break;
			            	}
			            	intRowNumber ++;  
			            	objRow = objComponentLibSheet.getRow(intRowNumber);
			            	objKDFEvent = new KDFEvent(objRow);
			            }   						
    				}
    				else if(strAction.equalsIgnoreCase("Goto_False") && SharedResource.Comp_Logic_If.equalsIgnoreCase("PASS"))
    				{
    					objRow = objComponentLibSheet.getRow(intRowNumber);
    					objKDFEvent = new KDFEvent(objRow);
    		            while(!(objKDFEvent.strAction.equals("")) && !(objKDFEvent.strAction.equals(null)))
    		            {            	          	
    		            	if(objKDFEvent.strAction.equals("End_False"))
    		            	{
    		            		break;
    		            	}
    		            	intRowNumber ++;  
    		            	objRow = objComponentLibSheet.getRow(intRowNumber);
    		            	objKDFEvent = new KDFEvent(objRow);
    		            }       		 
    					SharedResource.Comp_Logic_If = "";
    				}	
    				//## Handling Goto_True and Goto_False sections... ends       				
    				
    				try
                    {
						Thread.sleep(100);
					} 
                    catch (InterruptedException e)
                    {
						e.printStackTrace();
					}                    
                    updateScenarioResult(objComponentLibSheet, intRowNumber, 61 + (5*intCallCtr));					
				}
        	}        	
        	if(!(SharedResource.strContinueExecution.equalsIgnoreCase("TRUE")))
			{
        		return true;
			}
        	intRowNumber ++;
        	objRow = objComponentLibSheet.getRow(intRowNumber);
        	objKDFEvent = new KDFEvent(objRow);
        }
        
        System.out.println("Ends call to : " + strComponentName);
		return boolComponentStatus; 
    } 
    
         
    
    public String getParameterValueFormMap(Hashtable<String,List<String>> objMapCompParam, String strParam, int iteration)
    {
    	strParam = strParam.replaceFirst("<", "");
    	strParam = strParam.replaceFirst(">", "");
    	strParam = objMapCompParam.get(strParam).get(iteration);    	
    	return strParam;    	
    }
    
    public void enterDetailsForImprovedReporting(String expectedResult,XSSFSheet objScenarioSheet,int iTCRowCounter){
    	 objScenarioSheet.getRow(iTCRowCounter).getCell(80,NullAsBlank).setCellValue(expectedResult) ;
    	 objScenarioSheet.getRow(iTCRowCounter).getCell(81,NullAsBlank).setCellValue(SharedResource.actualResult) ;
    
     }
    
   
}
