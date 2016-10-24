package gov.demoiselle.desktop.command;

public class Status extends AbstractCommand {

	public String doCommand(String params) {
		return "{ \"status:\" : \"OK\" }";
	}

}
