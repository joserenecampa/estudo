package gov.demoiselle.desktop.command;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.google.gson.Gson;

public abstract class ParameterCommand<T> extends AbstractCommand {

	private static final String MESSAGE_ERRO = "{\"erro\" : \"Erro ao tentar interpretar "
			+ "os parametros do comando.\" }";

	public abstract String doCommand(T data);

	public String doCommand(String params) {
		Gson gson = new Gson();
		Object dataObject = null;
		try {
			Type sooper = getClass().getGenericSuperclass();
		    Type t = ((ParameterizedType)sooper).getActualTypeArguments()[0];
		    Class type = Class.forName(t.getTypeName());
			dataObject = gson.fromJson(params, type);
		} catch (Throwable errorData) {
			return ParameterCommand.MESSAGE_ERRO;
		}
		if (dataObject == null) {
			return ParameterCommand.MESSAGE_ERRO;
		}

		return this.doCommand((T)dataObject);
	}

}
