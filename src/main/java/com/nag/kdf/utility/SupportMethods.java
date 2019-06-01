package com.nag.kdf.utility;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SupportMethods 
{
	public Boolean boolResult;
	public String strReturnVal;
	
	//#######################################################
    public String ANDOperation(String Var1, String Var2)
    {
       return Boolean.toString((Var1.toUpperCase().equals("TRUE") && Var2.toUpperCase().equals("TRUE")));
    }
    
  //#######################################################
    public String Equal(String var1, String var2)
    {
        try {
            if (var1.contains(".") || var2.contains(".")) {
                double dvar1 = Double.parseDouble(var1);
                double dvar2 = Double.parseDouble(var2);
                return Boolean.toString((dvar1 == dvar2));
            } else {
                int ivar1 = Integer.parseInt(var1);
                int ivar2 = Integer.parseInt(var2);
                return Boolean.toString((ivar1 == ivar2));
            }
        } catch (NumberFormatException e) {
            return "Exception in Equal";
        }
    }
    
    //#######################################################
    public String LesserThan(String var1, String var2)
    {
        try {
            if (var1.contains(".") || var2.contains(".")) {
                double dvar1 = Double.parseDouble(var1);
                double dvar2 = Double.parseDouble(var2);
                return Boolean.toString((dvar1 < dvar2));
            } else {
                int ivar1 = Integer.parseInt(var1);
                int ivar2 = Integer.parseInt(var2);
                return Boolean.toString((ivar1 < ivar2));
            }
        }
        catch (NumberFormatException e)
        {
            return "Exception in LesserThan";
        }
    }
    
    //###############################################
    public String LesserThanEqualTo(String var1, String var2)
    {
        try {
            if (var1.contains(".") || var2.contains(".")) {
                double dvar1 = Double.parseDouble(var1);
                double dvar2 = Double.parseDouble(var2);
                return Boolean.toString((dvar1 <= dvar2));
            } else {
                int ivar1 = Integer.parseInt(var1);
                int ivar2 = Integer.parseInt(var2);
                return Boolean.toString((ivar1 < ivar2));
          }
        } catch (NumberFormatException e) {
            return "Exception in LesserThanEqualTo";
        }
    }
    
    //#########################################################
    public String GreaterThan(String var1, String var2)
    {
        try {
            if (var1.contains(".") || var2.contains(".")) {
                double dvar1 = Double.parseDouble(var1);
                double dvar2 = Double.parseDouble(var2);
                return Boolean.toString((dvar1 > dvar2));
            } else {
                int ivar1 = Integer.parseInt(var1);
                int ivar2 = Integer.parseInt(var2);
                return Boolean.toString((ivar1 > ivar2));
          }
        } catch (NumberFormatException e) {
            return "Exception in GreaterThan";
        }    
    }

    //#########################################################
    public String GreaterThanEqualTo(String var1, String var2)
    {
        try {
            if (var1.contains(".") || var2.contains(".")) {
                double dvar1 = Double.parseDouble(var1);
                double dvar2 = Double.parseDouble(var2);
                return Boolean.toString((dvar1 >= dvar2));
           } else {
                int ivar1 = Integer.parseInt(var1);
                int ivar2 = Integer.parseInt(var2);
                return Boolean.toString((ivar1 > ivar2));
          }
        }
        catch (NumberFormatException e)
        {
            return "Exception in GreaterThanEqualTo";
        }
    }
    
    //#########################################################
    public String Subtract(String var1, String var2)
    {
        try {
            if (var1.contains(".") || var2.contains(".")) {
                double result = Double.parseDouble(var1) - Double.parseDouble(var2);
                return String.valueOf(result);
            } else {
                int result = Integer.parseInt(var1) - Integer.parseInt(var2);
                return String.valueOf(result);
            }
        } catch (Exception e) {
            return "Exception in Subtract";
        }
    }
    
    //#########################################################
    public String Divide(String var1, String var2)
    {
        try {
            if (var1.contains(".") || var2.contains(".")) {
                double result = Double.parseDouble(var1) / Double.parseDouble(var2);
                return String.valueOf(result);
            } else {
                double result = Double.parseDouble(var1) /Double.parseDouble(var2);
                return String.valueOf(result);
            }
        } catch (Exception e) {
            return "Exception in Division";
        }
    }
    //#########################################################
    public String Multiply(String var1, String var2)
    {
        try {
            if (var1.contains(".") || var2.contains(".")) {
                double result = Double.parseDouble(var1) * Double.parseDouble(var2);
                return String.valueOf(result);
            } else {
            	double result = Integer.parseInt(var1) * Integer.parseInt(var2);
                return String.valueOf(result);
            }
        } catch (Exception e) {
            return "Exception in Multiplication";
        }
    }

    //#########################################################
    public String Add(String var1, String var2)
    {
        try {
            if (var1.contains(".") || var2.contains(".")) {            	
            	NumberFormat formatter = new DecimalFormat("#0.00");            	
                Double result = Double.parseDouble(var1) + Double.parseDouble(var2);                
                String res = formatter.format(result);// + formatter.format(var1);            	
            	return res;
            } else {
                int result = Integer.parseInt(var1) + Integer.parseInt(var2);
                return String.valueOf(result);
            }
        } catch (Exception e) {
            return "Exception in Add";
        }
    }
	
}
