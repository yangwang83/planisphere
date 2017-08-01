package osu.planisphere.paxos;
import java.net.Socket;

import osu.planisphere.*;

public class Client extends NormalNode{

	private int clientSequenceID = 0;
	private NodeIdentifier leaderID = new NodeIdentifier(Role.ACCEPTOR, 0);
	
	public Client(NodeIdentifier id, int timerInterval, int debugMode) {
		super(id, timerInterval, debugMode);
		// TODO Auto-generated constructor stub
	}
	
	public void write(int key, int value){
		Request req = new Request(getID(), 1, key, value, clientSequenceID);
		clientSequenceID++;
		this.sendMessage(leaderID, req);
	}

	@Override
	public void handleTimer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}

	
}
