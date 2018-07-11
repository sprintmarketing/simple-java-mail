package ca.sprintmarketing.simplejavamail.converter.internal.mimemessage;

import ca.sprintmarketing.simplejavamail.email.Email;

import javax.mail.internet.MimeMessage;

/**
 * This interface only serves to hide the DKIM implementation behind an easy-to-load-with-reflection class.
 */
interface IDKIMSigner {
	/**
	 * @see MimeMessageHelper#signMessageWithDKIM(MimeMessage, Email)
	 */
	MimeMessage signMessageWithDKIM(MimeMessage messageToSign, Email emailContainingSigningDetails);
}
