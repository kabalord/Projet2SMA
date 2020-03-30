import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Salle extends Agent {
	protected void setup() {
		addBehaviour(new OneShotBehaviour() {
			
			public void action() {
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
						msg.setContent("test of envoi de message...");
						msg.addReceiver(new AID("Enseignnant",AID.ISLOCALNAME));
						send(msg);
			}
			
		});
	}

}
