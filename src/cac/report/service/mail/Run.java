package cac.report.service.mail;

import java.io.IOException;

import cac.report.data.MailSenderInfo;
import cac.report.service.excel.ExcelBean;
import cac.report.service.excel.Sales;
import cac.report.service.schedule.DailyIterator;
import cac.report.service.schedule.Scheduler;
import cac.report.service.schedule.SchedulerTask;

/**
 * 
 * 设置收件人及附件进行邮件发送
 */

public class Run {

	public static void start() {
		final Scheduler scheduler = new Scheduler();
		scheduler.schedule(
				new SchedulerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						ExcelBean eb = new ExcelBean();
						String[] attachfile = { eb.getPath() };
						MailSenderInfo.setAttachFile(attachfile);
						// 这个类主要来发送邮件

						String[][] param = new Sales().getSales();
						String[][] param1 = new Sales().getSalesByGroup();
						try {
							eb.createFixationSheet(param, param1);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						SimpleMailSender sms = new SimpleMailSender();

						sms.sendHtmlMail(new MailSenderInfo());// 发送html格式(可带附件)

						p("邮件发送成功 ！");
					}
				},
				new DailyIterator(MailSenderInfo.getSendHour(), MailSenderInfo
						.getSendMinute(), MailSenderInfo.getSendSecond()));

	}

	private static void p(Object o) {
		System.out.println(o);
	}

}