package osu.planisphere;
import osu.planisphere.Message;
import osu.planisphere.Network;
import osu.planisphere.Node;
import osu.planisphere.NodeIdentifier;
import osu.planisphere.Role;

import java.net.Socket;
import java.util.*;
//im going to start with two nodes, then figure out the algorithm for multiple nodes
public class NumberAdding {
public static ArrayList<Integer> sums_from_addingnodes=new ArrayList<Integer>();
		public static class AddNumberMessage extends Message {
			private int numsum;
			public AddNumberMessage(NodeIdentifier sender,  int numsum)
			{
				super(sender);
				this.numsum=numsum;
			}
			@Override
			public String toString()
			{
				return String.valueOf(numsum)+" from "+sender;
			}
		}
		
		public static ArrayList<ArrayList<Integer>> dividing_nums_for_summing(ArrayList<AddingNode> nodelist, ArrayList<Integer> nums)
		{
		ArrayList<ArrayList<Integer>> num_to_be_summed=new ArrayList<ArrayList<Integer>>();
			for(int i=0;i<nodelist.size();i++)
			{
				num_to_be_summed.add(new ArrayList<Integer>());
			}
			for(int i=0;i<nums.size();i++)
			{
				int count=i%nodelist.size();
				num_to_be_summed.get(count).add(nums.get(i));
			}
			return num_to_be_summed;
		}
		
		public static class ClientNode extends NormalNode {
			public ClientNode(NodeIdentifier id) {
				super(id, 5000);
			}
			public void sendSumMessage(ArrayList<Integer> nums, ArrayList<AddingNode> RegularServers, EntryServerNode other)  //only use on entryservernodes
			{
				other.sendAllMessages(this, RegularServers, nums);
			}
			public void handleTimer() {
				System.out.println(this.getID() + " timer event");
			}
			public void handleMessage(Message msg) {
				System.out.println(this.getID() + " gets "+msg);
			}
		}
		public static class EntryServerNode extends AddingNode {
			public static int sumtotal=0;
			public static ArrayList<Node> nodelist=new ArrayList<Node>();
			public EntryServerNode(NodeIdentifier id) {
				super(id);
			}
			
			@Override
			public void handleTimer() {
				System.out.println(this.getID() + " timer event");
			}
			
			public void sendAllMessages(ClientNode client, ArrayList<AddingNode> nodelist, ArrayList<Integer> nums)
			{
				int k=nodelist.size();
				ArrayList<ArrayList<Integer>> nums_to_be_summed=new ArrayList<ArrayList<Integer>>(dividing_nums_for_summing(nodelist,nums));
				
				for(int i=0;i<nums_to_be_summed.size();i++)
				{
					sendOneMessage(nodelist.get(i),nums_to_be_summed.get(i));
				}
				for(int i: sums_from_addingnodes)
				{
					sumtotal+=i;
				}
				
				try{ //delay so the earlier threads can execute
					Thread.sleep(1000);
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
				
				this.sendBackMessage(client, sumtotal );
			}
			public void sendBackMessage(ClientNode other, int sum) //sending back to Client Node
			{
				this.sendMessage(other.getID(), new AddNumberMessage(this.getID(), sum));
			}
			public void sendOneMessage(AddingNode other, ArrayList<Integer> nums){ //sending to AddingNodes so that they can add numbers
				other.sendOneMessage(this.getID(), nums);
			}
			public void handleMessage(Message msg) {
				System.out.println(this.getID() + " gets "+msg);
			}
		}
		public static class AddingNode extends NormalNode{
			public static int currentsum=0;
			public AddingNode(NodeIdentifier id) {
				super(id, 5000);
			}

			public void sendOneMessage(NodeIdentifier other, ArrayList<Integer> nums){
				currentsum=addnums(nums);
				sums_from_addingnodes.add(currentsum);
				this.sendMessage(other, new AddNumberMessage(this.getID(), addnums(nums)));
			}
			
			public int addnums(ArrayList<Integer>nums)
			{
				int count=0;
				for(int i : nums)
				{
					count+=i;
				}
				return count;
			}
			@Override
			public void handleTimer() {
				System.out.println(this.getID() + " timer event");
			}

			@Override
			public void handleMessage(Message msg) {
				currentsum=0;
			}
			
		}
		
		public static void main(String args[]) throws Exception{
			Scanner reader=new Scanner(System.in);
			System.out.println("Enter the numbers you want summed---i.e.1,2,3,4,5");
			String a=reader.nextLine();
			String arrnum[]=a.split(",");
			ArrayList<Integer> nums=new ArrayList<Integer>();
			for(int i=0;i<arrnum.length;i++)
			{
				nums.add(Integer.parseInt(arrnum[i]));
			}
			
			
			//Create the network and all nodes
			Network network = new Network();
			NodeIdentifier id1= new NodeIdentifier(Role.CLIENT, 1);
			ClientNode node1 = new ClientNode(id1);
			
			NodeIdentifier id2= new NodeIdentifier(Role.ENTRYSERVER, 2);
			EntryServerNode node2 = new EntryServerNode(id2);
			NodeIdentifier id3= new NodeIdentifier(Role.SERVER, 3);
			AddingNode node3 = new AddingNode(id3);
			NodeIdentifier id4= new NodeIdentifier(Role.SERVER, 4);
			AddingNode node4 = new AddingNode(id4);
			NodeIdentifier id5= new NodeIdentifier(Role.SERVER, 5);
			AddingNode node5 = new AddingNode(id5);
			
			ArrayList<AddingNode> nodelist=new ArrayList<AddingNode>();
			nodelist.add(node3); nodelist.add(node4); nodelist.add(node5);
			//Start experiment
			node1.start();
			node2.start();
			node3.start();
			node4.start();
			node5.start();
			node1.sendSumMessage(nums, nodelist, node2);
			
		}
}
