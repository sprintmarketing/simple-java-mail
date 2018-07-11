package ca.sprintmarketing.simplejavamail.converter.internal.mimemessage;

import ca.sprintmarketing.simplejavamail.email.Email;

import javax.annotation.Nonnull;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

class MimeMessageProducerMixedAlternative extends MimeMessageProducer {
	@Override
	boolean compatibleWithEmail(@Nonnull Email email) {
		return emailContainsMixedContent(email) && !emailContainsRelatedContent(email) && emailContainsAlternativeContent(email);
	}
	
	@Override
	void populateMimeMessageMultipartStructure(@Nonnull MimeMessage message, @Nonnull Email email) throws MessagingException {
		MultipartStructureWrapper multipartStructureWrapper = new MultipartStructureWrapper();
		
		MimeMessageHelper.setTexts(email, multipartStructureWrapper.multipartAlternativeMessages);
		MimeMessageHelper.configureForwarding(email, multipartStructureWrapper.multipartRootMixed);
		MimeMessageHelper.setAttachments(email, multipartStructureWrapper.multipartRootMixed);
		
		message.setContent(multipartStructureWrapper.multipartRootMixed);
	}
	
	/**
	 * refer to {@link MimeMessageProducerMixedRelatedAlternative}.
	 */
	private static class MultipartStructureWrapper {
		
		private final MimeMultipart multipartRootMixed;
		private final MimeMultipart multipartAlternativeMessages;
		
		private MultipartStructureWrapper() {
			multipartRootMixed = new MimeMultipart("mixed");
			final MimeBodyPart contentAlternativeMessages = new MimeBodyPart();
			multipartAlternativeMessages = new MimeMultipart("alternative");
			try {
				// construct mail structure
				multipartRootMixed.addBodyPart(contentAlternativeMessages);
				contentAlternativeMessages.setContent(multipartAlternativeMessages);
			} catch (final MessagingException e) {
				throw new MimeMessageParseException(e.getMessage(), e);
			}
		}
	}
}