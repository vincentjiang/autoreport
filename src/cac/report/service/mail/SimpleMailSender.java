package cac.report.service.mail;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import cac.report.data.MailSenderInfo;

/**
 * 简单邮件（带附件的邮件）发送器
 */
public class SimpleMailSender {

	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 */
	public boolean sendHtmlMail(final MailSenderInfo mailInfo) {
		
		// 创建Properties对象
		Properties props = new Properties();
		// 创建信件服务器
		props.put("mail.smtp.host", MailSenderInfo.getMailServerHost());
		p("连接至邮件服务器：" + MailSenderInfo.getMailServerHost());
		// 通过验证
		props.put("mail.smtp.auth", "true");
		// 判断是否需要身份认证
		Authenticator authenticator = null;
		if (MailSenderInfo.isValidate()) {
			// 判断是否需要身份认证，如果需要身份认证，则创建一个密码验证器
			authenticator = new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
							MailSenderInfo.getUserName(),
							MailSenderInfo.getPassword());
				}
			};
		}
		
		try {
			// 根据邮件会话属性和密码验证器构造一个发送邮件的session
			Session sendMailSession = Session.getDefaultInstance(props,
					authenticator);
			// 根据session创建一个邮件消息
			MimeMessage mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(MailSenderInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			p("邮件发送人为：" + from);
			
			/**
			 * 创建邮件的接收者地址(可多个)
			 */
			String[] toAddress = MailSenderInfo.getToAddress();
			p("收件人为：");
			for (int i = 0; i < MailSenderInfo.getToAddress().length; i++) {
				mailMessage.addRecipients(Message.RecipientType.TO,
						InternetAddress.parse(toAddress[i], false));
				p(toAddress[i]);
			}

			// 设置邮件消息的主题
			MailSenderInfo msi = new MailSenderInfo();
			mailMessage.setSubject(msi.getSubject());
			p("邮件主题：" + msi.getSubject());

			MimeMultipart mmp = new MimeMultipart("mixed");
			//添加邮件正文
			MimeBodyPart content = addContent(MailSenderInfo.getContent());
			p("邮件正文：" + MailSenderInfo.getContent());
			mmp.addBodyPart(content);
			
			/**
			 * 添加邮件附件（可多个）
			 */
			String[] attachFile = MailSenderInfo.getAttachFile();
			p("添加附件：");
			for (int i = 0; i < MailSenderInfo.getAttachFile().length; i++) {
				MimeBodyPart attachment = addAttachment(attachFile[i]);
				mmp.addBodyPart(attachment);
				p(attachFile[i]);
			}
			
			// 将上面混合型的 MimeMultipart 对象作为邮件内容并保存
			mailMessage.setContent(mmp);
			//设置信件头的发送日期
			mailMessage.setSentDate(new Date());
			p("邮件发送时间：" + new Date());
			mailMessage.saveChanges(); 
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**  
     * 根据传入的文件路径创建附件并返回  
     */ 
	private MimeBodyPart addAttachment(String fileName) throws Exception {
		// 用于保存附件部分 
        MimeBodyPart attachmentPart = new MimeBodyPart();  
        FileDataSource fields = new FileDataSource(fileName);  
        attachmentPart.setDataHandler(new DataHandler(fields));  
        attachmentPart.setFileName(fields.getName());  
        return attachmentPart;  
    }
	
	/**  
     * 根据传入的邮件正文body和文件路径创建图文并茂的正文部分  
     */ 
    private MimeBodyPart addContent(String content)  
            throws Exception {  
        // 用于保存最终正文部分  
        MimeBodyPart contentBody = new MimeBodyPart();  
        // 用于组合文本和图片，"related"型的MimeMultipart对象  
        MimeMultipart contentMulti = new MimeMultipart("related");  
 
        // 正文的文本部分  
        MimeBodyPart textBody = new MimeBodyPart();  
        textBody.setContent(MailSenderInfo.getContent(), "text/html;charset=UTF-8");  
        contentMulti.addBodyPart(textBody);
 
        // 将上面"related"型的 MimeMultipart 对象作为邮件的正文  
        contentBody.setContent(contentMulti);  
        return contentBody;  
    }
	
    private void p(Object o) {
		System.out.println(o);
	}
}
