package com.nag.kdf.utility;

public class KDFEventResult {
	private String strStatus = "";
	private String strActualResult = "";
	public String getStrStatus() {
		return strStatus;
	}
	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}
	public String getStrActualResult() {
		return strActualResult;
	}
	public void setStrActualResult(String strActualResult) {
		this.strActualResult = strActualResult;
	}

	public KDFEventResult (String strActualResult, String strStatus)
	{
		this.strActualResult = strActualResult;
		this.strStatus = strStatus;
	}
}
