package gov.demoiselle.desktop.command.policy;

import br.gov.frameworkdemoiselle.policy.engine.factory.PolicyFactory.Policies;
import gov.demoiselle.desktop.command.AbstractCommand;
import gov.demoiselle.web.Execute;

public class ListPolicies extends AbstractCommand<ListPoliciesRequest, ListPoliciesResponse> {
	
	@Override
	public ListPoliciesResponse doCommand(ListPoliciesRequest request) {
		ListPoliciesResponse response = new ListPoliciesResponse();
		response.setRequestId(request.getId());
		for (Policies policy : Policies.values())
			response.getPolicies().add(policy.name());
		return response;
	}

	public static void main(String[] args) {
		System.out.println((new Execute()).executeCommand(new ListPoliciesRequest()));
	}


}
