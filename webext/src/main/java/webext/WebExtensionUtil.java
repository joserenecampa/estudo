package webext;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.log4j.Logger;

public class WebExtensionUtil {
	
	private final static Logger LOGGER = Logger.getLogger(WebExtensionUtil.class.getName());
	
	public static final byte[] toNativeBytes(int value) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try { dos.writeInt(value); } catch (IOException e) { }
		ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		return buffer.array();
	}
	
	public static final int fromNativeBytes(byte[] value) {
		ByteBuffer buffer = ByteBuffer.wrap(value);
		buffer.order(ByteOrder.BIG_ENDIAN);
		return buffer.getInt();
	}

	public static void writeMessage(final String message, final OutputStream os) {
		// TODO: Verificar o tamanho da mensagem
		// A especificacao WebExtension Native API
		// permite o envio de apenas 1mb por mensagem.
		LOGGER.info("Enviando mensagem: " + message + " para OutputStream");
		DataOutputStream dos = new DataOutputStream(os);
		try {
			// escrevendo 4 bytes iniciais que
			// representam o tamanho da mensagem
			// 1048576 bytes exatamente
			dos.write(toNativeBytes(message.length()));
			// escrevendo a mensagem
			dos.write(message.getBytes("UTF-8"));
			dos.flush();
			LOGGER.info("Enviando com sucesso");
		} catch (IOException error) { 
			LOGGER.info("Erro ao tentar enviar a mensagem [" + message + "]. ", error);
		}
	}
	
	public static String readMessage(final InputStream is) {
		DataInputStream dis = new DataInputStream(is);
		int length = 0;
		byte[] readLength = new byte[4];
		try {
			// lendo 4 bytes, convertendo para INT
			// o que representando o tamanho
			// da mensagem a ser lida posteriormente
			dis.read(readLength);
			length = fromNativeBytes(readLength);
			LOGGER.info("Recebendo mensagem. Tamanho [" + length + "].");
		} catch (IOException error) {
			LOGGER.info("Erro ao ler o tamanho da mensagem", error);
			return null;
		}
		// TODO: verificar o tamanho da mensagem
		// e se cabe na memoria disponivel para
		// o programa. Verificar a Java Heap Space
		// A especificacao do WebExtension Native API
		// permite ate 4gb de informacao para o app
		// nativo.
		byte[] message = new byte[length];
		try {
			// lendo o restante da mensagem
			dis.readFully(message);
			LOGGER.info("Mensagem lida com sucesso.");
		} catch (IOException error) {
			LOGGER.info("Erro ao ler a mensagem", error);
			return null;
		}
		String result = null;
		try {
			result = new String(message, "UTF-8");
			LOGGER.info("Mensagem em String UTF-8 [" + message + "].");
		} catch (UnsupportedEncodingException error) {
			LOGGER.info("Erro ao converter a mensagem em String UTF-8", error);
		}
		return result;
	}
	
}
