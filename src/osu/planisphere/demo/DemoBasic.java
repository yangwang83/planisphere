package osu.planisphere.demo;

import java.net.Socket;

import osu.planisphere.MasterNode;
import osu.planisphere.Message;
import osu.planisphere.Network;
import osu.planisphere.Node;
import osu.planisphere.NodeIdentifier;
import osu.planisphere.NormalNode;
import osu.planisphere.Role;

/**
 * This demo shows how to create two nodes and let them exchange
 * messages. These two nodes work in a ping-pong way: the first
 * node sends a message to the second; the second sends it back ...
 * @author Yang Wang
 *
 */
public class DemoBasic {
	
	public static class PingPongMessage extends Message{
		
		private String str;
		public PingPongMessage(NodeIdentifier sender, String str){
			super(sender);
			this.str = str;
		}
		
		@Override
		public String toString(){
			return str + " from "+sender;
		}
	}
	
	public static class PingPongNode extends NormalNode{

		public PingPongNode(NodeIdentifier id) {
			super(id, 5000, 0);
		}

		public void sendOneMessage(NodeIdentifier other){
			this.sendMessage(other, new PingPongMessage(this.getID(), "first message"));
		}
		
		@Override
		public void handleTimer() {
			System.out.println(this.getID() + " timer event");
		}

		@Override
		public void handleMessage(Message msg) {
			System.out.println(this.getID() + " hanldes "+msg);
			try{
				Thread.sleep(1000);
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
			this.sendMessage(msg.getSender(), new PingPongMessage(this.getID(), "haha"));
		}
	}
	
	public static void main(String args[]) throws Exception{
		if(args.length!=1){
			System.out.println("java DemoBasic <id>");
			System.exit(-1);
		}
		
		int id = Integer.parseInt(args[0]);
		
		NodeIdentifier ni= new NodeIdentifier(Role.DEMO, id);
		PingPongNode node = new PingPongNode(ni);
		node.start();
		if(id==2){
			NodeIdentifier ni1= new NodeIdentifier(Role.DEMO, 1);
			node.sendOneMessage(ni1);
		}
	}
	
}
