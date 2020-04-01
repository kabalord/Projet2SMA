import java.util.ArrayList;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Main {

	public static void main(String[] args) {
        jade.core.Runtime runtime=jade.core.Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST,"localhost");
        p.setParameter(Profile.MAIN_PORT, "1234");
        //p.setParameter(Profile.GUI,"true");

        ContainerController containerController = runtime.createMainContainer(p);
        AgentController agentController;
        
        Groupe g1 = new Groupe();
        Enseignant e1 = new Enseignant();
            
        try {
            agentController = containerController.acceptNewAgent("Groupe", g1);
            agentController.start();
            System.out.println("Groupe étudiant crée");
            
            agentController = containerController.acceptNewAgent("Enseignant", e1);
            agentController.start();
            System.out.println("Enseignant crée");
        }
        catch (StaleProxyException e) {
            e.printStackTrace();
        }
	}

}