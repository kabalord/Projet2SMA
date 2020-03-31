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
					System.out.println("Réception d'un nouveau message : ");
					System.out.println("Contenu du message : " + msg.getContent());
					System.out.println("Acte de communication : " + ACLMessage.getPerformative(msg.getPerformative()));
					System.out.println("Langage : " + msg.getLanguage());
					System.out.println("Ontologie : " + msg.getOntology());
					
					if(msg.getOntology().equals("Disponibilité salle") ) {
						System.out.println("disponibilité du cours");
						ACLMessage reponse = msg.createReply();
						reponse.setContent("Message bien reçu");
						send(reponse);
					} 
					else if(msg.getOntology().equals("Disponibilité craneaux") ) {
						System.out.println("il y a cours...");
						ACLMessage reponse = msg.createReply();
						reponse.setContent("Je vais m'y préparer...");
						send(reponse);
					}
				}else {
					block();
				} 
			}
			
		});
	}
}
