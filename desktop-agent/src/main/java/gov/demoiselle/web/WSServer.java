package gov.demoiselle.web;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.websocket;

import io.undertow.Undertow;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;

public class WSServer extends AbstractReceiveListener {

	private static final String HOST_WS_SERVER = "localhost";
	private static final int PORT_WS_SERVER = 9091;
//	private static final int PORT_WS_SSL_SERVER = 9092;
	public static Undertow UNDERTOWN_HTTP = null;
//	public static Undertow UNDERTOWN_SSL = null;

	public WSServer() {
		this.initializeWSServer(HOST_WS_SERVER, PORT_WS_SERVER);
//		this.initializeWSSSLServer(HOST_WS_SERVER, PORT_WS_SSL_SERVER);
		WSServer.start();
	}

	public void initializeWSServer(String host, int port) {
		final WSServer listener = this;
		WSServer.UNDERTOWN_HTTP = Undertow.builder().addHttpListener(port, host)
				.setHandler(path().addPrefixPath("/", websocket(new WebSocketConnectionCallback() {
					public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
						channel.getReceiveSetter().set(listener);
						channel.resumeReceives();
					}
				}))).build();
	}

//	public void initializeWSSSLServer(String host, int port) {
//		try {
//			SSLUtil sslUtil = new SSLUtil();
//			final WSServer listener = this;
//			WSServer.UNDERTOWN_SSL = Undertow.builder().addHttpsListener(port, host, sslUtil.createSslContext())
//					.setHandler(path().addPrefixPath("/", websocket(new WebSocketConnectionCallback() {
//						public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
//							channel.getReceiveSetter().set(listener);
//							channel.resumeReceives();
//						}
//					}))).build();
//		} catch (Throwable error) {
//			error.printStackTrace();
//		}
//	}

	public static void start() {
		WSServer.UNDERTOWN_HTTP.start();
//		WSServer.UNDERTOWN_SSL.start();
	}

	public static void stop() {
		WSServer.UNDERTOWN_HTTP.stop();
//		WSServer.UNDERTOWN_SSL.stop();
	}

	protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {
		final String messageData = message.getData();
		String result = null;
		try {
			ExecuteCommand execute = new ExecuteCommand();
			result = execute.executeCommand(messageData);
		} catch (InterpreterJsonException error) {
			replyToAll(channel, "{ \"erro\": \"Erro ao tentar interpretar o JSON\"}");
		} catch (Throwable error) {
			replyToAll(channel, "{ \"erro\": \"" + error.getMessage() + "\"}");
		}
		replyToAll(channel, result);
	}

	protected void replyToAll(WebSocketChannel channel, String messageData) {
		for (WebSocketChannel session : channel.getPeerConnections()) {
			try {
				WebSockets.sendText(messageData, session, null);
			} catch (Throwable error) {
			}
		}
	}
	
	public static void main(String[] args) {
		new WSServer();
	}
}