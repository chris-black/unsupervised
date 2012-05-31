package jobs;

import models.Dashboard;
import play.Logger;

import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

public class Notifier {

	  public void byEmail(Dashboard dashboard) {
		Logger.info("Mailer");
		MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
		mail.setSubject("Today");
		mail.addRecipient("dlange@tendrilinc.com");
		mail.addFrom("dev@tendrildemo.com");
		//mail.send("A text only message","<html><body><p>An <b>html</b> message</p></body></html>");
	}
}
