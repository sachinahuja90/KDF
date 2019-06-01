package com.nag.kdf.utility;

public class PrintMessage {

	public static void printKDFEvent(KDFEvent objAFTEvent)
	{
		System.out.println(objAFTEvent.strAction + " - "+ objAFTEvent.strParent + " - "  + objAFTEvent.strChild + " - " + objAFTEvent.strParam1 + " - " +objAFTEvent.strParam2+ " - " + objAFTEvent.strParam3+ " - " + objAFTEvent.strParam4+ " - " + objAFTEvent.strParam5+ " - " + objAFTEvent.strParam6);

	}
	
	public static void printTestCase(String strSheetName, KDFEvent objAFTEvent)
	{
		System.out.println("######################################");
		System.out.println("TestScenario : " + strSheetName);
		System.out.println("TestCase Number : " + objAFTEvent.strParent);
		System.out.println("TestCase Name : " + objAFTEvent.strChild);
		System.out.println("######################################");
	}	
	
	public static void printStepNumber(String strStep)
	{
		System.out.println("----------[Step" + " - " + strStep + "]----------");		
	}
	
}
