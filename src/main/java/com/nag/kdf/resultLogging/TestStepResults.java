package com.nag.kdf.resultLogging;

public class TestStepResults {

	int TestStepID;
	String TestStepAction;
	String TestStepMsg;
	String TestStepExpectedResult;
	String TestStepActualResult;
	String TestStepStatus;
	String SnapshotLink;
	
	public TestStepResults(){
		
	}
	
	public TestStepResults(int id,String action,String msg,String exRes,String actRes,String status, String snapshot){
		this.TestStepID=id;
		this.TestStepAction=action;
		this.TestStepMsg=msg;
		this.TestStepExpectedResult=exRes;
		this.TestStepActualResult=actRes;
		this.TestStepStatus=status;
		this.SnapshotLink=snapshot;			
	}

}
