package com.nag.kdf.keyword;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.nag.kdf.timeStamp.TimeStamp;
import com.nag.kdf.utility.KDFEvent;
import com.nag.kdf.utility.KDFEventResult;
import com.nag.kdf.utility.SharedResource;
import com.nag.kdf.utility.UtilityClass;
import com.nag.kdf.webDriverSupport.KDFWebElement;


public class SeleniumKeywords extends DatabaseKeyword {

	public SeleniumKeywords() {}
	public SeleniumKeywords(WebDriver objWebdriver,KDFEvent objAFTEvent)
	{
		super(objWebdriver,objAFTEvent);
	}
	
	// ###################################################################################
	// Function : Launch()
	// Description : This method is used to launch the application in the
	// mentioned browser.
	// Parameters : Browser Type || Web URL
	// Author : Sachin Ahuja
	// Date : 10 Aug 2016
	// Modified by -
	// Modification -
	// ###################################################################################
	public KDFEventResult Launch() {
		try {
			objWebDriver = null;
			DesiredCapabilities caps;

			strBrowser = "";
			String driverPath = System.getProperty("user.dir");
			// Handling Chrome Browser
			if (this.strParam2.toUpperCase().equals("CHROME")) {
				strBrowser = "CHROME";
				File file = new File("Browser_Drivers\\chromedriver.exe");
				System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
				caps = DesiredCapabilities.chrome();
				caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				ChromeOptions options = new ChromeOptions();
				options.addArguments("chrome.switches", "--disable-extensions");
				caps.setCapability(ChromeOptions.CAPABILITY, options);
				objWebDriver = new ChromeDriver(caps);
				SharedResource.dictRTVarCollection.put(strParam3, strParam2);
			}
			// Handling IE Browser
			else if (this.strParam2.toUpperCase().equals("IE")) {
				strBrowser = "IE";
				File file = new File("Browser_Drivers\\IEDriverServer.exe");
				System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
				DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
				capabilities.setCapability("ignoreProtectedModeSettings", true);
				capabilities.setCapability("ignoreZoomSetting", true);
				objWebDriver = new InternetExplorerDriver(capabilities);
				SharedResource.dictRTVarCollection.put(strParam3, strParam2);
			}
			// By default it is handling Firefox browser.

			else if (this.strParam2.toUpperCase().equals("MOZILLA") || this.strParam2.equals("")) {
				strBrowser = "Mozilla";
				File file = new File("Browser_Drivers\\geckodriver.exe");
				System.setProperty("webdriver.gecko.driver", file.getAbsolutePath());
				objWebDriver = new FirefoxDriver();
				SharedResource.dictRTVarCollection.put(strParam3, strParam2);
			}
			objWebDriver.manage().window().maximize();
			objWebDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

			objWebDriver.get(this.strParam1);
			if (objWebDriver.getClass().getName().equals("org.openqa.selenium.ie.InternetExplorerDriver")
					&& objWebDriver.getTitle().contains("Certificate")) {
				objWebDriver.navigate().to("javascript:document.getElementById('overridelink').click()");
			}
			this.strActualRes = "Successfully Launched Application. " + this.strParam1;
			this.strStatus = "PASS";
		} catch (Exception e) {
			this.strActualRes = "Exception in Launch method : " + strParam2;
			this.strStatus = "FAIL";
			e.printStackTrace();
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	
	// ###################################################################################
	// Function : Launch_ChngLocale()
	// Description : This method is used to launch the application in the
	// mentioned browser and also change the Language .
	// Parameters : Browser Type || Web URL || Language
	// Author : Sachin Ahuja
	// Date : 10 Aug 2016
	// Modified by -
	// Modification -
	// ###################################################################################

	public KDFEventResult Launch_ChngLocale() {
		try {
			objWebDriver = null;
			DesiredCapabilities caps;
			strBrowser = "";
			// Handling Chrome Browser
			if (this.strParam2.toUpperCase().equals("CHROME")) {
				strBrowser = "CHROME";
				ChromeDriverService service = new ChromeDriverService.Builder()
						.usingDriverExecutable(new File("Chrome_Driver\\chromedriver.exe")).usingAnyFreePort().build();
				service.start();
				caps = DesiredCapabilities.chrome();
				caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

				ChromeOptions options = new ChromeOptions();
				options.addArguments("chrome.switches", "--disable-extensions");

				// how to set Parameter Lang
				options.addArguments("--lang=" + this.strParam3);
				caps.setCapability(ChromeOptions.CAPABILITY, options);
				objWebDriver = new RemoteWebDriver(service.getUrl(), caps);
				SharedResource.dictRTVarCollection.put(strParam3, strParam2);
			} else {
				strBrowser = "";
				caps = DesiredCapabilities.firefox();

				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference("intl.accept_languages", this.strParam3);
				caps.setCapability(FirefoxDriver.PROFILE, profile);
				caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				caps.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
						org.openqa.selenium.UnexpectedAlertBehaviour.IGNORE);
				objWebDriver = new FirefoxDriver(caps);
				SharedResource.dictRTVarCollection.put(strParam3, strParam2);
			}

			objWebDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			objWebDriver.get(this.strParam1);
			if (objWebDriver.getClass().getName().equals("org.openqa.selenium.ie.InternetExplorerDriver")
					&& objWebDriver.getTitle().contains("Certificate")) {
				objWebDriver.navigate().to("javascript:document.getElementById('overridelink').click()");
			}
			this.strActualRes = "Successfully Launched Application. " + this.strParam1;
			this.strStatus = "PASS";
		} catch (Exception e) {
			this.strActualRes = "Exception in Launch method : " + strParam2;
			this.strStatus = "FAIL";
			e.printStackTrace();
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}


	
	// ##################################################################################
	// Function : DoubleClick()
	// Description : This method Double Click on Object provided
	// Parameters : Screen Name || Web Element Object 
	// Author : Sachin Ahuja
	// Date : 10 Aug 2016
	// Modified by -
	// Modification -
	public KDFEventResult DoubleClick() {
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			Actions builder = new Actions(objWebDriver);
			builder.doubleClick(objWebElement).build().perform();
			this.strActualRes = "Successfully double clicked on " + this.strChild;
			this.strStatus = "PASS";
		} catch (Exception e) {
			System.out.println("Exception occured in Right Clicking the object");
			System.out.println(e.getMessage());
			this.strActualRes = e.getMessage();
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	
	// ##################################################################################
	// Function : RightClick()
	// Description : This method Right Click on Object provided
	// Parameters : Screen Name || Web Element Object 
	// Author : Sachin Ahuja
	// Date : 10 Aug 2016
	// Modified by -
	// Modification -
			
	public KDFEventResult RightClick() {
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			Actions builder = new Actions(objWebDriver);
			builder.contextClick(objWebElement).build().perform();
			this.strActualRes = "Successfully right clicked on " + this.strChild;
			this.strStatus = "PASS";
		} catch (Exception e) {
			System.out.println("Exception occured in Right Clicking the object");
			System.out.println(e.getMessage());
			this.strActualRes = e.getMessage();
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	
	// ##################################################################################
	// Function : Click()
	// Description : This method Click on Object provided
	// Parameters : Screen Name || Web Element Object 
	// Author : Sachin Ahuja
	// Date : 10 Aug 2016
	// Modified by -
	// Modification -
	
	
	public KDFEventResult Click() {
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			if (this.objWebElement.isEnabled()) {
				this.objWebElement.click();
				this.strActualRes = "Successfully Clicked on " + this.strChild;
				this.strStatus = "PASS";
			} else {
				this.strActualRes = "Cannot click on " + this.strChild + " because object is disabled";
				this.strStatus = "FAIL";
			}
		} catch (Exception e) {
			try {
				WrapsDriver we = (WrapsDriver) this.objWebElement;
				WebDriver driver = we.getWrappedDriver();
				JavascriptExecutor javascript = (JavascriptExecutor) driver;
				javascript.executeScript("arguments[0].click()", this.objWebElement);
				this.strActualRes = "Successfully Clicked on " + this.strChild + " - By JavaScript";
				this.strStatus = "PASS";
			} catch (Exception ex) {
				System.out.println("Exception occured in Clicking the object");
				e.printStackTrace();
				System.out.println(e.getMessage());
				this.strActualRes = "Exception occured in Clicking the object";
				this.strStatus = "FAIL";
			}
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	public KDFEventResult MouseClick() {
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String javaScript = "var evObj = document.createEvent('MouseEvents');"
					+ "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
					+ "arguments[0].dispatchEvent(evObj);";
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JavascriptExecutor javascript = (JavascriptExecutor) objWebDriver;

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			javascript.executeScript(javaScript, objWebElement);
			javaScript = "var evObj = document.createEvent('MouseEvents');"
					+ "evObj.initMouseEvent(\"click\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
					+ "arguments[0].dispatchEvent(evObj);";
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			javascript.executeScript(javaScript, objWebElement);
			this.strActualRes = "MouseClick is successfull";
			this.strStatus = "PASS";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			this.strActualRes = "Exception Occur in MouseOver";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}
	

	public KDFEventResult MouseHOver() {
		try {
			this.objWebElement = new KDFWebElement(this.objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			Actions actions = new Actions(this.objWebDriver);
			actions.moveToElement(this.objWebElement).build().perform();
			this.strActualRes = "Successfully Mouse Hover on " + this.strChild;
			this.strStatus = "PASS";
		} 
		catch (Exception e) {
			System.out.println("Exception occured in Mouse Hover the object");
			System.out.println(e.getMessage());
			this.strActualRes = e.getMessage();
			this.strStatus = "FAIL";
		}
		
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}


	
	// ##################################################################################
	// Function : DragAndDrop()
	// Description : This method Drag and Drop an Object to another provided
	// Parameters : Screen Name || two Web Element Object 
	// Author : Sachin Ahuja
	// Date : 08 Aug - 2017
	// Modified by -
	// Modification -
	
	public KDFEventResult DragAndDrop() {
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			if (this.objWebElement.isEnabled()) {
				String dropLocator=KDFEvent.GetORProperties(this.strParent,this.strParam1);
				WebElement drop=new KDFWebElement(objWebDriver).getTargetObject(dropLocator, this.intIndex);
				
				Actions builder = new Actions(objWebDriver);
				
				Action dragAndDrop = builder.clickAndHold(this.objWebElement)
						 
						.moveToElement(drop)
						 
						.release(drop)
						 
						.build();
						 
						dragAndDrop.perform();
					
				this.strActualRes = "Successfully Drag and Drop " + this.strChild +" on "+this.strParam1;
				this.strStatus = "PASS";
			} else {
				this.strActualRes = "Cannot Drag and Drop " + this.strChild +" on "+this.strParam1 + " because object is disabled";
				this.strStatus = "FAIL";
			}
		} catch (Exception e) {
			
				System.out.println("Exception occured in Drag and Drop  the object");
				e.printStackTrace();
				System.out.println(e.getMessage());
				this.strActualRes = "Exception occured in Drag and Drop  the object";
				this.strStatus = "FAIL";
			
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	
	
	
	// Browser Operations
	
	// ##################################################################################
	// Function : NavigateBack()
	// Description : This method navigate user to previous screen
	// Parameters : Screen Name || Web Element Object 
	// Author : Sachin Ahuja
	// Date : 10 Aug 2016
	// Modified by -
	// Modification -
			
	public KDFEventResult NavigateBack() {
		try {
			this.objWebDriver.navigate().back();
			// this.objWebDriver = null;
			this.strStatus = "PASS";
			this.strActualRes = "Successfully Navigated back to parent window";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.strStatus = "FAIL";
			this.strActualRes = "Exception in navigating back to parent window ";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	

	
	public KDFEventResult Navigate() {
		try {
			String str1 = this.strParam1;
			if (str1.equalsIgnoreCase("back")) {
				this.objWebDriver.navigate().back();
				this.strStatus = "PASS";
				this.strActualRes = ("Successfully Navigated to " + this.strParam1);
			} else if (str1.equalsIgnoreCase("forward")) {
				this.objWebDriver.navigate().forward();
				this.strStatus = "PASS";
				this.strActualRes = ("Successfully Navigated to " + this.strParam1);
				
			} 
			else if (str1.equalsIgnoreCase("Refresh")) {
				this.objWebDriver.navigate().refresh();
				this.strStatus = "PASS";
				this.strActualRes = ("Successfully Navigated to " + this.strParam1);
				
			}
			else {
				this.objWebDriver.navigate().to(this.strParam1);

				this.strStatus = "PASS";
				this.strActualRes = ("Successfully Navigated to " + this.strParam1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.strStatus = "FAIL";
			this.strActualRes = ("Exception in navigation to " + this.strParam1);
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}


	
	public KDFEventResult RefreshBrowser() {
		try {
			this.objWebDriver.navigate().refresh();
			this.strActualRes = this.strChild + " is successfully refreshed.";
			this.strStatus = "PASS";
		} catch (Exception ex) {
			this.strActualRes = "Exception in refreshing " + this.strChild;
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}



	public KDFEventResult Maximize() {
		try {
			objWebDriver.manage().window().maximize();
			this.strStatus = "PASS";
			this.strActualRes = "Successfully Maximized";
		} catch (Exception e) {
			this.strStatus = "FAIL";
			this.strActualRes = e.getMessage() + ".\n Exception occurred in Maximize;";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	
	
	// ##################################################################################
	// Function : SetValue()
	// Description : This method is used to insert text in textbox and textareas
	// Parameters : Screen Name || Web Element Object || Text to be inserted
	// Author : Sachin Ahuja
	// Date : 10 Aug 2016
	// Modified by -
	// Modification -
	// ###################################################################################

	public KDFEventResult SetValue() {
		String objectType = "";
		this.strStatus = "";
		this.strActualRes = "";

		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.objKDFEvent.objProperty,
					this.intIndex);
			objectType = this.objWebElement.getAttribute("type");
		} catch (Exception e) {
			objectType = "text";
		}
		if (objectType.equalsIgnoreCase("checkbox")) {
			// Validate For Checkbox object
			if (this.strParam1.equalsIgnoreCase("OFF")) {
				if (this.objWebElement.isSelected()) {
					try {
						this.objWebElement.click();
					} catch (Exception e) {
						this.strActualRes = "Exception in SetValue method with " + strChild;
						this.strStatus = "FAIL";
					}
				}
			} else {
				if (!this.objWebElement.isSelected()) {
					try {
						this.objWebElement.click();
					} catch (Exception e) {
						this.strActualRes = "Exception in SetValue method with " + strChild;
						this.strStatus = "FAIL";
					}
				}
			}
			if (this.strStatus == "") {
				this.strActualRes = strParam1 + " value has been set at " + strChild;
				this.strStatus = "PASS";
			}
		} else {
			// Verified Textbox object
			try {
				this.objWebElement.clear();
				this.objWebElement.sendKeys(strParam1);
				this.strActualRes = strParam1 + " value has been set at " + strChild;
				this.strStatus = "PASS";
			} catch (Exception e) {
				this.strActualRes = "Exception in SetValue method with " + strChild;
				this.strStatus = "FAIL";
			}
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	
	// ##################################################################################
	// Select box
	// ##################################################################################



	public KDFEventResult SelectValue() {
		try {
			boolean boolOptionFound = false;
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			List<WebElement> options = this.objWebElement.findElements(By.tagName("option"));
			for (WebElement option : options) {
				if (option.getText().trim().toUpperCase().contains(this.strParam1.trim().toUpperCase())) {
					option.click();
					boolOptionFound = true;
					break;
				}
			}
			if (boolOptionFound) {
				this.strActualRes = this.strParam1 + " option is successfully selected.";
				this.strStatus = "PASS";
			} else {
				this.strActualRes = this.strParam1 + " option is absent in the Combobox.";
				this.strStatus = "FAIL";
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.strActualRes = "Exception occurred in SelectValue.";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	public KDFEventResult SelectValueByIndex() {
		try {
			Select objSelec = new Select(
					new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex));
			try {
				objSelec.deselectAll();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			objSelec.selectByIndex(Integer.valueOf(this.objKDFEvent.strParam1));
			this.strActualRes = this.strParam1 + " option is successfully selected.";
			this.strStatus = "PASS";

		} catch (Exception e) {
			e.printStackTrace();
			this.strActualRes = "Exception occurred in SelectValue.";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	
	// **********************************************************************************
	public KDFEventResult VerifyChecked() {
		String ActualStatus = "";
		try {
			this.objWebElement = new KDFWebElement(this.objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			if (this.objWebElement.isSelected()) {
				if (this.strParam1.equalsIgnoreCase("TRUE")) {
					this.strActualRes = (this.objKDFEvent.strChild + " is checked");
					this.strStatus = "PASS";
				} else {
					this.strActualRes = (this.objKDFEvent.strChild + " is checked");
					this.strStatus = "FAIL";
				}
				ActualStatus = "TRUE";
			} else {
				if (this.strParam1.equalsIgnoreCase("TRUE")) {
					this.strActualRes = (this.objKDFEvent.strChild + " is not checked");
					this.strStatus = "FAIL";
				} else {
					this.strActualRes = (this.objKDFEvent.strChild + " is not checked");
					this.strStatus = "PASS";
				}
				ActualStatus = "FALSE";
			}
		} catch (Exception e) {
			this.strActualRes = "Exception Occurred";
			this.strStatus = "FAIL";

			ActualStatus = "FALSE";
		}
		SharedResource.isVerificationKeyword = Boolean.valueOf(true);
		SharedResource.expectedResult = "Field Existance = " + this.strParam1;
		SharedResource.actualResult = ActualStatus;

		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	
	// ##################################################################################

	public KDFEventResult GetValue() {
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			this.strStoreValue = this.objWebElement.getAttribute("value");
			SharedResource.dictRTVarCollection.put(strParam1, this.strStoreValue);
			this.strActualRes = this.strStoreValue;
			this.strStatus = "PASS";
		} catch (Exception e) {
			this.strStoreValue = "Exception Occurred";
			this.strActualRes = this.strStoreValue;
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	
	public KDFEventResult GetText() {
		String sValue = "";
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			sValue = this.objWebElement.getText();
			SharedResource.dictRTVarCollection.put(strParam1, sValue);
			this.strStatus = "PASS";
			this.strActualRes = this.strParam1 +" = "+ sValue;
		} catch (Exception e) {
			this.strActualRes = e.getMessage();
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	
	
	public KDFEventResult GetTitle() {
		String sValue = "";
		try {
			sValue = objWebDriver.getTitle();
			SharedResource.dictRTVarCollection.put(strParam1, sValue);
			this.strStatus = "PASS";
			this.strActualRes = sValue;
		} catch (Exception e) {
			this.strActualRes = e.getMessage();
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	// ###################################################################

	public KDFEventResult GetURL() {
		String sValue = "";
		try {
			sValue = objWebDriver.getCurrentUrl();
			SharedResource.dictRTVarCollection.put(strParam1, sValue);
			this.strStatus = "PASS";
			this.strActualRes = sValue;
		} catch (Exception e) {
			this.strStatus = "FAIL";
			this.strActualRes = e.getMessage() + ".\n Exception occurred in GetURL();";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}
	

	public KDFEventResult GetVisibleProperty() {
		boolean boolVisible;
		String strResultMsg = "";
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			if (objWebElement != null) {
				boolVisible = objWebElement.isDisplayed();
				strResultMsg = new Boolean(boolVisible).toString();
				SharedResource.dictRTVarCollection.put(strParam1, strResultMsg);
				this.strStatus = "PASS";
				this.strActualRes = strResultMsg;
			} else {
				this.strActualRes = "Object not found";
				this.strStatus = "FAIL";
			}

		} catch (Exception e) {
			this.strActualRes = e.getStackTrace().toString();
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}
	
	public KDFEventResult GetObjectLocation() {

		try {
			Point objLocation = this.objWebElement.getLocation();
			SharedResource.dictRTVarCollection.put(strParam1, objLocation.x + "," + objLocation.y);
			this.strActualRes = objLocation.x + "," + objLocation.y;
			this.strStatus = "PASS";
		} catch (Exception e) {
			this.strStoreValue = "Exception Occurred";
			this.strActualRes = this.strStoreValue;
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	// ########################################################################

	public KDFEventResult GetAttribute() {
		String strAttributeValue = "";
		String strAttribute = strParam1;
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			if (objWebElement != null) {
				strAttributeValue = objWebElement.getAttribute(strAttribute);
				// strResultMsg = new Boolean(boolVisible).toString();
				SharedResource.dictRTVarCollection.put(strParam2, strAttributeValue);
				this.strStatus = "PASS";
				this.strActualRes = strAttributeValue;
			} else {
				this.strActualRes = "Object not found";
				this.strStatus = "FAIL";
			}

		} catch (Exception e) {
			this.strActualRes = "Exception occurred in GetAttribute()";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}



	

	public KDFEventResult SendKeyboardInput() {
		String str1 = this.strParam1;
		int count = Integer.parseInt(this.strParam2);
		try {
			for (int i = 1; i <= count; i++) {
				if (this.objKDFEvent.strParent.equalsIgnoreCase(this.objKDFEvent.strChild)) {
					Actions action = new Actions(this.objWebDriver);
					if (str1.equalsIgnoreCase("Enter")) {
						action.sendKeys(new CharSequence[] { Keys.ENTER });
					} else if (str1.equalsIgnoreCase("RightArrow")) {
						action.sendKeys(new CharSequence[] { Keys.ARROW_RIGHT });
					} else if (str1.equalsIgnoreCase("Space")) {
						action.sendKeys(new CharSequence[] { Keys.SPACE });
					} else if (str1.equalsIgnoreCase("UpArrow")) {
						action.sendKeys(new CharSequence[] { Keys.ARROW_UP });
					} else if (str1.equalsIgnoreCase("DownArrow")) {
						action.sendKeys(new CharSequence[] { Keys.ARROW_DOWN });
					} else if (str1.equalsIgnoreCase("End")) {
						action.sendKeys(new CharSequence[] { Keys.END });
					} else if (str1.equalsIgnoreCase("Shifthome")) {
						action.sendKeys(new CharSequence[] { Keys.SHIFT, Keys.HOME });
					} else if (str1.equalsIgnoreCase("PageDown")) {
						action.sendKeys(new CharSequence[] { Keys.PAGE_DOWN });
					} else if (str1.equalsIgnoreCase("LeftArrow")) {
						action.sendKeys(new CharSequence[] { Keys.ARROW_LEFT });
					}

					action.perform();
				} else {
					this.objWebElement = new KDFWebElement(this.objWebDriver).getTargetObject(this.strLocator,
							this.intIndex);
					if (this.objWebElement != null) {
						if (str1.equalsIgnoreCase("Enter")) {
							this.objWebElement.sendKeys(new CharSequence[] { Keys.ENTER });
						} else if (str1.equalsIgnoreCase("RightArrow")) {
							this.objWebElement.sendKeys(new CharSequence[] { Keys.ARROW_RIGHT });
						} else if (str1.equalsIgnoreCase("Space")) {
							this.objWebElement.sendKeys(new CharSequence[] { Keys.SPACE });
						} else if (str1.equalsIgnoreCase("UpArrow")) {
							this.objWebElement.sendKeys(new CharSequence[] { Keys.ARROW_UP });
						} else if (str1.equalsIgnoreCase("DownArrow")) {
							this.objWebElement.sendKeys(new CharSequence[] { Keys.ARROW_DOWN });
						} else if (str1.equalsIgnoreCase("End")) {
							this.objWebElement.sendKeys(new CharSequence[] { Keys.END });
						} else if (str1.equalsIgnoreCase("Shifthome")) {
							this.objWebElement.sendKeys(new CharSequence[] { Keys.SHIFT, Keys.HOME });
						} else if (str1.equalsIgnoreCase("PageDown")) {
							this.objWebElement.sendKeys(new CharSequence[] { Keys.PAGE_DOWN });
						} else if (str1.equalsIgnoreCase("LeftArrow")) {
							this.objWebElement.sendKeys(new CharSequence[] { Keys.ARROW_LEFT });
						}

					}
				}
			}
			this.strActualRes = "Keys are successfully sent.";
			this.strStatus = "PASS";
		} catch (Exception ex) {
			this.strActualRes = "Exception in sending keys.";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}


	public KDFEventResult GetObjectProperty() {
		String strAttributeValue = "";
		String strAttribute = strParam1;
		Dimension objDimension;
		Point objLocation;
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			if (objWebElement != null) {
				strAttributeValue = objWebElement.getAttribute(strAttribute);
				// strResultMsg = new Boolean(boolVisible).toString();
				if (strAttributeValue == null) {

					if (strAttribute.equalsIgnoreCase("HEIGHT")) {
						objDimension = objWebElement.getSize();
						strAttributeValue = String.valueOf(objDimension.height);
					} else if (strAttribute.equalsIgnoreCase("WIDTH")) {
						objDimension = objWebElement.getSize();
						strAttributeValue = String.valueOf(objDimension.width);
					} else if (strAttribute.equalsIgnoreCase("X")) {
						objLocation = objWebElement.getLocation();
						strAttributeValue = String.valueOf(objLocation.x);
					} else if (strAttribute.equalsIgnoreCase("Y")) {
						objLocation = objWebElement.getLocation();
						strAttributeValue = String.valueOf(objLocation.y);
					} else if (strAttribute.equalsIgnoreCase("VISIBLE")) {
						strAttributeValue = String.valueOf(objWebElement.isDisplayed());
					} else if (strAttribute.equalsIgnoreCase("ENABLE")) {
						strAttributeValue = String.valueOf(objWebElement.isEnabled());
					} else if (strAttribute.equalsIgnoreCase("CHECKED")) {
						strAttributeValue = String.valueOf(objWebElement.isSelected());
					}
				}
				SharedResource.dictRTVarCollection.put(strParam2, strAttributeValue);
				this.strStatus = "PASS";
				this.strActualRes = strAttributeValue;

			} else {
				this.strActualRes = "Object not found";
				this.strStatus = "FAIL";

			}
		} catch (Exception e) {
			this.strActualRes = "Exception occurred";
			this.strStatus = "FAIL";

		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	
	
	public KDFEventResult GetExistance() {
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			if ((this.objWebElement) != null) {
				SharedResource.dictRTVarCollection.put(this.strParam1, "TRUE");
				this.strActualRes = "Object Exists";
				this.strStatus = "PASS";
			} else {
				SharedResource.dictRTVarCollection.put(this.strParam1, "FALSE");
				this.strActualRes = "Object doesn't exist.";
				this.strStatus = "PASS";
			}
		} catch (Exception e) {
			SharedResource.dictRTVarCollection.put(this.strParam1, "FALSE");
			this.strActualRes = "Exception Occured in Get Existence";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}


	

	
	// Verify
	public KDFEventResult VerifyTitle() {
		String sValue = "";
		try {
			sValue = objWebDriver.getTitle();
			if (sValue.equals(strParam1)) {
				this.strStatus = "PASS";
				this.strActualRes = "Actual title : " + sValue + " matches with expected : " + strParam1;
			} else {
				this.strStatus = "FAIL";
				this.strActualRes = "Actual title : " + sValue + "doesn't matche with expected : " + strParam1;
			}
		} catch (Exception e) {
			this.strActualRes = e.getMessage();
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	
	public KDFEventResult VerifyProperty() {
		String strAttributeValue = "";
		String strAttribute = strParam1;
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			if (objWebElement != null) {
				strAttributeValue = objWebElement.getAttribute(strAttribute);
				// strResultMsg = new Boolean(boolVisible).toString();
				if (strParam2.equals(strAttributeValue)) {
					this.strStatus = "PASS";
					this.strActualRes = "Expected " + strParam2 + " matches with actual " + strAttributeValue;
				} else {
					this.strStatus = "FAIL";
					this.strActualRes = "Expected " + strParam2 + " doesn't match with actual " + strAttributeValue;
				}
			} else {
				this.strActualRes = "Object not found";
				this.strStatus = "FAIL";
			}

		} catch (Exception e) {
			this.strActualRes = "Exception occurred in VerifyProperty";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	public KDFEventResult VerifyEnabled() {
		boolean boolEnable;
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			if (this.objWebElement != null) {
				boolEnable = this.objWebElement.isEnabled();
				if ((boolEnable == true) && (this.strParam1.trim().equalsIgnoreCase("TRUE"))) {
					this.strStatus = "PASS";
					this.strActualRes = "Object is Enabled";
				} else if ((boolEnable == false) && (this.strParam1.trim().equalsIgnoreCase("FALSE"))) {
					this.strStatus = "PASS";
					this.strActualRes = "Object is Disabled";
				} else if ((boolEnable == false) && (this.strParam1.trim().equalsIgnoreCase("TRUE"))) {
					this.strStatus = "FAIL";
					this.strActualRes = "Object is Disabled";
				} else if ((boolEnable == true) && (this.strParam1.trim().equalsIgnoreCase("FALSE"))) {
					this.strStatus = "FAIL";
					this.strActualRes = "Object is Enabled";
				} else {
					this.strStatus = "FAIL";
					this.strActualRes = "Please use Keyword Properly";
				}
			} else {
				this.strActualRes = "Object not found";
				this.strStatus = "FAIL";
			}
		} catch (Exception e) {
			this.strActualRes = "Exception occured in VerifyEnabled()";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	

	public KDFEventResult VerifyText() {
		String sActualMatch = "", sActualText = "";
		Boolean boolReturnVal = false;
		String sExpectedMatch = strParam2.toUpperCase();
		if (!(sExpectedMatch.equalsIgnoreCase("TRUE")) && !(sExpectedMatch.equalsIgnoreCase("FALSE"))) {
			this.strStatus = "FAIL";
			this.strActualRes = "Enter TRUE / FALSE in Param2.";
		}
		String sExpectedValue = "";
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			if ((objWebElement) != null) {
				sActualText = objWebElement.getText();
				sExpectedValue = strParam1;
				Double val1, val2;
				try {
					val1 = Double.parseDouble(sActualText);
					val2 = Double.parseDouble(sExpectedValue);
					if (val1.compareTo(val2) == 0) {
						sActualMatch = "TRUE";
					} else {
						sActualMatch = "FALSE";
					}
				} catch (Exception e) {
					String strLineSeparator = System.getProperty("line.separator");
					sActualText = sActualText.replace(strLineSeparator, " ");
					sActualText = sActualText.replace("\n", " ");
					sActualText = sActualText.replace("\r\n", " ");
					sActualText = sActualText.replace("\r", " ");
					sActualText = sActualText.replace("\n\r", " ");

					sExpectedValue = sExpectedValue.replace(strLineSeparator, " ");
					sExpectedValue = sExpectedValue.replace("\n", " ");
					sExpectedValue = sExpectedValue.replace("\r\n", " ");
					sExpectedValue = sExpectedValue.replace("\r", " ");
					sExpectedValue = sExpectedValue.replace("\n\r", " ");

					if (sActualText.equals(sExpectedValue)) {
						sActualMatch = "TRUE";
					} else {
						sActualMatch = "FALSE";
					}
				}
				if ((sActualMatch.equals("FALSE")) && (sExpectedMatch.equals("TRUE"))) {
					this.strStatus = "FAIL";
					this.strActualRes = sExpectedValue + " does not match with " + sActualText;
					boolReturnVal = false;
				} else if ((sActualMatch.equals("TRUE")) && (sExpectedMatch.equals("FALSE"))) {
					this.strStatus = "FAIL";
					this.strActualRes = sExpectedValue + " match with " + sActualText;
					boolReturnVal = false;
				} else if ((sActualMatch.equals("FALSE")) && (sExpectedMatch.equals("FALSE"))) {
					this.strStatus = "PASS";
					this.strActualRes = sExpectedValue + " does not match with " + sActualText;
					boolReturnVal = true;
				} else if ((sActualMatch.equals("TRUE")) && (sExpectedMatch.equals("TRUE"))) {
					this.strStatus = "PASS";
					this.strActualRes = sExpectedValue + " match with " + sActualText;
					boolReturnVal = true;
				}
			} else {
				this.strStatus = "FAIL";
				this.strActualRes = "Object not found";
			}
		} catch (Exception e) {
			this.strStatus = "FAIL";
			this.strActualRes = "Exception occurred.";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}



	// ##################################################################################
	public KDFEventResult VerifyExist() {
		// To reduce execution time
		// objWebDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		String ActualStatus = "";
		String strOutput = strParam2;

		try {

			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			if ((this.objWebElement) != null) {
				if (this.strParam1.equalsIgnoreCase("TRUE")) {
					this.strActualRes = this.objKDFEvent.strChild + " Object exists";
					this.strStatus = "PASS";
					// return true;
				} else {
					this.strActualRes = this.objKDFEvent.strChild + " Object exists";
					this.strStatus = "FAIL";
					// return false;
				}
				ActualStatus = "TRUE";
			} else {
				if (this.strParam1.equalsIgnoreCase("TRUE")) {
					this.strActualRes = this.objKDFEvent.strChild + " Object doesn't exist";
					this.strStatus = "FAIL";
					// return false;
				} else {
					this.strActualRes = this.objKDFEvent.strChild + " Object doesn't exist";
					this.strStatus = "PASS";
					// return false;
				}
				ActualStatus = "FALSE";
			}
			SharedResource.dictRTVarCollection.put(strOutput, ActualStatus);
		} catch (Exception e) {
			this.strActualRes = "Exception Occurred";
			this.strStatus = "FAIL";
			// return false;
			ActualStatus = "FALSE";
		}

		SharedResource.isVerificationKeyword = true;
		SharedResource.expectedResult = "Field Existance = " + this.strParam1;
		SharedResource.actualResult = ActualStatus;
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}


	
	
	
	public KDFEventResult Activate() {
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			WrapsDriver we = (WrapsDriver) this.objWebElement;
			WebDriver driver = we.getWrappedDriver();
			JavascriptExecutor javascript = (JavascriptExecutor) driver;
			javascript.executeScript("arguments[0].focus()", this.objWebElement);
			// new
			// BaseAction(this.objWebDriver).moveToElement(this.objWebElement).perform();
			// new
			// Actions(this.objWebDriver).moveToElement(this.objWebElement).perform();
			this.strActualRes = this.strChild + " is successfully activated.";
			this.strStatus = "PASS";
		} catch (Exception ex) {
			this.strActualRes = "Exception in activating " + this.strChild;
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}
	
	
	
	
	
	// ##################################################################################
	// Function : CaptureScreen()
	// Description : This method Capture screen on diff. browser
	// Parameters : Screen Name 
	// Author : Sachin Ahuja
	// Date : 08 Aug - 2017
	// Modified by -
	// Modification -
	
	public KDFEventResult CaptureScreen() {
		this.strActualRes = "Screen Capture FAILS";
		this.strStatus = "FAIL";

		String screenshotPath = new TimeStamp().getTimeStamp("MMM dd, yyyy, h-mm-ss a");
		screenshotPath = this.strParam1 + screenshotPath.concat(".png");
		try {
			// Case: Firefox, IE
			if ((this.objWebDriver != null) && (!(strBrowser.equals("CHROME")))
					&& (!(strBrowser.equals("IOSNATIVEDRIVER")))
					&& (!(this.objWebDriver.getClass().toString().contains("RemoteWebDriver")))) {
				File screenshotFile = ((TakesScreenshot) this.objWebDriver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(screenshotFile, new File(screenshotPath));
			}
			// Case: Chrome
			else if ((this.objWebDriver != null) && ((strBrowser.equals("CHROME")))) {
				WebDriver objRemoteWebdriverScreenshot = new Augmenter().augment(this.objWebDriver);
				File screenshotFile = ((TakesScreenshot) objRemoteWebdriverScreenshot).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(screenshotFile, new File(screenshotPath));
			} else if ((this.objWebDriver != null)
					&& ((this.objWebDriver.getClass().toString().contains("RemoteWebDriver")))) {
				WebDriver objRemoteWebdriverScreenshot = new Augmenter().augment(this.objWebDriver);
				File screenshotFile = ((TakesScreenshot) objRemoteWebdriverScreenshot).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(screenshotFile, new File(screenshotPath));
			}

			this.strActualRes = "Screen Captured and saved to " + screenshotPath;
			this.strStatus = "PASS";
		} catch (IOException e) {
			e.printStackTrace();
			screenshotPath = "";
		} catch (UnhandledAlertException e) {

		} catch (WebDriverException e) {

		}

		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	// ##################################################################################
	// Wait operations
	// ##################################################################################

	
	
	// ##################################################################################
	// Function : WaitFor()
	// Description : This method allow user to wait for second provided
	// Parameters : Screen Name 
	// Author : Sachin Ahuja
	// Date : 08 Aug - 2016
	// Modified by -
	// Modification -

	public KDFEventResult WaitFor() {
		try {
			Long objLong = new Long(strParam1);
			Thread.sleep(objLong * 1000);
			this.strActualRes = "Successfully waited for " + strParam1 + " Sec.";
			this.strStatus = "PASS";
		} catch (Exception e) {
			this.strActualRes = e.getMessage();
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	public KDFEventResult WaitUntilTextFound() {
		try {
			long dblTimer = Integer.valueOf(this.objKDFEvent.strParam3);
			Boolean loopBreak = true;
			while (loopBreak) {
				this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
				String strValue = this.objWebElement.getText();
				if (objKDFEvent.strParam1.toUpperCase().contains("CONTAIN")) {
					if (strValue.contains(objKDFEvent.strParam2)) {
						loopBreak = false;
						break;
					}
				} else if (objKDFEvent.strParam1.toUpperCase().contains("EQUAL")) {
					if (strValue.equals(objKDFEvent.strParam2)) {
						loopBreak = false;
						break;

					}
				} else if (objKDFEvent.strParam1.toUpperCase().contains("STARTSWITH")) {
					if (strValue.startsWith(objKDFEvent.strParam2)) {
						loopBreak = false;
						break;
					}
				} else if (objKDFEvent.strParam1.toUpperCase().contains("ENDSWITH")) {
					if (strValue.endsWith(objKDFEvent.strParam2)) {
						loopBreak = false;
						break;
					}
				} else {
					break;
				}
				Thread.sleep(dblTimer * 1000);
			}
			if (!loopBreak) {
				this.strActualRes = "Successfully executed.";
				this.strStatus = "PASS";
			} else {
				this.strActualRes = "Improper keyword parameters";
				this.strStatus = "FAIL";
			}

		} catch (Exception ex) {
			this.strActualRes = "Exception in keyword";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	
	
	public KDFEventResult WaitForUntill() {

		String Operator;
		String StatementResult = "";
		long timer;
		long threshodTime_MiliSec = 0;
		double objectLoadTime = 0;
		String CoreExpression = this.strParam1;
		//String sOutPut = this.strParam3;

		timer = System.currentTimeMillis();
		System.out.println(timer);
		try {
			threshodTime_MiliSec = Long.parseLong(this.strParam2) * 1000;
		} catch (Exception e) {
			threshodTime_MiliSec = 20 * 1000;
		}

		CoreExpression = CoreExpression.replace("(", "");
		CoreExpression = CoreExpression.replace(")", "");
		CoreExpression = CoreExpression.replace("=", "==");
		CoreExpression = CoreExpression.replace("<>", "!=");

		// Convert operand into string format
		if (CoreExpression.contains("==")) {
			Operator = "==";
		} else {
			Operator = "!=";
		}

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");

		String[] Operand = CoreExpression.split(Operator);
		Long defaultTimeOut = null;

		do {
			String Operand1 = Operand[0];
			String Operand2 = Operand[1];
			objWebDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			try {
				defaultTimeOut = Long.parseLong(SharedResource.dictRTVarCollection.get("ObjectSyncTime"));
			} catch (Exception e) {
				defaultTimeOut = (long) 20;
			} finally {
				objWebDriver.manage().timeouts().implicitlyWait(defaultTimeOut, TimeUnit.SECONDS);
			}

			try {
				if (Operand1.trim().toUpperCase().contains("EXIST")) {
					if (this.objWebElement != null) {
						Operand1 = "'" + "TRUE" + "'";
					} else {
						Operand1 = "'" + "FALSE" + "'";
					}
				} else if (Operand1.trim().toUpperCase().contains("ENABLE")) {
					Operand1 = String.valueOf(this.objWebElement.isEnabled());
					Operand1 = "'" + Operand1.toUpperCase() + "'";
				} else if (Operand1.trim().toUpperCase().contains("TEXT")) {
					Operand1 = this.objWebElement.getText().toUpperCase();
					Operand1 = "'" + Operand1 + "'";
				} else if (Operand1.trim().toUpperCase().contains("VISIBLE")) {
					Operand1 = String.valueOf(this.objWebElement.isDisplayed());
					Operand1 = "'" + Operand1.toUpperCase() + "'";
				} else {
					Operand1 = Operand1.trim().toUpperCase();
					Operand1 = "'" + Operand1 + "'";
				}
			} catch (Exception e) {
				Operand1 = "'" + Operand1 + "'";
			}
			// Now Modify Operand2 value
			Operand2 = "'" + Operand2.trim().toUpperCase() + "'";

			String partialExpression = Operand1 + Operator + Operand2;

			try {
				Object expressionOutput = engine.eval(partialExpression);
				StatementResult = String.valueOf(expressionOutput).toUpperCase();
			} catch (Exception e) {
				StatementResult = "Exception Occured while calculating exp = " + partialExpression;
			}
		} while (!StatementResult.equalsIgnoreCase("TRUE")
				&& (System.currentTimeMillis() - timer) < threshodTime_MiliSec);

		System.out.println(System.currentTimeMillis());

		if (StatementResult.equalsIgnoreCase("TRUE")) {
			objectLoadTime = ((double) (System.currentTimeMillis()) - (double) timer) / (double) 1000;
			SharedResource.dictRTVarCollection.put(strParam3,
					String.valueOf(((double) (System.currentTimeMillis()) - (double) timer) / (double) 1000));
			this.strStatus = "PASS";
			this.strActualRes = "Waiting Time - " + objectLoadTime + " Seconds";
		} else {
			SharedResource.dictRTVarCollection.put(strParam3, "0");
			this.strStatus = "FAIL";
			this.strActualRes = "Condition not achived within threshold time - " + this.strParam2 + " Seconds";
		}
		// enterDetailsForImprovedReporting("Load
		// Time",String.valueOf(objectLoadTime) + "Seconds");
		SharedResource.isVerificationKeyword = true;
		SharedResource.expectedResult = "Loading Time";
		SharedResource.actualResult = String.valueOf(objectLoadTime) + "Seconds";
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}



	
	
	// ##################################################################################
	// Close operations
 	// ##################################################################################

	// ##################################################################################
	// Function : Close()
	// Description : This method Close the current browser/tab
	// Parameters : 
	// Author : Sachin Ahuja
	// Date : 08 Aug - 2016
	// Modified by -
	// Modification -
	public KDFEventResult Close() {
		if (this.objWebDriver != null) {
			this.objWebDriver.close();
			this.objWebDriver = null;
			this.strStatus = "PASS";
			this.strActualRes = "Successfully Closed.";
		} else {
			this.objWebDriver = null;
			this.strStatus = "FAIL";
			this.strActualRes = "Unsuccessful";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	
	
	// ##################################################################################
	// Function : CloseAllBrowsers()
	// Description : This method Close all browser/tab
	// Parameters : 
	// Author : Sachin Ahuja
	// Date : 08 Aug - 2016
	// Modified by -
	// Modification -

	public KDFEventResult CloseAllBrowsers() {

		try {
			if (objWebDriver != null) {
				objWebDriver.quit();
				objWebDriver = null;
				this.strStatus = "PASS";
				this.strActualRes = "Successfully Closed.";

			} else {
				this.strStatus = "PASS";
				this.strActualRes = "There is no browser window open";
			}
		} catch (Exception e) {
			this.strStatus = "FAIL";
			this.strActualRes = "Exception Occured - " + e.getMessage();
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	
	// ##################################################################################
	// Function : GetObjectCount()
	// Description : This method return to count of element return from locator provided
	// Parameters : Screen | locator for webelement
	// Author : Sachin Ahuja
	// Date : 08 Aug - 2016
	// Modified by -
	// Modification -
	
	
	
	public KDFEventResult GetObjectCount() {
		this.strStatus = "";
		int objCount,cnt=0;

		try {
			List<WebElement> objWebElementList = new KDFWebElement(objWebDriver).getTargetObjectList(this.strLocator);
			WebElement wb=null;
			if (objWebElementList == null) {
				objCount = 0;
			} else {
				objCount = objWebElementList.size();
				for (int i=0;i<objCount;i++) {
					wb=objWebElementList.get(i);
					if(wb.isDisplayed()) {
						cnt++;
					}
				}
			}
			objCount=cnt;
			this.strStatus = "PASS";
			this.strActualRes=					"Number Objects = " + objCount;
		} catch (Exception e) {
			objCount = 0;
			this.strActualRes = e.getMessage();
			this.strStatus = "Fail";
		}

		if (!this.strParam1.equals("")) {
			SharedResource.dictRTVarCollection.put(strParam1, String.valueOf(objCount));
		}

		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	
	
	public KDFEventResult SelectRadio() {
		this.strStatus = "";
		this.Arr_objWebElement = new KDFWebElement(objWebDriver).getTargetObjectList(this.strLocator);
		List<WebElement> Radio_OptionList = this.Arr_objWebElement;
		for (int i = 0; i < Radio_OptionList.size(); i++) {
			String optionValue = Radio_OptionList.get(i).getAttribute("value");
			if (optionValue.equalsIgnoreCase(this.strParam1)) {
				Radio_OptionList.get(i).click();
				this.strActualRes = this.strParam1 + " Option selected successfully in radiogroup";
				this.strStatus = "PASS";
				break;
			}
		}

		if (this.strParam1.equals("")) {
			this.strActualRes = this.strParam1 + " Option not found in radiogroup";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	
	
	
	
	// Alert box
	
	public KDFEventResult SelectDefaultContent() {
		try {
			this.objWebDriver.switchTo().defaultContent();
			this.strActualRes = strChild + " Defaultcontent has been succesfully Selected";
			this.strStatus = "PASS";
		} catch (Exception e) {
			this.strActualRes = "Exception occured";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	
	public KDFEventResult SwitchToAlert() {
		try {
			this.objWebDriver.switchTo().alert();
			this.strActualRes = "Control switch to browser alert box";
			this.strStatus = "PASS";
		} catch (Exception e) {
			this.strActualRes = "Exception occured";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	
	public KDFEventResult ClickAlert_OK() {
		try {
			this.objWebDriver.switchTo().alert().accept();
			this.strActualRes = "Alert box accepted";
			this.strStatus = "PASS";
		} catch (Exception e) {
			this.strActualRes = "Exception occured";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	
	public KDFEventResult ClickAlert_Cancel() {
		try {
			this.objWebDriver.switchTo().alert().dismiss();
			this.strActualRes = "Alert box dismissed";
			this.strStatus = "PASS";
		} catch (Exception e) {
			this.strActualRes = "Exception occured";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	
	public KDFEventResult VerifyDialogText() {
		try {
			Alert alert = this.objWebDriver.switchTo().alert();
			String text = alert.getText();
			if (text.equals(this.strParam1)) {
				this.strActualRes = "Text matches. Expected : '" + this.strParam1 + "' and Actual : '" + text + "'";
				this.strStatus = "PASS";
			} else {
				this.strActualRes = "Text doesn't match. Expected : '" + this.strParam1 + "' and Actual : '" + text
						+ "'";
				this.strStatus = "FAIL";
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.strActualRes = "Exception occurred";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	
	

	public KDFEventResult UpdateObject() {
		String strParentNodeXPath = "";
		String filePath = SharedResource.strORPath;
		// TestObject objTO = new TestObject(); // this object represents the
		// TestObject contains target and index of the element.
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(filePath);
			UtilityClass objUtil = new UtilityClass();
			this.strParam2 = objUtil.handleStringForRuntimeVariable_UpdateObject(this.strParam2);

			strParentNodeXPath = "//Object[@Name='" + strParent.trim() + "']/Object[@Name='" + strChild
					+ "']/Def/Description";
			try {
				NodeList nlnodelist = (NodeList) xpath.evaluate(strParentNodeXPath, doc, XPathConstants.NODESET);
				if (nlnodelist.getLength() > 0) {
					if (!this.strParam1.equalsIgnoreCase("index")) {
						if (nlnodelist.item(0).getChildNodes().item(0).getNodeName().equalsIgnoreCase(this.strParam1)) {
							// String myVal =
							// nlnodelist.item(0).getChildNodes().item(0).getTextContent();
							// System.out.println(myVal);
							nlnodelist.item(0).getChildNodes().item(0).getFirstChild().setNodeValue(this.strParam2);
						} else {
							Element p = doc.createElement(this.strParam1);
							p.appendChild(doc.createTextNode(this.strParam2));
							nlnodelist.item(0).replaceChild(p, nlnodelist.item(0).getChildNodes().item(0));
						}
					} else {
						this.strParam3 = this.strParam2;
					}

					// Update Index
					if (!this.strParam3.isEmpty()) {
						String strIndexNodeXPath = "//Object[@Name='" + strParent.trim() + "']/Object[@Name='"
								+ strChild + "']/Def/Description/index";
						NodeList indexNodeList = (NodeList) xpath.evaluate(strIndexNodeXPath, doc,
								XPathConstants.NODESET);
						if (indexNodeList.getLength() > 0) {
							// Update index Value
							indexNodeList.item(0).getFirstChild().setNodeValue(this.strParam3);
						} else {
							// Add index node
							Element indexNode = doc.createElement("index");
							indexNode.appendChild(doc.createTextNode(this.strParam3));
							nlnodelist.item(0).appendChild(indexNode);
						}
					}
				}
			} catch (XPathExpressionException e) {
				System.out.println("XPathExpressionException in getORProperties() method");
				e.printStackTrace();
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer;

			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			// StreamResult result = new StreamResult(new File(filePath));
			StreamResult result = new StreamResult(new File(filePath).toURI().getPath());
			transformer.transform(source, result);
			// System.out.println("Done");
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.strActualRes = "Object updated successfully - " + this.strParam1 + "=" + this.strParam2;
		this.strStatus = "PASS";
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	

	public KDFEventResult SelectRadioGrp() {
		this.strStatus = "";
		this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
		List<WebElement> Radio_OptionList = this.objWebElement.findElements(By.tagName("label"));
		for (int i = 0; i < Radio_OptionList.size(); i++) {
			String optionValue = Radio_OptionList.get(i).getText();
			if (optionValue.equalsIgnoreCase(this.strParam1)) {
				String targetRadio_ID = Radio_OptionList.get(i).getAttribute("for");
				List<WebElement> Radio_InputList = this.objWebElement.findElements(By.id(targetRadio_ID));
				if (Radio_InputList.size() > 0) {
					Radio_InputList.get(0).click();
					this.strActualRes = this.strParam1 + " Option selected successfully in radiogroup";
					this.strStatus = "PASS";
				} else {
					this.strActualRes = this.strParam1 + " Option not found in radiogroup";
					this.strStatus = "FAIL";
				}
			}
		}

		if (this.strParam1.equals("")) {
			this.strActualRes = this.strParam1 + " Option not found in radiogroup";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}




	public KDFEventResult VerifyListItemSelected() {
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			Select SelectElem = new Select(this.objWebElement);
			if (SelectElem.getFirstSelectedOption().getText().trim().equals(this.strParam1)) {
				this.strActualRes = this.strParam1 + " option is selected.";
				this.strStatus = "PASS";
			} else {
				this.strActualRes = this.strParam1 + " option is not selected.";
				this.strStatus = "FAIL";
			}

		} catch (Exception e) {
			e.printStackTrace();
			this.strActualRes = "Exception occurred in VerifyListItemSelected.";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	public KDFEventResult GetListItemSelected() {
		String sValue = "";
		try {

			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			Select SelectElem = new Select(this.objWebElement);
			sValue = SelectElem.getFirstSelectedOption().getText();
			SharedResource.dictRTVarCollection.put(strParam1, sValue);
			this.strStatus = "PASS";
			this.strActualRes = sValue;
		} catch (Exception e) {
			e.printStackTrace();
			this.strActualRes = "Exception occurred in GetItemSelected.";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	public KDFEventResult GetAllListValues() {
		String strAllItems = "";

		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			if (this.objWebElement.getTagName().trim().equalsIgnoreCase("UI")) {
				List<WebElement> objListItems = this.objWebElement.findElements(By.xpath("./li"));
				for (WebElement item : objListItems) {
					strAllItems = item.getText() + ";" + strAllItems;
				}
			} else if (this.objWebElement.getTagName().trim().equalsIgnoreCase("Select")) {
				List<WebElement> objListItems = this.objWebElement.findElements(By.xpath("./option"));
				for (WebElement item : objListItems) {
					strAllItems = item.getText() + ";" + strAllItems;
				}
			} else if (this.objWebElement.getTagName().trim().equalsIgnoreCase("div")) {
				List<WebElement> objListItems = this.objWebElement.findElements(By.xpath("./div"));
				for (WebElement item : objListItems) {
					strAllItems = item.getText() + ";" + strAllItems;
				}
			}

			else {
				this.strStatus = "FAIL";
				this.strActualRes = "Invalid object. Valid for Select and UL only";
			}
			SharedResource.dictRTVarCollection.put(strParam1, strAllItems);
			this.strStatus = "PASS";
			this.strActualRes = strAllItems;
		} catch (Exception e) {
			e.printStackTrace();
			this.strActualRes = "Exception occurred in GetAllListItems.";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	public KDFEventResult GetListCount() {
		int intValue = 0;
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			String strTagName = this.objWebElement.getTagName();
			if (strTagName.trim().equalsIgnoreCase("Select")) // handling Select
			{
				Select SelectElem = new Select(this.objWebElement);
				intValue = SelectElem.getOptions().size();
			} else if (strTagName.trim().equalsIgnoreCase("UI")) // handling UI
			{
				intValue = this.objWebElement.findElements(By.xpath("./li")).size();
			} else {
				this.strStatus = "FAIL";
				this.strActualRes = "Invalid object. Valid for Select and UL only";
			}
			SharedResource.dictRTVarCollection.put(strParam1, String.valueOf(intValue));
			this.strStatus = "PASS";
			this.strActualRes = String.valueOf(intValue);

		} catch (Exception e) {
			e.printStackTrace();
			this.strActualRes = "Exception occurred in GetListCount.";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	public KDFEventResult VerifyListCount() {
		int intValue = 0;
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			String strTagName = this.objWebElement.getTagName();
			if (strTagName.trim().equalsIgnoreCase("Select")) // handling Select
			{
				Select SelectElem = new Select(this.objWebElement);
				intValue = SelectElem.getOptions().size();
			} else if (strTagName.trim().equalsIgnoreCase("UI")) // handling UI
			{
				intValue = this.objWebElement.findElements(By.xpath("./li")).size();
			} else {
				this.strStatus = "FAIL";
				this.strActualRes = "Invalid object. Valid for Select and UL only";
			}
			if (intValue == Integer.valueOf(strParam1.trim())) {
				this.strStatus = "PASS";
				this.strActualRes = String.valueOf(intValue);
			} else {
				this.strStatus = "FAIL";
				this.strActualRes = String.valueOf(intValue);
			}

		} catch (Exception e) {
			e.printStackTrace();
			this.strActualRes = "Exception occurred in VerifyListCount.";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	public KDFEventResult GetMultipleListItemsSelected() {
		String sValue = "";
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			Select SelectElem = new Select(this.objWebElement);
			List<WebElement> selectedOptions = SelectElem.getAllSelectedOptions();
			if (selectedOptions.size() > 0) {
				for (WebElement selectedOption : selectedOptions) {
					if (sValue.isEmpty()) {
						sValue = selectedOption.getText() + ";";
					} else {
						sValue = selectedOption.getText() + ";" + selectedOption.getText();
					}
				}
			}
			SharedResource.dictRTVarCollection.put(strParam1, sValue);
			this.strStatus = "PASS";
			this.strActualRes = sValue;
		} catch (Exception e) {
			e.printStackTrace();
			this.strActualRes = "Exception occurred in GetMultipleItemsSelected.";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}


	public WebElement getParentObject(WebElement targetObject) {
		WebElement parentObject = null;
		try {
			// this.objWebElement = new
			// KDFWebElement(objWebDriver).getTargetObject(this.strLocator,this.intIndex);
			WrapsDriver we = (WrapsDriver) targetObject;
			if (!(we == null)) {
				WebDriver driver = we.getWrappedDriver();
				JavascriptExecutor javascript = (JavascriptExecutor) driver;
				parentObject = (WebElement) javascript.executeScript("return arguments[0].parentNode;", targetObject);
			}
			// new
			// Actions(this.objWebDriver).click(this.objWebElement).perform();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return parentObject;
	}

	


	
	
	

}
