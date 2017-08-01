package osu.planisphere.paxos;
import java.net.Socket;
import java.util.TreeMap;

import osu.planisphere.*;

public class Acceptor extends NormalNode{

	private int epoch = 0;
	private int slot = 0;
	
	private TreeMap<Integer, Quorum> outstandingProposals = new TreeMap<Integer, Quorum>();
	
	public Acceptor(NodeIdentifier id, int timerInterval, int debugMode) {
		super(id, timerInterval, debugMode);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleTimer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMessage(Message msg) {
		System.out.println(this.getID()+" processing "+msg);
		if(msg instanceof Request){
			Request req = (Request)msg;
			P2aMessage p2a = new P2aMessage(this.getID(), slot, epoch, req);
			Quorum quorum = new Quorum(req, PaxosConfiguration.f+1);
			outstandingProposals.put(slot, quorum);
			for(int i=0; i<PaxosConfiguration.acceptors.length; i++){
				this.sendMessage(PaxosConfiguration.acceptors[i], p2a);
			}
			slot++;
		}
		else if(msg instanceof P2aMessage){
			P2aMessage p2a = (P2aMessage)msg;
			P2bMessage p2b = new P2bMessage(this.getID(), p2a.getSlot(), p2a.getEpoch(), true);
			this.sendMessage(p2a.getSender(), p2b);
		}
		else if(msg instanceof P2bMessage){
			P2bMessage p2b = (P2bMessage)msg;
			if(p2b.getEpoch() == epoch && p2b.isAccepted()){
				if(outstandingProposals.containsKey(p2b.getSlot())){
					Quorum q = outstandingProposals.get(p2b.getSlot());
					q.addReceipt(p2b.getSender());
					if(q.isReady()){
						CommitMessage commit = new CommitMessage(getID(), p2b.getSlot(), epoch, (Request)q.getMessage());
						for(int i=0; i<PaxosConfiguration.learners.length; i++)
							sendMessage(PaxosConfiguration.learners[i], commit);
						outstandingProposals.remove(p2b.getSlot());
					}
				}
				
			}
		}
		
	}

}
