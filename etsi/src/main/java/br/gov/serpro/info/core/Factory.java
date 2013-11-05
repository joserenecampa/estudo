package br.gov.serpro.info.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ServiceLoader;

public class Factory {
	
	private static Collection getServices(Class clazz) {
		ServiceLoader serviceLoaders = ServiceLoader.load(clazz);
		if (serviceLoaders != null && serviceLoaders.iterator() != null) {
			Collection result = Collections.synchronizedList(new ArrayList());
			for (Object service : serviceLoaders)
				result.add(service);
			return result;
		}
		return null;
	}

	public static Collection<Loader> getLoaders() {
		return Factory.getServices(Loader.class);
	}

	public static Collection<Notifier> getNotifiers() {
		return Factory.getServices(Notifier.class);
	}

	public static Collection<Verifier> getVerifiers() {
		return Factory.getServices(Verifier.class);
	}
}
