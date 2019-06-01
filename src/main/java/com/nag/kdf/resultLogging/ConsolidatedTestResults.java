package com.nag.kdf.resultLogging;

import java.util.Date;
import java.util.HashMap;

import com.nag.kdf.utility.SharedResource;

public class ConsolidatedTestResults {
	

	String TestName;
	int TotalTestSuite;
	int TestSuitePass;
	int TestSuiteFail;
	int TestSuiteSkipped;
	String ExecutionDate, StartTime, EndTime,ExecutionTime;
	int TestPassPercentage,TestFailPercentage;
	String ConsolidatedTestStatus;
	String ResultFileLink;
	
	HashMap<Integer,TestSuiteResults> TestSuiteResultHashMap;//
	
	
	public ConsolidatedTestResults(){
		
	}
	public ConsolidatedTestResults(Date startDate, Date endDate,String testName,int TestSuitePass, int TestSuiteFail, HashMap<Integer,TestSuiteResults> ts ){
		try {
		this.ExecutionDate=SharedResource.dateFormat.format(startDate);
		this.StartTime=SharedResource.timeFormat.format(startDate);
		this.EndTime=SharedResource.timeFormat.format(endDate);
		long diff = endDate.getTime() - startDate.getTime();
		this.ExecutionTime=(diff/1000)+"";
		
		this.TestName=testName;		
		this.TotalTestSuite=TestSuitePass+TestSuiteFail;
		this.TestSuitePass=TestSuitePass;
		this.TestSuiteFail=TestSuiteFail;
		this.TestPassPercentage=(TestSuitePass*100/TotalTestSuite);
		this.TestFailPercentage=100-this.TestPassPercentage;
		//this.TestCaseSkipped=numSkipped;
		if (TestSuiteFail>0) 
			this.ConsolidatedTestStatus="Fail";
		else
			this.ConsolidatedTestStatus="Pass";
		
		this.TestSuiteResultHashMap=ts; // Pass TestCase Name and TestCase Result Object
		
		}catch (Exception e) {
			System.out.print(e.toString());
		}
	}
}



