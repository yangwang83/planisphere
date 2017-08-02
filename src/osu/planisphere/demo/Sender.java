package osu.planisphere.demo;

import osu.planisphere.Message;
import osu.planisphere.NodeIdentifier;
import osu.planisphere.NormalNode;
import osu.planisphere.Role;

public class Sender extends NormalNode{

	public Sender(NodeIdentifier id, int debugMode) {
		super(id, 5000, debugMode);
	}

	public void sendMessages(NodeIdentifier other){
		for(int i=0; i<5; i++)
			this.sendMessage(other, new TestMessage(this.getID(), i));
	}
	
	@Override
	public void handleTimer() {
	}

	@Override
	public void handleMessage(Message msg) {

	}
	
	public static void main(String[] args) {
		if(args.length!=1) {
			System.out.println("java Sender <debugMode>");
			return;
		}
		int debugMode = Integer.parseInt(args[0]);
		Sender sender = new Sender(new NodeIdentifier(Role.DEMO, 2), debugMode);
		sender.start();
		sender.sendMessages(new NodeIdentifier(Role.DEMO, 1));
	}
}
