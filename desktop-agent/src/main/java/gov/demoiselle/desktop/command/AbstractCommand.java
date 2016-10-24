package gov.demoiselle.desktop.command;

import gov.demoiselle.desktop.Command;

public abstract class AbstractCommand implements Command {

	public String getCommandName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

}
