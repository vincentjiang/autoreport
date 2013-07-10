package cac.report.data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 发送邮件需要使用的基本信息
 */
public class MailSenderInfo {
	// 设置邮件的发送时间
	static int sendHour = 23;
	static int sendMinute = 26;
	static int sendSecond = 30;

	public static int getSendHour() {
		return sendHour;
	}

	public static int getSendMinute() {
		return sendMinute;
	}

	public static int getSendSecond() {
		return sendSecond;
	}

	// 发送邮件的服务器的IP和端口
	private final static String mailServerHost = "smtp.163.com";
	private final static String mailServerPort = "25";
	// 邮件发送者的地址
	private final static String fromAddress = "CAC AutoSend <cac_rgsp@163.com>";
	// 登陆邮件发送服务器的用户名和密码
	private final static String userName = "cac_rgsp@163.com";
	private final static String password = "123456789";
	// 是否需要身份验证
	private final static boolean validate = true;
	// 邮件主题
	SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
	String myDate = s.format(new Date());
	String subject = "国内每日销售数据报送 (中艺&天工阁) -- " + myDate;
	// 邮件的文本内容
	private static String content = "各位，请看附件！";
	// 邮件接收者的地址
	private static String[] toAddress = {
		"Vincent - QQ <471494539@qq.com>",
		"Vincent - Gmail <jxs471494539@gmail.com>"
		};
	// 邮件附件的文件
	private static String[] attachFile;

	/**
	 * 获得邮件会话属性
	 */
	public static String getMailServerHost() {
		return mailServerHost;
	}

	public static String getMailServerPort() {
		return mailServerPort;
	}

	public static String getFromAddress() {
		return fromAddress;
	}

	public static String getUserName() {
		return userName;
	}

	public static String getPassword() {
		return password;
	}

	public static boolean isValidate() {
		return validate;
	}

	public String getSubject() {
		return subject;
	}

	public static String getContent() {
		return content;
	}

	public static String[] getToAddress() {
		return toAddress;
	}

	public static String[] getAttachFile() {
		return attachFile;
	}

	public static void setAttachFile(String[] attachFile1) {
		attachFile = attachFile1;
	}
}