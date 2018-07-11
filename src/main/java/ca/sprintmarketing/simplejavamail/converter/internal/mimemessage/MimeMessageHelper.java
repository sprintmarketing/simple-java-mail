package ca.sprintmarketing.simplejavamail.converter.internal.mimemessage;

import net.markenwerk.utils.mail.dkim.DkimMessage;
import net.markenwerk.utils.mail.dkim.DkimSigner;
import ca.sprintmarketing.simplejavamail.email.AttachmentResource;
import ca.sprintmarketing.simplejavamail.email.Email;
import ca.sprintmarketing.simplejavamail.email.Recipient;
import ca.sprintmarketing.simplejavamail.internal.util.MiscUtil;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.Nonnull;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static java.lang.String.format;
import static ca.sprintmarketing.simplejavamail.internal.util.MiscUtil.valueNullOrEmpty;

/**
 * Helper class that produces and populates a mime messages. Deals with javax.mail RFC MimeMessage stuff, as well as DKIM signing.
 */
public class MimeMessageHelper {
	
	/**
	 * Encoding used for setting body text, email address, headers, reply-to fields etc. ({@link StandardCharsets#UTF_8}).
	 */
	private static final String CHARACTER_ENCODING = StandardCharsets.UTF_8.name();

	private MimeMessageHelper() {

	}
	
	static void setSubject(Email email, MimeMessage message) throws MessagingException {
		message.setSubject(email.getSubject(), CHARACTER_ENCODING);
	}
	
	static void setFrom(Email email, MimeMessage message) throws UnsupportedEncodingException, MessagingException {
		if (email.getFromRecipient() != null) {
			message.setFrom(new InternetAddress(email.getFromRecipient().getAddress(), email.getFromRecipient().getName(), CHARACTER_ENCODING));
		}
	}
	
	/**
	 * Fills the {@link Message} instance with recipients from the {@link Email}.
	 *
	 * @param email   The message in which the recipients are defined.
	 * @param message The javax message that needs to be filled with recipients.
	 * @throws UnsupportedEncodingException See {@link InternetAddress#InternetAddress(String, String)}.
	 * @throws MessagingException           See {@link Message#addRecipient(Message.RecipientType, Address)}
	 */
	static void setRecipients(final Email email, final Message message)
			throws UnsupportedEncodingException, MessagingException {
		for (final Recipient recipient : email.getRecipients()) {
			final Address address = new InternetAddress(recipient.getAddress(), recipient.getName(), CHARACTER_ENCODING);
			message.addRecipient(recipient.getType(), address);
		}
	}
	
	/**
	 * Fills the {@link Message} instance with reply-to address.
	 *
	 * @param email   The message in which the recipients are defined.
	 * @param message The javax message that needs to be filled with reply-to address.
	 * @throws UnsupportedEncodingException See {@link InternetAddress#InternetAddress(String, String)}.
	 * @throws MessagingException           See {@link Message#setReplyTo(Address[])}
	 */
	static void setReplyTo(final Email email, final Message message)
			throws UnsupportedEncodingException, MessagingException {
		final Recipient replyToRecipient = email.getReplyToRecipient();
		if (replyToRecipient != null) {
			final InternetAddress replyToAddress = new InternetAddress(replyToRecipient.getAddress(), replyToRecipient.getName(),
					CHARACTER_ENCODING);
			message.setReplyTo(new Address[] { replyToAddress });
		}
	}
	
	/**
	 * Fills the {@link Message} instance with the content bodies (text, html and calendar).
	 *
	 * @param email                        The message in which the content is defined.
	 * @param multipartAlternativeMessages See {@link MimeMultipart#addBodyPart(BodyPart)}
	 * @throws MessagingException See {@link BodyPart#setText(String)}, {@link BodyPart#setContent(Object, String)} and {@link
	 *                            MimeMultipart#addBodyPart(BodyPart)}.
	 */
	static void setTexts(final Email email, final MimeMultipart multipartAlternativeMessages)
			throws MessagingException {
		if (email.getPlainText() != null) {
			final MimeBodyPart messagePart = new MimeBodyPart();
			messagePart.setText(email.getPlainText(), CHARACTER_ENCODING);
			multipartAlternativeMessages.addBodyPart(messagePart);
		}
		if (email.getHTMLText() != null) {
			final MimeBodyPart messagePartHTML = new MimeBodyPart();
			messagePartHTML.setContent(email.getHTMLText(), "text/html; charset=\"" + CHARACTER_ENCODING + "\"");
			multipartAlternativeMessages.addBodyPart(messagePartHTML);
		}
		if (email.getCalendarText() != null && email.getCalendarMethod() != null) {
			final MimeBodyPart messagePartCalendar = new MimeBodyPart();
			messagePartCalendar.setContent(email.getCalendarText(), "text/calendar; charset=\"" + CHARACTER_ENCODING + "\"; method=\"" + email.getCalendarMethod().toString() + "\"");
			multipartAlternativeMessages.addBodyPart(messagePartCalendar);
		}
	}
	
	/**
	 * Fills the {@link MimeBodyPart} instance with the content body content (text, html and calendar).
	 *
	 * @param email       The message in which the content is defined.
	 * @param messagePart The {@link MimeBodyPart} that will contain the body content (either plain text, HTML text or iCalendar text)
	 *
	 * @throws MessagingException See {@link BodyPart#setText(String)}, {@link BodyPart#setContent(Object, String)}.
	 */
	static void setTexts(final Email email, final MimePart messagePart)
			throws MessagingException {
		if (email.getPlainText() != null) {
			messagePart.setText(email.getPlainText(), CHARACTER_ENCODING);
		}
		if (email.getHTMLText() != null) {
			messagePart.setContent(email.getHTMLText(), "text/html; charset=\"" + CHARACTER_ENCODING + "\"");
		}
		if (email.getCalendarText() != null && email.getCalendarMethod() != null) {
			messagePart.setContent(email.getCalendarText(), "text/calendar; charset=\"" + CHARACTER_ENCODING + "\"; method=\"" + email.getCalendarMethod().toString() + "\"");
		}
	}
	
	/**
	 * If provided, adds the {@code emailToForward} as a MimeBodyPart to the mixed multipart root.
	 * <p>
	 * <strong>Note:</strong> this is done without setting {@code Content-Disposition} so email clients can choose
	 * how to display embedded forwards. Most client will show the forward as inline, some may show it as attachment.
	 */
	static void configureForwarding(@Nonnull final Email email, @Nonnull final MimeMultipart multipartRootMixed) {
		if (email.getEmailToForward() != null) {
			try {
				final BodyPart fordwardedMessage = new MimeBodyPart();
				fordwardedMessage.setContent(email.getEmailToForward(), "message/rfc822");
				multipartRootMixed.addBodyPart(fordwardedMessage);
			} catch (final MessagingException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Fills the {@link Message} instance with the embedded images from the {@link Email}.
	 *
	 * @param email            The message in which the embedded images are defined.
	 * @param multipartRelated The branch in the email structure in which we'll stuff the embedded images.
	 * @throws MessagingException See {@link MimeMultipart#addBodyPart(BodyPart)} and {@link #getBodyPartFromDatasource(AttachmentResource, String)}
	 */
	static void setEmbeddedImages(final Email email, final MimeMultipart multipartRelated)
			throws MessagingException {
		for (final AttachmentResource embeddedImage : email.getEmbeddedImages()) {
			multipartRelated.addBodyPart(getBodyPartFromDatasource(embeddedImage, Part.INLINE));
		}
	}
	
	/**
	 * Fills the {@link Message} instance with the attachments from the {@link Email}.
	 *
	 * @param email         The message in which the attachments are defined.
	 * @param multipartRoot The branch in the email structure in which we'll stuff the attachments.
	 * @throws MessagingException See {@link MimeMultipart#addBodyPart(BodyPart)} and {@link #getBodyPartFromDatasource(AttachmentResource, String)}
	 */
	static void setAttachments(final Email email, final MimeMultipart multipartRoot)
			throws MessagingException {
		for (final AttachmentResource resource : email.getAttachments()) {
			multipartRoot.addBodyPart(getBodyPartFromDatasource(resource, Part.ATTACHMENT));
		}
	}
	
	/**
	 * Sets all headers on the {@link Message} instance. Since we're not using a high-level JavaMail method, the JavaMail library says we need to do
	 * some encoding and 'folding' manually, to get the value right for the headers (see {@link MimeUtility}.
	 * <p>
	 * Furthermore sets the notification flags <code>Disposition-Notification-To</code> and <code>Return-Receipt-To</code> if provided. It used
	 * JavaMail's built in method for producing an RFC compliant email address (see {@link InternetAddress#toString()}).
	 *
	 * @param email   The message in which the headers are defined.
	 * @param message The {@link Message} on which to set the raw, encoded and folded headers.
	 * @throws UnsupportedEncodingException See {@link MimeUtility#encodeText(String, String, String)}
	 * @throws MessagingException           See {@link Message#addHeader(String, String)}
	 * @see MimeUtility#encodeText(String, String, String)
	 * @see MimeUtility#fold(int, String)
	 */
	static void setHeaders(final Email email, final Message message)
			throws UnsupportedEncodingException, MessagingException {
		// add headers (for raw message headers we need to 'fold' them using MimeUtility
		for (final Map.Entry<String, String> header : email.getHeaders().entrySet()) {
			final String headerName = header.getKey();
			final String headerValue = MimeUtility.encodeText(header.getValue(), CHARACTER_ENCODING, null);
			final String foldedHeaderValue = MimeUtility.fold(headerName.length() + 2, headerValue);
			message.addHeader(header.getKey(), foldedHeaderValue);
		}
		
		if (email.isUseDispositionNotificationTo()) {
			final Address address = new InternetAddress(email.getDispositionNotificationTo().getAddress(),
					email.getDispositionNotificationTo().getName(), CHARACTER_ENCODING);
			message.setHeader("Disposition-Notification-To", address.toString());
		}
		
		if (email.isUseReturnReceiptTo()) {
			final Address address = new InternetAddress(email.getReturnReceiptTo().getAddress(),
					email.getReturnReceiptTo().getName(), CHARACTER_ENCODING);
			message.setHeader("Return-Receipt-To", address.toString());
		}
	}
	
	/**
	 * Helper method which generates a {@link BodyPart} from an {@link AttachmentResource} (from its {@link DataSource}) and a disposition type
	 * ({@link Part#INLINE} or {@link Part#ATTACHMENT}). With this the attachment data can be converted into objects that fit in the email structure.
	 * <p>
	 * For every attachment and embedded image a header needs to be set.
	 *
	 * @param attachmentResource An object that describes the attachment and contains the actual content data.
	 * @param dispositionType    The type of attachment, {@link Part#INLINE} or {@link Part#ATTACHMENT} .
	 *
	 * @return An object with the attachment data read for placement in the email structure.
	 * @throws MessagingException All BodyPart setters.
	 */
	private static BodyPart getBodyPartFromDatasource(final AttachmentResource attachmentResource, final String dispositionType)
			throws MessagingException {
		final BodyPart attachmentPart = new MimeBodyPart();
		// setting headers isn't working nicely using the javax mail API, so let's do that manually
		final String resourceName = determineResourceName(attachmentResource, false);
		final String fileName = determineResourceName(attachmentResource, true);
		attachmentPart.setDataHandler(new DataHandler(new NamedDataSource(fileName, attachmentResource.getDataSource())));
		attachmentPart.setFileName(fileName);
		final String contentType = attachmentResource.getDataSource().getContentType();
		attachmentPart.setHeader("Content-Type", contentType + "; filename=" + fileName + "; name=" + resourceName);
		attachmentPart.setHeader("Content-ID", format("<%s>", resourceName));
		attachmentPart.setDisposition(dispositionType);
		return attachmentPart;
	}

	/**
	 * Determines the right resource name and optionally attaches the correct extension to the name.
	 */
	static String determineResourceName(final AttachmentResource attachmentResource, final boolean includeExtension) {
		final String datasourceName = attachmentResource.getDataSource().getName();

		String resourceName;

		if (!valueNullOrEmpty(attachmentResource.getName())) {
			resourceName = attachmentResource.getName();
		} else if (!valueNullOrEmpty(datasourceName)) {
			resourceName = datasourceName;
		} else {
			resourceName = "resource" + UUID.randomUUID();
		}
		if (includeExtension && !valueNullOrEmpty(datasourceName)) {
			@SuppressWarnings("UnnecessaryLocalVariable") final
			String possibleFilename = datasourceName;
			if (!resourceName.contains(".") && possibleFilename.contains(".")) {
				final String extension = possibleFilename.substring(possibleFilename.lastIndexOf("."), possibleFilename.length());
				if (!resourceName.endsWith(extension)) {
					resourceName += extension;
				}
			}
		} else if (!includeExtension && resourceName.contains(".") && resourceName.equals(datasourceName)) {
			final String extension = resourceName.substring(resourceName.lastIndexOf("."), resourceName.length());
			resourceName = resourceName.replace(extension, "");
		}
		return resourceName;
	}
	
	/**
	 * Primes the {@link MimeMessage} instance for signing with DKIM. The signing itself is performed by {@link DkimMessage} and {@link DkimSigner}
	 * during the physical sending of the message.
	 *
	 * @param messageToSign                 The message to be signed when sent.
	 * @param emailContainingSigningDetails The {@link Email} that contains the relevant signing information
	 *
	 * @return The original mime message wrapped in a new one that performs signing when sent.
	 */
	public static MimeMessage signMessageWithDKIM(final MimeMessage messageToSign, final Email emailContainingSigningDetails) {
		return MiscUtil.<IDKIMSigner>loadLibraryClass(
				"net.markenwerk.utils.mail.dkim.DkimSigner",
				"ca.sprintmarketing.simplejavamail.converter.internal.mimemessage.DKIMSigner",
				MimeMessageParseException.ERROR_SIGNING_DKIM_LIBRARY_MISSING,
				MimeMessageParseException.ERROR_LOADING_DKIM_LIBRARY)
				.signMessageWithDKIM(messageToSign, emailContainingSigningDetails);
	}
}