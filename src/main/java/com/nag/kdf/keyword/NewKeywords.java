package com.nag.kdf.keyword;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;

import com.nag.kdf.utility.KDFEvent;
import com.nag.kdf.utility.KDFEventResult;
import com.nag.kdf.utility.SharedResource;
import com.nag.kdf.utility.TestObject;
import com.nag.kdf.utility.UtilityClass;
import com.nag.kdf.webDriverSupport.KDFWebElement;

public class NewKeywords {

	public String strLocator = "",strAction="",strParent="",strChild="",strParam1="",strParam2="",strParam3="",strParam4="",strParam5="",strParam6="";
	public int intIndex = 0;
	public WebElement objWebElement = null;
	public WebDriver objWebDriver = null;
	public List<WebElement> Arr_objWebElement = null;
	public String strBrowser = ""; //for handling screenshot capability incase of chrome browser.
	public String strStatus = "";
	public String strTimeStamp = "";
	public String strActualRes = "";	
	public String strLinkSnapshot = "";
	public String strStoreValue = "";	
	public enum WindowIdentifier { Index, Name, Title, WrongChoice };
	public TestObject  objTestObject = null;
	public JavascriptExecutor js = null ; 
	public KDFEvent objAFTEvent = null;
	
	


	//###################################################################################
	// Function : CaptureResultScreenshot(String strBrowser)   	
	// Description : 
	// Parameters : 
	// Author :
	// Date : 
	//###################################################################################
//	public String CaptureResultScreenshot(String strBrowser)
//	{
//	    DateFormat objdateFormat = new SimpleDateFormat("MMM dd, yyyy, h-mm-ss a");
//		Date objdate = new Date();
//		String screenshotPath = objdateFormat.format(objdate);
//	    //screenshotPath = "C:\\Program Files\\AFTv1.2\\Reports\\" + strAction + "_" + screenshotPath.concat(".png");
//	    screenshotPath = SharedResource.strRootFolder + "\\Reports\\" + strAction + "_" + screenshotPath.concat(".png");
//	    try {
//	    	//Case: Firefox, IE
//	    	if((this.objWebDriver != null) && (!(strBrowser.equals("CHROME")))&& (!(strBrowser.equals("IOSNATIVEDRIVER")))&& (!(this.objWebDriver.getClass().toString().contains("RemoteWebDriver")))) {
//	    		File screenshotFile = ((TakesScreenshot)this.objWebDriver).getScreenshotAs(OutputType.FILE);     
//	    		FileUtils.copyFile(screenshotFile, new File(screenshotPath));
//	    	} 
//	    	//Case: Chrome
//	    	else if((this.objWebDriver != null) && ((strBrowser.equals("CHROME")))) {
//	    		WebDriver objRemoteWebdriverScreenshot = new Augmenter().augment(this.objWebDriver);
//	    		File screenshotFile = ((TakesScreenshot)objRemoteWebdriverScreenshot).getScreenshotAs(OutputType.FILE);     
//	    		FileUtils.copyFile(screenshotFile, new File(screenshotPath));
//	    	} 
//	    	else if((this.objWebDriver != null) && ((this.objWebDriver.getClass().toString().contains("RemoteWebDriver")))) {
//	    		WebDriver objRemoteWebdriverScreenshot = new Augmenter().augment(this.objWebDriver);
//	    		File screenshotFile = ((TakesScreenshot)objRemoteWebdriverScreenshot).getScreenshotAs(OutputType.FILE);     
//	    		FileUtils.copyFile(screenshotFile, new File(screenshotPath));
//	    	}
//	    	//Case: without webdriver, IOSNativeDriver
//	    	else {
//	    		Robot robot = new Robot();
//	    		BufferedImage bi = robot.createScreenCapture(new Rectangle(800,800));
//	    		ImageIO.write(bi, "png", new File(screenshotPath));
//	    	}
//	    } catch (IOException e) {			
//	    	e.printStackTrace();
//	    	screenshotPath = "";
//	    } catch (AWTException e) {
//	    	e.printStackTrace();
//	    	screenshotPath = "";
//		}	
//	    catch (UnhandledAlertException  e) {
//	    	try {
//				Robot robot = new Robot();
//				BufferedImage bi = robot.createScreenCapture(new Rectangle(800,800));
//				ImageIO.write(bi, "png", new File(screenshotPath));
//				
//			} catch (AWTException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//				screenshotPath = "";
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//				screenshotPath = "";
//			}
//		}	
//		return screenshotPath;	
//	}

	

	 //###################################################################################
  	// Function : ConvertDateFormat()   	
  	// Description : This method is used to Convert Date Format
  	// Parameters : Input Date || Required Format || Output date  
  	// Author : 
  	// Date : 
  	// Modified by - 
  	// Modification - 
  	public KDFEventResult ConvertDateFormat()
    {
        try {
			String strCurrentDateFormate = this.strParent;      
			String strOutputVar = this.strParam1;
			strCurrentDateFormate = strCurrentDateFormate.split(" ")[0];			
			String strReqDate = (strCurrentDateFormate.split("-")[1] + "-" + strCurrentDateFormate.split("-")[2] + "-" + strCurrentDateFormate.split("-")[0]);
			SharedResource.dictRTVarCollection.put(strOutputVar, strReqDate);
            return new KDFEventResult("strReqDate", "PASS");
		} 
        catch (Exception e) {
	          return new KDFEventResult("Exception occured", "FAIL");
		}        
    }
    //Correct Date Format change Keyword.
    public KDFEventResult ChangeDateFormat()
    {
        try {
			String strCurrentDateFormate = this.strParent;      
			String strRequiredDateFormate = this.strChild; 
			String strInput = this.strParam1;
			String strOutputVar = this.strParam2;
			Date date = new Date();		
			Format formatter1 = new SimpleDateFormat(strCurrentDateFormate);		
			date = (Date)formatter1.parseObject(strInput);
			formatter1 = new SimpleDateFormat(strRequiredDateFormate);
			String strOutputDate = formatter1.format(date);
			SharedResource.dictRTVarCollection.put(strOutputVar, strOutputDate);
			return new KDFEventResult(strOutputDate, "PASS");
		} 
        catch (Exception e) {
 			return new KDFEventResult("Exception occured.\n" + e.getStackTrace(), "FAIL");
		}        
    }
    
    
    public KDFEventResult CompareValuesIgnoreCase()
    {
        String Var1 = this.strParam1;
        String sActualMatch = "";
        String Var2 = this.strParam2;
        String sExpectedMatch = strParam3.toUpperCase();       
   
        if (Var1.toString().trim().equalsIgnoreCase(Var2.toString().trim()))
        {
            sActualMatch = "TRUE";
        }
        else
        {
            sActualMatch = "FALSE";
        }
        if ((sActualMatch.equals("FALSE")) & (sExpectedMatch.equals("TRUE")))
        {
        	this.strStatus = "FAIL";
        	this.strActualRes = Var1 + " does not match with " + Var2;
        }
        else if ((sActualMatch.equals("TRUE")) & (sExpectedMatch.equals("FALSE")))
        {
        	this.strStatus = "FAIL";
        	this.strActualRes = Var1 + " match with " + Var2;
        }
        else if ((sActualMatch.equals("FALSE")) & (sExpectedMatch.equals("FALSE")))
        {
        	this.strStatus = "PASS";
        	this.strActualRes = Var1 + " does not match with " + Var2;
        }
        else if ((sActualMatch.equals("TRUE")) & (sExpectedMatch.equals("TRUE")))
        {
        	this.strStatus = "PASS";
        	this.strActualRes = Var1 + " match with " + Var2;
        }
        else
        {
        	this.strStatus = "FAIL";
        	this.strActualRes = "";
        }
		return new KDFEventResult(this.strActualRes, this.strStatus);

    }
	

	//##################################################################################
	// Function : SetSecure()   	
   	// Description : This method is used to insert secured password in textbox
   	// Parameters : Screen Name || Web Element Object || Secure Password to be inserted
   	// Author : 	
   	// Date : 
   	// Modified by - 
   	// Modification - 
   	//###################################################################################		
   	
	
	public KDFEventResult SetSecure()
	{
		String d_String ="";
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator,this.intIndex);
			this.objWebElement.clear();
			UtilityClass objUtil = new UtilityClass();
			d_String = objUtil.decryptPassword(strParam1);
			this.objWebElement.sendKeys(d_String);
            this.strActualRes = strParam1  + " value has been set at " + strChild;
            this.strStatus = "PASS";
        } catch (Exception e) {
        	this.strActualRes = "Exception in SetValue method with " + strChild;
        	this.strStatus = "FAIL";
        }
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	

	//##################################################################################
	// SelectWindow directly takes title/Name/WindowHandle. of the window. It doesn't use WebElement to select.
	public KDFEventResult SelectWindow()
	{	
		String currentWindow = null;
		
		
		
		try {
			if((this.strLocator.split("=")[0].toString()).equalsIgnoreCase("NAME"))
			{
				objWebDriver.switchTo().window(this.strLocator.split("=")[1].trim());
				this.strActualRes = strChild + " window has been succesfully Selected with name = " + this.strLocator.split("=")[1].trim(); 
				this.strStatus = "PASS";
			}
			else if((this.strLocator.split("=")[0].toString()).equalsIgnoreCase("TITLE"))
			{
				try{
					currentWindow = objWebDriver.getWindowHandle();
				}catch(Exception e){
					//Do Nothing
				}
			
			    Set<String> availableWindows = objWebDriver.getWindowHandles();
			    boolean windowfound = false;
			    if (!availableWindows.isEmpty()) 
			    {
			    	for (String windowId : availableWindows)
			    	{
			    	    if (objWebDriver.switchTo().window(windowId).getTitle().equals(this.strLocator.split("=")[1].trim())) 
			    	    {
			    	    	windowfound = true;
			    	    	this.strActualRes = strChild + " window has been succesfully Selected with Tile = " + this.strLocator.split("=")[1].trim(); 
							this.strStatus = "PASS";
							//break; commented 
			    	    } 
			    	    else
					    {
			    	    	try{
			    	    	//objWebDriver.switchTo().window(currentWindow); commented 
			    	    	}catch(Exception e){
			    	    		//Do Nothing
			    	    	}
					    }
			    	}			    	
				}
			    if(windowfound == false)
		    	{
		    		this.strActualRes = strChild + " window not found with Tile = " + this.strLocator.split("=")[1].trim(); 
					this.strStatus = "FAIL";
		    	}
			}
			else if((this.strLocator.split("=")[0].toString()).equalsIgnoreCase("WINDOWINDEX"))
			{
				//currentWindow = objWebDriver.getWindowHandle();
			    Set<String> availableWindows = objWebDriver.getWindowHandles();
			    int windowCntr = availableWindows.size();
			    boolean windowfound = false;
			    if (!availableWindows.isEmpty() && ! (availableWindows.size() < Integer.parseInt(strLocator.split("=")[1].trim()))) 
			    {
			    	int WindowIndex = 1;
			    	for (String windowId : availableWindows)
			    	{
			    	    if (WindowIndex == Integer.parseInt(strLocator.split("=")[1].trim())) 
			    	    {
			    	    	windowfound = true;
			    	    	objWebDriver.switchTo().window(windowId);
			    	    	this.strActualRes = strChild + " window has been succesfully Selected with WindowIndex = " + this.strLocator.split("=")[1].trim(); 
							this.strStatus = "PASS";
			    	    } 
			  
			    	    WindowIndex++;
			    	}
			    	
				}
			    else
			    {
			    	windowfound = false;
			    }
			    if(windowfound == false)
		    	{
		    		this.strActualRes = strChild + " window not found with WindowIndex = " + this.strLocator.split("=")[1].trim(); 
					this.strStatus = "FAIL";
		    	}
			}				
		}
		catch(Exception e) {
			this.strActualRes = "Exception occured is selecting Window"; 
            this.strStatus = "FAIL";
		}		
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	

	
    
    //###################################################################################
  	// Function : AddDate()   	
  	// Description : This method is used to Add Day/Week/Month/Year to input Date
  	// Parameters : InputFormat	|| InputDate	|| DAY/YEAR/MONTH	|| Incrementer/Decrementer ||	OutputVariable  
  	// Author : 	
  	// Date : 
  	// Modified by - 
  	// Modification - 
  	
    
    public KDFEventResult AddDate()
    {
    	String strCurrentDateFormate = this.strParent;      
		String strCurrentDate = this.strChild;
		String strToUpdate = this.strParam1;
		String strIncDec = this.strParam2;
		String strOutputDate = "";
		int intToUpdate = 0;
		if (strToUpdate.equalsIgnoreCase("DAY"))
		{
			intToUpdate = 5;
		}
		else if(strToUpdate.equalsIgnoreCase("MONTH"))
		{
			intToUpdate = 2;
		}
		else if(strToUpdate.equalsIgnoreCase("YEAR"))
		{
			intToUpdate = 1;
		}			
			
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(strCurrentDateFormate);
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(strCurrentDate));
			c.add(intToUpdate, Integer.valueOf(strIncDec));  // number of days to add
			strOutputDate = sdf.format(c.getTime());  // strOutputDate is now the new date
		} catch (ParseException e) {
			return new KDFEventResult("Exception occured.\n" + e.getStackTrace(), "FAIL");
		}
		SharedResource.dictRTVarCollection.put(this.strParam3, strOutputDate);
		return new KDFEventResult(strOutputDate, "PASS");
    }
    
    
    
    
    //#######################################################################
    // Tabel
    
    public KDFEventResult GetRowCount()
    {
     	 try {
				this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator,this.intIndex);
				List<WebElement> rows = objWebElement.findElements(By.tagName("tr"));
				int intRowCount = rows.size();
				SharedResource.dictRTVarCollection.put(this.strParam1, Integer.toString(intRowCount));
				this.strActualRes = "Total number of Rows in the Table : " + intRowCount;
			    this.strStatus = "PASS";
	 	 }
	 	 catch (Exception e)
	 	 {
	 		 this.strActualRes = e.getStackTrace().toString();
			 this.strStatus = "FAIL";
	 	 }  
 		return new KDFEventResult(this.strActualRes, this.strStatus);

    }
    
    public KDFEventResult VerifyTableRowCount()
    {
     	 try {
				this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator,this.intIndex);
				List<WebElement> rows = objWebElement.findElements(By.tagName("tr"));
				int intRowCount = rows.size();
				if(intRowCount == Integer.valueOf(this.strParam1))
				{
					this.strActualRes = "No. of rows present in table is " + intRowCount;
				    this.strStatus = "PASS";
				}
				else
				{
					this.strActualRes = "No. of rows present in table is " + intRowCount;
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
  	// Function : SelectTableRowCellValue ()   	
  	// Description : This method is used to select the row containing value.
  	// Parameters : 
  	// Author : 
  	// Date : 
  	// Modified by - 
  	// Modification - 
  	//###################################################################################		
     public KDFEventResult SelectTableRowCellValue()
     {
    	 try {
    		 	this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator,this.intIndex);
				List<WebElement> rows = objWebElement.findElements(By.xpath("./tbody/tr"));
				Iterator<WebElement> i = rows.iterator();
				boolean boolTextFound = false;
				while(i.hasNext()) { 
					WebElement row = i.next();
					String strRowText = row.getText();				
					if((strRowText != null) && strRowText.contains(this.strParam1))
					{
						row.click();
						boolTextFound = true;
						break;
					}				
				}
				if(boolTextFound) {
					this.strActualRes = "Successfully selected Row containing text : " + this.strParam1;
				    this.strStatus = "PASS";
					
				} else {
					this.strActualRes = "No row is present containing "+ this.strParam1;
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
     	// Function : GetTableCellValue()   	
     	// Description : This method is used to select the row whose number is given as parameter.
      	// GetTableCellValue	ParentObject	TableObject	RowNo	ColumnNo	OutputVariable
     	// Parameters : 
     	// Author : 
     	// Date : 
     	// Modified by - 
     	// Modification - 
     	//###################################################################################		
        public KDFEventResult GetTableCellValue()
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
					List<WebElement> rows = objWebElement.findElements(By.xpath("./tbody/tr"));
					WebElement row = rows.get(intRow - 1);
					List<WebElement> cols = row.findElements(By.xpath("./*"));
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
     	// Function : GetTableRowValue()   	
     	// Description : This method is used to fetch the row value whose number is given as parameter.
      	// GetTableRowValue	ParentObject	TableObject	RowNo	OutputVariable
     	// Parameters : 
     	// Author : 
     	// Date : 
     	// Modified by - 
     	// Modification - 
     	//###################################################################################		
        public KDFEventResult GetTableRowValue()
        {        	 
        	try { 				
        		if(strParam1.equals("") || strParam2.equals(""))
        		{
        			System.out.println("Either Rownumber or Variable name is empty.");
        			this.strActualRes = "Either Rownumber or Variable name is empty.";
 				    this.strStatus = "FAIL";
        		}        		
        		int intRow =0;
        		try {
					intRow = Integer.valueOf(strParam1);
				} catch (NumberFormatException e) {
					System.out.println("row number is not in number format.");
        			this.strActualRes = "row number is not in number format.";
 				    this.strStatus = "FAIL";
				}       		
        		WebElement col;
				try {
					this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator,this.intIndex);
					List<WebElement> rows = objWebElement.findElements(By.xpath("./tbody/tr"));
					WebElement row = rows.get(intRow - 1);
					SharedResource.dictRTVarCollection.put(strParam2, row.getText());
					this.strActualRes = "Row value is : " + row.getText();
					this.strStatus = "PASS";
				} catch (IndexOutOfBoundsException e) {
					this.strActualRes = "Row index is out of bound";
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
     	// Function : VerifyTableCellData()   	
     	// Description : This method is used to verify cell data with expected text      	// 	
     	// Parameters : ParentObject	TableObject	RowNo	ColumnNo	ExpectedText
     	// Author : 
     	// Date : 
     	// Modified by - 
     	// Modification - 
     	//###################################################################################		
        public KDFEventResult VerifyTableCellData()
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
					List<WebElement> rows = objWebElement.findElements(By.xpath("./tbody/tr"));
					WebElement row = rows.get(intRow - 1);
					List<WebElement> cols;					
					cols = row.findElements(By.xpath("./*"));
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
        //###################################################################################
     	// Function : VerifyTableCellDataInAllRows()   	
     	// Description : This method is used to verify cell data with expected text      	// 	
     	// Parameters : ParentObject	TableObject	RowNo	ColumnNo	ExpectedText
     	// Author : 
     	// Date : 
     	// Modified by - 
     	// Modification - 
     	//###################################################################################		
        public KDFEventResult VerifyTableCellDataInAllRows()
        {    
        	String result = "";
        	try { 				
        		if(strParam1.equals("") || strParam2.equals("")|| strParam3.equals(""))
        		{
        			System.out.println("Either param1 or param2 or param3 is empty.");
        			this.strActualRes = "Either param1 or param2 or param3 is empty.";
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
					List<WebElement> rows = objWebElement.findElements(By.xpath("./tbody/tr"));
					int i = 0;
					for(WebElement row:rows)
					{
						if(!((intRow -1)>i))
						{
							List<WebElement> cols;	
							cols = row.findElements(By.xpath("./td"));
							col = cols.get(intCol - 1);
							if (!(strParam3.trim().equals(col.getText().trim())))
							{	
								if(result.trim().isEmpty())
								result = "row# "+ i + " value = " + col.getText().trim();
								else
								result = result +";"+ "row# "+ i + " value = " + col.getText().trim();
							}
						}
						i++;

					}				
					if ((result.trim().isEmpty()))
					{
						this.strActualRes = "Expected text " + strParam3 + " is present in all Cells of col ("+ strParam2 + ")";
						this.strStatus = "PASS";
					}
					else
					{
						this.strActualRes = result;
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

	    //*********************************************
	    //Function - SetPageLoadTime
	    //Description - Manage Loading Time of WebPage, Script will hault untill Page laoded or Load time Achived.
	    //Parameters - Accept Param1 as no of Seconds we want to set as Page Load Time
	    //Author- Sumit Maru
	    //Dated - 27-Nov-14
	    //************************************************
	    public KDFEventResult SetPageLoadTime(){
	    	try{
	    		int timeInSecs = Integer.valueOf(this.strParam1);
	    		this.objWebDriver.manage().timeouts().pageLoadTimeout(timeInSecs,TimeUnit.SECONDS);
	    		this.strActualRes = "Page Load time out has benn set to - " + timeInSecs + "Seconds";
	    		this.strStatus = "PASS";
	    	}catch(Exception e){
	    		this.strActualRes = "Exception Occured - " + e.getMessage();
	    		this.strStatus = "FAIL";
	    	}
	    	return new KDFEventResult(this.strActualRes, this.strStatus);	    	
	    }
	    
	    
	    
	    public KDFEventResult GetTableRow()
	    { 
	    	boolean textFound = false;

	    	try { 				
	    		if(strParam1.equals("") || strParam2.equals(""))
	    		{
	    			System.out.println("Either text of row  or OutputVariable is empty.");
	    			this.strActualRes = "Either Text or OutputVariable is empty.";
					    this.strStatus = "FAIL";
	    		}  
	        	this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator,this.intIndex);
	        	// Now get all the TR elements from the table
	            List<WebElement> allRows = objWebElement.findElements(By.xpath("./tbody/tr"));
	           // System.out.println("This is how many allRows: " + allRows.size());

	            // This is how many items are in the order item table. number of rows - 1 coz top row is the headings
	            int z = allRows.size() - 1;
	           // System.out.println("This is how many items are in the table : " + z );

	            // Work out the cells we need to get by
	            //  iterate over all the td
	            int rowNumber = 1;
	            for (WebElement row : allRows) {
	            	
	                // now we have all the td
	                List<WebElement> cells = row.findElements(By.xpath("./td"));
	                   //boolean textFound = false;
	                for (WebElement cell : cells) {
	                	String cellValue = cell.getText();
	                
	                	if ((strParam1.trim().equals(cell.getText().trim())))
						{
	                		textFound = true;
	                		//System.out.println("This is how many items are in the table : " + cell.getText());	
	                		//System.out.println(rowNumber);
	                		break;
						}
	                }
	                	if(textFound)
	                	{
	                	 String str = Integer.toString(rowNumber);
	                	 SharedResource.dictRTVarCollection.put(strParam2,str);
	                	 this.strActualRes = strParam1 + " is present at row no " + str;
	         			 this.strStatus = "PASS";
	                	 break;
	                	}
	                	else
	                	{
	                		rowNumber ++;
	                	}
	            }
	    	}
	        	
	 	 catch (Exception e)
	 	 {
	 		 this.strActualRes = e.getStackTrace().toString();
				 this.strStatus = "FAIL";
	 	 }     
	 		return new KDFEventResult(this.strActualRes, this.strStatus);

	    }
	    public KDFEventResult VerifyValueExistDropDown()
	    {
	      try
	      {
	    	  boolean boolOptionFound;
	    	  String[]   Value1;
	    	  String Value = this.strParam1;
	        if (Value.contains(",")){
	         Value1=Value.split(",");
	          boolOptionFound = false;
	         this.objWebElement = new KDFWebElement(this.objWebDriver)
	          .getTargetObject(this.strLocator, this.intIndex);
	        List<WebElement> options = this.objWebElement.findElements(
	          By.tagName("option"));
	       for(String Val : Value1) {
	        for (WebElement option : options) {
	          if (option.getText().trim().equals(Val.trim()))
	          {
	            boolOptionFound = true;
	            break;
	          }
	          else{
	        	  boolOptionFound = false; 
	          }
	          
	        }
	        if (!(boolOptionFound)){
	        	  this.strActualRes = 
	      	            (Val + " option is absent in the Combobox.");
	      	          this.strStatus = "FAIL";
	      	        return new KDFEventResult(this.strActualRes, this.strStatus); 
	          }
	       } 
	        }else{   
	         boolOptionFound = false;
	        this.objWebElement = new KDFWebElement(this.objWebDriver)
	          .getTargetObject(this.strLocator, this.intIndex);
	        List<WebElement> options = this.objWebElement.findElements(
	          By.tagName("option"));
	        for (WebElement option : options) {
	          if (option.getText().trim().equals(this.strParam1.trim()))
	          {
	            boolOptionFound = true;
	            break;
	          }
	        }
	        }
	        if (boolOptionFound)
	        {
	          this.strActualRes = (this.strParam1 + " option is prsent");
	          this.strStatus = "PASS";
	        }
	        else
	        {
	          this.strActualRes = 
	            (this.strParam1 + " option is absent in the Combobox.");
	          this.strStatus = "FAIL";
	        }
	        }
	      catch (Exception e)
	      {
	        e.printStackTrace();
	        this.strActualRes = "Exception occurred in SelectValue.";
	        this.strStatus = "FAIL";
	      }
	      return new KDFEventResult(this.strActualRes, this.strStatus);
	    }

	    
	    public KDFEventResult VerifyAllCheckboxChecked()
		{
		  String ActualStatus = "";
		  try
		  {
		    this.Arr_objWebElement = new KDFWebElement(this.objWebDriver)
		      .getTargetObjectList(this.strLocator);
		    for (WebElement objWebElement : this.Arr_objWebElement) {
		      if (objWebElement.isSelected())
		      {
		        if (this.strParam1.equalsIgnoreCase("TRUE"))
		        {
		          this.strActualRes = 
		            (this.objAFTEvent.strChild + " is checked");
		          this.strStatus = "PASS";
		        }
		        else
		        {
		          this.strActualRes = 
		            (this.objAFTEvent.strChild + " is checked");
		          this.strStatus = "FAIL";
		          break;
		        }
		        ActualStatus = "TRUE";
		      }
		      else
		      {
		        if (this.strParam1.equalsIgnoreCase("TRUE"))
		        {
		          this.strActualRes = 
		            (this.objAFTEvent.strChild + " is not checked");
		          this.strStatus = "FAIL";
		          break;
		        }
		        this.strActualRes = 
		          (this.objAFTEvent.strChild + " is not checked");
		        this.strStatus = "PASS";
		        

		        ActualStatus = "FALSE";
		      }
		    }
		  }
		  catch (Exception e)
		  {
		    this.strActualRes = "Exception Occurred";
		    this.strStatus = "FAIL";
		    
		    ActualStatus = "FALSE";
		  }
		  SharedResource.isVerificationKeyword = Boolean.valueOf(true);
		  SharedResource.expectedResult = "Field Existance = " + this.strParam1;
		  SharedResource.actualResult = ActualStatus;
		  

		  return new KDFEventResult(this.strActualRes, this.strStatus);
		}
		
		public KDFEventResult CheckAllCheckbox()
		{
		  String ActualStatus = "";
		  try
		  {
		    this.Arr_objWebElement = new KDFWebElement(this.objWebDriver).getTargetObjectList(this.strLocator);
		    for (WebElement objWebElement : this.Arr_objWebElement)
		    {
		      if (!objWebElement.isSelected()) {
		        objWebElement.click();
		      }
		      this.strActualRes = (this.objAFTEvent.strChild + " is checked");
		      this.strStatus = "PASS";
		    }
		  }
		  catch (Exception e)
		  {
		    this.strActualRes = "Exception Occurred";
		    this.strStatus = "FAIL";
		    
		    ActualStatus = "FALSE";
		  }
		  SharedResource.isVerificationKeyword = Boolean.valueOf(true);
		  SharedResource.expectedResult = "Field Existance = " + this.strParam1;
		  SharedResource.actualResult = ActualStatus;
		  

		  return new KDFEventResult(this.strActualRes, this.strStatus);
		}
		//**********************************************************************************
		public KDFEventResult UnCheckAllCheckbox()
		{
		  String ActualStatus = "";
		  try
		  {
		    this.Arr_objWebElement = new KDFWebElement(this.objWebDriver).getTargetObjectList(this.strLocator);
		    for (WebElement objWebElement : this.Arr_objWebElement)
		    {
		      if (objWebElement.isSelected()) {
		        objWebElement.click();
		      }
		      this.strActualRes = (this.objAFTEvent.strChild + " is checked");
		      this.strStatus = "PASS";
		    }
		  }
		  catch (Exception e)
		  {
		    this.strActualRes = "Exception Occurred";
		    this.strStatus = "FAIL";
		    
		    ActualStatus = "FALSE";
		  }
		  SharedResource.isVerificationKeyword = Boolean.valueOf(true);
		  SharedResource.expectedResult = "Field Existance = " + this.strParam1;
		  SharedResource.actualResult = ActualStatus;
		  

		  return new KDFEventResult(this.strActualRes, this.strStatus);
		}

    
}
