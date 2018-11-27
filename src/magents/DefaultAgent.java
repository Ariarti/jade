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
import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.*;

public class DefaultAgent extends Agent {

    public Integer random_number = (int) (1 + Math.random() * 100);
    Integer num_for_send, conv = 1;
    Integer msg_id, num_rn, agent_id;
    Integer real_msg_id = 0;
    Integer readiness = 0; //Готовность получать сообщения (0 готов, 1 не готов)

    @Override
    protected void setup() {
        ArrayList<Integer> people = new ArrayList<Integer>();
        HashMap<Integer, Integer> MSG_HASH = new HashMap<Integer, Integer>();
        int id = Integer.parseInt(getAID().getLocalName());
        agent_id = id;
        MSG_HASH.put(id, random_number);
        System.out.println("Агент " + id + " загадал " + random_number);

        addBehaviour(new CyclicBehaviour(this) {

            public void action() {
                ACLMessage msg = myAgent.receive();

                if (MSG_HASH.size() != 6) {

                    if (msg != null) {

                        // Вывод на экран локального имени агента и полученного 
                        // сообщения 
                        AID MSG_sender = msg.getSender();
                        //  System.out.println("Сообщение полученно от " + MSG_sender.getLocalName()); 
                        String toRead = msg.getContent();
                        String[] result = toRead.split("/");
                        int i = 0;
                        for (String token : result) {

                            if (i == 0) {
                                num_rn = Integer.parseInt(token);
                            }

                            if (i == 1) {
                                Integer msg_id = Integer.parseInt(token);

                                if (!MSG_HASH.containsKey(msg_id)) {
                                    MSG_HASH.put(msg_id, num_rn);
                                    // System.out.println("id " + id + " " + MSG_HASH); 
                                }

                                if (MSG_HASH.containsKey(msg_id)) {
                                    Integer test_value = MSG_HASH.get(msg_id);

                                    if (num_rn != test_value) {
                                        num_rn = (num_rn + test_value) / 2;
                                        MSG_HASH.put(msg_id, num_rn);
                                    }
                                }
                            }
                            if (i == 2) {
                                Integer tak = Integer.parseInt(token);

                                if (tak == 1) {
                                    people.remove(num_rn);
                                }
                            }
                            i++;
                        }

                        if (MSG_HASH.size() == 6) {
                            double average_value = 0; //Среднее значение
                            for (int value : MSG_HASH.values()) {
                                average_value = average_value + value;
                            }
                            average_value = average_value / 6;

                            if (id == 6) {
                                System.out.println("Среднее значение = " + average_value);
                            }

                        }
                    }
                } else {
                    msg = null;
                }
            }
        });

        addBehaviour(new TickerBehaviour(this, 100) {

            private final int MAX_STEPS = 10;
            private int step = 0;

            protected void onTick() {

                Integer connections_for_4_5 = (int) (Math.random() * 2); //Связь между 4 и 5 если connections_for_4_5 = 1 то связь есть 

                if (step < MAX_STEPS) {
                    ACLMessage msg1 = new ACLMessage(ACLMessage.REQUEST);
                    int[] aNums = null;
                    if (id == 1) {
                        people.add(2);
                    }
                    if (id == 2) {
                        people.add(3);
                        people.add(4);
                        people.add(1);
                    }
                    if (id == 3) {
                        people.add(2);
                        people.add(5);
                    }
                    if (id == 4) {
                        people.add(2);
                        if (connections_for_4_5 == 1) {
                            people.add(5);
                        }
                        people.add(6);
                    }
                    if (id == 5) {
                        people.add(3);
                        people.add(4);
                        people.add(6);
                    }
                    if (id == 6) {
                        people.add(4);
                        people.add(5);
                    }

                    for (int i : people) {
                        if (i != id) {
                            if (MSG_HASH.size() == 6) {
                                readiness = 1;
                            }
                            for (Map.Entry<Integer, Integer> entry : MSG_HASH.entrySet()) {
                                Integer key = entry.getKey();
                                Integer tab = entry.getValue();
                                if (id == 5 & key == 5) {
                                    Integer error_for_send = (int) (Math.random() * 3);
                                    if (error_for_send == 2) {
                                        tab--;
                                    } else {
                                        tab = tab + error_for_send;
                                    }
                                }
                                String for_send = String.valueOf(i);
                                msg1.addReceiver(new AID(for_send, AID.ISLOCALNAME));
                                String for2 = String.valueOf(key);
                                String for1 = String.valueOf(tab);
                                String for3 = String.valueOf(readiness);
                                String for_message = for1 + "/" + for2 + "/" + for3;
                                msg1.setContent(for_message);
                                send(msg1);
                            }

                        }

                    }
                    step++;
                } else {
                    this.stop();
                }
            }
        });
    }   // setup()

}
