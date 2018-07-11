package ca.sprintmarketing.simplejavamail.converter;

import ca.sprintmarketing.simplejavamail.email.Email;
import ca.sprintmarketing.simplejavamail.email.EmailPopulatingBuilder;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.InputStream;

public interface IOutlookEmailConverter {
	Email outlookMsgToEmail(@Nonnull final File msgFile);
	Email outlookMsgToEmail(@Nonnull final String msgData);
	EmailPopulatingBuilder outlookMsgToEmailBuilder(@Nonnull final InputStream msgInputStream);
}