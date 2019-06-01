package com.nag.kdf.excel;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

public class CellStyles {
	
	  public Map<String, CellStyle> createStyles(Workbook wb){
	        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

	        CellStyle style;
	        Font itemFont;     
	     
	        // Status Pass
	        itemFont = wb.createFont();
	        itemFont.setFontHeightInPoints((short)9);
	        itemFont.setColor(IndexedColors.GREEN.getIndex());
	        itemFont.setBold(true);
	        style = wb.createCellStyle();
	        style.setAlignment(HorizontalAlignment.LEFT);
	        style.setBorderRight(BorderStyle.NONE);
	        style.setRightBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());        
	        style.setBorderBottom(BorderStyle.NONE);
	        style.setBottomBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());        
	        style.setBorderLeft(BorderStyle.NONE);
	        style.setLeftBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());        
	        style.setBorderTop(BorderStyle.NONE);
	        style.setTopBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());    
	        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	        style.setFillPattern(FillPatternType.SOLID_FOREGROUND );
	        style.setFont(itemFont);
	        styles.put("Pass", style);
	        //-------------
	        
	        //Status Fail     
	        
	        itemFont = wb.createFont();
	        itemFont.setFontHeightInPoints((short)9);
	        itemFont.setColor(IndexedColors.RED.getIndex());
	        itemFont.setBold(true);
	        style = wb.createCellStyle();        
	        style.setAlignment(HorizontalAlignment.LEFT);
	        style.setBorderRight(BorderStyle.NONE);
	        style.setRightBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());        
	        style.setBorderBottom(BorderStyle.NONE);
	        style.setBottomBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());        
	        style.setBorderLeft(BorderStyle.NONE);
	        style.setLeftBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());        
	        style.setBorderTop(BorderStyle.NONE);
	        style.setTopBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());  
	        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	        style.setFillPattern(FillPatternType.SOLID_FOREGROUND );
	        style.setFont(itemFont);
	        styles.put("Fail", style);
	        //-------------            
	        
	        //Normal Text With Grey Background        
	        itemFont = wb.createFont();
	        itemFont.setFontHeightInPoints((short)9);
	        itemFont.setColor(IndexedColors.BLACK.getIndex());
	        style = wb.createCellStyle();        
	        style.setAlignment(HorizontalAlignment.LEFT);
	        style.setBorderRight(BorderStyle.NONE);
	        style.setRightBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());        
	        style.setBorderBottom(BorderStyle.NONE);
	        style.setBottomBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());        
	        style.setBorderLeft(BorderStyle.NONE);
	        style.setLeftBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());        
	        style.setBorderTop(BorderStyle.NONE);
	        style.setTopBorderColor(IndexedColors.GREY_40_PERCENT.getIndex()); 
	        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	        style.setFillPattern(FillPatternType.SOLID_FOREGROUND );
	        style.setFont(itemFont);
	        styles.put("Normal", style);
	        
	        //Link Text With Grey Background        
	        itemFont = wb.createFont();
	        itemFont.setFontHeightInPoints((short)9);
	        itemFont.setColor(IndexedColors.BLUE.getIndex());        
	        itemFont.setUnderline(Font.U_SINGLE);       
	        style = wb.createCellStyle();        
	        style.setAlignment(HorizontalAlignment.LEFT);
	        style.setBorderRight(BorderStyle.NONE);
	        style.setRightBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());        
	        style.setBorderBottom(BorderStyle.NONE);
	        style.setBottomBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());        
	        style.setBorderLeft(BorderStyle.NONE);
	        style.setLeftBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());        
	        style.setBorderTop(BorderStyle.NONE);
	        style.setTopBorderColor(IndexedColors.GREY_40_PERCENT.getIndex()); 
	        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        style.setFont(itemFont);
	        styles.put("Link", style);        
	        return styles;
	  }
	  
	  public void createHyperLinkAlternate(HSSFCell LinkCell, String strScreenshotPath )
		{	
		  
			String strHyperlink = "HYPERLINK(" + "\""+  strScreenshotPath  + "\"" + "," + "\"" + "Link\"" + ")";   
			LinkCell.setCellFormula(strHyperlink);
			LinkCell.setCellStyle(new CellStyles().createStyles(LinkCell.getSheet().getWorkbook()).get("Link"));
		}
	  
	
}
