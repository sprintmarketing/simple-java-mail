package ca.sprintmarketing.simplejavamail.converter.internal.mimemessage;

import ca.sprintmarketing.simplejavamail.email.Email;

import javax.annotation.Nonnull;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

class MimeMessageProducerAlternative extends MimeMessageProducer {
	@Override
	boolean compatibleWithEmail(@Nonnull Email email) {
		return !emailContainsMixedContent(email) && !emailContainsRelatedContent(email) && emailContainsAlternativeContent(email);
	}
	
	@Override
	void populateMimeMessageMultipartStructure(@Nonnull MimeMessage message, @Nonnull Email email) throws MessagingException {
		MimeMultipart multipartRootAlternative = new MimeMultipart("alternative");
		MimeMessageHelper.setTexts(email, multipartRootAlternative);
		message.setContent(multipartRootAlternative);
	}
}