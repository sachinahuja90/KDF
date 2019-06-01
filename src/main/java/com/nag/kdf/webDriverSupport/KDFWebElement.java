package com.nag.kdf.webDriverSupport;

import java.lang.reflect.Method;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class KDFWebElement {

	private WebDriver objWebDriver = null;
	
	public KDFWebElement(WebDriver objWebDriver) {		
		this.objWebDriver = objWebDriver;
	}

	//###################################################################################
	// Function : getTargetObject(String strObjName, int index)   	
	// Description : This method is used to get the Object of the Webelement.
	// Parameters : String strObjName - represents the Target value of the object.
	//				int index - represents the index of the object.
	// Author : 
	// Date : 
	// Modified by - 
	// Modification - 
	//###################################################################################		
	public WebElement getTargetObject(String strObjName, int index)
	{
		WebElement objTarget = null;
		DerivedBy objBy = new DerivedBy();
		try {
			Class<?> C1 = objBy.getClass()	;	
			Method arrMtd[] = C1.getMethods();
			for(Method m : arrMtd)
			{
                if ((m.getName().toUpperCase()).equals((strObjName.split("=")[0].toUpperCase())))
                {
                    try{
                        String strObjProp;
                        strObjProp = strObjName.replace((strObjName.split("=")[0]).toString() + "=", "");
                        List<WebElement> webelemCol = objWebDriver.findElements((By)m.invoke(objBy, strObjProp));
                        objTarget = webelemCol.get(index);
                        //objTarget = objWebDriver.findElement((By)m.invoke(objBy, strObjProp));
                        //objTarget = wait.until(visibilityOfElementLocated((By)m.invoke(objBy, strObjProp)));       
                        break;
                    }catch(Exception e){
                        objTarget = null;
                        break;
                    }
                }                
            }			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return objTarget;
	}
	
	public List<WebElement> getTargetObjectList(String strObjName)
	{
		List<WebElement> objTargetList = null;
		DerivedBy objBy = new DerivedBy();
		try {
			Class<?> C1 = objBy.getClass();			
			Method arrMtd[] = C1.getMethods();
			for(Method m : arrMtd)
			{
                if ((m.getName().toUpperCase()).equals((strObjName.split("=")[0].toUpperCase())))
                {
                    try{
                        String strObjProp;
                        strObjProp = strObjName.replace((strObjName.split("=")[0]).toString() + "=", "");
                        List<WebElement> webelemCol = objWebDriver.findElements((By)m.invoke(objBy, strObjProp));
                        objTargetList = webelemCol;
                        break;
                    }catch(Exception e){
                    	objTargetList = null;
                        break;
                    }
                }                
            }			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return objTargetList;
	}
	
	
	
}
