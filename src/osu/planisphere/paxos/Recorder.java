package osu.planisphere.paxos;
import java.util.Iterator;
import java.util.LinkedList;

import osu.planisphere.*;

public class Recorder {

	private static LinkedList<Message>[] delivered =
			new LinkedList[2*PaxosConfiguration.f+1];
	
	public static void recordLearnerDelivery(NodeIdentifier nodeID, Message msg){
		int id = nodeID.getID();
		if(delivered[id] == null)
			delivered[id] = new LinkedList<Message>();
		delivered[id].add(msg);
	}
	
	public static void printAll(){
		for(int i=0; i<2*PaxosConfiguration.f + 1; i++){
			System.out.println("Learner "+i);
			if(delivered[i]!=null){
				for(Message msg : delivered[i])
					System.out.println("\t"+msg);
			}
		}
	}
	
	public static void checkConsistency(){
		Iterator<Message>[] iters = new Iterator[2*PaxosConfiguration.f+1];
		for(int i=0; i<delivered.length; i++){
			if(delivered[i] != null)
				iters[i] = delivered[i].iterator();
		}
		while(true){
			Message current = null;
			for(int i=0; i<2*PaxosConfiguration.f+1; i++){
				if(iters[i]!=null && iters[i].hasNext()){
					if(current==null)
						current = iters[i].next();
					else{
						Message tmp = iters[i].next();
						if(!current.equals(tmp)){
							System.out.println("Inconsistency detected");
							printAll();
							return;
						}
					}
				}
			}
			if(current==null)
				break;
		}
		System.out.println("Consistency OK");
	}
}
