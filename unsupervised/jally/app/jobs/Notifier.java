package jobs;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.mail.HtmlEmail;

import play.Logger;
import reports.Dashboard;

import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

public class Notifier {

	  public void byEmail(Dashboard dashboard, String recipient) {
		  String html = views.html.dashboard.render(dashboard).body();

		  MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
		  mail.setSubject("Today");
		  mail.addRecipient(recipient);
		  mail.addFrom("dev@tendrildemo.com");
		  mail.send("text version", html);
		  Logger.info("Notifier:byEmail sent:"+dashboard.reports.get(0));
	}
	  
	  public void byEmailCommons(Dashboard dashboard, String recipient) {
		  HtmlEmail email = new HtmlEmail();
		  email.setHostName("smtp.com");
		  email.setDebug(true);
		  email.setAuthentication("dev@tendrildemo.com", "tendrildev");
		  try {
			  email.addTo(recipient);
			  email.setFrom("dev@tendrildemo.com", "Dev");
			  email.setSubject("Test");
			  
			  // set the html message
			  String res = StringEscapeUtils.escapeHtml(dashboard.reports.get(0));
			  email.setHtmlMsg(res);

			  // set the alternative message
			  email.setTextMsg("Your email client does not support HTML messages");

			  // send the email
			  email.send();		  
			  Logger.info("Notifier:byEmail sent:");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
}
