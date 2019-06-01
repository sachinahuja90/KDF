package com.nag.kdf.resultLogging;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.nag.kdf.utility.UtilityClass;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class Reporting {
	





/**
	 * This method is used to create result file
	 */
	int int_Pass,int_Fail,testcasetotal,SuccessRate,FailRate,Passwidth,Failwidth;
	
	UtilityClass utility;
	
	String ResultFolderPath="E:\\Results\\";
	String ConsolidateResultFile="E:\\Results\\";
	
	Reporting(){
		utility=new UtilityClass();
	}
	
	public void OpenFile() 
	{
		DateFormat timeFormat = new SimpleDateFormat("ddMMYYYY HHmmss");
		Calendar cal = Calendar.getInstance();
		String StartTime =timeFormat.format(cal.getTime());
		utility.setProperty("StartTime", StartTime);
		utility.setProperty("TeststepCount","0");
		utility.setProperty("PassCount","0");
		utility.setProperty("FailCount","0");
		String sTestCaseName = utility.getProperty("TestCaseName");
		String buildName = utility.getProperty("BuildName");
		String resultDirectory = ResultFolderPath; //DriverScript.ResultFolderPath;
		(new File(resultDirectory)).mkdir();
		String imageDirectory = resultDirectory+File.separator+"Image";
		//String imageDirectory = resultDirectory+"\\Image";
		(new File(imageDirectory)).mkdir();
		try
		{ 
			//Start try
			//String resultPath = resultDirectory + "\\"+"Result_"+ sTestCaseName+".html";
			String resultPath = resultDirectory + File.separator+"Result_"+ sTestCaseName+".html";
			FileWriter fstream = new FileWriter(resultPath);
			BufferedWriter resultFile = new BufferedWriter(fstream);
			resultFile.write ("<html>");
			resultFile.write("<title> Test Script Report </title>");
			resultFile.write("<head></head>");
			resultFile.write("<body>");
			resultFile.write("<font face='Tahoma'size='2'>");
			resultFile.write("<h2 align='center'>"+ sTestCaseName+"_"+ buildName +"_"+StartTime+"</h2>");
			resultFile.write("<h3 align='right' ><font color='#000000' face='Tahoma' size='3'></font></h3>");
			resultFile.write("<table border='0' width='100%' height='47'>");
			resultFile.write("<tr>");
			resultFile.write("<td width='2%' bgcolor='#CCCCFF' align='center'><b><font color='#000000' face='Tahoma' size='2'>TestStepID</font></b></td>");
			resultFile.write("<td width='52%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Message</font></b></td>");
			resultFile.write("<td width='30%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Expected Result</font></b></td>");
			resultFile.write("<td width='30%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Actual Result</font></b></td>");
			resultFile.write("<td width='28%' bgcolor='#CCCCFF' align='center'><b><font color='#000000' face='Tahoma' size='2'>Status</font></b></td>");
			resultFile.write("<td width='20%' bgcolor='#CCCCFF' align='center'><b><font color='#000000' face='Tahoma' size='2'>SnapShots</font></b></td>");
			resultFile.write("</tr>");
			resultFile.close();
			//end try
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}// end the function openfile

	/**
	 * This method is used to report status of each method into result file
	 * @param strSatus
	 * @param strExpected
	 * @param strActual
	 * @param strMessage
	 */
	public void repoter(String strSatus,String strExpected,String strActual, String strMessage)
	{
		String buildName = utility.getProperty("BuildName");
		String imageDirectory = ResultFolderPath + File.separator + "Image";
		String testCaseName = utility.getProperty("TestCaseName");
		String StartTime = utility.getProperty("StartTime");
		String TeststepCount = utility.getProperty("TeststepCount");
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		String snapshotpath = "";

		try
		// start try..
		{
			Dimension screenSize = toolkit.getScreenSize();
			Rectangle rectangle = new Rectangle(0, 0, screenSize.width, screenSize.height);
			Robot robot = new Robot();
			BufferedImage image = robot.createScreenCapture(rectangle);
			String imagepath =imageDirectory + File.separator + testCaseName+"_"+buildName+"_"+StartTime+"_"+TeststepCount + ".jpg";
			File file = new File(imagepath);
			ImageIO.write(image, "jpg", file);
			snapshotpath = "<a href=\"" + utility.getRelativePath(imagepath) + "\">" + "Snapshot</a>";

			if (strSatus.equalsIgnoreCase("PASS"))// start if
			{
				int_Pass = Integer.parseInt(utility.getProperty("PassCount"));
				addTestStepReport(strMessage, strExpected, strActual, "Pass", snapshotpath);
				int_Pass = int_Pass + 1;
				utility.setProperty("PassCount", Integer.toString(int_Pass));
			}// end if
			else// start else
			{
				int_Fail = Integer.parseInt(utility.getProperty("FailCount"));
				addTestStepReport(strMessage, strExpected, strActual, "Fail", snapshotpath);
				int_Fail = int_Fail + 1;
				utility.setProperty("FailCount", Integer.toString(int_Fail));
			}// end else
		}// end try
		catch (Exception ex)// start catch
		{
			int_Fail = Integer.parseInt(utility.getProperty("FailCount"));
			String failval = ex.toString();
			addTestStepReport(failval, strExpected, "Exception occured", "Fail", snapshotpath);
			int_Fail = int_Fail + 1;
			utility.setProperty("FailCount", Integer.toString(int_Fail));
		}// end catch
	}// end function

	/**
	 * This method is used to result of each test step into report file
	 * @param strMessage
	 * @param strExpectedResult
	 * @param strActualResult
	 * @param strPassFail
	 * @param strSceenshot
	 */
	public void addTestStepReport(String strMessage,String strExpectedResult,String strActualResult, String strPassFail, String strSceenshot)
	{
		String testCaseName = utility.getProperty("TestCaseName");
		int TeststepCount = Integer.parseInt(utility.getProperty("TeststepCount"));

		try
		{
			// start try..
			String resultPath =ResultFolderPath + File.separator +"Result_"+testCaseName+".html";
			FileWriter fstream = new FileWriter(resultPath,true);
			BufferedWriter resultFile = new BufferedWriter(fstream);
			resultFile.append("<tr>");
			TeststepCount = TeststepCount + 1;
			utility.setProperty("TeststepCount", Integer.toString(TeststepCount));
			resultFile.append("<td width='13%' bgcolor='#FFFFDC' valign='middle' align='center' ><font color='#000000' face='Tahoma' size='2'>" + TeststepCount + "</font></td>");
			resultFile.append("<td width='22%' bgcolor='#FFFFDC' valign='top' align='justify' ><font color='#000000' face='Tahoma' size='2'>" + strMessage + "</font></td>");
			resultFile.append("<td width='20%' bgcolor='#FFFFDC' valign='top' align='justify' ><font color='#000000' face='Tahoma' size='2'>" + strExpectedResult + "</font></td>");
			resultFile.append("<td width='20%' bgcolor='#FFFFDC' valign='top' align='justify' ><font color='#000000' face='Tahoma' size='2'>" + strActualResult + "</font></td>");
			if (strPassFail == "Pass")// start if
			{
				resultFile.append("<td width='18%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='#000000' face='Tahoma' size='2'>" + strPassFail + "</font></b></td>");
			}// end if
			else// start else
			{
				resultFile.append("<td width='18%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='Red' face='Tahoma' size='2'>" + strPassFail + "</font></b></td>");
			}// end else
			resultFile.append("<td width='13%' bgcolor='#FFFFDC' valign='middle' align='center' ><font color='#000000' face='Tahoma' size='2'>" + strSceenshot + "</font></td>");
			resultFile.append("</tr>");
			resultFile.close();
			// end try
		}catch (Exception e) 
		{
			e.printStackTrace();
		}// end catch

	}// end function addTestStepReport

	/**
	 * This method is used to create footer in the result file
	 */
	public void Footer()
	{
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy HHmmss");
		DateFormat displayDateFormat = new SimpleDateFormat("dd.MM.yyyy");
		DateFormat displayTimeFormat = new SimpleDateFormat("HH:mm:ss");

		Calendar cal = Calendar.getInstance();
		String StartTime = utility.getProperty("StartTime");
		Date dFrom =null;
		Date dTo = null;
		try{//Start try
			dFrom = dateFormat.parse(StartTime);
			dTo = dateFormat.parse(dateFormat.format(cal.getTime()));
			//end try
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}//end catch

		String sDateFrom = displayDateFormat.format(dFrom);//need to pass global variable having script start time
		String sTimeFrom = displayTimeFormat.format(dFrom);
		String sTimeTo = displayTimeFormat.format(dTo);

		float timeDiffSec=(dTo.getTime() - dFrom.getTime())/(1000);
		int timeDiffMin = (int)(dTo.getTime() - dFrom.getTime())/(60*1000);
		String timeDiff = Integer.toString(timeDiffMin)+"."+(int)(timeDiffSec - (timeDiffMin*60));

		int_Pass = Integer.parseInt(utility.getProperty("PassCount"));
		int_Fail = Integer.parseInt(utility.getProperty("FailCount"));
		testcasetotal = int_Pass + int_Fail;
		SuccessRate = (int_Pass * 100 / (testcasetotal));
		FailRate = 100 - SuccessRate;
		//FailRate = Integer.toString(100 - SuccessRate);
		Passwidth = (300 * SuccessRate) / 100;
		Failwidth = (300 - Passwidth);
		//Failwidth = Integer.toString(300 - Passwidth);
		String resultDirectory = ResultFolderPath;
		String testCaseName = utility.getProperty("TestCaseName");

		try
		{
			//start try
			String resultPath = resultDirectory + File.separator+"Result_"+ testCaseName+".html";
			FileWriter fstream = new FileWriter(resultPath,true);
			BufferedWriter resultFile = new BufferedWriter(fstream);
			resultFile.append("</table>");
			resultFile.append("<hr>");
			resultFile.append("<table border='0' width='50%'>");
			resultFile.append("<tr><td width='100%' colspan='2' bgcolor='#000000'><b><font face='Tahoma' size='2' color='#FFFFFF'>Test Case Execution Details :</font></b></td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Executed On (DD.MM.YYYY)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + sDateFrom + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Start Time (HH:MM:SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + sTimeFrom + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>End Time (HH:MM:SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + sTimeTo + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Execution Time (MM.SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + timeDiff + "</td></tr>");
			resultFile.append("</table>");
			String totaltest =Integer.toString(testcasetotal);
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr><td width='100%' colspan='2' bgcolor='#000000'><b><font face='Tahoma' size='2' color='#FFFFFF'>Test Result Summary :</font></b></td></tr></table>");

			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr>  <td width=100 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75 ><b>Total Steps</b></td> <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT FACE='Tahoma' SIZE=2.75><b>" + totaltest + "</b></td>  <td width=300 bgcolor='#E7A1B0'></td>  <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>100%</b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr>  <td width=100 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75 ><b>Total Steps Passed</b></td> <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT FACE='Tahoma' SIZE=2.75><b>" + Integer.toString(int_Pass) + "</b></td>  <td width= "+Passwidth+" bgcolor='#008000'></td>  <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>" + SuccessRate + "%</b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr> <td width=100 bgcolor= '#FFFFDC'><FONT   FACE='Tahoma' SIZE=2.75 ><b>Total Steps Failed</b></td>  <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>" + Integer.toString(int_Fail) + "</b></td>   <td width= " + Failwidth + " bgcolor='#FF0000'></td>     <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>" + FailRate + "%</b></td> </tr></table>");
			resultFile.append("</font>");
			resultFile.append("</body>");
			resultFile.append("</html>");
			resultFile.close();
			//end try
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}//end catch

	}// end function footer	

	/**
	 * This method is used to write consolidate status of a test suite into HTML file
	 */
	public void writeConsolidateResult()
	{
		String resultFolderPath = null;;
		String status = null;
		String [] testCaseNames;
		File[] fileList; 
		String [] temp;
		String [] tempTestName = null ;
		String resultFile=null;
		String [] sStatus;
		//*setting the value of global property ResultFolderPath created during createFolder fxn call in the local variable
		resultFolderPath = ResultFolderPath;
		//*setting the value of global property ConsolidateResultFile during createFolder fxn call in the local variable
		String consolidateResultFilePath = ConsolidateResultFile;
		OpenFileConsolidate();
		try
		{
			fileList = new File(resultFolderPath).listFiles();
			testCaseNames=new String[(fileList.length)-1];
			int j=0;
			int iPassCount=0;
			int iFailCount=0;
			for (int i = 0; i < fileList.length; i++) {
				if ((fileList[i].getName().endsWith(".html"))) 
				{
					temp = fileList[i].getName().split(".html");
					testCaseNames[j]= temp[0];
					j++;
				}
			}
			for(int i = 0; i < testCaseNames.length; i++) 
			{
				status = null;
				try
				{
					if(testCaseNames[i].isEmpty())
					{
						break;
					}					
				}
				catch (NullPointerException e) 
				{
					break;
				}
				resultFile=resultFolderPath + File.separator + testCaseNames[i]+".html";
				tempTestName = testCaseNames[i].split("Result_");
				//			}		

				sStatus=getOverAllTestCaseStatus(resultFile);
				status=sStatus[0];
				if(status=="FAIL")
				{
					iFailCount=iFailCount+1;	
					utility.setProperty("FailCount",Integer.toString(iFailCount));

				}
				else
				{
					iPassCount=iPassCount+1;	
					utility.setProperty("PassCount",Integer.toString(iPassCount));	
				}
				String sAPIResultFileLink="<a href=\"" + utility.getRelativePath(resultFile) + "\">TestCaseResultFile</a>";
				addTestStepReportConsolidate(tempTestName[1],status, sAPIResultFileLink,sStatus[1]);
			}

			FooterConsolidate();
			utility.setProperty("PassCount","0");
			utility.setProperty("FailCount","0");
			zipResultFolder();
			String zipFileName = ResultFolderPath + ".zip";
			sendEmail(consolidateResultFilePath, zipFileName);

		}
		catch(Exception ex1)
		{
			System.out.println("Unable to consolidate result" + ex1.toString());
		}
	}

	/**
	 * This method is used to create consolidate HTML result file
	 */
	public void OpenFileConsolidate()
	{
		String StartTime =utility.getProperty("StartTime");
		utility.setProperty("TeststepCount","0");
		utility.setProperty("PassCount","0");
		utility.setProperty("FailCount","0");
		String sTestSuiteName = "LandingPage";//utility.getProperty("SuiteName");
		String buildName = "1.0.29";//utility.getProperty("BuildName");
		try
		{ 
			//Start try
			String resultPath = ConsolidateResultFile;
			FileWriter fstream = new FileWriter(resultPath);
			BufferedWriter resultFile = new BufferedWriter(fstream);
			resultFile.write("<html>");
			resultFile.write("<title> Test Script Report </title>");
			resultFile.write("<head></head>");
			resultFile.write("<body>");
			resultFile.write("<font face='Tahoma'size='2'>");
			resultFile.write("<h2 align='center'>"+ sTestSuiteName+"_"+ buildName +"_"+StartTime+"</h2>");
			resultFile.write("<h3 align='right' ><font color='#000000' face='Tahoma' size='3'></font></h3>");
			resultFile.write("<table border='0' width='100%' height='47'>");
			resultFile.write("<tr>");
			resultFile.write("<td width='2%' bgcolor='#CCCCFF' align='center'><b><font color='#000000' face='Tahoma' size='2'>Test Case Name</font></b></td>");
			resultFile.write("<td width='30%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Status</font></b></td>");
			resultFile.write("<td width='30%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Result File</font></b></td>");
			resultFile.write("<td width='50%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Test Case Pass Percentage</font></b></td>");
			resultFile.write("</tr>");
			resultFile.close();
			//end try
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}//end catch
	}// end the function OpenFileConsolidate	

	/**
	 * This method is used to add test step (line) to consolidate result file
	 * @param testCaseName
	 * @param strPassFail
	 * @param ResultFileLink
	 * @param sPassPercent
	 */
	public void addTestStepReportConsolidate(String testCaseName, String strPassFail, String ResultFileLink,String sPassPercent )
	{
		int TeststepCount = Integer.parseInt(utility.getProperty("TeststepCount"));
		try{
			// start try..
			String resultPath =ConsolidateResultFile;
			FileWriter fstream = new FileWriter(resultPath,true);
			BufferedWriter resultFile = new BufferedWriter(fstream);
			resultFile.append("<tr>");
			TeststepCount = TeststepCount + 1;
			utility.setProperty("TeststepCount", Integer.toString(TeststepCount));
			resultFile.append("<td width='22%' bgcolor='#FFFFDC' valign='top' align='justify' ><font color='#000000' face='Tahoma' size='2'>" + testCaseName + "</font></td>");

			if (strPassFail.equalsIgnoreCase("Pass"))// start if
			{
				resultFile.append("<td width='18%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='#008000' face='Tahoma' size='2'>" + strPassFail + "</font></b></td>");
			}// end if
			else// start else
			{
				resultFile.append("<td width='18%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='Red' face='Tahoma' size='2'>" + strPassFail + "</font></b></td>");
			}// end else
			resultFile.append("<td width='20%' bgcolor='#FFFFDC' valign='top' align='justify' ><font color='#000000' face='Tahoma' size='2'>" + ResultFileLink + "</font></td>");
			resultFile.append("<td width='13%' bgcolor='#FFFFDC' valign='middle' align='center' ><font color='#000000' face='Tahoma' size='2'>" + sPassPercent+"%" + "</font></td>");
			resultFile.append("</tr>");
			resultFile.close();
			// end try
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}// end catch

	}// end function addTestStepReport

	/**
	 * This method is used by  writeConsolidateResult method to create footer of test suite HTML result file
	 */
	public void FooterConsolidate()
	{
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy HHmmss");
		DateFormat displayDateFormat = new SimpleDateFormat("dd.MM.yyyy");
		DateFormat displayTimeFormat = new SimpleDateFormat("HH:mm:ss");

		Calendar cal = Calendar.getInstance();
		String StartTime = utility.getProperty("SuiteStartTime");
		Date dFrom =null;
		Date dTo = null;
		try{//Start try
			dFrom = dateFormat.parse(StartTime);
			dTo = dateFormat.parse(dateFormat.format(cal.getTime()));
			//end try
		}catch (Exception e) {
			e.printStackTrace();
		}//end catch

		String sDateFrom = displayDateFormat.format(dFrom);//need to pass global variable having script start time
		String sTimeFrom = displayTimeFormat.format(dFrom);
		String sTimeTo = displayTimeFormat.format(dTo);

		float timeDiffSec=(dTo.getTime() - dFrom.getTime())/(1000);
		int timeDiffMin = (int)(dTo.getTime() - dFrom.getTime())/(60*1000);
		String timeDiff = Integer.toString(timeDiffMin)+"."+(int)(timeDiffSec - (timeDiffMin*60));

		int_Pass = Integer.parseInt(utility.getProperty("PassCount"));
		int_Fail = Integer.parseInt(utility.getProperty("FailCount"));
		testcasetotal = int_Pass + int_Fail;
		SuccessRate = (int_Pass * 100 / (testcasetotal));
		FailRate = 100 - SuccessRate;
		//FailRate = Integer.toString(100 - SuccessRate);
		Passwidth = (300 * SuccessRate) / 100;
		Failwidth = 300 - Passwidth;
		//Failwidth = Integer.toString(300 - Passwidth);

		try
		{
			//start try
			String resultPath = ConsolidateResultFile;
			FileWriter fstream = new FileWriter(resultPath,true);
			BufferedWriter resultFile = new BufferedWriter(fstream);
			resultFile.append("</table>");
			resultFile.append("<hr>");
			resultFile.append("<table border='0' width='50%'>");
			resultFile.append("<tr><td width='100%' colspan='2' bgcolor='#000000'><b><font face='Tahoma' size='2' color='#FFFFFF'>Test Suite Execution Details :</font></b></td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Executed On (DD.MM.YYYY)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + sDateFrom + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Start Time (HH:MM:SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + sTimeFrom + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>End Time (HH:MM:SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + sTimeTo + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Execution Time (MM.SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + timeDiff + "</td></tr>");
			resultFile.append("</table>");
			String totaltest =Integer.toString(testcasetotal);
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr><td width='100%' colspan='2' bgcolor='#000000'><b><font face='Tahoma' size='2' color='#FFFFFF'>Test Suite Result Summary :</font></b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr>  <td width=130 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75 ><b>Total Test case Executed</b></td> <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT FACE='Tahoma' SIZE=2.75><b>" + totaltest + "</b></td>  <td width=300 bgcolor='#E7A1B0'></td>  <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>100%</b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr>  <td width=130 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75 ><b>Total Test case Passed</b></td> <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT FACE='Tahoma' SIZE=2.75><b>" + Integer.toString(int_Pass) + "</b></td>  <td width= "+Passwidth+" bgcolor='#008000'></td>  <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>" + SuccessRate + "%</b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr> <td width=130 bgcolor= '#FFFFDC'><FONT   FACE='Tahoma' SIZE=2.75 ><b>Total Test case Failed</b></td>  <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>" + Integer.toString(int_Fail) + "</b></td>   <td width= " + Failwidth + " bgcolor='#FF0000'></td>     <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>" + FailRate + "%</b></td> </tr></table>");
			resultFile.append("</font>");
			resultFile.append("</body>");
			resultFile.append("</html>");
			resultFile.close();
			//end try
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}//end catch

	}// end function footer	

	/**
	 * This method is used to create zip result folder for mailing result.
	 * It uses addFolderToZip internally
	 * @throws IOException
	 */
	public void  zipResultFolder() throws IOException
	{
		//name of zip file to create
		String ResultFolderPath=this.ResultFolderPath;
		String outFilename = ResultFolderPath + ".zip";


		//create ZipOutputStream object
		ZipOutputStream out=null;
		try {
			out = new ZipOutputStream(new FileOutputStream(outFilename));
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}

		//path to the folder to be zipped
		File zipFolder = new File(ResultFolderPath);

		//get path prefix so that the zip file does not contain the whole path
		// eg. if folder to be zipped is /home/lalit/test
		// the zip file when opened will have test folder and not home/lalit/test folder
		int len = zipFolder.getAbsolutePath().lastIndexOf(File.separator);
		String baseName = zipFolder.getAbsolutePath().substring(0,len+1);
		try
		{
			addFolderToZip(zipFolder, out, baseName);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 

		out.close();
	} 

	/**
	 * This method is used to add files/folder to zip folder
	 * @param folder
	 * @param zip
	 * @param baseName
	 * @throws IOException
	 */
	public void addFolderToZip(File folder, ZipOutputStream zip, String baseName) throws IOException 
	{
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				addFolderToZip(file, zip, baseName);
			} else {
				String name = file.getAbsolutePath().substring(baseName.length());
				ZipEntry zipEntry = new ZipEntry(name);
				zip.putNextEntry(zipEntry);
				IOUtils.copy(new FileInputStream(file), zip);
				zip.closeEntry();
			}
		}
	}

	/**
	 * This method is used to send mails to the mail ids configured in Properties.properties file
	 * @param attachment
	 * @param zipAttachment
	 */
	public void sendEmail(String attachment,String zipAttachment)
	{
		final String username = "metacube.blueline@gmail.com";
		final String password = "Metacube123";
		String filename = null;
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected javax.mail.PasswordAuthentication  getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(username, password);
			}
		});


		try
		{
			MimeMessage message = new MimeMessage(session);
			((MimeMessage) message).setFrom(new InternetAddress("metacube.blueline@gmail.com"));
			InternetAddress [] address=InternetAddress.parse(utility.getProperty("toMail"));


			message.addRecipients(MimeMessage.RecipientType.TO, address);  

			((MimeMessage) message).setSubject("Automation Report");
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText("Hello Dear,"
					+ "\n Please find the attached automation report!"
					+ "\n NOTE: Please copy paste consolidate report and zip file at same directory and extract zip content into the same directory, so that links in the report work as expected."
					+"\n\n Thanks & Regards"
					+"\n Metacube Automation Team");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			if(attachment!="Null")
			{

				messageBodyPart = new MimeBodyPart();
				filename = attachment;
				DataSource source = new FileDataSource(filename);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(filename);
				multipart.addBodyPart(messageBodyPart);
				((Part) message).setContent(multipart);
			}      

			if(zipAttachment!="Null")
			{

				messageBodyPart = new MimeBodyPart();
				filename = zipAttachment;
				DataSource source = new FileDataSource(filename);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(filename);
				multipart.addBodyPart(messageBodyPart);
				message.setContent(multipart);
			}    
			Transport.send((javax.mail.Message) message);
		}
		/*catch (MessagingException  e)
		{
			//throw new RuntimeException(e);
		}*/
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * This method is used to get the Pass/fail status of a test case along with its pass percentage
	 * @param sTestCaseResultFile
	 * @return an array containing Pass/Fail and Pass %
	 */
	public String []  getOverAllTestCaseStatus (String sTestCaseResultFile)
	{

		String sStatus="PASS";
		String [] line2;
		String [] line3;
		String []sFinalStatus=new String [5];
		int temp=0;
		try
		{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream(sTestCaseResultFile);
			// Get the object of DataInputStream
			DataInputStream dis = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null) 
			{
				temp=strLine.indexOf("Test Case Execution Details :");
				if(temp>-1)
				{
					if(strLine.indexOf("Fail")<temp)
					{
						sStatus = "FAIL";
						sFinalStatus[0]=sStatus;
					}
					else if (sStatus.equals("PASS"))
					{
						sFinalStatus[0]=sStatus;
					}			
					String [] sub1;
					line2=strLine.split("Total Steps Passed");

					line3=line2[1].split(":");
					sub1=line3[1].split("%");
					int stotallength=sub1[0].length();
					int lastindex=sub1[0].lastIndexOf(">");
					int size=stotallength-lastindex;
					String overAllPassPerc=sub1[0].substring(lastindex+1, lastindex+size);
					sFinalStatus[1]=overAllPassPerc;
				}else if(strLine.indexOf("Fail")<temp)
				{
					sStatus = "FAIL";
					sFinalStatus[0]=sStatus;
				}

			}
			//Close the input stream
			dis.close();
		}
		catch (Exception e)
		{
			//Catch exception if any
			System.out.println( "Error: " + e.getMessage());
		}
		return sFinalStatus;
	}
	
	
	public void createReportTestSuit(TestSuiteResults tsResults) {
		
		
	}
	
}


