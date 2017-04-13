package webext;

import org.apache.log4j.Logger;

public class WebExtensionEchoApp {
	
	private final static Logger LOGGER = Logger.getLogger(WebExtensionEchoApp.class.getName());
	
	public static void main(String[] args) throws Throwable {
		LOGGER.info("Iniciando a aplicação Java Echo App!");
		boolean execute = true;
		while (true) {
			LOGGER.info("Vou esperar o stream do browser AGORA!!!");
			System.in.available();
			String message = WebExtensionUtil.readMessage(System.in);
			LOGGER.info("Leitura retornada com a mensagem: [" + message + "]");
			if (message != null && !message.equalsIgnoreCase("false")) {
				LOGGER.info("Enviando a mensagem de echo.");
				WebExtensionUtil.writeMessage("echo from java: " + message, System.out);
				LOGGER.info("Mensagem enviada!");
			}
		}
	}

}
