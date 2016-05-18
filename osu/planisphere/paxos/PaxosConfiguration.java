package osu.planisphere.paxos;
import osu.planisphere.*;

public class PaxosConfiguration {
	public static final int clientNo = 1;
	public static final int f = 1;
	
	public static final NodeIdentifier[] clients = new NodeIdentifier[clientNo];
	public static final NodeIdentifier[] acceptors = new NodeIdentifier[2*f+1];
	public static final NodeIdentifier[] learners = new NodeIdentifier[2*f+1];
	
	static {
        for(int i=0; i<clients.length;i++)
        	clients[i] = new NodeIdentifier(Role.CLIENT, i);
        
        for(int i=0; i<acceptors.length;i++)
        	acceptors[i] = new NodeIdentifier(Role.ACCEPTOR, i);
        
        for(int i=0; i<learners.length;i++)
        	learners[i] = new NodeIdentifier(Role.LEARNER, i);
    }
	
}
