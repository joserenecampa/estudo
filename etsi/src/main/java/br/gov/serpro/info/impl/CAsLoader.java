package br.gov.serpro.info.impl;

import java.security.cert.X509Certificate;
import java.util.Map;

import br.gov.serpro.info.core.Context;
import br.gov.serpro.info.core.Loader;

public class CAsLoader implements Loader {

	@Override
	public void load(Context context) {
		Map<String, X509Certificate> cas = KeyStoreUpdater.getInstance().getACsFromICPURL(false);
		for (String key : cas.keySet())
			context.addObject(cas.get(key));
	}

}
