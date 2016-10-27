package gov.demoiselle.desktop.command.policy;

import gov.demoiselle.web.Request;

public class ListPoliciesRequest extends Request {
	
	public ListPoliciesRequest() {
		super.setCommand("listpolicies");
	}
	
}
