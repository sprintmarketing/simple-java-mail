package sockslib.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sockslib.common.net.MonitorSocketWrapper;
import sockslib.common.net.NetworkMonitor;
import sockslib.server.msg.ReadableMessage;
import sockslib.server.msg.WritableMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

class SocksSession {

	private static final Logger logger = LoggerFactory.getLogger(SocksSession.class);

	private static int nextSessionId = 0;

	private final Socket socket;

	private final long id = ++nextSessionId;

	private InputStream inputStream;

	private OutputStream outputStream;

	private final SocketAddress clientAddress;

	public SocksSession(Socket socket) {
		if (!socket.isConnected()) {
			throw new IllegalArgumentException("Socket should be a connected socket");
		}
		if (socket instanceof MonitorSocketWrapper) {
			((MonitorSocketWrapper) socket).setNetworkMonitor(new NetworkMonitor());
		}
		this.socket = socket;
		try {
			inputStream = this.socket.getInputStream();
			outputStream = this.socket.getOutputStream();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		clientAddress = socket.getRemoteSocketAddress();
	}

	public Socket getSocket() {
		return socket;
	}

	public void write(WritableMessage message)
			throws IOException {
		byte[] bytes = message.getBytes();
		outputStream.write(bytes, 0, bytes.length);
		outputStream.flush();
	}

	public void read(ReadableMessage message)
			throws IOException {
		message.read(inputStream);
	}

	public long getId() {
		return id;
	}

	public void close() {
		try {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
			if (socket != null && !socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public String toString() {
		return "SESSION[" + id + "]" + "@" + clientAddress;
	}

}
