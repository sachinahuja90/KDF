package com.nag.kdf.customizeKeywords;

import org.openqa.selenium.WebDriver;

import com.nag.kdf.keyword.SeleniumKeywords;
import com.nag.kdf.utility.KDFEvent;

public class EntryClassForCustomizeKeywords extends SeleniumKeywords {
	
public EntryClassForCustomizeKeywords(){}	 //empty constructor
	
	public EntryClassForCustomizeKeywords(WebDriver objWebdriver,KDFEvent objAFTEvent) //parameterized constructor
	{
		super(objWebdriver,objAFTEvent);
	}

}
