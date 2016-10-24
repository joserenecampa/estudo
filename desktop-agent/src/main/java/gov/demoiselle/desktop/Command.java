package gov.demoiselle.desktop;

public interface Command {
	
	public String getCommandName();
	public String doCommand(String params);

}
