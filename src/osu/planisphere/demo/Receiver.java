package osu.planisphere.demo;

import osu.planisphere.Message;
import osu.planisphere.NodeIdentifier;
import osu.planisphere.NormalNode;
import osu.planisphere.Role;

public class Receiver extends NormalNode{
	public Receiver(NodeIdentifier id, int debugMode) {
		super(id, 5000, debugMode);
	}

	@Override
	public void handleTimer() {
	}

	@Override
	public void handleMessage(Message msg) {
		System.out.println(this.getID() + " hanldes "+msg);
	}
	
	public static void main(String[] args) {
		if(args.length!=1) {
			System.out.println("java Receiver <debugMode>");
			return;
		}
		int debugMode = Integer.parseInt(args[0]);
		Receiver receiver = new Receiver(new NodeIdentifier(Role.DEMO, 1), debugMode);
		receiver.start();
	}

}
