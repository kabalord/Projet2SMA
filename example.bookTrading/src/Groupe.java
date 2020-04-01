
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class Groupe extends Agent {

	private ArrayList<Creneau> creneauxIndispo = new ArrayList<>();	
	private int id;
	private int propositions = 0;
	private int max = 8;		
	private int cours = 0; 			
		

	protected void setup(int id) {
		this.id = id;
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("reservation-request");
		sd.setName("JADE-EDT");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		addBehaviour(new Horaire());
		addBehaviour(new Rejections());
		addBehaviour(new Gestion());
		
		
	}
	
	protected void takeDown() {
		
		System.out.println("Etudiant "+getAID()+" terminating.");
	}
	
	private class Horaire extends CyclicBehaviour {
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// ACCEPT_PROPOSAL Message received. Process it
				Creneau creneauAccepte = null;
				try {
					creneauAccepte = (Creneau) msg.getContentObject();
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ACLMessage reponse = msg.createReply();

				if (creneauAccepte != null) {
					creneauxIndispo.add(creneauAccepte);
					reponse.setPerformative(ACLMessage.INFORM);
					System.out.println("Ajout :"+creneauAccepte.toString()+" à l'horaire.");
				}
				else {
					reponse.setPerformative(ACLMessage.FAILURE);
					reponse.setContent("not-available");
				}

				myAgent.send(reponse);
			}
			else {
				block();
			}
		}
	}
	
	private class Rejections extends CyclicBehaviour {
		public void action() {
			
				MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				System.out.println("L'enseignant a rejeté");
			}
			else {
				block();
			}
		}
	}
	
	private class Gestion extends CyclicBehaviour {
		
		private int jour = 1;
		private int creneau = 1;
		private Creneau proposedCreneau;
		
		public void action() {
			
			Creneau RequestCreneau = null;
			MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
			ACLMessage msg = myAgent.receive(messageTemplate);
			
			try {
				RequestCreneau = (Creneau) msg.getContentObject();
			} catch (UnreadableException e) {
				
				e.printStackTrace();
			}
			if (RequestCreneau != null) {
				// REQUEST Message received. Process it
				ACLMessage reponse = msg.createReply();

				if (cours < 2) {
					if (creneauxIndispo.contains(RequestCreneau)) {

						if (creneauxIndispo.size() < max) {
							RequestCreneau = proposedCreneau;
							while(creneauxIndispo.contains(proposedCreneau)) {
								if(jour == 1) {
							    	jour = 2;
							    	if(creneau < 4) {
							    		creneau += 1;
							    		proposedCreneau = new Creneau(jour,creneau);
							    	}else {
							    		creneau = 1;
							    		proposedCreneau = new Creneau(jour,creneau);
							    	}
							    	jour = 2;
							    }else {
							    	jour = 1;
							    	if(creneau < 4) {
							    		creneau += 1;
							    		proposedCreneau = new Creneau(jour,creneau);
							    	}else {
							    		creneau = 1;
							    		proposedCreneau = new Creneau(jour,creneau);
							    	}
							    }
							}
							if (creneauxIndispo.size() == max - 1) {
								
								System.out.println("Dernière proposition :");
								reponse.addUserDefinedParameter("lastProposal","true");
							}
							reponse.setPerformative(ACLMessage.PROPOSE);
							try {
								reponse.setContentObject((Serializable) proposedCreneau);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							propositions += 1;
						}else {							
							reponse.setPerformative(ACLMessage.REFUSE);
							reponse.setContent("no");
						}
					}
					else {						
						reponse.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
						reponse.setContent("ok");
						creneauxIndispo.add(RequestCreneau);
						cours += 1;
					}
				}
				else {
					reponse.setPerformative(ACLMessage.REJECT_PROPOSAL);
					reponse.setContent("Il y a cours");
				}
				myAgent.send(reponse);
			}
			else {
				block();
			}
		}
	}
		
}

