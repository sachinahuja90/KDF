package com.nag.kdf.timeStamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamp {
	
	public String getTimeStamp(String strFormat)
	{
		DateFormat objdateFormat = new SimpleDateFormat(strFormat);
		Date objdate = new Date();
		return objdateFormat.format(objdate);
	}	

}
