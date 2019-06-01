package com.nag.kdf.webDriverSupport;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.Augmenter;

import com.nag.kdf.timeStamp.TimeStamp;
import com.nag.kdf.utility.SharedResource;

public class CaptureScreenShot {
	public WebDriver objWebDriver = null;
	public String strAction = null;
	
	public CaptureScreenShot(WebDriver objWD, String strAction)
	{
		this.objWebDriver = objWD;
		this.strAction = strAction;
	}
	public String CaptureResultScreenshot(String strBrowser)
	{
		String screenshotPath = new TimeStamp().getTimeStamp("MMM dd, yyyy, h-mm-ss a"); 
		screenshotPath=screenshotPath.replace(" ", "");
		
		
	    screenshotPath = SharedResource.TestCaseScreenShotPath+"\\"  + strAction + "_" + screenshotPath.concat(".png");
	    try {
	    	//Case: Firefox, IE
	    	if((this.objWebDriver != null) && (!(strBrowser.equals("CHROME")))&& (!(strBrowser.equals("IOSNATIVEDRIVER")))&& (!(this.objWebDriver.getClass().toString().contains("RemoteWebDriver")))) {
	    		File screenshotFile = ((TakesScreenshot)this.objWebDriver).getScreenshotAs(OutputType.FILE);     
	    		FileUtils.copyFile(screenshotFile, new File(screenshotPath));
	    	} 
	    	//Case: Chrome
	    	else if((this.objWebDriver != null) && ((strBrowser.equals("CHROME")))) {
	    		WebDriver objRemoteWebdriverScreenshot = new Augmenter().augment(this.objWebDriver);
	    		File screenshotFile = ((TakesScreenshot)objRemoteWebdriverScreenshot).getScreenshotAs(OutputType.FILE);     
	    		FileUtils.copyFile(screenshotFile, new File(screenshotPath));
	    	} 
	    	else if((this.objWebDriver != null) && ((this.objWebDriver.getClass().toString().contains("RemoteWebDriver")))) {
	    		WebDriver objRemoteWebdriverScreenshot = new Augmenter().augment(this.objWebDriver);
	    		File screenshotFile = ((TakesScreenshot)objRemoteWebdriverScreenshot).getScreenshotAs(OutputType.FILE);     
	    		FileUtils.copyFile(screenshotFile, new File(screenshotPath));
	    	}
	    	//Case: without webdriver, IOSNativeDriver
	    	else {
	    		return getScreenshotByRobot(screenshotPath);
	    	}
	    }catch (IOException e) {			
	    	e.printStackTrace();
	    	screenshotPath = "";
	    }
	    catch (UnhandledAlertException  e) {
	    	return getScreenshotByRobot(screenshotPath);
		}	
	    catch(WebDriverException e)
	    {
	    	return getScreenshotByRobot(screenshotPath);
	    }
	    
		return screenshotPath;	
	}
	
	public String getScreenshotByRobot(String screenshotPath)
	{
		
		try {
			Robot robot = new Robot();
			BufferedImage bi = robot.createScreenCapture(new Rectangle(800,800));
			ImageIO.write(bi, "png", new File(screenshotPath));
			
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			screenshotPath = "";
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			screenshotPath = "";
		}
		
		return screenshotPath;
	}
}
