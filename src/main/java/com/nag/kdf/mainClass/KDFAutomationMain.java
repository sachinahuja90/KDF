
package com.nag.kdf.mainClass;
import org.apache.commons.io.FileUtils;

import com.nag.kdf.drivers.ExcelDriver;
import com.nag.kdf.keyword.BasicKeyword;

import java.io.*;

public class KDFAutomationMain
{   
	//public static String configPath;
    public static void main(String[] args)
    {	
		String strExcelFile = "E:\\WebshopAutomation\\Framework\\KDF.xlsm";//BasicKeyword.prop.getProperty("TestCaseFile");//args[0];
		try
        {
            //CASE I : When excel path is coming in the args.
            if (true)//(args.length > 0) && args[0].contains(".xls"))
            {
                File f = new File(strExcelFile);
                if (f.exists())
                {
                    //strExcelFile = args[0];
                    FileUtils.copyFile(
                        f,
                        new File(f.getParent() + "\\Copy_of_" + f.getName())
                        );
                }
                else
                {
                    System.out.println(args[0] + "path is not valid");
                    strExcelFile = "";
                    System.exit(0);
                }
                ExcelDriver objCom1 = ExcelDriver.getInstance();
                objCom1.Executor(strExcelFile);
            }	
            
            // case II DB integration
        }
        catch (Exception e)
        {
            System.out.println("Error Occurred");
        }
        System.out.println("End of Execution...................");
        System.exit(0);
    }
  
}

