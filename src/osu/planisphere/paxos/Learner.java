package osu.planisphere.paxos;
import osu.planisphere.*;

public class Learner extends Node{

	public Learner(NodeIdentifier id, int timerInterval, Network network) {
		super(id, timerInterval, network);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleTimer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMessage(Message msg) {
		if(msg instanceof CommitMessage){
			CommitMessage commit = (CommitMessage)msg;
			Recorder.recordLearnerDelivery(getID(), commit.getRequest());
		}
		
	}

}
