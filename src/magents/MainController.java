/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magents;

/**
 *
 * @author Ariarti25
 */
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

class MainController {

    private static final int numberOfAgents = 6;

    void initAgents() {

        Runtime rt = Runtime.instance();
//Create a container to host the Default Agent
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.MAIN_PORT, "10099");
        p.setParameter(Profile.GUI, "true");
        ContainerController cc = rt.createMainContainer(p);
        try {
            for (int i = 1; i <= MainController.numberOfAgents; i++) {
                AgentController agent = cc.createNewAgent(Integer.toString(i), "magents.DefaultAgent", null);
                agent.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
