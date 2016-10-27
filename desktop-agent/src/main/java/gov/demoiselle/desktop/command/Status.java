package gov.demoiselle.desktop.command;

import gov.demoiselle.desktop.Command;

public class Status implements Command {

	public String doCommand(String params) {
		return "{ \"status:\" : \"OK\" }";
	}
	
	public String getCommandName() {
		return this.getClass().getSimpleName().toLowerCase();
	}
	

}
