package com.nag.kdf.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.ini4j.Ini;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.regexp.internal.recompile;

import java.util.Properties;

public class UtilityClass {
	XSSFSheet sheet;
	
	
	//public  Hashtable<String, String> dictRTVarCollection1 = new Hashtable<String, String>();	
	//###################################################################################
	// Function : getORProperties(String strORFile, String strParent, String strChild)   	
	// Description : 
	// Parameters : 
	// Author : 
	// Date : 
	//###################################################################################
	public org.apache.poi.ss.usermodel.Row.MissingCellPolicy NullAsBlank = org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;
	
	//Constructor
	public UtilityClass(XSSFSheet sheet2){
		this.sheet = sheet2;
	}
	public UtilityClass(){
		//Do Nothing
	}
	public TestObject getORProperties(String strORFile, String strParent, String strChild)
	{
		TestObject objTO = new TestObject(); // this object represents the TestObject contains target and index of the element.
	 	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try 
		{
			XPath xpath = XPathFactory.newInstance().newXPath();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(strORFile);
			String strParentNodeXPath = "";
			if(strParent.trim().equalsIgnoreCase(strChild.trim()))
			{
				strParentNodeXPath = "//Object[@Name='"+ strChild  +"']/Def/Description";
				SelTestObject objTest = getSeleniumTestObject(xpath, doc, strParentNodeXPath);
				objTO.intIndex = objTest.intIndex;
				objTO.intParentIndex = objTest.intIndex;
				objTO.strtarget = objTest.strtarget;
				objTO.strParentProp = objTest.strtarget;
			}
			else
			{
				strParentNodeXPath = "//Object[@Name='"+ strParent.trim()  +"']/Def/Description";		
				SelTestObject objTest = getSeleniumTestObject(xpath, doc, strParentNodeXPath);				
				objTO.intParentIndex = objTest.intIndex;			
				objTO.strParentProp = objTest.strtarget;
				strParentNodeXPath = "//Object[@Name='"+ strParent.trim() +"']/Object[@Name='"+ strChild  +"']/Def/Description";
				objTest = getSeleniumTestObject(xpath, doc, strParentNodeXPath);
				objTO.intIndex = objTest.intIndex;			
				objTO.strtarget = objTest.strtarget;				
			}
			
		}catch(SAXException se){
			objTO = new TestObject();
			System.out.println("SAXException in getORProperties() method");
		}catch(IOException ioe){
			objTO = new TestObject();
			System.out.println("IOException in getORProperties() method");
		}catch(Exception ex){
			objTO = new TestObject();
			System.out.println("Exception in getORProperties() method");
		}
	    return objTO;
	} //ends getORProperties() method

	
	public SelTestObject getSeleniumTestObject(XPath xpath,Document doc, String strParentNodeXPath)
	{
		SelTestObject objTO = new SelTestObject();		
		try {
			NodeList nlnodelist = (NodeList)xpath.evaluate(strParentNodeXPath,doc,XPathConstants.NODESET);
			if(nlnodelist.getLength()>0)
			{
				NodeList nListDesc = nlnodelist.item(0).getChildNodes();
				for(int i=0; i< nListDesc.getLength();i++)
				{
					Element objElem = (Element) nListDesc.item(i);
					if (objElem.getNodeName().equalsIgnoreCase("INDEX") && objElem.hasChildNodes())
					{
						String strIndexValue = objElem.getChildNodes().item(0).getNodeValue().trim();
						int intValue = 0;
						if(!strIndexValue.equals(""))
						{							
							try{
								intValue = Integer.valueOf(strIndexValue);
							}catch(NumberFormatException e){intValue = 0;}
						}
						objTO.intIndex = intValue;						
					}
					else
					{
						if(objElem.hasChildNodes())
						{
							objTO.strtarget = objElem.getNodeName().trim() + "=" + objElem.getChildNodes().item(0).getNodeValue().trim();
						}
					}							
				}		
			}
			
		} catch (XPathExpressionException e) {
			objTO = new SelTestObject();
			System.out.println("XPathExpressionException in getORProperties() method");
			//e.printStackTrace();
		} 		
		return objTO;		
	}
	
	
	
	//###################################################################################
	// Function : handleRuntimeVariable(String strRuntimeVariable)   	
	// Description : 
	// Parameters : 
	// Author : 
	// Date : 
	//###################################################################################
	public String handleRuntimeVariable(String strRuntimeVariable)
	{
        String strVariable = "";
        if (strRuntimeVariable == null){
        	strRuntimeVariable = "";
        
        }else{
        	int loc = strRuntimeVariable.indexOf("^");
            try
            {
                if (loc != -1){
                    if (SharedResource.dictRTVarCollection.containsKey(strRuntimeVariable.replace("^", ""))){
                        strVariable = SharedResource.dictRTVarCollection.get(strRuntimeVariable.replace("^", ""));
                    }
                    else{
                        strVariable = strRuntimeVariable;
                    }
                }
                else{
                    strVariable = strRuntimeVariable;
                }
            }
            catch (Exception e)
            {
                strVariable = "";
            }        	
        }
        //strVariable = handleStringForRuntimeVariable(strVariable);
        return strVariable;
    }//ends handleRuntimeVariable() method. 
    
     
    public String handleStringForRuntimeVariable(String strQuery)
 	{
 		String[] arrString = strQuery.split(" ");
 		String var = "";
 		//strQuery =strQuery.replaceAll(" ","");
 		for(int i=0;i<arrString.length;i++)
 		{
 			if (arrString[i].contains("^"))
 			{
 				var = handleRuntimeVariable(arrString[i]);			
 				strQuery = strQuery.replace(arrString[i], var);
 			}
 		}
 		strQuery = strQuery.replace(" .", ".");
 		strQuery = strQuery.replace(". ", ".");
 		//strQuery = strQuery.replace(" '", "'");
 		strQuery = strQuery.replace("' ", "'");
 		return strQuery;
 	}
	//added 
    public String handleStringForRuntimeVariable_UpdateObject(String strQuery)
 	{
     if (strQuery.contains("^")) {
 		String[] arrString = strQuery.split(" ");
 		String var = "";
 		strQuery =strQuery.replaceAll(" ","");
 		for(int i=0;i<arrString.length;i++)
 		{
 			if (arrString[i].contains("^"))
 			{
 				var = handleRuntimeVariable(arrString[i]);			
 				strQuery = strQuery.replace(arrString[i], var);
 			}
 		}
 		strQuery = strQuery.replace(" .", ".");
 		strQuery = strQuery.replace(". ", ".");
 		strQuery = strQuery.replace(" '", "'");
 		strQuery = strQuery.replace("' ", "'");
     }
     else if (strQuery.contains("$")) {
  		String[] arrString = strQuery.split(" ");
  		String var = "";
  		strQuery =strQuery.replaceAll(" ","");
  		for(int i=0;i<arrString.length;i++)
  		{
  			if (arrString[i].contains("$"))
  			{
  				var = handleDollarSignForLoop(arrString[i],SharedResource.currentIterationNumber);			
  				strQuery = strQuery.replace(arrString[i], var);
  			}
  		}
  		strQuery = strQuery.replace(" .", ".");
  		strQuery = strQuery.replace(". ", ".");
  		strQuery = strQuery.replace(" '", "'");
  		strQuery = strQuery.replace("' ", "'");
      }
 		return strQuery;
     
 	}
    public void waitFor(long miliseconds)
    {
	    try {
			Thread.sleep(miliseconds);
		} catch (Exception e) {}
    }
    
    public String handleString_For_DollarSign_OF_ForLoop(String strQuery, int LoopValueIndex)
 	{
 		String[] arrString = strQuery.split(" ");
 		String var = "";
 		for(int i=0;i<arrString.length;i++)
 		{
 			if (arrString[i].contains("$"))
 			{
 				var = handleDollarSignForLoop(arrString[i], LoopValueIndex);			
 				strQuery = strQuery.replace(arrString[i], var);
 			}
 		}
 		strQuery = strQuery.replace(" .", ".");
 		strQuery = strQuery.replace(". ", "."); 		
 		return strQuery;
 	}
    
  //Added By 
	public String handleDollarSignForLoop(String strRuntimeVariable, int LoopValueIndex)
	{
     String strVariable = strRuntimeVariable;
        if (strRuntimeVariable == null){
        	strRuntimeVariable = "";
        
        }else{
        	int loc = strRuntimeVariable.indexOf("$");
            try
            {
                if (loc != -1){
                    	int rowIndex = 0,columnIndex=7;
                    	XSSFSheet objVariableSheet;
                    	XSSFRow varRow;
                    	String ForLoopParameterName ;
                    	objVariableSheet= this.sheet.getWorkbook().getSheet("Variable_Values");
                    	varRow = objVariableSheet.getRow(rowIndex);
                    	varRow.getCell(columnIndex, NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
                    	ForLoopParameterName = varRow.getCell(columnIndex,NullAsBlank).getStringCellValue();
                    	while(!(ForLoopParameterName.equals("")) && !(ForLoopParameterName.equals(null)))
            			{
                    		if(strRuntimeVariable.replace("$", "").equalsIgnoreCase(ForLoopParameterName)){
                    			objVariableSheet.getRow(LoopValueIndex+1).getCell(columnIndex,NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
                    			strVariable =objVariableSheet.getRow(LoopValueIndex+1).getCell(columnIndex,NullAsBlank).getStringCellValue();
                    			break;
                    		}
                    		columnIndex++;
                    		varRow.getCell(columnIndex, NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
                        	ForLoopParameterName = varRow.getCell(columnIndex,NullAsBlank).getStringCellValue();
                        }
                    	//strVariable = SharedResource.dictForLoopVariables.get(strRuntimeVariable.replace("$", ""))[LoopValueIndex];
                    
                }
                else{
                    strVariable = strRuntimeVariable;
                }
            }
            catch (Exception e)
            {
                strVariable = "";
            }        	
        }
        //strVariable = handleStringForRuntimeVariable(strVariable);*/
        return strVariable;
    }//ends handleRuntimeVariable() method. 
	
	public String decryptPassword (String strPassword)
	{
		int intOrigStringLen = strPassword.length()/4;
		String Decrypt_password = strPassword.substring(0, intOrigStringLen);
		String OriginalString = "";
		for(int i=1; i<=intOrigStringLen;i++)
		{
			int intAsciiVal = Decrypt_password.substring(0, 1).charAt(0) - 5;
			OriginalString = OriginalString + String.valueOf((char)intAsciiVal);
			Decrypt_password = Decrypt_password.substring(1);
		}	
		return OriginalString;		
	}
	
	private static final String TASKLIST = "tasklist";	 
	public static boolean isProcessRunging(String serviceName) throws Exception {
	 Process p = Runtime.getRuntime().exec(TASKLIST);
	 BufferedReader reader = new BufferedReader(new InputStreamReader(
	   p.getInputStream()));
	 String line;
	 while ((line = reader.readLine()) != null) {
		 
		  System.out.println(line);
		  if (line.contains(serviceName)) {
			  return true;
		  }
		 }	 
	 return false;	 
	}
	
	private static final String KILL = "taskkill /IM ";
	public static void killProcess(String serviceName) throws Exception {
	
		Runtime.getRuntime().exec(KILL + serviceName);
	 
	 }
		
	public String getCellValue(HSSFRow currentRow, int Cellcounter)
	{
		String strValue = "";
		try {
			currentRow.getCell(Cellcounter, NullAsBlank).setCellType(Cell.CELL_TYPE_STRING);
			strValue = currentRow.getCell(Cellcounter, NullAsBlank).getStringCellValue();
		} catch (Exception e1) {					
			strValue = Double.toString(currentRow.getCell(Cellcounter, NullAsBlank).getNumericCellValue());
		}
    	//UtilityClass objUtility = new UtilityClass();
		strValue = handleStringForRuntimeVariable(strValue);
		return strValue;
	}
	
	public String ReadIniFile(String strIniFilePath, String strSection, String strVariable)
	{
		try{				 
			Ini ini = new Ini(new File(strIniFilePath));
			return ini.get(strSection, strVariable);	    	
	    	
	     }catch(Exception ex){
	            System.out.println(ex.getMessage());
	            return null;
	     }		
	}
	
	 public void DeleteFolder(final File folder) {
	      // check if folder file is a real folder
		 if (folder.exists())
		 {
	      if (folder.isDirectory()) {
	          File[] list = folder.listFiles();
	          if (list != null) {
	              for (int i = 0; i < list.length; i++) {
	                  File tmpF = list[i];
	                  if (tmpF.isDirectory()) {
	                	  DeleteFolder(tmpF);
	                  }
	                  tmpF.delete();
	              }
	          }
	          if (!folder.delete()) {
	            System.out.println("can't delete folder : " + folder);
	          }
	      }
	 	}
	  }
	 
	 /**
		 * This method is used to get property value from properties file
		 * @param sPropertyName
		 * @return property value
		 */
		public String getProperty(String sPropertyName) 
		{
			String currentDirectory = getProjectLocation();
			String sProperty = null;
			Properties properties = new Properties();
			InputStream inputFile = null;
			String sConfigLocation = currentDirectory + File.separator + "src"+ File.separator + "com" + File.separator + "nag" + File.separator + "kdf" + File.separator + "utility" + File.separator + "Properties.properties";
			try {//Start try
				inputFile = new FileInputStream(sConfigLocation);
				properties.load(inputFile);
				// Get the value from the properties file
				sProperty = properties.getProperty(sPropertyName);
				inputFile.close();
				//end try
			} catch(IOException e) {
				System.out.println("getProperty:::Some Exception has occured:::" + e.toString());
			}//end catch
			return sProperty;
		}//end function getProperty
	 
		
		public String getProjectLocation()
		{

			String currentDirectory;
			currentDirectory = System.getProperty("user.dir");
			return currentDirectory;

		}
		
		

		/**
		 * This method is used to set property value into properties file 
		 * @param sPropertyName
		 * @param sPropertyValue
		 */
		public void setProperty(String sPropertyName, String sPropertyValue) 
		{
			String currentDirectory = getProjectLocation();
			Properties properties = new Properties();
			InputStream inputFile = null;
			OutputStream outputFile = null;
			String sConfigLocation = currentDirectory + File.separator + "src"+ File.separator + "com" + File.separator + "nag" + File.separator + "kdf" + File.separator + "utility" + File.separator + "Properties.properties";
			try {//Start try
				inputFile = new FileInputStream(sConfigLocation);
				properties.load(inputFile);
				outputFile = new FileOutputStream(sConfigLocation);
				// Get the value from the properties file
				properties.setProperty(sPropertyName, sPropertyValue);
				properties.store(outputFile, "");
				inputFile.close();
				outputFile.close();
				//end try
			} catch (IOException e) {
				System.out.println("setProperty:::Some exception has occured:::" + e.toString());
			}//end catch
		}//end function setProperty
		
		
			/**
			 * This method is used to convert system directory path into relative path to create links in HTML report
			 * @param sFilePath
			 * @return relativePath
			 */
			public String  getRelativePath (String sFilePath)
			{

				String [] tempPathElement;
				String sRelativePath="./";
				String fileRelativePath = null;
				try{
					tempPathElement= sFilePath.split(File.separator+File.separator);
					for(int i=(tempPathElement.length-2);i<(tempPathElement.length);i++)
					{
						sRelativePath=sRelativePath+File.separator+tempPathElement[i];
					}
					fileRelativePath = new File(sRelativePath).toString();
				}catch(Exception e){
					e.printStackTrace();
				}
				return fileRelativePath;

			}
			
			
			
			public static String createFolder(String path) {
				/*Date tsStart = new Date();					
				String timeStamp=SharedResource.dateTimeFormat.format(tsStart);
				tsStart=null;
				path=path + timeStamp;
				path=path.replace(" ", "");
				*/
				
				File file = new File(path);
				boolean st=true;
				if (!file.exists()) {
		            st=file.mkdir();
		        }
				return path;
			}
		 

}



//this class is used as template to contain the properties like target and index.
class SelTestObject
{
	String strtarget = "";//for target value "e.g, "id=" ".
	int intIndex = 0; //for index value.
}


