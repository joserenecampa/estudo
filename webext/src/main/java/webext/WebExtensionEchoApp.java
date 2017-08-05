package webext;

import org.apache.log4j.Logger;

public class WebExtensionEchoApp {
	
	private final static Logger LOGGER = Logger.getLogger(WebExtensionEchoApp.class.getName());
	
	public static void main(String[] args) throws Throwable {
		LOGGER.info("Iniciando a aplicação Java Echo App!");
		while (true) {
			LOGGER.info("Vou esperar o stream do browser AGORA!!!");
			String message = WebExtensionUtil.readMessage(System.in);
			LOGGER.info("Leitura retornada com a mensagem: [" + message + "]");
			if (message == null || message.trim().isEmpty()) {
				LOGGER.info("Não houve retorno. Finalizando Aplicativo Nativo");
				System.exit(1);
			}
			if (message != null) {
				LOGGER.info("Enviando a mensagem de echo.");
				WebExtensionUtil.writeMessage("\"pong from java\"", System.out);
				LOGGER.info("Mensagem enviada!");
			} else {
				LOGGER.info("Retorno [false]. Finalizando Aplicativo Nativo");
				System.exit(1);
			}
		}
	}

}
