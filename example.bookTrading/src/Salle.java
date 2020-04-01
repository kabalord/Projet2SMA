import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;

public class Salle extends Agent {
	protected void setup() {
		// Printout a welcome message
		System.out.println("Hello! Salle "+getAID().getName()+" is ready.");
		addBehaviour(new OneShotBehaviour() {
			
			public void action() {
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
						msg.setContent("test of envoi de message...");
						msg.addReceiver(new AID("Enseignant",AID.ISLOCALNAME));
						send(msg);
			}
			
		});
	}

	// Put agent clean-up operations here
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("Salle "+getAID().getName()+" terminating.");
	}
}
