/**
 * 显示2个sheet的数据
 * 
 */
package cac.report.service.excel;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cac.report.util.APDBconn;
import cac.report.util.DateUtil;
import cac.report.util.DbConn;

public class Sales {
	private String[]str=null;
	DbConn dbc=new DbConn();
	DateUtil du=new DateUtil();
	APDBconn abc=new APDBconn();
	String[]param1={new SimpleDateFormat("yyyy-MM-dd").format(new Date())};
	//每日销售数据
	public String[][] getSales()
	{
		
		
		String param[][]={{"金额单位：元　　客单数单位：笔"},{"日期","店号","收银金额（含税）","销售净额（去税)","客单数"},
				   {param1[0],"ZY8888","0","0","0"},{"","ZYBJ01","0","0","0",}
				   ,{"","ZYBJ02","0","0","0"}
				   ,{"","ZYGZ01","0","0","0"}
				   ,{"","ZYHZ01","0","0","0"}
				   ,{"","ZYSH01","0","0","0"}
				   ,{"","ZYSZ01","0","0","0"}
				   ,{"","APBJ01","0","0","0"}
				   ,{"合计",null,"","",""}};
		
/*
select count(distinct t.xf_docno)
  from xf_trans t,xf_vipitemdm
 where t.xf_docno not in
 (select distinct a.xf_voiddocno from xf_trans a where a.xf_voiddocno like 'S%')
and t.xf_docno like 'S%'
   and t.xf_txdate = date '2013-01-02'
   and t.xf_storecode = 'SZ01'*/

		String sql=

"         select v.xf_storecode,sum(v.xf_amtsold),count(distinct v.xf_docno),round(sum(v.xf_amtsold)/1.17,2) from xf_vipitemdm v\n" +
"where v.xf_docno in(\n" + 
"select distinct t.xf_docno from xf_trans t where t.xf_docno not in (select distinct a.xf_voiddocno\n" + 
"       from xf_trans a\n" + 
"      where a.xf_voiddocno like 'S%' and a.xf_txdate=date'"+param1[0]+ "') and  t.xf_txdate=date'"+param1[0]+ "' and t.xf_docno like 'S%')and v.xf_txdate=date'"+param1[0]+ "'\n" + 
"      group by v.xf_storecode";

		String sql1=
				"select v.xf_storecode,sum(v.xf_amtsold),count(distinct v.xf_docno),round(sum(v.xf_amtsold)/1.17,2) from xf_vipitemdm v\n" +
						"where v.xf_docno in(\n" + 
						"select distinct t.xf_docno from xf_trans t where t.xf_docno not in (select distinct a.xf_voiddocno\n" + 
						"       from xf_trans a\n" + 
						"      where a.xf_voiddocno like 'S%') and  t.xf_txdate=date'"+param1[0]+ "' and t.xf_docno like 'S%')\n" + 
						"     and v.xf_storecode ='BJAP01' and v.xf_txdate=date'"+param1[0]+ "' group by v.xf_storecode";
     
		ResultSet rs=dbc.excuteSql(sql, null);
		ResultSet rs1=abc.excuteSql(sql1, null);
		try {
			while (rs.next()) {
				
				
				if ("BJ01".equals(rs.getString(1))) {
					param[3][2] = rs.getString(2);
					param[3][3]=rs.getString(4);
					param[3][4]=rs.getString(3);
				} else if ("BJ02".equals(rs.getString(1))) {
					param[4][2] = rs.getString(2);
					param[4][3]=rs.getString(4);
					param[4][4]=rs.getString(3);
				} else if ("GZ01".equals(rs.getString(1))) {
					param[5][2] = rs.getString(2);
					param[5][3]=rs.getString(4);
					param[5][4]=rs.getString(3);
				} else if ("HZ01".equals(rs.getString(1))) {
					param[6][2] = rs.getString(2);
					param[6][3]=rs.getString(4);
					param[6][4]=rs.getString(3);
				} else if ("SH01".equals(rs.getString(1))) {
					param[7][2] = rs.getString(2);
					param[7][3]=rs.getString(4);
					param[7][4]=rs.getString(3);
				} else if ("SZ01".equals(rs.getString(1))) {
					param[8][2] = rs.getString(2);
					param[8][3]=rs.getString(4);
					
					param[8][4]=rs.getString(3);
				}
			}
			if(rs1.next())
			{
				param[9][2]=rs1.getString(2);
				param[9][3]=rs1.getString(4);
				param[9][4]=rs1.getString(3);
				//System.out.println(param[9][3]);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally
		{
			try {
				
				dbc.close(rs,dbc.getPs(),dbc.getCt());
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		 for(int i=2;i<5;i++)
		 {
			 float f=0.0f;
			 for(int j=2;j<10;j++)
			 {
				 f+=Float.parseFloat(param[j][i]);
				 param[10][i]=f+"";
			 }
		 }
		return param;
		
	}
	//每日品类销售数据
 public String[][] getSalesByGroup()
 { 
	 String param3[][]=new String[9][6];
	 String param2[][]={{"金额单位  ：元"	},
			   {"日期	","区域","业态","大类编码","大类名称","销售净额","销售毛利润"},
			   {param1[0],"国内","中艺","11","首饰","0","0"},
			   {"","国内","中艺","12","工艺","0","0"},
			   {"","国内","中艺","13","服装","0","0"},
			   {"","国内","中艺","无","批发商品","0","0"},
			   {"","国内","天工阁","14","工艺饰品","0","0"},
			   {"","国内","天工阁","15","工艺","0","0"},
			   {"","国内","天工阁","16","服装","0","0"}
			   ,{"合计","","","","","",""}};
	// HSSFCell hc[][]=new HSSFCell[10][7];
	 String sql="select sum(t.xf_amtsold),round(sum(t.xf_amtsold)/1.17 -sum(t.xf_costsold),2),sum(t.xf_costsold),p.xf_group1 from xf_vipitemdm t, xf_itemmas p where t.xf_txdate=date '"+param1[0]+"' and t.xf_plu=p.xf_plu and xf_storecode not in('8888') group by p.xf_group1 ";
	//String sql1="select sum(t.xf_amtsold),round(sum(t.xf_amtsold)/1.17 -sum(t.xf_costsold),2),sum(t.xf_costsold),p.xf_group1 from xf_vipitemdm t, xf_itemmas p where t.xf_txdate=date '"+param1[0]+"' and t.xf_plu=p.xf_plu group by p.xf_group1";
	 ResultSet rs=dbc.excuteSql(sql, null);
	// ResultSet rs1=abc.excuteSql(sql1, null);
	 try {
		while (rs.next()) {
			if ("11".equals(rs.getString(4))) {
				param2[2][5] = rs.getString(1);
				param2[2][6] = rs.getString(2);
			}else if("12".equals(rs.getString(4)))
			{
				param2[3][5] = rs.getString(1);
				param2[3][6] = rs.getString(2);
			}
			else if("13".equals(rs.getString(4)))
			{
				param2[4][5] = rs.getString(1);
				param2[4][6] = rs.getString(2);
			}
			
		
		}
		Float[]cacSales4=dbc.excuteSqlByGroup("14");
		Float[]cacSales5=dbc.excuteSqlByGroup("15");
		Float[]cacSales6=dbc.excuteSqlByGroup("16");
		Float[]apSales4=abc.excuteSqlByGroup("14");
		Float[]apSales5=abc.excuteSqlByGroup("15");
		Float[]apSales6=abc.excuteSqlByGroup("16");

		param2[6][5] = cacSales4[0]+apSales4[0]+"";
		param2[6][6] = cacSales4[1]+apSales4[1]+"";
		param2[7][5] = cacSales5[0]+apSales5[0]+"";
		param2[7][6] = cacSales5[1]+apSales5[1]+"";
		param2[8][5] = cacSales6[0]+apSales6[0]+"";
		param2[8][6] = cacSales6[1]+apSales6[1]+"";
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}finally
	{
		try {
			rs.close();
		} catch (Exception e2) {
			// TODO: handle exception
		}
		dbc.close(rs,dbc.getPs(),dbc.getCt());
	}

	 for(int i=5;i<7;i++)
	 {
		 float f=0.0f;
		 for(int j=2;j<9;j++)
		 {
			 f+=Float.parseFloat(param2[j][i]);
			 param2[9][i]=f+"";
		 }
	 }
	return param2;
	
 }

}
