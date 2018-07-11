

package ca.sprintmarketing.simplejavamail.mailer.internal.socks.socks5client;

import ca.sprintmarketing.simplejavamail.internal.util.MiscUtil;

@SuppressWarnings("SameParameterValue")
public class KeyStoreInfo {

	private final String keyStorePath;
	private final String password;
	private final String type /*= "JKS"*/;

//	public KeyStoreInfo() {
//	}

	public KeyStoreInfo(final String keyStorePath, final String password, final String type) {
		this.keyStorePath = MiscUtil.checkNotNull(keyStorePath, "Argument [keyStorePath] may not be null");
		this.password = MiscUtil.checkNotNull(password, "Argument [password] may not be null");
		this.type = MiscUtil.checkNotNull(type, "Argument [type] may not be null");
	}

//	public KeyStoreInfo(final String keyStorePath, final String password) {
//		this(keyStorePath, password, "JKS");
//	}

	public String getKeyStorePath() {
		return keyStorePath;
	}

	public String getPassword() {
		return password;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "[KEY STORE] PATH:" + keyStorePath + " PASSWORD:" + password + " TYPE:" + type;
	}

}
