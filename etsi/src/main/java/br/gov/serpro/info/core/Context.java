package br.gov.serpro.info.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Context {
	
	public Map<String, Collection> objects = Collections.synchronizedMap(new HashMap<String, Collection>());
	private boolean notifier = true;
	
	public Context(boolean notifier) {
		this.notifier = notifier;
	}
	
	public void exec() {
		this.fillObjects();
		
		this.verifyObjects();
		
		if (this.notifier)
			this.notifyObjects();
	}
	
	public void fillObjects() {
		for(Loader loader : Factory.getLoaders())
			loader.load(this);
	}
	
	public void verifyObjects() {
		for(Verifier verify : Factory.getVerifiers())
			verify.verify(this);
	}

	public void notifyObjects() {
		for(Notifier notify : Factory.getNotifiers())
			notify.notify(this);
	}

	public void addObject(Object object) {
		String className = object.getClass().getName();
		Collection objects = this.objects.get(className);
		if (objects == null) {
			objects = new HashSet();
			this.objects.put(className, objects);
		}
		objects.add(object);
	}

	public Collection getObjects() {
		Collection result = Collections.synchronizedList(Collections.emptyList());
		for (String className : this.objects.keySet())
			result.addAll(this.objects.get(className));
		return result;
	}

	public Collection getObjects(Class clazz) {
		return this.objects.get(clazz.getName());
	}
	
	public static void main(String[] args) {
		Context context = new Context(true);
		context.exec();
	}
}
