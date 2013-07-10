package cac.report.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class APDBconn {
	private Connection ct=null;
	private  PreparedStatement ps=null;
	private  ResultSet rs=null;
	DateUtil du=new DateUtil();
	public Connection getcon()
	{
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			ct = DriverManager.getConnection(
					"jdbc:oracle:thin:@168.101.1.194:1521:e40utf8b", "mt1hq", "mt1hq");
			//ps=ct.prepareStatement(sql);
			//rs=ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return ct;
	}
	public void close()
	
	{
		
        try {
			if (ct != null) {
				ct.close();
				ct = null;
			}
			if (ps != null) {
				ps.close();
				ps = null;
			}
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
	}

	public ResultSet excuteSql(String sql,String param[])
	{
		
		ct=this.getcon();
		
		try {
			ps=ct.prepareStatement(sql);
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					ps.setString(i + 1, param[i]);
				}
			}
			
			rs=ps.executeQuery();
			
		} catch (Exception e1) {
			
			// TODO: handle exception
			e1.printStackTrace();
		}finally{
			
		}
		return rs;
	}
 public Float[] excuteSqlByGroup(String group)
 {
	 Float amt[]=new Float[2];
	 ct=this.getcon();
	 String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	 String sql="select sum(t.xf_amtsold),round(sum(t.xf_amtsold)/1.17,2) -sum(t.xf_costsold),sum(t.xf_costsold) from xf_vipitemdm t, xf_itemmas p where t.xf_txdate=date '"+date+"' and t.xf_plu=p.xf_plu and p.xf_group1='"+group+"' and xf_storecode ='BJAP01'";
	// System.out.println(sql);
	 try {
		ps = ct.prepareStatement(sql);
		rs=ps.executeQuery();
		if (rs.next()) {
			
			amt[0] = rs.getFloat(1);
			System.out.println(amt[0]);
			amt[1] = rs.getFloat(2);
			System.out.println(amt[1]);
			
		}else
		{
			
			amt[0]=0.00f;
			amt[1]=0.00f;
		}
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	
	}finally
	{
		this.close();
	}
	return amt;
 }
 public static void main(String[]args)
 {
	 APDBconn abc=new APDBconn();
	 abc.excuteSqlByGroup("16");
	 System.out.println("程序运行完毕");
 }
}
