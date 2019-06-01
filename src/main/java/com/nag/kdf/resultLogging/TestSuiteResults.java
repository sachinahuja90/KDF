package com.nag.kdf.resultLogging;

import java.util.Date;
import java.util.HashMap;

import com.nag.kdf.utility.SharedResource;

public class TestSuiteResults {
	

	String TestSuiteName;
	String TestName;
	int TotalTestCases;
	int TestCasePass;
	int TestCaseFail;
	int TestCaseSkipped;
	
	String ExecutionDate, StartTime, EndTime,ExecutionTime;
	int TestSuitePassPercentage,TestSuiteFailPercentage;
	
	String testSuiteStatus;
	String ResultFileLink;
	HashMap<Integer,TestCaseResults> TestCaseResultHashMap;//
	
	
	public TestSuiteResults(){
		
	}
	public TestSuiteResults(Date startDate, Date endDate,String testSuite,String TestName,int numPass, int numFail, String ResultFileLink, HashMap<Integer,TestCaseResults> tc ){
		this.ExecutionDate=SharedResource.dateFormat.format(startDate);
		this.StartTime=SharedResource.timeFormat.format(startDate);
		this.EndTime=SharedResource.timeFormat.format(endDate);
		long diff = endDate.getTime() - startDate.getTime();
		this.ExecutionTime=(diff/1000)+"";
		
		this.TestSuiteName=testSuite;
		this.TestName=TestName;
		this.TotalTestCases=numPass+numFail;
		this.TestCasePass=numPass;
		this.TestCaseFail=numFail;
		
		this.TestSuitePassPercentage=(TestCasePass*100/TotalTestCases);
		this.TestSuiteFailPercentage=100-this.TestSuitePassPercentage;
		this.ResultFileLink=ResultFileLink;
		//this.TestCaseSkipped=numSkipped;
		if (numFail>0) {
			this.testSuiteStatus="Fail";
			SharedResource.strTestSuiteFailCount++;
		}
		else {
			this.testSuiteStatus="Pass";
			SharedResource.strTestSuitePassCount++;
		}
		this.TestCaseResultHashMap=tc; // Pass TestCase Name and TestCase Result Object
	}
}
