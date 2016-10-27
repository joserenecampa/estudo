package gov.demoiselle.desktop.command.policy;

import java.util.HashSet;

import gov.demoiselle.web.Response;

public class ListPoliciesResponse extends Response {
	
	private HashSet<String> policies = new HashSet<String>();
	
	public ListPoliciesResponse() {
		super.setCommand("listpolicies");
	}

	public HashSet<String> getPolicies() {
		return policies;
	}

	public void setPolicies(HashSet<String> policies) {
		this.policies = policies;
	}
	
}
