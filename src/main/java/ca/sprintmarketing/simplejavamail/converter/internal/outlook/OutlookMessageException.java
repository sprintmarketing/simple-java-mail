package ca.sprintmarketing.simplejavamail.converter.internal.outlook;

import ca.sprintmarketing.simplejavamail.MailException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static ca.sprintmarketing.simplejavamail.internal.util.Preconditions.checkNonEmptyArgument;

/**
 * This exception is used to communicate errors during parsing of a MsgParser {@link ca.sprintmarketing.simplejavamail.outlookmessageparser.model.OutlookMessage} of
 * Outlook .msg data.
 */
@SuppressWarnings("serial")
class OutlookMessageException extends MailException {

	static final String ERROR_PARSING_OUTLOOK_MSG = "Unable to parse Outlook message";

	OutlookMessageException(@SuppressWarnings("SameParameterValue") @Nonnull final String message, @Nullable final Exception cause) {
		super(checkNonEmptyArgument(message, "message"), cause);
	}
}