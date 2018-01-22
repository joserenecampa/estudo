package gov.demoiselle.desktop;

import javax.servlet.ServletException;
import javax.swing.JOptionPane;

import gov.demoiselle.web.WSServer;

public class Main {
	
	public static void envValidate() {
		String version = System.getProperty("java.version");
		if (version == null || !version.startsWith("1.8")) {
			JOptionPane.showMessageDialog(null, "Java deve ser 8 ou superior", "Desktop Agent", 0, null);
			// TESTE
			System.exit(0);
		} 
		JOptionPane.showMessageDialog(null, "Desktop Agent OK!", "Desktop Agent", 1, null);
	}

	public static void main(String[] args) throws ServletException {
		Main.envValidate();
		new TrayIcon();
		new WSServer();
	}
}
