/**
 * 
 * 对中艺数据库的操作
 */
package cac.report.util;
import  java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
public class DbConn {
	private Connection ct=null;
	private PreparedStatement ps=null;
	private ResultSet rs=null;
	public Connection getCt() {
		return ct;
	}
	public PreparedStatement getPs() {
		return ps;
	}
	//得到连接
	public Connection getcon()
	{
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			ct = DriverManager.getConnection(
					"jdbc:oracle:thin:@168.101.1.193:1521:espos", "cac", "cac");
			//ps=ct.prepareStatement(sql);
			//rs=ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return ct;
	}
	//关闭数据库
	public void close(ResultSet rs,Statement st,Connection ct)
	
	{
		
        try {
			if (ct != null) {
				ct.close();
				ct = null;
			}
			if (st != null) {
				st.close();
				st = null;
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
	//执行查询的统一方法
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
	public ResultSet querySqlByPage(String sql1,String sql,String param[],int pageNow,int pageSize)
	{
		
		int pageCount=this.getPaeCount(sql);
		int pageMax=(pageCount-1)/pageSize+1;
		ct=this.getcon();
		try {
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					ps.setString(i + 1, param[i]);
				}
			}
			ps=ct.prepareStatement(sql);
			rs=ps.executeQuery();
		}catch(Exception e)
			{
				e.printStackTrace();
			}
			
		return rs;
	}

	public int getPaeCount(String sql)
	{
		int pageCont=0;
		rs=this.excuteSql(sql, null);
		try {
			rs.next();
		 pageCont=rs.getInt(1);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  pageCont;
	}
	//按品类分类的销售
	public Float[] excuteSqlByGroup(String group)
	 {
		 Float amt[]=new Float[2];
		 ct=this.getcon();
		String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		 String sql="select sum(t.xf_amtsold),round(sum(t.xf_amtsold)/1.17,2) -sum(t.xf_costsold),sum(t.xf_costsold) from xf_vipitemdm t, xf_itemmas p where t.xf_txdate=date '"+date+"' and t.xf_plu=p.xf_plu and p.xf_group1=? and xf_storecode not in('8888')";		
		 try {
			ps = ct.prepareStatement(sql);
			ps.setString(1, group);
			rs=ps.executeQuery();
			if (rs.next()) {
				amt[0] = rs.getFloat(1);
				amt[1] = rs.getFloat(2);
				
			}else
			{
				amt[0]=0.00f;
				amt[1]=0.00f;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally
		{
			this.close(rs,ps,ct);
		}
		return amt;
	 }

	 public static void main(String[]args)
	 {
		 DbConn abc=new DbConn();
		 abc.excuteSqlByGroup("11");
		 System.out.println("程序运行完毕");
	 }
}
