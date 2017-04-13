package webext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Tests {
	
	public static void main(String[] args) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		WebExtensionUtil.writeMessage("{ 'json': 'teste' }", baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		System.out.println(WebExtensionUtil.readMessage(is));
	}

}
