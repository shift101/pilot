package com.shift105.controller;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shift105.model.Attendance;
import com.shift105.model.DayProp;
import com.shift105.model.Shift;
import com.shift105.model.UserShiftData;
import com.shift105.repository.ShiftRepository;

@Component
public class ShiftHelper {
	@Autowired
	private ShiftRepository shifts;

	HashMap<String, CellStyle> cellStyles;
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	private int headerRow = 0;
	private int empIdCol = 1;

	public byte[] generateExcel(Shift shift) throws Exception {
		Workbook wb = new XSSFWorkbook();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Sheet sheet1 = wb.createSheet("Planned-" + shift.getMonth_id() + "-" + shift.getYear());
		sheet1.setZoom(70);
		createStyles(wb);
		setHeader(sheet1, shift.getMonth_id(), shift.getYear());
		buildShiftRota(sheet1,shift.getUsershift(),shift.getMonth_id(), shift.getYear());
		sheet1.autoSizeColumn(0);
		try {
			wb.write(bos);
		} finally {
			bos.close();
			wb.close();
		}
		/*
		 * try { //excel = new ByteArrayResource(Files.readAllBytes(Paths.get(
		 * "C:\\Users\\hsharma\\Documents\\Sonata_Support_Shift_Mar_2020.xlsx"))); }
		 * catch (Exception e) { // TODO Auto-generated catch block e.printStackTrace();
		 * }
		 */
		return bos.toByteArray();
	}


	private void buildShiftRota(Sheet sheet1,List<UserShiftData> usershift, int month, int year) {
		int i=2;
		int shiftid=-1;
		for(UserShiftData user:usershift) {
			if(shiftid != user.getShift_id()) {
				addEmptyRow(sheet1);
				shiftid = user.getShift_id();
				i++;
			}
			setUserRecord(user,i,sheet1, month, year);
			i++;
		}
		
	}


	private void addEmptyRow(Sheet sheet1) {
		sheet1.createRow(sheet1.getPhysicalNumberOfRows());		
	}


	private void setHeader(Sheet sheet1, int month, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		Row dateRow = sheet1.createRow(0);
		Row dayRow = sheet1.createRow(1);

		SimpleDateFormat simpleDayformat = new SimpleDateFormat("E");
		SimpleDateFormat simpleDateformat = new SimpleDateFormat("DD-MMM-YYYY");
		int myMonth = cal.get(Calendar.MONTH);
		int i = 1;
		while (myMonth == cal.get(Calendar.MONTH)) {
			Cell dateCell = dateRow.createCell(i);
			dateCell.setCellStyle(cellStyles.get("header_date"));
			Cell dayCell = dayRow.createCell(i);
			dayCell.setCellStyle(cellStyles.get("header"));
			dateCell.setCellValue(simpleDateformat.format(cal.getTime()));
			dayCell.setCellValue(simpleDayformat.format(cal.getTime()));
			cal.add(Calendar.DAY_OF_MONTH, 1);
			i++;
		}

	}
	
	private void setUserRecord(UserShiftData user,int rownum,Sheet sheet1, int month, int year) {
		Row dateRow = sheet1.createRow(rownum);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat simpleDayformat = new SimpleDateFormat("E");
		int i=0;
		Cell nameCell = dateRow.createCell(i);
		nameCell.setCellValue(user.getUser_name());
		Cell dataCell;
		String shift = getShiftChar(user);
		int isGenWeekoff = 0;
		for(DayProp day: user.getExcelData()) {
			dataCell = dateRow.createCell(++i);
			if(day.getExcp_code().equalsIgnoreCase("WO")) {
				dataCell.setCellValue(day.getExcp_code());
				dataCell.setCellStyle(cellStyles.get("weekoff"));
			}else if(day.getExcp_code().equalsIgnoreCase("PL")){
				dataCell.setCellValue("P");
				dataCell.setCellStyle(cellStyles.get("planned"));
			}else {
				dataCell.setCellValue(shift);
				dataCell.setCellStyle(cellStyles.get("normal"));
			}
			if(isGenWeekoff<2 && 
					day.getExcp_code().equalsIgnoreCase("WO") && day.getDay()<=7 &&
					(simpleDayformat.format(cal.getTime()).equalsIgnoreCase("SAT") ||
							simpleDayformat.format(cal.getTime()).equalsIgnoreCase("SUN"))
					) {
				isGenWeekoff++;
			}
			cal.add(Calendar.DAY_OF_MONTH, 1);
			
		}
		if(isGenWeekoff == 2) {
			nameCell.setCellStyle(cellStyles.get("Resource_norm_"+shift));
		}else {
			nameCell.setCellStyle(cellStyles.get("Resource_bold_"+shift));
		}
		
	}

	private String getShiftChar(UserShiftData user) {
		String shift ="";
		switch(user.getShift_id()) {
		case 0: shift="G";
		break;
		case 1:shift="E";
		break;
		case 2:shift="M";
		break;
		case 3:shift="N";
		break;
		}
		return shift;
	}


	/**
	 * create a library of cell styles
	 */
	private void createStyles(Workbook wb) {
		if (cellStyles == null) {
			cellStyles = new HashMap<String, CellStyle>();

			DataFormat df = wb.createDataFormat();

			CellStyle style;
			
			Font verticalWhiteDateFont = wb.createFont();
			verticalWhiteDateFont.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
			
			Font headerFont = wb.createFont();
			
			Font textNorm = wb.createFont();
			
			Font textBold = wb.createFont();
			textBold.setBold(true);
			
			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setFont(textNorm);
			cellStyles.put("normal", style);
			
			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setFont(textNorm);
			cellStyles.put("weekoff", style);
			
			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setFont(headerFont);
			style.setRotation((short)90);
			cellStyles.put("header", style);

			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setFont(verticalWhiteDateFont);
			style.setRotation((short)90);
			cellStyles.put("header_date", style);
			
			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.index);
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setFont(textBold);
			cellStyles.put("Resource_bold_E", style);
			
			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.index);
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setFont(textNorm);
			cellStyles.put("Resource_norm_E", style);
			
			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.index);
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setFont(textBold);
			cellStyles.put("Resource_bold_N", style);
			
			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.index);
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setFont(textNorm);
			cellStyles.put("Resource_norm_N", style);
			
			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setFont(textBold);
			cellStyles.put("Resource_bold_G", style);
			
			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setFont(textNorm);
			cellStyles.put("Resource_norm_G", style);
			
			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setFont(textBold);
			cellStyles.put("Resource_bold_M", style);
			
			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setFont(textNorm);
			cellStyles.put("Resource_norm_M", style);
			
			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setFont(textNorm);
			cellStyles.put("planned", style);

			Font font1 = wb.createFont();
			font1.setBold(true);
			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.LEFT);
			style.setFont(font1);
			cellStyles.put("name_bold", style);

			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setFont(font1);
			cellStyles.put("cell_b_centered", style);

			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.RIGHT);
			style.setFont(font1);
			style.setDataFormat(df.getFormat("d-mmm"));
			cellStyles.put("cell_b_date", style);

			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.RIGHT);
			style.setFont(font1);
			style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setDataFormat(df.getFormat("d-mmm"));
			cellStyles.put("cell_g", style);

			Font font2 = wb.createFont();
			font2.setColor(IndexedColors.BLUE.getIndex());
			font2.setBold(true);
			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.LEFT);
			style.setFont(font2);
			cellStyles.put("cell_bb", style);

			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.RIGHT);
			style.setFont(font1);
			style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setDataFormat(df.getFormat("d-mmm"));
			cellStyles.put("cell_bg", style);

			Font font3 = wb.createFont();
			font3.setFontHeightInPoints((short) 14);
			font3.setColor(IndexedColors.DARK_BLUE.getIndex());
			font3.setBold(true);
			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.LEFT);
			style.setFont(font3);
			style.setWrapText(true);
			cellStyles.put("cell_h", style);

			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.LEFT);
			style.setWrapText(true);
			cellStyles.put("cell_normal", style);

			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setWrapText(true);
			cellStyles.put("cell_normal_centered", style);

			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.RIGHT);
			style.setWrapText(true);
			style.setDataFormat(df.getFormat("d-mmm"));
			cellStyles.put("cell_normal_date", style);

			style = createBorderedStyle(wb);
			style.setAlignment(HorizontalAlignment.LEFT);
			style.setIndention((short) 1);
			style.setWrapText(true);
			cellStyles.put("cell_indented", style);

			style = createBorderedStyle(wb);
			style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cellStyles.put("cell_blue", style);
		}

	}

	private CellStyle createBorderedStyle(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		return style;
	}


	public List<Attendance> mapAttendanceSheet(XSSFSheet sheetAt) {
		XSSFRow row;
		XSSFCell cell;
		SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
		XSSFRow header =sheetAt.getRow(headerRow);
		int colCount = header.getPhysicalNumberOfCells()-1;
		int rowCount=sheetAt.getPhysicalNumberOfRows();
		ArrayList<String> dates = new ArrayList<String>();
		ArrayList<Attendance> attList = new ArrayList<Attendance>();
		ArrayList<Integer> empList = shifts.getExistingEmpId();
		Attendance att;
		for(int k=2;k<colCount;k++) {
			//System.out.println(getDBDate(header.getCell(k).getStringCellValue()));
			dates.add(getDBDate(header.getCell(k).getStringCellValue()));
		}
		for(int i=1;i<rowCount;i++) {
			row=sheetAt.getRow(i);
			if(!empList.contains(new Double(row.getCell(1).getNumericCellValue()).intValue()))continue;
			for(int j=2;j<row.getLastCellNum()-1;j++) {
				cell=row.getCell(j);
				att = new Attendance();
				att.setDate(dates.get(j-2));
				att.setEmp_id(new Double(row.getCell(1).getNumericCellValue()).intValue());
                //System.out.print((new Double(row.getCell(1).getNumericCellValue()).intValue())+"%");
                if(DateUtil.isCellDateFormatted(cell) || cell.getDateCellValue()==null){
			    	 Date date = cell.getDateCellValue();
			    	 Double punchTime= date==null?0:getPunchTime(formatTime.format(date));
			    	 //System.out.print(punchTime+"!");
			    	 att.setPunch_time(punchTime);
			     }
                attList.add(att);
			}
			//System.out.println();
		}
		return attList;
		
	}


	private double getPunchTime(String format) {
		String[] str = format.split(":");
		return Double.parseDouble(df2.format((Double.parseDouble(str[0]))+(Double.parseDouble(str[1])/60))) ;
	}


	private String getDBDate(String stringCellValue) {
		SimpleDateFormat formatDate = new SimpleDateFormat("dd/mm/yyyy");
		String str="";
		try {
			Date date = formatDate.parse(stringCellValue);
			str = new SimpleDateFormat("yyyy-mm-dd").format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

}
