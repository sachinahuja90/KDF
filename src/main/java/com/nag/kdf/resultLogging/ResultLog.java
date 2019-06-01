package com.nag.kdf.resultLogging;

import java.io.BufferedWriter;

import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.nag.kdf.excel.CellStyles;
import com.nag.kdf.utility.SharedResource;

public class ResultLog {
	org.apache.poi.ss.usermodel.Row.MissingCellPolicy NullAsBlank = org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;
	HSSFSheet objTargetSheet = null;
	
	public ResultLog(HSSFSheet objTargetSheet) {
		super();
		this.objTargetSheet = objTargetSheet;
	}

	//###################################################################################
	// Function : updateScenarioResult(HSSFSheet objTargetSheet, int intRowNum, int intColumnNum)   	
	// Description : This method is used to update the result status of the action performed.
	// Parameters : HSSFSheet objTargetSheet - represents the Scenario Sheet under execution.
	//				int intRowNum - represents the row number of the excel sheet.
	//				int intColumnNum - represents the column number from where we need to start adding results.
	// Author : 
	// Date : 
	//###################################################################################		
	public void updateScenarioResult(int intRowNum, int intColumnNum, String strResultMsg, String strResult)
	{
		 try {			
			 //String strScreenShotlink = CaptureResultScreenshot();
			 CellStyles styles = new CellStyles();
			
			 styles.createHyperLinkAlternate(objTargetSheet.getRow(intRowNum).getCell(intColumnNum,NullAsBlank ),"");
			
			if (!(objTargetSheet.getSheetName().equalsIgnoreCase("ComponentLibrary")))
			{
				objTargetSheet.getRow(intRowNum).getCell(0, NullAsBlank).setCellValue("N");  // Sets Flag to N.
			}
			objTargetSheet.getRow(intRowNum).getCell(intColumnNum + 1, NullAsBlank).setCellValue(strResultMsg); // Sets result Message.
			
			
			DateFormat objdateFormat = new SimpleDateFormat("dd-MMMMM-yyyy-h:mm:ss a");
			Date objdate = new Date();			
			objTargetSheet.getRow(intRowNum).getCell(intColumnNum + 3, NullAsBlank).setCellValue(objdateFormat.format(objdate)); // Sets Execution time
			
			
			objTargetSheet.getRow(intRowNum).getCell(intColumnNum + 4, NullAsBlank).setCellValue(strResult); // Sets Status as PASS/FAIL.
			if ((strResult.toUpperCase().contains("PASS")) && (!(strResult.toUpperCase().contains("FAIL"))))
	        {
				objTargetSheet.getRow(intRowNum).getCell(intColumnNum + 4, NullAsBlank).setCellStyle(styles.createStyles(objTargetSheet.getWorkbook()).get("Pass"));	
	        }
	        else
	        {
	        	objTargetSheet.getRow(intRowNum).getCell(intColumnNum + 4, NullAsBlank).setCellStyle(styles.createStyles(objTargetSheet.getWorkbook()).get("Fail"));	
	        }
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
	}	
	
	
	
	public void updateTestPlan(int intRow)
	{
		//workbook.getSheet("Test_Plan").getRow(intRow).getCell(0, NullAsBlank).setCellValue("N");				
	}	
	
	public static void TestCaseReport(TestCaseResults result) {
		
		int Passwidth,Failwidth;
		
		
		try
		{ 
			String TestCaseResultPath = SharedResource.TestCaseResultFolderPath +"\\" +"Result_"+ result.TestCaseName +".html";
			String TestSuiteResultPath =SharedResource.SuiteResultFolderPath +"\\" +"Result_"+ result.TestSuiteName +".html";
			
			FileWriter fstream = new FileWriter(TestCaseResultPath);
			BufferedWriter resultFile = new BufferedWriter(fstream);
			resultFile.write ("<html>");
			resultFile.write("<title> Test Case Report </title>");
			resultFile.write("<head></head>");
			resultFile.write("<body>");
			resultFile.write("<font face='Tahoma'size='2'>");
			resultFile.write("<a href="+TestSuiteResultPath+">Back to TestSuite Results</a>");
			resultFile.write("<h2 align='center'>"+ result.TestCaseName+"</h2>");
			resultFile.write("<h3 align='right' ><font color='#000000' face='Tahoma' size='3'></font></h3>");
			resultFile.write("<table border='0' width='100%' height='47'>");
			resultFile.write("<tr>");
			resultFile.write("<td width='2%' bgcolor='#CCCCFF' align='center'><b><font color='#000000' face='Tahoma' size='2'>TestStepID</font></b></td>");
			resultFile.write("<td width='2%' bgcolor='#CCCCFF' align='center'><b><font color='#000000' face='Tahoma' size='2'>Action/Event</font></b></td>");
			resultFile.write("<td width='52%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Message</font></b></td>");
			//resultFile.write("<td width='30%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Expected Result</font></b></td>");
			//resultFile.write("<td width='30%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Actual Result</font></b></td>");
			resultFile.write("<td width='28%' bgcolor='#CCCCFF' align='center'><b><font color='#000000' face='Tahoma' size='2'>Status</font></b></td>");
			resultFile.write("<td width='20%' bgcolor='#CCCCFF' align='center'><b><font color='#000000' face='Tahoma' size='2'>SnapShots</font></b></td>");
			resultFile.write("</tr>");
			
			HashMap<Integer, TestStepResults> map = result.TestStepMap;
			Iterator<Integer> itr= map.keySet().iterator();
			while (itr.hasNext()) {
			    Integer key = itr.next();
			    TestStepResults stepResult=map.get(key);
			    resultFile.append("<tr>");
			    resultFile.append("<td width='13%' bgcolor='#FFFFDC' valign='middle' align='center' ><font color='#000000' face='Tahoma' size='2'>" + stepResult.TestStepID + "</font></td>");
			    resultFile.append("<td width='13%' bgcolor='#FFFFDC' valign='middle' align='center' ><font color='#000000' face='Tahoma' size='2'>" + stepResult.TestStepAction + "</font></td>");
			    resultFile.append("<td width='22%' bgcolor='#FFFFDC' valign='top' align='justify' ><font color='#000000' face='Tahoma' size='2'>" + stepResult.TestStepMsg + "</font></td>");
				//resultFile.append("<td width='20%' bgcolor='#FFFFDC' valign='top' align='justify' ><font color='#000000' face='Tahoma' size='2'>" + stepResult.TestStepExpectedResult + "</font></td>");
				//resultFile.append("<td width='20%' bgcolor='#FFFFDC' valign='top' align='justify' ><font color='#000000' face='Tahoma' size='2'>" + stepResult.TestStepExpectedResult + "</font></td>");
			    if (stepResult.TestStepStatus=="PASS" | stepResult.TestStepStatus=="Pass")
			    	resultFile.append("<td width='18%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='#008000' face='Tahoma' size='2'>" + stepResult.TestStepStatus + "</font></b></td>");
			    else
			    	resultFile.append("<td width='18%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='Red' face='Tahoma' size='2'>" + stepResult.TestStepStatus + "</font></b></td>");
				resultFile.append("<td width='13%' bgcolor='#FFFFDC' valign='middle' align='center' ><font color='#000000' face='Tahoma' size='2'>" + "<a href="+stepResult.SnapshotLink + ">Snapshot</a></font></td>");
				resultFile.append("</tr>");
			}
			
			
			resultFile.append("</table>");
			resultFile.append("<hr>");
			resultFile.append("<table border='0' width='50%'>");
			resultFile.append("<tr><td width='100%' colspan='2' bgcolor='#000000'><b><font face='Tahoma' size='2' color='#FFFFFF'>Test Case Execution Details :</font></b></td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Executed On (DD.MM.YYYY)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + result.ExecutionDate + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Start Time (HH:MM:SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + result.StartTime + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>End Time (HH:MM:SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + result.EndTime + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Execution Time (MM.SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + result.ExecutionTime + "</td></tr>");
			resultFile.append("</table>");
			String totaltest =Integer.toString(result.TotalTestSteps);
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr><td width='100%' colspan='2' bgcolor='#000000'><b><font face='Tahoma' size='2' color='#FFFFFF'>Test Result Summary :</font></b></td></tr></table>");

			Passwidth= (300 * result.TestStepPassPercentage) / 100;
			Failwidth = (300 - Passwidth);
			
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr>  <td width=100 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75 ><b>Total Steps</b></td> <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT FACE='Tahoma' SIZE=2.75><b>" + totaltest + "</b></td>  <td width=300 bgcolor='#E7A1B0'></td>  <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>100%</b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr>  <td width=100 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75 ><b>Total Steps Passed</b></td> <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT FACE='Tahoma' SIZE=2.75><b>" + Integer.toString(result.TestStepsPass) + "</b></td>  <td width= "+ Passwidth +" bgcolor='#008000'></td>  <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>" + result.TestStepPassPercentage + "%</b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr> <td width=100 bgcolor= '#FFFFDC'><FONT   FACE='Tahoma' SIZE=2.75 ><b>Total Steps Failed</b></td>  <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>" + Integer.toString(result.TestStepsFail) + "</b></td>   <td width= " + Failwidth + " bgcolor='#FF0000'></td>     <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>" + result.TestStepFailPercentage + "%</b></td> </tr></table>");
			resultFile.append("</font>");
			resultFile.append("</body>");
			resultFile.append("</html>");
			resultFile.close();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	
	public static void TestSuiteReport(TestSuiteResults suiteResult) {
		
		int Passwidth,Failwidth;
		
		try {
			String TestSuiteResultPath = SharedResource.SuiteResultFolderPath +"\\" +"Result_"+ suiteResult.TestSuiteName +".html";
			String TestResultPath = SharedResource.ResultFolderPath +"\\" +"Result_"+ suiteResult.TestName +".html";
			
			
			
			FileWriter fstream = new FileWriter(TestSuiteResultPath);
			BufferedWriter resultFile = new BufferedWriter(fstream);
			resultFile.write("<html>");
			resultFile.write("<title> Test Suite Report </title>");
			resultFile.write("<head></head>");
			resultFile.write("<body>");
			resultFile.write("<font face='Tahoma'size='2'>");
			resultFile.write("<a href="+TestResultPath+">Back to Consolidated Results</a>");
			resultFile.write("<h2 align='center'>Test Suite Report - "+ suiteResult.TestSuiteName+"</h2>");
			resultFile.write("<h3 align='right' ><font color='#000000' face='Tahoma' size='3'></font></h3>");
			resultFile.write("<table border='0' width='100%' height='47'>");
			resultFile.write("<tr>");
			resultFile.write("<td width='2%' bgcolor='#CCCCFF' align='center'><b><font color='#000000' face='Tahoma' size='2'>Test Case Name</font></b></td>");
			resultFile.write("<td width='30%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Status</font></b></td>");
			resultFile.write("<td width='30%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Result File</font></b></td>");
			resultFile.write("<td width='50%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Test Case Pass Percentage</font></b></td>");
			resultFile.write("</tr>");

			
			HashMap<Integer, TestCaseResults> map = suiteResult.TestCaseResultHashMap;
			Iterator<Integer> itr= map.keySet().iterator();
			while (itr.hasNext()) {
			    Integer key = itr.next();
			    TestCaseResults caseResult=map.get(key);			
			    resultFile.append("<tr>");
			    resultFile.append("<td width='22%' bgcolor='#FFFFDC' valign='top' align='justify' ><font color='#000000' face='Tahoma' size='2'>" + caseResult.TestCaseName + "</font></td>");
			    if (caseResult.TestCaseStatus=="PASS" | caseResult.TestCaseStatus=="Pass")
			    	resultFile.append("<td width='18%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='#008000' face='Tahoma' size='2'>" + caseResult.TestCaseStatus + "</font></b></td>");
			    else
			    	resultFile.append("<td width='18%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='Red' face='Tahoma' size='2'>" + caseResult.TestCaseStatus + "</font></b></td>");
				resultFile.append("<td width='20%' bgcolor='#FFFFDC' valign='top' align='justify' ><font color='#000000' face='Tahoma' size='2'>"+"<a href="+caseResult.ResultFileLink+">"+caseResult.TestCaseName+"</a></font></td>");
				resultFile.append("<td width='13%' bgcolor='#FFFFDC' valign='middle' align='center' ><font color='#000000' face='Tahoma' size='2'>" + caseResult.TestStepPassPercentage+"%" + "</font></td>");
				resultFile.append("</tr>");
			}
			
			resultFile.append("</table>");
			resultFile.append("<hr>");
			resultFile.append("<table border='0' width='50%'>");
			resultFile.append("<tr><td width='100%' colspan='2' bgcolor='#000000'><b><font face='Tahoma' size='2' color='#FFFFFF'>Test Suite Execution Details :</font></b></td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Executed On (DD.MM.YYYY)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + suiteResult.ExecutionDate + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Start Time (HH:MM:SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + suiteResult.StartTime + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>End Time (HH:MM:SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + suiteResult.EndTime + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Execution Time (MM.SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + suiteResult.ExecutionTime + "</td></tr>");
			resultFile.append("</table>");
			String totaltest =Integer.toString(suiteResult.TotalTestCases);
			

			Passwidth= (300 * suiteResult.TestSuitePassPercentage) / 100;
			Failwidth = (300 - Passwidth);
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr><td width='100%' colspan='2' bgcolor='#000000'><b><font face='Tahoma' size='2' color='#FFFFFF'>Test Suite Result Summary :</font></b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr>  <td width=130 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75 ><b>Total Test case Executed</b></td> <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT FACE='Tahoma' SIZE=2.75><b>" + totaltest + "</b></td>  <td width=300 bgcolor='#E7A1B0'></td>  <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>100%</b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr>  <td width=130 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75 ><b>Total Test case Passed</b></td> <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT FACE='Tahoma' SIZE=2.75><b>" + Integer.toString(suiteResult.TestCasePass) + "</b></td>  <td width= "+Passwidth+" bgcolor='#008000'></td>  <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>" + suiteResult.TestSuitePassPercentage + "%</b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr> <td width=130 bgcolor= '#FFFFDC'><FONT   FACE='Tahoma' SIZE=2.75 ><b>Total Test case Failed</b></td>  <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>" + Integer.toString(suiteResult.TestCaseFail) + "</b></td>   <td width= " + Failwidth + " bgcolor='#FF0000'></td>     <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>" + suiteResult.TestSuiteFailPercentage + "%</b></td> </tr></table>");
			resultFile.append("</font>");
			resultFile.append("</body>");
			resultFile.append("</html>");
			resultFile.close();
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	

public static void ConsolidatedTestReport(ConsolidatedTestResults testResult) {
		
		int Passwidth,Failwidth;
		
		try {
			
			String TestResultPath = SharedResource.ResultFolderPath +"\\" +"Result_"+ testResult.TestName +".html";
			
			
			FileWriter fstream = new FileWriter(TestResultPath);
			BufferedWriter resultFile = new BufferedWriter(fstream);
			resultFile.write("<html>");
			resultFile.write("<title> Consolidated Test Report </title>");
			resultFile.write("<head></head>");
			resultFile.write("<body>");
			resultFile.write("<font face='Tahoma'size='2'>");
			resultFile.write("<h2 align='center'>Consolidated Test Report - "+ testResult.TestName+"</h2>");
			resultFile.write("<h3 align='right' ><font color='#000000' face='Tahoma' size='3'></font></h3>");
			resultFile.write("<table border='0' width='100%' height='47'>");
			resultFile.write("<tr>");
			resultFile.write("<td width='2%' bgcolor='#CCCCFF' align='center'><b><font color='#000000' face='Tahoma' size='2'>Test Suite Name</font></b></td>");
			resultFile.write("<td width='30%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Status</font></b></td>");
			resultFile.write("<td width='30%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Result File</font></b></td>");
			resultFile.write("<td width='50%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Test Suite Pass Percentage</font></b></td>");
			resultFile.write("</tr>");

			
			HashMap<Integer, TestSuiteResults> map = testResult.TestSuiteResultHashMap;
			Iterator<Integer> itr= map.keySet().iterator();
			while (itr.hasNext()) {
			    Integer key = itr.next();
			    TestSuiteResults suiteResult=map.get(key);			
			    resultFile.append("<tr>");
			    resultFile.append("<td width='22%' bgcolor='#FFFFDC' valign='top' align='justify' ><font color='#000000' face='Tahoma' size='2'>" + suiteResult.TestSuiteName + "</font></td>");
			    if (suiteResult.testSuiteStatus=="PASS" | suiteResult.testSuiteStatus=="Pass")
				    resultFile.append("<td width='18%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='#008000' face='Tahoma' size='2'>" + suiteResult.testSuiteStatus + "</font></b></td>");
			    else
			    	resultFile.append("<td width='18%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='Red' face='Tahoma' size='2'>" + suiteResult.testSuiteStatus + "</font></b></td>");
				resultFile.append("<td width='20%' bgcolor='#FFFFDC' valign='top' align='justify' ><font color='#000000' face='Tahoma' size='2'>"+"<a href="+suiteResult.ResultFileLink+">"+suiteResult.TestSuiteName+"_Result</a></font></td>");
				resultFile.append("<td width='13%' bgcolor='#FFFFDC' valign='middle' align='center' ><font color='#000000' face='Tahoma' size='2'>" + suiteResult.TestSuitePassPercentage+"%" + "</font></td>");
				resultFile.append("</tr>");
			}
			
			resultFile.append("</table>");
			resultFile.append("<hr>");
			resultFile.append("<table border='0' width='50%'>");
			resultFile.append("<tr><td width='100%' colspan='2' bgcolor='#000000'><b><font face='Tahoma' size='2' color='#FFFFFF'>Test Suite Execution Details :</font></b></td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Executed On (DD.MM.YYYY)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + testResult.ExecutionDate + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Start Time (HH:MM:SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + testResult.StartTime + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>End Time (HH:MM:SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + testResult.EndTime + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Execution Time (MM.SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + testResult.ExecutionTime + "</td></tr>");
			resultFile.append("</table>");
			String totaltest =Integer.toString(testResult.TotalTestSuite);
			

			Passwidth= (300 * testResult.TestPassPercentage) / 100;
			Failwidth = (300 - Passwidth);
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr><td width='100%' colspan='2' bgcolor='#000000'><b><font face='Tahoma' size='2' color='#FFFFFF'>Consolidated Test Result Summary :</font></b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr>  <td width=130 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75 ><b>Total Test Suite Executed</b></td> <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT FACE='Tahoma' SIZE=2.75><b>" + totaltest + "</b></td>  <td width=300 bgcolor='#E7A1B0'></td>  <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>100%</b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr>  <td width=130 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75 ><b>Total Test Suite Passed</b></td> <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT FACE='Tahoma' SIZE=2.75><b>" + Integer.toString(testResult.TestSuitePass) + "</b></td>  <td width= "+Passwidth+" bgcolor='#008000'></td>  <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>" + testResult.TestPassPercentage + "%</b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr> <td width=130 bgcolor= '#FFFFDC'><FONT   FACE='Tahoma' SIZE=2.75 ><b>Total Test Suite Failed</b></td>  <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>" + Integer.toString(testResult.TestSuiteFail) + "</b></td>   <td width= " + Failwidth + " bgcolor='#FF0000'></td>     <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>" + testResult.TestFailPercentage + "%</b></td> </tr></table>");
			resultFile.append("</font>");
			resultFile.append("</body>");
			resultFile.append("</html>");
			resultFile.close();
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	

	
}
