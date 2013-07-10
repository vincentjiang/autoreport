package cac.report.service.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;


public class ExcelBean {
private static HSSFWorkbook wb=null;
static String fileName=null;
/*int year=Calendar.getInstance().get(Calendar.YEAR);
String  day=Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"";
int month=Calendar.getInstance().get(Calendar.MONTH)+1;;*/
//构造函数，生成一个excel
public ExcelBean()
{
wb=new HSSFWorkbook();
}
//创建sheet
public void createFixationSheet(String [][]param,String[][]param1) throws IOException
{

FileOutputStream os=new FileOutputStream(this.getPath());
HSSFSheet sheet=wb.createSheet("new sheet");
wb.setSheetName(0,"每日销售数据报送格式");
HSSFRow row2[]=new HSSFRow[5];
Region region1 = new Region(0, (short) 0, 0, (short) 6);
HSSFRow row1=sheet.createRow((short)0);

//row1.setRowStyle(style);
sheet.setColumnWidth(0, 10000);
sheet.setColumnWidth(1, 4000);
sheet.setColumnWidth(2, 6000);
sheet.setColumnWidth(3, 6000);
sheet.setColumnWidth(4, 4000);

for(int i=0;i<param.length;i++)
{
	HSSFRow row=sheet.createRow((short)i);
	
	
	for(int j=0;j<param[i].length;j++)
	{
			cteateCell(wb, row, (short)j, param[i][j]);
			
		
			if(i==1||i==10)
			{
				
				setStyle(row.getCell(j),param[i][j] , HSSFColor.GOLD.index);
				
			}
	}
}


if(param1.length>0)
{
	HSSFSheet sheet1=wb.createSheet("new sheet");
	wb.setSheetName(1,"每日品类销售数据报送格式");
	
	sheet1.setColumnWidth(0, 9000);
	sheet1.setColumnWidth(1, 4000);
	sheet1.setColumnWidth(2, 6000);
	sheet1.setColumnWidth(3, 8000);
	sheet1.setColumnWidth(4, 10000);
	sheet1.setColumnWidth(5, 4000);
	sheet1.setColumnWidth(6, 4000);
	
	
	for(int i=0;i<param1.length;i++)
	{
		HSSFRow row=sheet1.createRow((short)i);
		for(int j=0;j<param1[i].length;j++)
		{
			cteateCell(wb, row, (short)j, param1[i][j]);
			if(i==1||i==9)
			{
				setStyle(row.getCell(j),param1[i][j] , HSSFColor.GOLD.index);
			}
		}
		
	}	

}
	
wb.write(os);
os.flush();
os.close();
}
private void cteateCell(HSSFWorkbook wb,HSSFRow row,short col,String val)
{
	
HSSFCell cell=row.createCell(col);
//((Strin) cell).setEncoding(HSSFCell.ENCODING_UTF_16);
cell.setCellValue(val);
HSSFCellStyle cellstyle=wb.createCellStyle();
if(row.getRowNum()==1)
{
	row.setHeight((short)600);
	cellstyle.setFillBackgroundColor(HSSFColor.ORANGE.index);
	cellstyle.setFillForegroundColor(HSSFColor.ORANGE.index);
	
	//cellstyle.setFillBackgroundColor(HSSFColor.ORANGE.index);
	
	
}if(row.getRowNum()==0)
{
	row.setHeight((short)600);
}
cellstyle.setFillBackgroundColor(HSSFColor.ORANGE.index);
cellstyle.setFillBackgroundColor(HSSFColor.ORANGE.index);
cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
cellstyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
cellstyle.setBottomBorderColor(HSSFColor.BLACK.index);
cellstyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
cellstyle.setLeftBorderColor(HSSFColor.BLACK.index);
cellstyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
cellstyle.setRightBorderColor(HSSFColor.BLACK.index);
cellstyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
cellstyle.setTopBorderColor(HSSFColor.BLACK.index);
HSSFDataFormat format = wb.createDataFormat();

cellstyle.setDataFormat(format.getFormat("0.00"));


//设置字体

HSSFFont font = wb.createFont();

font.setFontHeightInPoints((short) 10.5); // 字体高度

font.setFontName(" Calibri "); // 字体
cellstyle.setFont(font);
cell.setCellStyle(cellstyle);

/*
HSSFRow firstRow = hssfSheet.createRow((short) 0);

HSSFCell firstCell = firstRow.createCell(0);

firstRow.setHeight((short) 400);
*/
}
//颜色的设置
public static void setStyle(HSSFCell cell, String col, short fg){
    HSSFCellStyle style = wb.createCellStyle();
    style.setFillBackgroundColor(fg);
    style.setFillForegroundColor(fg);
    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    cell.setCellStyle(style);
    cell.setCellValue(col);
  }
public  String  getPath()
{
	SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd");
	String date=sf.format(new Date());
	fileName="C://每天销售报表//D"+date+".xls";
	return fileName;
}
public static void main(String[]args) throws IOException
{
   ExcelBean eb=new ExcelBean();
   String[][] param=new Sales().getSales();
   String[][] param1=new Sales().getSalesByGroup();
   eb.createFixationSheet(param, param1);
   
}
}