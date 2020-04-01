import jade.core.Agent;
import java.util.ArrayList;
import java.io.IOException;
import java.io.Serializable;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class Enseignant extends Agent{
	
	private Creneau cible;
	private AID[] etudiants;
	private ArrayList<Creneau> creneauxPasDisponibles = new ArrayList<>();
	private int id;		
	private int max = 8;

	
		
	
	
	protected void setup() {
		
        Creneau creneau1 = new Creneau(1,4);
        Creneau creneau2 = new Creneau(2,3);
        creneauxPasDisponibles.add(creneau1);
        creneauxPasDisponibles.add(creneau2);
       
		
		System.out.println("Les craneaux pas disponibles sont :"); 
		for(int i = 0; i < creneauxPasDisponibles.size(); i++) {
			System.out.println(creneauxPasDisponibles.get(i).toString());
		}
		
		int jour = 1;
		int creneau = 1;
		Creneau targetCreneau = new Creneau(jour,creneau);

		// Add a TickerBehaviour that schedules a request to GroupeEtudiant agents every minute
		addBehaviour(new TickerBehaviour(this, 6000) {
			protected void onTick() {
				System.out.println("Réserver créneau : "+targetCreneau.toString());
				// Update the list of seller agents
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("reservation-request");
				template.addServices(sd);
				try {
					DFAgentDescription[] result = DFService.search(myAgent, template); 
					System.out.println("Les étudiants suivant ont été trouvé :");
					etudiants = new AID[result.length];
					for (int i = 0; i < result.length; ++i) {
						etudiants[i] = result[i].getName();
						System.out.println(etudiants[i].getName());
					}
				}
				catch (FIPAException fe) {
					fe.printStackTrace();
				}

				// Perform the request
				myAgent.addBehaviour(new ReponseCourse());
				}
			} );
	}
	
	// Put agent clean-up operations here
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("Enseignant "+getAID().getName()+" terminating.");
	}
	private class ReponseCourse extends Behaviour {

		private int creneau = 1;
		private int jour = 1;						
		private int cas = 0;		
		private int propositions = 0;
		private Creneau creneauPropose = null;
		private String lastProposal;
		private MessageTemplate messageTemplate;
		ACLMessage reponse;
		ACLMessage proposition;		
		AID groupe;
		

		
		public void action() {
			// TODO Auto-generated method stub

			switch (cas) {
			case 0:
				
				ACLMessage query = new ACLMessage(ACLMessage.PROPOSE);
				query.addReceiver(etudiants[0]); 
				try {
					query.setContentObject((Serializable) cible);
				} catch (IOException e) {

					e.printStackTrace();
				}
				query.setConversationId("course-request");
				query.setReplyWith("query"+System.currentTimeMillis()); // Unique value
				myAgent.send(query);
				
				// Prepare the template to get proposals
				messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId("course-request"),
						MessageTemplate.MatchInReplyTo(query.getReplyWith()));
				cas = 1;
				break;
				
			case 1:		
				
				reponse = myAgent.receive(messageTemplate);
				
				try {
					creneauPropose = (Creneau) reponse.getContentObject();
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (creneauPropose != null) {
					// Reply received
					if (reponse.getPerformative() == ACLMessage.PROPOSE) {
						// This is an offer 
						groupe = reponse.getSender();
						lastProposal = reponse.getUserDefinedParameter("lastProposal");
						cas = 2; 
						System.out.println("Créneau proposé à : " + creneauPropose.toString());	
						if (lastProposal == "true") {
							System.out.println("Last proposition");
							if (creneauxPasDisponibles.contains(creneauPropose)) {  									    
									System.out.println("L'enseignant il est pas disponible");
									cas = 5;								
			                }
			                else {
			                		
			                	System.out.println("Creneau disponible");
			                	cas = 3;
			                }		                	
						}
					}	

					if (reponse.getPerformative() == ACLMessage.REFUSE) {
						System.out.println("Le groupe il est pas disponible");	
					}
					if (reponse.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
						System.out.println("L'enseignant a assuré les creneaux pour cet groupe.");
					}
					if (reponse.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
						System.out.println("Creneau disponible.");
						cas = 3;
					}
					proposition = reponse.createReply();
			  }
			  else {
					block();
				}
			break;	
			
			case 2: 	

					    if (!creneauxPasDisponibles.contains(creneauPropose)) { System.out.println("Le groupe étudiant a proposé le créneau désiré"); cas = 3; }
					    else { 
						    propositions++;
						    System.out.println("Le créneau il est pas disponible : ");						
						    						    
						    while(creneauxPasDisponibles.contains(creneauPropose)) {
								if(jour == 1) {
							    	jour = 2;
							    	if(creneau < 4) {
							    		creneau += 1;
							    		creneauPropose = new Creneau(jour,creneau);
							    	}else {
							    		creneau = 1;
							    		creneauPropose = new Creneau(jour,creneau);
							    	}
							    }else {
							    	jour = 1;
							    	if(creneau < 4) {
							    		creneau += 1;
							    		creneauPropose = new Creneau(jour,creneau);
							    	}else {
							    		creneau = 1;
							    		creneauPropose = new Creneau(jour,creneau);
							    	}
							    }
							}
	     					
	     					System.out.println("Quand est il du créneau suivant ? : " + creneauPropose);
	     					if (propositions == max) {
	     							
							    	System.out.println("Dernière proposition");
							    	proposition.addUserDefinedParameter("lastProposal","true"); 
							    }
	     					System.out.println("[Proposal number: " +Integer.toString(propositions)+"]"); 
	     					proposition.setPerformative(ACLMessage.PROPOSE);					
							try {
								proposition.setContentObject((Serializable) creneauPropose);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							myAgent.send(proposition);
												
							messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId("course-request"),
									MessageTemplate.MatchInReplyTo(proposition.getReplyWith()));
							
							cas = 1;
				    	
			    }
			break;
			    
			case 3: 

				ACLMessage cours = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				cours.addReceiver(groupe);
				try {
					cours.setContentObject((Serializable) cible);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cours.setConversationId("course-request");
				cours.setReplyWith("cours " + System.currentTimeMillis());
				myAgent.send(cours);
				
				// Prepare the template to get the adding of the course reply
				messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId("course-request"),
						MessageTemplate.MatchInReplyTo(cours.getReplyWith()));
                
				cas = 4;
			break;
			
			case 4:     
				
				reponse = myAgent.receive(messageTemplate);
				if (reponse != null) {
					if (reponse.getPerformative() == ACLMessage.INFORM) {
						System.out.println("Le cours au créneau : " + cible + " ajouté à l'horaire");
						System.out.println("Groupe = " + reponse.getSender().getName());
						System.out.println("Enseignant = " + id);
						myAgent.doDelete();
					}
					else {
						System.out.println("Failed.");}

					cas = 6;
				}
				else {
					block();}
				
			break;
			
			case 5:		
		
				ACLMessage rejection = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
				rejection.addReceiver(groupe);
				try {
					rejection.setContentObject((Serializable) cible);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rejection.setConversationId("course-request");
				rejection.setReplyWith("rejection"+System.currentTimeMillis());
				myAgent.send(rejection);
				cas = 6;
			break;
	
			}
			
		}


		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}

	}
}

