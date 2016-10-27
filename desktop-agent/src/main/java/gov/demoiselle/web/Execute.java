package gov.demoiselle.web;

import java.util.ServiceLoader;

import com.google.gson.Gson;

import gov.demoiselle.desktop.Command;

public class Execute {
	
	public String executeCommand(Request request) {
		return this.executeCommand(request.toJson());
	}
	
	public String executeCommand(String messageData) {
		final Gson gson = new Gson();
		Request request = null;
		try {
			request = gson.fromJson(messageData, Request.class);
		} catch (Throwable error) {
			throw new InterpreterException(error);
		}
		if (request == null || request.getCommand() == null || request.getCommand().isEmpty())
			throw new RuntimeException("commando nao informado");
		ServiceLoader<Command> loader = ServiceLoader.load(Command.class);
		if (loader != null)
			for (Command commandLoaded : loader)
				if (commandLoaded.getCommandName().equalsIgnoreCase(request.getCommand()))
					return commandLoaded.doCommand(messageData);
		return null;
	}

}
