package gov.demoiselle.web;

import java.util.ServiceLoader;

import com.google.gson.Gson;

import gov.demoiselle.desktop.Command;

public class ExecuteCommand {
	
	public String executeCommand(String messageData) {
		final Gson gson = new Gson();
		CommandJson commandJson = null;
		try {
			commandJson = gson.fromJson(messageData, CommandJson.class);
		} catch (Throwable error) {
			throw new InterpreterJsonException(error);
		}
		if (commandJson == null || commandJson.getCommand() == null || commandJson.getCommand().isEmpty())
			throw new RuntimeException("nao foi informado qualquer commando");
		
		ServiceLoader<Command> loader = ServiceLoader.load(Command.class);
		if (loader != null)
			for (Command commandLoaded : loader)
				if (commandLoaded.getCommandName().equalsIgnoreCase(commandJson.getCommand()))
					return commandLoaded.doCommand(messageData);
		return null;
	}

}
