package gov.demoiselle.desktop.command;

import java.lang.reflect.ParameterizedType;

import com.google.gson.Gson;

import gov.demoiselle.desktop.Command;

public abstract class AbstractCommand<Request, Response> implements Command {

	private static final String MESSAGE_ERRO = "{\"erro\" : \"Erro ao tentar interpretar "
			+ "os parametros do comando.\" }";

	public abstract Response doCommand(Request request);

	public String getCommandName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	public String doCommand(String params) {
		Gson gson = new Gson();
		Request request = null;
		try {
			Class<Request> type = (Class<Request>) ((ParameterizedType) getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
			request = gson.fromJson(params, type);
		} catch (Throwable errorData) {
			return AbstractCommand.MESSAGE_ERRO;
		}
		if (request == null)
			return AbstractCommand.MESSAGE_ERRO;
		Response response = this.doCommand(request);
		String resultJson = gson.toJson(response);
		return resultJson;
	}

}
