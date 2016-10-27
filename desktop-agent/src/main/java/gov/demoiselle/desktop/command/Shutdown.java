package gov.demoiselle.desktop.command;

import gov.demoiselle.desktop.Command;

public class Shutdown implements Command {

	public String doCommand(String params) {
		System.exit(0);
		return null;
	}

	public String getCommandName() {
		return this.getClass().getSimpleName().toLowerCase();
	}
	

}
