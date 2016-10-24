package gov.demoiselle.desktop.command;

public class Shutdown extends AbstractCommand {

	public String doCommand(String params) {
		System.exit(0);
		return null;
	}

}
