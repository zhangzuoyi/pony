package com.zzy.pony.util;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtil {
	public static CellStyle getCommonStyle(Workbook wb){
		CellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderBottom(CellStyle.BORDER_THIN);
	    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    style.setBorderLeft(CellStyle.BORDER_THIN);
	    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	    style.setBorderRight(CellStyle.BORDER_THIN);
	    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    style.setBorderTop(CellStyle.BORDER_THIN);
	    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    style.setWrapText(true);
	    
	    return style;
	}
	/**
	 * 
	 * @param wb
	 * @param style commonStyle
	 * @return
	 */
	public static CellStyle getTitleStyle(Workbook wb,CellStyle style){
		CellStyle styleTitle = wb.createCellStyle();
	    styleTitle.cloneStyleFrom(style);
	    styleTitle.setAlignment(CellStyle.ALIGN_CENTER);
	    styleTitle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
	    styleTitle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    
	    return styleTitle;
	}
}
