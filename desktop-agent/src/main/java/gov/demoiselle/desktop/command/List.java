package gov.demoiselle.desktop.command;

import java.util.ServiceLoader;

import gov.demoiselle.desktop.Command;

public class List extends AbstractCommand {

	public String doCommand(String params) {
		StringBuilder builder = new StringBuilder();
		builder.append("{ \"commands\": [");
		ServiceLoader<Command> loader = ServiceLoader.load(Command.class);
		for (Command command : loader)
			builder.append("\"" + command.getCommandName() + "\", ");
		builder.append("\"\"]} ");
		return builder.toString().replaceAll(", \"\"", "");
	}

}
