package com.nag.kdf.utility;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
//import org.openqa.selenium.chrome.ChromeDriverService;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;

// Description : This class is defined for shared resources.
public class SharedResource {
	public static String strContinueExecution = "TRUE";
	public static String strBrowser = "";
	public static Hashtable<String,String> dictRTVarCollection = new Hashtable<String, String>();
	public static Hashtable<String,String> dictLoopVarCollection = new Hashtable<String, String>();
	public static LinkedHashMap<String,String> dictVarCollection_FAMS = new LinkedHashMap<String, String>();
	public static Hashtable<String,String> dictResuableParameter = new Hashtable<String, String>();
	public static Hashtable<String, Integer> dictReusableComponentCallCounter = new Hashtable<String, Integer>();
	public static Hashtable<String, String[]> dictForLoopVariables = new Hashtable<String, String[]>();
	public static String strRootFolder = System.getProperty("user.dir");
	public static String Logic_If = "";
	public static String Else_If = "";
	public static String Else = "";
	public static String SelectCase = "";
	public static String SelectCaseStatus = "";
	public static String For_Loop_Logic_If = "";
	public static String Comp_Logic_If = "";
	public static String strExcelPath = "";
	public static List<String> listMacroEvents = Arrays.asList(new String[]{"RefreshVariables"});
	public static Hashtable<String, String> dictOCTTestData = new Hashtable<String, String>();
	
	//this variable is used to track which part is to be skipped from the execution. Valid Values(Testcase, step, Execution/"")	
	public static String strSkipExecution = ""; //	
	//public static String strCompoLogic_If = "";	
	//public static ChromeDriverService service;	
	public static Boolean isVerificationKeyword = false;
    public static String expectedResult = "" ;
    public static String actualResult = "" ;
    public static String strORPath = "";
    public static int startLoop_BeginLoop=0;
    public static int endLoop_BeginLoop=0;
    public static int currentIterationNumber=-1;
    public static int LogicIfDepth = 0 ;
    public static Hashtable<Integer, String> dictLogcIfStatusCollection = new Hashtable<Integer, String>();
    public static HSSFWorkbook workbook;
    public static String ResultFolderPath;
    public static String SuiteResultFolderPath;
    public static String TestCaseResultFolderPath,CombinedKeywordScreenShotPath;
    public static String TestCaseScreenShotPath;
    public static String CombinedKeywordResultFolderPath; 
    public static DateFormat dateTimeFormat= new SimpleDateFormat("dd-MMMMM-yyyy-h:mm:ss a");
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    public static DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    public static int strTestSuitePassCount,strTestSuiteFailCount;
	
}
