package ca.sprintmarketing.simplejavamail.converter.internal.mimemessage;

import ca.sprintmarketing.simplejavamail.MailException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static ca.sprintmarketing.simplejavamail.internal.util.Preconditions.checkNonEmptyArgument;

/**
 * This exception is used to communicate errors during parsing of a {@link javax.mail.internet.MimeMessage}.
 */
@SuppressWarnings("serial")
class MimeMessageParseException extends MailException {

	static final String ERROR_PARSING_FROMADDRESS = "Error parsing from-address";
	static final String ERROR_PARSING_ADDRESS = "Error parsing [%s] address";
	static final String ERROR_PARSING_DISPOSITION = "Error parsing MimeMessage disposition";
	static final String ERROR_PARSING_CONTENT = "Error parsing MimeMessage Content";
	static final String ERROR_PARSING_MULTIPART_COUNT = "Error parsing MimeMessage multipart count";
	static final String ERROR_GETTING_BODYPART_AT_INDEX = "Error getting bodypart at index %s";
	static final String ERROR_GETTING_CONTENT_ID = "Error getting content ID";
	static final String ERROR_GETTING_CALENDAR_CONTENTTYPE = "Error getting content type from Calendar bodypart. Unable to determine Calendar METHOD";
	static final String ERROR_GETTING_FILENAME = "Error getting file name";
	static final String ERROR_GETTING_ALL_HEADERS = "Error getting all headers";
	static final String ERROR_GETTING_DATAHANDLER = "Error getting data handler";
	static final String ERROR_GETTING_CONTENT_TYPE = "Error getting content type";
	static final String ERROR_GETTING_INPUTSTREAM = "Error getting input stream";
	static final String ERROR_READING_CONTENT = "Error reading content";
	static final String ERROR_DECODING_TEXT = "Error decoding text";
	static final String ERROR_GETTING_RECIPIENTS = "Error getting [%s] recipient types";
	static final String ERROR_GETTING_SUBJECT = "Error getting subject";
	static final String ERROR_GETTING_MESSAGE_ID = "Error getting message ID";
	static final String ERROR_PARSING_REPLY_TO_ADDRESSES = "Error parsing replyTo addresses";
	static final String ERROR_SIGNING_DKIM_LIBRARY_MISSING = "DKIM library not found, make sure it is on the classpath (https://github.com/markenwerk/java-utils-mail-dkim)";
	static final String ERROR_LOADING_DKIM_LIBRARY = "Error loading DKIM library...";
	static final String ERROR_SIGNING_DKIM_INVALID_DOMAINKEY = "Error signing MimeMessage with DKIM";
	
	MimeMessageParseException(@Nonnull final String message, @Nullable final Exception cause) {
		super(checkNonEmptyArgument(message, "message"), cause);
	}
}