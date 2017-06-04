package info.doula.system.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.net.SMTPAppender;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Log configuration to prepare and send alert mail
 *
 * @author hossaindoula
 *
 */
@Component
public class LogConfiguratorImpl {

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	private String sendAlert;
	private String to;
	private String from;
	private String debugMode;

	public void setTo(String to) {
		this.to = to;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setSendAlert(String sendAlert) {
		this.sendAlert = sendAlert;
	}

	public void setDebugMode(String debugMode) {
		this.debugMode = debugMode;
	}

	@PostConstruct
	public void init() throws UnknownHostException {
		if (sendAlert != null && sendAlert.equals("true")) {
			Logger myErrorLogger = (Logger) LoggerFactory
					.getLogger("ERROR");
			LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

			PatternLayout layout = new PatternLayout();

			layout.setContext(context);
			layout.setPattern("%-5level [%thread]: %msg %n");
			layout.start();

			SMTPAppender appender = new SMTPAppender();
			appender.setContext(context);
			appender.setSmtpHost("localhost");
			appender.setFrom(from);
			appender.addTo(to);
			appender.setSubject("API ALERT " + InetAddress.getLocalHost().getHostName());
			appender.setLayout(layout);
			appender.start();


			myErrorLogger.addAppender(appender);

			logger.info("set alert mail appender");
		} else {
			logger.info("skip to set alert mail appender");
		}

		if (debugMode != null && debugMode.equals("true")) {
			logger.setLevel(Level.DEBUG);
		}


		logger.debug("we support debug log!!!");
	}
}
