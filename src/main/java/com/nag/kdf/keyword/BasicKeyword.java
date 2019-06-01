package com.nag.kdf.keyword;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import com.nag.kdf.timeStamp.TimeStamp;
import com.nag.kdf.utility.KDFEvent;
import com.nag.kdf.utility.KDFEventResult;
import com.nag.kdf.utility.SharedResource;
import com.nag.kdf.utility.SupportMethods;
import com.nag.kdf.utility.TestObject;
import com.nag.kdf.utility.UtilityClass;
import com.nag.kdf.webDriverSupport.KDFWebElement;

public class BasicKeyword {
	public static Properties prop = null;

	static {
		File file = new File("..//Framework//src//com//nag//kdf//configuration//TestConfig.properties");
		prop = new Properties();
		// load properties file
		try {
			FileInputStream fileInput = new FileInputStream(file);
			prop.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String strLocator = "", strAction = "", strParent = "", strChild = "", strParam1 = "", strParam2 = "",
			strParam3 = "", strParam4 = "", strParam5 = "", strParam6 = "";
	public int intIndex = 0;
	public WebElement objWebElement = null;
	public WebDriver objWebDriver = null;
	public List<WebElement> Arr_objWebElement = null;
	public String strBrowser = ""; // for handling screenshot capability incase
									// of chrome browser.
	public String strStatus = "";
	public String strTimeStamp = "";
	public String strActualRes = "";
	public String strLinkSnapshot = "";
	public String strStoreValue = "";

	public enum WindowIdentifier {
		Index, Name, Title, WrongChoice
	};

	public TestObject objTestObject = null;
	public JavascriptExecutor js = null;
	public KDFEvent objKDFEvent = null;

	// empty constructor
	public BasicKeyword() {
	}

	// Parameterized constructor.
	public BasicKeyword(WebDriver objWebdriver, String strLocator, int intIndex, String strAction, String strParent,
			String strChild, String strParam1, String strParam2, String strParam3, String strParam4, String strParam5,
			String strParam6) {
		this.objWebDriver = objWebdriver;
		this.strLocator = strLocator;
		this.intIndex = intIndex;
		this.strParent = strParent;
		this.strChild = strChild;
		this.strAction = strAction;
		this.strParam1 = strParam1;
		this.strParam2 = strParam2;
		this.strParam3 = strParam3;
		this.strParam4 = strParam4;
		this.strParam5 = strParam5;
		this.strParam6 = strParam6;
		this.js = (JavascriptExecutor) objWebdriver;
	}

	public BasicKeyword(WebDriver objWebdriver, KDFEvent objKDFEvent) {
		this.objWebDriver = objWebdriver;
		this.objKDFEvent = objKDFEvent;
		this.strParent = objKDFEvent.strParent;
		this.strChild = objKDFEvent.strChild;
		this.strAction = objKDFEvent.strAction;
		this.strParam1 = objKDFEvent.strParam1;
		this.strParam2 = objKDFEvent.strParam2;
		this.strParam3 = objKDFEvent.strParam3;
		this.strParam4 = objKDFEvent.strParam4;
		this.strParam5 = objKDFEvent.strParam5;
		this.strParam6 = objKDFEvent.strParam6;
		this.js = (JavascriptExecutor) objWebdriver;
		this.objKDFEvent = objKDFEvent;
		this.strLocator = objKDFEvent.objProperty;
	}

	public WebDriver getBrowserObject() {
		return objWebDriver;
	}

	public void performAction() {
		this.strTimeStamp = new TimeStamp().getTimeStamp("dd-MMMMM-yyyy-h:mm:ss a");

		if (strAction.startsWith("Flex_")) {
			if (!objTestObject.strParentProp.equals(objTestObject.strtarget))
				objTestObject.strtarget = objTestObject.strtarget.replace(objTestObject.strtarget.split("=")[0] + "=",
						"");
			objTestObject.strParentProp = objTestObject.strParentProp
					.replace(objTestObject.strParentProp.split("=")[0] + "=", "");
		}
		if (SharedResource.listMacroEvents.contains(strAction)) {
			RunMacro();
		} else {
			BasicKeyword objCmnLib = this;
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

	// #################################################Keywords####################################################################################

	// ###################################################################################
	// Function : ArithCalculate()
	// Description : This method is used to Perform Arithmatic Calculations.
	// Parameters : Input String[Arithmatic String e.g. 10,+,20] || Output
	// Variable
	// Author :
	// Date :
	// Modified by -
	// Modification -
	// ###################################################################################
	public KDFEventResult ArithCalculate() {
		String sArithmeticOpString = this.strParam1;
		String sValue = "";
		String Var1 = "";
		String Var2 = "";
		try {
			Var1 = sArithmeticOpString.split(",")[0]; // (',')[0];
			Var2 = sArithmeticOpString.split(",")[2];
		} catch (ArrayIndexOutOfBoundsException e) {
			SharedResource.dictRTVarCollection.put(this.strParam2, "");
			// this.strStatus = "FAIL";
			e.printStackTrace();
			// this.strActualRes ="Expression must be
			// [operand1],[operator],[operand2]. ";
			// return false;
			return new KDFEventResult("Expression must be [operand1],[operator],[operand2]. ", "FAIL");
		}
		String sOperator = sArithmeticOpString.split(",")[1];
		UtilityClass objUtil = new UtilityClass();
		Var1 = objUtil.handleRuntimeVariable(Var1);
		Var2 = objUtil.handleRuntimeVariable(Var2);
		SupportMethods objSupp = new SupportMethods();
		if (sOperator.equals("+"))
			sValue = objSupp.Add(Var1, Var2);
		else if (sOperator.equals("-"))
			sValue = objSupp.Subtract(Var1, Var2);
		else if (sOperator.equals("<"))
			sValue = objSupp.LesserThan(Var1, Var2).toUpperCase();
		else if (sOperator.equals(">"))
			sValue = objSupp.GreaterThan(Var1, Var2).toUpperCase();
		else if (sOperator.equals("="))
			sValue = objSupp.Equal(Var1, Var2).toUpperCase();
		else if (sOperator.equals("&"))
			sValue = objSupp.ANDOperation(Var1, Var2).toUpperCase();
		else if (sOperator.equals("<="))
			sValue = objSupp.LesserThanEqualTo(Var1, Var2).toUpperCase();
		else if (sOperator.equals(">="))
			sValue = objSupp.GreaterThanEqualTo(Var1, Var2).toUpperCase();
		else if (sOperator.equals("*"))
			sValue = objSupp.Multiply(Var1, Var2).toUpperCase();
		else if (sOperator.equals("/"))
			sValue = objSupp.Divide(Var1, Var2).toUpperCase();

		else {
			sValue = "Error";
			this.strActualRes = sOperator + "operator is not supported as yet";
		}
		if (sValue.toUpperCase().contains("EXCEPTION") || sValue.toUpperCase().contains("ERRO")) {
			return new KDFEventResult(sValue, "FAIL");
		} else {
			SharedResource.dictRTVarCollection.put(this.strParam2, sValue);
			return new KDFEventResult(sValue, "PASS");
		}
	}

	// ###################################################################################
	// Function : CompareValues()
	// Description : This method is used to Compare two Values
	// Parameters : Input Variable/String 1 || Input Variable/String 2 ||
	// Expected Result [True/False]
	// Author :
	// Date :
	// Modified by -
	// Modification -

	public KDFEventResult CompareValues() {
		String Var1 = this.strParam1;
		String sActualMatch = "";
		String Var2 = this.strParam2;
		String sExpectedMatch = strParam3.toUpperCase();

		if (Var1.toString().trim().equalsIgnoreCase(Var2.toString().trim())) {
			sActualMatch = "TRUE";
		} else {
			sActualMatch = "FALSE";
		}
		if ((sActualMatch.equals("FALSE")) & (sExpectedMatch.equals("TRUE"))) {
			this.strStatus = "FAIL";
			this.strActualRes = Var1 + " does not match with " + Var2;
		} else if ((sActualMatch.equals("TRUE")) & (sExpectedMatch.equals("FALSE"))) {
			this.strStatus = "FAIL";
			this.strActualRes = Var1 + " match with " + Var2;
		} else if ((sActualMatch.equals("FALSE")) & (sExpectedMatch.equals("FALSE"))) {
			this.strStatus = "PASS";
			this.strActualRes = Var1 + " does not match with " + Var2;
		} else if ((sActualMatch.equals("TRUE")) & (sExpectedMatch.equals("TRUE"))) {
			this.strStatus = "PASS";
			this.strActualRes = Var1 + " match with " + Var2;
		} else {
			this.strStatus = "FAIL";
			this.strActualRes = "";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	// ###################################################################################
	// Function : StringReplace()
	// Description : This method is used to Replace values in String
	// Parameters : Value || Substring || ReplaceWith || OutputVariable
	// Author :
	// Date :
	// Modified by -
	// Modification -

	public KDFEventResult StringReplace() {
		try {
			String strOriginal = strParent;
			String strToReplace = strChild;
			String strReplaceWith = strParam1;
			
			String strResultString = strOriginal.replace(strToReplace, strReplaceWith);
			SharedResource.dictRTVarCollection.put(strParam2, strResultString);
			this.strStatus = "PASS";
			this.strActualRes = strParam2 + "=" +strResultString;
			
		} catch (Exception e) {
			this.strStatus="FAIL";
			this.strActualRes = e.getStackTrace().toString();
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	// ###################################################################################
	// Function : StringTrim()
	// Description : This method is used to trim all spaces in String
	// Parameters : InputString || OutputVariable
	// Author :
	// Date :
	// Modified by -
	// Modification -

	public KDFEventResult StringTrim() {
		try {
			String strOriginal = strParam1;
			strOriginal = strOriginal.trim();
			SharedResource.dictRTVarCollection.put(strParam2, strOriginal);
			this.strStatus = "PASS";
			this.strActualRes = strOriginal;
		} catch (Exception e) {
			this.strStatus = "FAIL";
			this.strActualRes = e.getStackTrace().toString();
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}
	
	

	public KDFEventResult Logic_IF() {
		Double val1, val2;
		String str1 = this.strParam1;
		String str2 = this.strParam2;
		String str3 = this.strParam3;
		String expression = "";
		int result = 0;

		// Set Logic If Depth
		SharedResource.LogicIfDepth = SharedResource.LogicIfDepth + 1;

		if (str3.equals("0") || str3.equals("1") || str3.equals("-1")) {

			// Existing Logic If Code
			try {
				val1 = Double.parseDouble(str1);
				val2 = Double.parseDouble(str2);
				if (((str3.equals("0")) && (val1.compareTo(val2) == 0)) || ((str3.equals("-1")) && (val1 > val2))
						|| ((str3.equals("1")) && (val1 < val2))) {
					// SharedResource.Logic_If = "TRUE";
					this.strStatus = "PASS";
					this.strActualRes = "PASS";
					// System.out.println ("TRUE");
					// return true;
				} else {
					// SharedResource.Logic_If = "FALSE";
					this.strStatus = "PASS";
					this.strActualRes = "FAIL";
					// System.out.println ("FALSE");
					// return true;
				}
			} catch (Exception e) {
				result = str1.toUpperCase().compareTo(str2.toUpperCase());
				String sResult = Integer.toString(result);
				if ((sResult.toUpperCase()).equals((strParam3).toUpperCase())) {
					// SharedResource.Logic_If = "TRUE";
					this.strStatus = "PASS";
					this.strActualRes = "PASS";
					// return true;
				} else {
					// SharedResource.Logic_If = "FALSE";
					this.strStatus = "PASS";
					this.strActualRes = "FAIL";
					// return true;
				}
			}

		} else {
			// New Logic If -Handel AND,OR Operator also
			String[] strArray = str1.split(" ");
			int noOfItem = strArray.length;

			for (int i = 0; i < noOfItem; i++) {
				if (strArray[i].startsWith("^")) {
					strArray[i] = strArray[i].replace("^", "");
					String varValue = SharedResource.dictRTVarCollection.get(strArray[i]);
					expression = expression + " " + varValue;
				} else {
					expression = expression + " " + strArray[i];
				}
			}
			expression = expression.replace("=", "==");
			expression = expression.replace("<>", "!=");
			expression = expression.replace(" AND ", "&&");
			expression = expression.replace(" OR ", "||");

			String RefinedExpression = CreateLogicIFExpression(expression);

			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("js");

			try {
				Object expressionOutput = engine.eval(RefinedExpression);
				if ((String.valueOf(expressionOutput)) == "true") {
					// SharedResource.Logic_If = "TRUE";
					this.strStatus = "PASS";
					this.strActualRes = "PASS";
					// System.out.println ("TRUE");
					// return true;
				} else {
					// SharedResource.Logic_If = "FALSE";
					this.strStatus = "PASS";
					this.strActualRes = "FAIL";
					// System.out.println ("FALSE") ;
					// return true;
				}
			} catch (Exception e) {
				System.out.println("Exception occured in logic_If");
				// SharedResource.Logic_If = "FALSE";
				this.strStatus = "PASS";
				this.strActualRes = "FAIL";
				// return true;
			}
		}

		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	public KDFEventResult ELSE() {
		this.strStatus = "Pass";
		if (SharedResource.Logic_If.equalsIgnoreCase("PASS") || SharedResource.Else_If.equalsIgnoreCase("PASS")) {
			SharedResource.Else = "FAIL";
			this.strActualRes = "Else Statement to be Skipped";

			// return new KDFEventResult(this.strActualRes, this.strStatus);
		} else {
			SharedResource.Else = "PASS";
			this.strActualRes = "Else Statement to be executed";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	public KDFEventResult END_IF() {
		this.strActualRes = "Conditional If Exits";
		this.strStatus = "Pass";
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	public KDFEventResult ELSE_IF() {
		if (SharedResource.Logic_If.equalsIgnoreCase("PASS")) {
			SharedResource.Else_If = "FAIL";
			this.strActualRes = "Skipped";
			this.strStatus = "Pass";
			return new KDFEventResult(this.strActualRes, this.strStatus);
		} else {
			Double val1, val2;
			String str1 = this.strParam1;
			String str2 = this.strParam2;
			String str3 = this.strParam3;
			String expression = "";
			int result = 0;

			// Set Logic If Depth
			SharedResource.LogicIfDepth = SharedResource.LogicIfDepth + 1;

			if (str3.equals("0") || str3.equals("1") || str3.equals("-1")) {

				// Existing Logic If Code
				try {
					val1 = Double.parseDouble(str1);
					val2 = Double.parseDouble(str2);
					if (((str3.equals("0")) && (val1.compareTo(val2) == 0)) || ((str3.equals("-1")) && (val1 > val2))
							|| ((str3.equals("1")) && (val1 < val2))) {
						// SharedResource.Logic_If = "TRUE";
						this.strStatus = "PASS";
						this.strActualRes = "PASS";
					} else {
						// SharedResource.Logic_If = "FALSE";
						this.strStatus = "PASS";
						this.strActualRes = "FAIL";
					}
				} catch (Exception e) {
					result = str1.toUpperCase().compareTo(str2.toUpperCase());
					String sResult = Integer.toString(result);
					if ((sResult.toUpperCase()).equals((strParam3).toUpperCase())) {
						this.strStatus = "PASS";
						this.strActualRes = "PASS";
					} else {
						this.strStatus = "PASS";
						this.strActualRes = "FAIL";
					}
				}

			} else {
				String[] strArray = str1.split(" ");
				int noOfItem = strArray.length;

				for (int i = 0; i < noOfItem; i++) {
					if (strArray[i].startsWith("^")) {
						strArray[i] = strArray[i].replace("^", "");
						String varValue = SharedResource.dictRTVarCollection.get(strArray[i]);
						expression = expression + " " + varValue;
					} else {
						expression = expression + " " + strArray[i];
					}
				}
				expression = expression.replace("=", "==");
				expression = expression.replace("<>", "!=");
				expression = expression.replace(" AND ", "&&");
				expression = expression.replace(" OR ", "||");

				String RefinedExpression = CreateLogicIFExpression(expression);

				ScriptEngineManager manager = new ScriptEngineManager();
				ScriptEngine engine = manager.getEngineByName("js");

				try {
					Object expressionOutput = engine.eval(RefinedExpression);
					if ((String.valueOf(expressionOutput)) == "true") {
						this.strStatus = "PASS";
						this.strActualRes = "PASS";
					} else {
						this.strStatus = "PASS";
						this.strActualRes = "FAIL";
					}
				} catch (Exception e) {
					System.out.println("Exception occured in logic_If");
					this.strStatus = "PASS";
					this.strActualRes = "FAIL";
				}
			}
		}
		SharedResource.Else_If = this.strActualRes;
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	public String CreateLogicIFExpression(String coreExpression) {
		String expression = coreExpression;
		String partialExpression = "";
		String finalExpression = "";
		String finalExpression_OR = "";
		String[] expSubArray;

		String[] expArray = expression.split("&&");

		if (expArray.length != 1) {
			for (int i = 0; i < expArray.length; i++) {
				expSubArray = expArray[i].split("\\|\\|");
				finalExpression_OR = "";
				if (expSubArray.length != 1) {
					for (int j = 0; j < expSubArray.length; j++) {
						partialExpression = this.convertToStringExp(expSubArray[j]);
						if ((j + 1) == expSubArray.length) {
							finalExpression_OR = finalExpression_OR + partialExpression;
						} else {
							finalExpression_OR = finalExpression_OR + partialExpression + "||";
						}
					}
				}
				if (finalExpression_OR == "") {
					partialExpression = this.convertToStringExp(expArray[i]);
					if ((i + 1) == expArray.length) {
						finalExpression_OR = finalExpression_OR + partialExpression;
					} else {
						finalExpression_OR = finalExpression_OR + partialExpression + "&&";
					}
				}

				if (i == 0) {
					finalExpression = finalExpression_OR;
				} else if (finalExpression.endsWith("&&")) {
					finalExpression = finalExpression + finalExpression_OR;
				} else {
					finalExpression = finalExpression + "&&" + finalExpression_OR;
				}
			}
		} else if (expression.contains("||")) {
			expSubArray = expression.split("\\|\\|");
			finalExpression_OR = "";
			if (expSubArray.length != 1) {
				for (int j = 0; j < expSubArray.length; j++) {
					partialExpression = this.convertToStringExp(expSubArray[j]);
					if ((j + 1) == expSubArray.length) {
						finalExpression_OR = finalExpression_OR + partialExpression;
					} else {
						finalExpression_OR = finalExpression_OR + partialExpression + "||";
					}
				}
			}
			finalExpression = finalExpression_OR;
		} else {
			finalExpression = this.convertToStringExp(expression);
		}

		System.out.println(finalExpression);
		return finalExpression;
	}

	public String convertToStringExp(String CorePartialExp) {

		String partialExpression = "";

		if ((CorePartialExp.contains(">")) || (CorePartialExp.contains("<"))) {
			// Numeric operand
			partialExpression = CorePartialExp;
		} else {
			String Operator;
			// Convert operand into string format
			if (CorePartialExp.contains("==")) {
				Operator = "==";
			} else {
				Operator = "!=";
			}
			String[] Operand = CorePartialExp.split(Operator);
			String Operand1 = Operand[0];
			String Operand2 = "";
			if (Operand.length > 1) {
				Operand2 = Operand[1];
			}

			if (Operand1.trim().startsWith("(")) {
				Operand1 = "'" + Operand1.replace("(", "").trim() + "'";
				int noOfBrackets = StringUtils.countMatches(Operand[0], "(");
				for (int k = 1; k <= noOfBrackets; k++) {
					Operand1 = "(" + Operand1;
				}
				Operand1 = Operand1.toUpperCase();
			} else {
				Operand1 = "'" + Operand1.trim() + "'";
				Operand1 = Operand1.toUpperCase();
			}

			if (Operand2.trim().endsWith(")")) {
				Operand2 = "'" + Operand2.replace(")", "").trim() + "'";
				int noOfBrackets = StringUtils.countMatches(Operand[1], ")");
				for (int k = 1; k <= noOfBrackets; k++) {
					Operand2 = Operand2.trim() + ")";
				}
				Operand2 = Operand2.toUpperCase();
			} else {
				Operand2 = "'" + Operand2.trim() + "'";
				Operand2 = Operand2.toUpperCase();
			}

			partialExpression = Operand1 + Operator + Operand2;
		}

		return partialExpression;
	}

	

	// Handling Select case

	public KDFEventResult SelectCase() {
		SharedResource.SelectCase = objKDFEvent.strParam1;
		this.strStatus = "Pass";
		this.strActualRes = "Select Statement Processed for expression : " + objKDFEvent.strParam1;
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	public KDFEventResult CaseValue() {
		if (SharedResource.SelectCase.equalsIgnoreCase(objKDFEvent.strParam1)
				&& SharedResource.SelectCaseStatus.equalsIgnoreCase("")) {
			SharedResource.SelectCaseStatus = "Done";
			this.strStatus = "Pass";
			this.strActualRes = "Case value (" + objKDFEvent.strParam1 + ") match with Test Expression ("
					+ SharedResource.SelectCase + ")";
		} else // if (SharedResource.SelectCaseStatus.equalsIgnoreCase("Done"))
		{
			this.strStatus = "Pass";
			this.strActualRes = "Select Case Skipped";
		}

		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	public KDFEventResult EndSelect() {
		SharedResource.SelectCaseStatus = "";
		this.strStatus = "Pass";
		this.strActualRes = "Exit Select Case";

		return new KDFEventResult(this.strActualRes, this.strStatus);

	}


	public KDFEventResult SysDate() {
		try {
			String strDateFormat = strParam1.trim().equals("") ? null : this.strParam1;
			String strOutputVar = this.strParam2;
			Date date = new Date();
			Format formatter1 = null;
			try {
				formatter1 = new SimpleDateFormat(strDateFormat);
			} catch (NullPointerException e) {
				this.strStatus = "FAIL";
				this.strActualRes = "Param1 is Null/Empty. Please enter proper date format in Param1. e.g, MM-dd-yyyy";
			} catch (IllegalArgumentException e) {
				this.strStatus = "FAIL";
				this.strActualRes = "Param1 is not a valid date format. Please enter proper date format in Param1. e.g, MM-dd-yyyy";
			}

			String strOutputDate = formatter1.format(date);
			this.strStatus = "PASS";
			this.strActualRes = strOutputDate;
			SharedResource.dictRTVarCollection.put(strOutputVar, strOutputDate);
		} catch (Exception e) {
			this.strStatus = "FAIL";
			this.strActualRes = "Exception occured.\n" + e.getStackTrace();
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	// ###################################################################################
	// Function : SplitString()
	// Description : This method is used to Replace values in String
	// Parameters : Input String || Delimeter || ItemNum || OutputVariable
	// Author : Sachin Ahuja
	// Date : 10 Aug 2016
	// Modified by -
	// Modification -

	public KDFEventResult SplitString() {
		try {
			String strOriginal = this.strParent;
			String strDelimiter = this.strChild;
			String strItemNum = this.strParam1;
			String strOutputVar = this.strParam2;
			String[] arrOriginalString = strOriginal.split(strDelimiter);
			int intItemNum = 0;
			try {
				intItemNum = Integer.valueOf(strItemNum);
			} catch (NumberFormatException e) {
				this.strStatus = "FAIL";
				this.strActualRes = "Please enter ItemNum in numeral form";
			}
			String strResultString = "";
			try {
				String strResultString1 = arrOriginalString[intItemNum - 1];
				strResultString=strResultString1.trim();
			} catch (ArrayIndexOutOfBoundsException e) {
				this.strStatus = "FAIL";
				this.strActualRes = "ItemNum is out of bound";
			}

			SharedResource.dictRTVarCollection.put(strOutputVar, strResultString);
			this.strStatus = "PASS";
			this.strActualRes = intItemNum + " number item is \"" + strResultString + "\"";
		} catch (Exception e) {
			this.strStatus = "FAILS";
			this.strActualRes = "Exception occured \n " + e.getStackTrace();
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	// ###################################################################################
	// Function : ConcatenateString()
	// Description : This method is used to Replace values in String
	// Parameters : Input String1 || Input String 2 || OutputVariable
	// Author : Sachin Ahuja
	// Date : 10 Aug 2016
	// Modified by -
	// Modification

	public KDFEventResult ConcatenateString() {
		String strFirstString = this.strParam1;
		String strSecondString = this.strParam2;
		String strResultString = "";
		String strOutput = strParam3;
		try {
			strResultString = strFirstString + strSecondString;

			SharedResource.dictRTVarCollection.put(strOutput, strResultString);
			this.strStatus = "PASS";
			this.strActualRes = strResultString;
		} catch (Exception e) {
			SharedResource.dictRTVarCollection.put(strOutput, "FALSE");
			this.strStatus = "FAIL";
			this.strActualRes = "Exception occurred";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}
	

	// ###################################################################################
	// Function : EvaluateString()
	// Description : This method is used to Replace values in String
	// Parameters : Input String1 || OutputVariable
	// Author : Sachin Ahuja
	// Date : 3 Aug 2017
	// Modified by -
	// Modification   'http://' + user + ':' +  pass + '@' +url

	public KDFEventResult EvaluateString() {
		String strFirstString = this.strParam1;
		String strOutput = this.strParam2;
		String strResultString = "";
		
		try {
			
			String tempStr[]=strFirstString.split("+");
			for (int i=0; i<tempStr.length;i++) {
				if(tempStr[i].contains("^"))
					strResultString=strResultString+SharedResource.dictRTVarCollection.get(tempStr[i].replace("^","").trim());
				else
					strResultString=strResultString+tempStr[i].trim();
			}
			
			//strResultString = strFirstString;

			SharedResource.dictRTVarCollection.put(strOutput, strResultString);
			this.strStatus = "PASS";
			this.strActualRes = strResultString;
		} catch (Exception e) {
			SharedResource.dictRTVarCollection.put(strOutput, "FALSE");
			this.strStatus = "FAIL";
			this.strActualRes = "Exception occurred";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}
	
	



	// This is to run macro of the Excel file.
	public KDFEventResult RunMacro() {
		String strBatchFile = this.strParent;
		String strMacroName = this.strChild;
		String strP1 = this.strParam1;
		String strP2 = this.strParam2;
		String sVBSPath = "";
		String sCommand = "";
		String ArgString = "";
		// ArgString = " \"" + strMacroName + "\" \"" + strP1 + "\" \"" + strP2
		// + "\"";
		ArgString = " \"" + strMacroName + "\" \"" + strP1 + "\" \"" + strP2 + "\" \"" + SharedResource.strExcelPath
				+ "\"";

		try {
			sVBSPath = SharedResource.strRootFolder + "\\Batch\\";
			sCommand = "cscript.exe \"" + sVBSPath + strBatchFile + "\"" + ArgString;
			// System.out.print(sCommand);
			Process p = Runtime.getRuntime().exec((sCommand));
			p.waitFor();
			int intExitCode = p.exitValue();
			p.destroy();
			if (intExitCode == 0) {
				this.strStatus = "PASS";
				this.strActualRes = "VBScript file is successful";
			} else {
				this.strStatus = "FAIL";
				this.strActualRes = "VBScript file is not successfull";
			}
		} catch (Exception err) {
			this.strActualRes = err.getStackTrace().toString();
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}
	//
	public KDFEventResult ModalDialogButtonClick() {
		String[] dialog;
		// String strBatchFile = "";
		// String strUID = this.strParent;
		// String strPwd = this.strChild;
		String strModalDialogExeName = this.strParam1;
		String strButtonText = this.strParam3;
		String strWindowTitle = this.strParam2;
		try {
			strModalDialogExeName = SharedResource.strRootFolder + "\\Batch\\" + strModalDialogExeName;
			dialog = new String[] { strModalDialogExeName, strWindowTitle, strButtonText };
			Process p = Runtime.getRuntime().exec(dialog);
			p.waitFor();
			int intExitCode = p.exitValue();
			p.destroy();
			if (intExitCode == 0) {
				this.strStatus = "PASS";
				this.strActualRes = "Modal Dailog button is successfully Clicked";
			} else {
				this.strStatus = "FAIL";
				this.strActualRes = "Unable to Click Modal Dialog Button";
			}
		} catch (Exception err) {
			this.strActualRes = err.getStackTrace().toString();
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}


	public void highlightElement(WebDriver driver, WebElement element) {
		for (int i = 0; i < 2; i++) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
					"color: yellow; border: 2px solid yellow;");
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
		}
	}

	public KDFEventResult MouseOver() {
		try {
			this.objWebElement = new KDFWebElement(objWebDriver).getTargetObject(this.strLocator, this.intIndex);
			// this.objWebElement.click();
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
			JavascriptExecutor javascript = (JavascriptExecutor) this.objWebDriver;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			javascript.executeScript(javaScript, this.objWebElement);
			this.strActualRes = "MouseOver Event is successfull";
			this.strStatus = "PASS";
		} catch (Exception e) {
			e.printStackTrace();
			this.strActualRes = "Exception Occur in MouseOver";
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	public KDFEventResult KDFTimer() {
		String sValue = String.valueOf((double) (System.currentTimeMillis()));
		SharedResource.dictRTVarCollection.put(strParam1, sValue);
		this.strStatus = "PASS";
		this.strActualRes = sValue;
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	public KDFEventResult ExtractQuarter_FromDate() {
		String strCurrentDateFormate = this.strParam1;
		String strDate = this.strParam2;
		// String strOutPut = this.strParam3;

		SimpleDateFormat sdf = new SimpleDateFormat(strCurrentDateFormate);
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(strDate));
			int month = cal.get(cal.MONTH);
			int quarter = (month / 3) + 1;

			SharedResource.dictRTVarCollection.put(strParam3, String.valueOf(quarter));

			this.strStatus = "PASS";
			this.strActualRes = "Quarter of input date is - " + quarter;
		} catch (Exception e) {
			this.strStatus = "FAIL";
			this.strActualRes = "Exception occured.\n" + e.getStackTrace();
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);

	}

	public KDFEventResult MarkExecutionStatus() {
		String str1 = this.strParam1;
		String str2 = this.strParam2;

		if (str1.equalsIgnoreCase("PASS")) {
			this.strStatus = "PASS";
		} else {
			this.strStatus = "FAIL";
		}
		this.strActualRes = str2;

		// enterDetailsForImprovedReporting("Check Actual Result",str2);
		SharedResource.isVerificationKeyword = true;
		SharedResource.expectedResult = "Check Actual Result";
		SharedResource.actualResult = str2;
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}



	public KDFEventResult SetObjectSyncronizationTimeOut() {
		try {
			long objectSyncTime_Sec = Long.parseLong(this.strParam1);
			objWebDriver.manage().timeouts().implicitlyWait(objectSyncTime_Sec, TimeUnit.SECONDS);
			this.strStatus = "PASS";
			this.strActualRes = this.strParam1 + " seconds set as Object Sync time.";
		} catch (Exception e) {
			this.strStatus = "FAIL";
			this.strActualRes = "Exception occured.\n" + e.getStackTrace();
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	// ************************************************************************
		
	

	public List<WebElement> getChildObjects(WebElement targetObject) {
		List<WebElement> ChildObjectList = null;
		try {
			// this.objWebElement = new
			// KDFWebElement(objWebDriver).getTargetObject(this.strLocator,this.intIndex);
			WrapsDriver we = (WrapsDriver) targetObject;
			if (!(we == null)) {
				WebDriver driver = we.getWrappedDriver();
				JavascriptExecutor javascript = (JavascriptExecutor) driver;
				ChildObjectList = (List<WebElement>) javascript.executeScript("return arguments[0].childNodes;",targetObject);
				// int listLength = (integer) javascript.executeScript("return
				// arguments[0].length;", ChildObjectList);

			}
			// new
			// Actions(this.objWebDriver).click(this.objWebElement).perform();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// Filter Unwanted Items
		for (int i = 0; i < ChildObjectList.size(); i++) {
			try {
				ChildObjectList.get(i).getTagName();
			} catch (Exception ex) {
				ChildObjectList.remove(i);
				i = i - 1;
			}
		}
		return ChildObjectList;
	}


	// **********************************************************************************

	// **********************************************************************************
	
	public KDFEventResult AssignValueToVariable() {

		try {
			SharedResource.dictRTVarCollection.put(this.strParam1, this.strParam2);
			this.strActualRes = this.strParam2 + " value has assign to variable - " + this.strParam1;
			this.strStatus = "PASS";
		} catch (Exception ex) {
			this.strActualRes = "Exception Occured";
			this.strStatus = "FAIL";
			ex.printStackTrace();
		}

		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

	public KDFEventResult DeleteFile() {
		File f = new File(this.strParam1);
		if (f.exists()) {
			try {
				FileUtils.copyFile(f, new File(f.getParent() + "\\Copy_of_" + f.getName()));
				f.delete();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				this.strStatus = "FAIL";
				this.strActualRes = "Exception Occured";
				e.printStackTrace();
			}
			this.strStatus = "PASS";
			this.strActualRes = "File Deleted Successfully";
		} else {
			this.strStatus = "PASS";
			this.strActualRes = "File does not exist";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}

		


	

	// ##################################################################################
	// Function : GetRandomNum()
	// Description : This method provide user with a random num
	// Parameters : Min and Max value
	// Author : Sachin Ahuja
	// Date : 10 Aug 2016
	// Modified by -
	// Modification -

	public KDFEventResult GetRandomNum() {
		try {

			int Min = Integer.valueOf(this.strParent);
			int Max = Integer.valueOf(this.strChild);
			int randomNum = Min + (int) (Math.random() * ((Max - Min) + 1));
			SharedResource.dictRTVarCollection.put(strParam1, Integer.toString(randomNum));
			this.strActualRes = "Random number is generated" + randomNum + "Seconds";
			this.strStatus = "PASS";
		} catch (Exception e) {
			this.strActualRes = "Exception Occured - " + e.getMessage();
			this.strStatus = "FAIL";
		}
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}


}
