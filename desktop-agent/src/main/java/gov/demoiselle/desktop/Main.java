package gov.demoiselle.desktop;

import javax.servlet.ServletException;

import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;

import gov.demoiselle.web.WebApplication;
import io.undertow.Undertow;

public class Main {

	private static final String HOST = "127.0.0.1";
	private static final int PORT = 9090;

	public static void main(String[] args) throws ServletException {
		UndertowJaxrsServer ut = new UndertowJaxrsServer();
		WebApplication ta = new WebApplication();
		ut.deploy(ta);
		ut.start(Undertow.builder().addHttpListener(PORT, HOST));
	}

}
