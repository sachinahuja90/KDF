package com.nag.kdf.utility;

public class TestObject {	
	public String strtarget = "";//for target value "e.g, "id=" ".
	public int intIndex = 0; //for index value.
	public String strParentProp = "";
	public int intParentIndex = 0;
	public TestObject()
	{
		this.strtarget = "";
		this.strParentProp = "";
		this.intIndex = 0;
		this.intParentIndex = 0;
	}
	TestObject(String strTarget, String strParentProp, int intChildIndex, int intParentIndex)
	{
		this.strtarget = strTarget;
		this.strParentProp = strParentProp;
		this.intIndex = intChildIndex;
		this.intParentIndex = intParentIndex;
	}
}
