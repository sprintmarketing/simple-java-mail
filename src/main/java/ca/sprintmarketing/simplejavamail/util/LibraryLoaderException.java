package ca.sprintmarketing.simplejavamail.internal.util;

import ca.sprintmarketing.simplejavamail.MailException;

class LibraryLoaderException extends MailException {
	LibraryLoaderException(String message) {
		super(message);
	}
	
	LibraryLoaderException(String message, Throwable cause) {
		super(message, cause);
	}
}
