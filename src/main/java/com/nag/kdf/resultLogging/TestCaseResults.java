package com.nag.kdf.resultLogging;

import java.util.Date;
import java.util.HashMap;

import com.nag.kdf.utility.SharedResource;

public class TestCaseResults {
	
	String ExecutionDate, StartTime, EndTime,ExecutionTime;
	String TestSuiteName;
	String TestCaseName;
	String TestCaseStatus;
	int TotalTestSteps;
	int TestStepsPass, TestStepsFail;
	int TestStepPassPercentage,TestStepFailPercentage;
	String ResultFileLink;
	HashMap<Integer,TestStepResults> TestStepMap;//
	
	public TestCaseResults(){
		
	}
	
	public TestCaseResults(Date stDt,Date endDt,String tsName,String tcName,int stepPass,int stepFail,HashMap<Integer,TestStepResults> ts){
		
		this.ExecutionDate=SharedResource.dateFormat.format(stDt);
		this.StartTime=SharedResource.timeFormat.format(stDt);
		this.EndTime=SharedResource.timeFormat.format(endDt);
		long diff = endDt.getTime() - stDt.getTime();
		this.ExecutionTime=(diff/1000)+"";
		this.TestSuiteName=tsName;
		this.TestCaseName=tcName;
		this.TotalTestSteps=stepPass+stepFail;
		this.TestStepsPass=stepPass;
		this.TestStepsFail=stepFail;
		if (stepFail>0) 
			this.TestCaseStatus="Fail";
		else 
			this.TestCaseStatus="Pass";
		
		this.ResultFileLink=SharedResource.TestCaseResultFolderPath +"\\" +"Result_"+ tcName +".html";
		
		this.TestStepPassPercentage=(stepPass*100)/TotalTestSteps;
		
		this.TestStepFailPercentage=100-this.TestStepPassPercentage;
		
		this.TestStepMap=ts;
	}

}
