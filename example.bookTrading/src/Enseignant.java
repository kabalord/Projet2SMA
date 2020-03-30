import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Enseignant extends Agent{
	protected void setup() {
		addBehaviour(new CyclicBehaviour() {
			
			public void action() {
				ACLMessage msg = receive();
				if(msg != null) {
					System.out.println(msg.getContent());
				}else block();
			}
			
		});
	}
}
