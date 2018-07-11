package ca.sprintmarketing.simplejavamail.mailer.internal.mailsender;

import ca.sprintmarketing.simplejavamail.mailer.MailerGenericBuilder;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Contains all the configuration that affect how a {@link ca.sprintmarketing.simplejavamail.mailer.Mailer} operates. This includes connection settings such as
 * timeouts, debug mode and which hosts to trust.
 * <p>
 * All of these settings are configured on the {@link MailerGenericBuilder}.
 */
public class OperationalConfig {
	/**
	 * @see ca.sprintmarketing.simplejavamail.mailer.MailerBuilder.MailerRegularBuilder#withSessionTimeout(Integer)
	 */
	private final int sessionTimeout;
	
	/**
	 * @see ca.sprintmarketing.simplejavamail.mailer.MailerBuilder.MailerRegularBuilder#withProperties(Properties)
	 */
	private final Properties properties;
	
	/**
	 * @see ca.sprintmarketing.simplejavamail.mailer.MailerBuilder.MailerRegularBuilder#withThreadPoolSize(Integer)
	 */
	private final int threadPoolSize;
	
	/**
	 * @see ca.sprintmarketing.simplejavamail.mailer.MailerBuilder.MailerRegularBuilder#withTransportModeLoggingOnly(Boolean)
	 */
	private final boolean transportModeLoggingOnly;
	
	/**
	 * @see ca.sprintmarketing.simplejavamail.mailer.MailerBuilder.MailerRegularBuilder#withDebugLogging(Boolean)
	 */
	private final boolean debugLogging;
	
	/**
	 * @see ca.sprintmarketing.simplejavamail.mailer.MailerBuilder.MailerRegularBuilder#trustingSSLHosts(String...)
	 */
	private final List<String> sslHostsToTrust;
	
	/**
	 * @see ca.sprintmarketing.simplejavamail.mailer.MailerBuilder.MailerRegularBuilder#trustingAllHosts(Boolean)
	 */
	private final boolean trustAllSSLHost;

	private final boolean validationBypass;
	
	/**
	 * For internal use only.
	 */
	public OperationalConfig(@Nonnull Properties properties, int sessionTimeout, int threadPoolSize, boolean transportModeLoggingOnly, boolean debugLogging, List<String> sslHostsToTrust, boolean trustAllSSLHost, boolean validationBypass) {
		this.properties = properties;
		this.sessionTimeout = sessionTimeout;
		this.threadPoolSize = threadPoolSize;
		this.transportModeLoggingOnly = transportModeLoggingOnly;
		this.debugLogging = debugLogging;
		this.sslHostsToTrust = Collections.unmodifiableList(sslHostsToTrust);
		this.trustAllSSLHost = trustAllSSLHost;
		this.validationBypass = validationBypass;
	}
	
	/**
	 * @see ca.sprintmarketing.simplejavamail.mailer.MailerBuilder.MailerRegularBuilder#withSessionTimeout(Integer)
	 */
	public int getSessionTimeout() {
		return sessionTimeout;
	}
	
	/**
	 * @see ca.sprintmarketing.simplejavamail.mailer.MailerBuilder.MailerRegularBuilder#withThreadPoolSize(Integer)
	 */
	public int getThreadPoolSize() {
		return threadPoolSize;
	}
	
	/**
	 * @see ca.sprintmarketing.simplejavamail.mailer.MailerBuilder.MailerRegularBuilder#withTransportModeLoggingOnly(Boolean)
	 */
	public boolean isTransportModeLoggingOnly() {
		return transportModeLoggingOnly;
	}
	
	/**
	 * @see ca.sprintmarketing.simplejavamail.mailer.MailerBuilder.MailerRegularBuilder#withDebugLogging(Boolean)
	 */
	public boolean isDebugLogging() {
		return debugLogging;
	}
	
	/**
	 * @see ca.sprintmarketing.simplejavamail.mailer.MailerBuilder.MailerRegularBuilder#trustingSSLHosts(String...)
	 */
	public List<String> getSslHostsToTrust() {
		return sslHostsToTrust;
	}
	
	/**
	 * @see ca.sprintmarketing.simplejavamail.mailer.MailerBuilder.MailerRegularBuilder#trustingAllHosts(Boolean)
	 */
	public boolean isTrustAllSSLHost() {
		return trustAllSSLHost;
	}
	
	/**
	 * @see ca.sprintmarketing.simplejavamail.mailer.MailerBuilder.MailerRegularBuilder#withProperties(Properties)
	 */
	public Properties getProperties() {
		return properties;
	}

	public boolean isValidationBypass() { return validationBypass; }
}