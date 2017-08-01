package osu.planisphere;

import java.util.HashSet;
import java.util.Scanner;

public class StepRunning implements EventHook {

	private HashSet<Node> terminated = new HashSet<Node>();
	@Override
	public synchronized void handleMessage(Message msg, Node node) {
		if(terminated.contains(node))
			return;
		System.out.println(node.getID()+" receives " + msg);
		int decision = -1;
		while(true){
			System.out.println("Enter your choice: 1 process it; 2 drop it; 3 drop it and kill the node.");
			Scanner s = new Scanner(System.in);
			String input = s.nextLine();
			try{
				decision = Integer.parseInt(input);
			}
			catch(Exception e){
				System.out.println("Invalid input.");
				continue;
			}
			if(decision<1||decision>3){
				System.out.println("Invalid input.");
			}
			else
				break;
		}
		switch(decision){
		case 1:
	//		node.handleMessage(msg);
			break;
		case 2:
			break;
		case 3:
			terminated.add(node);
			break;
		}
		
		
		
		
	}

	@Override
	public synchronized void handleTimer(Node node) {
		if(terminated.contains(node))
			return;
		System.out.println(node.getID()+" receives timer event.");
		int decision = -1;
		while(true){
			System.out.println("Enter your choice: 1 process it; 2 drop it and kill the node.");
			Scanner s = new Scanner(System.in);
			String input = s.nextLine();
			try{
				decision = Integer.parseInt(input);
			}
			catch(Exception e){
				System.out.println("Invalid input.");
				continue;
			}
			if(decision<1||decision>2){
				System.out.println("Invalid input.");
			}
			else
				break;
		}
		switch(decision){
		case 1:
			node.handleTimer();
			break;
		case 2:
			terminated.add(node);
			break;
		}
	}

	@Override
	public synchronized void sendMessage(NodeIdentifier receiver, Message msg, Node node) {
		if(terminated.contains(node))
			return;
		System.out.println(node.getID()+" tries to send " + msg);
		int decision = -1;
		while(true){
			System.out.println("Enter your choice: 1 send it; 2 drop it; 3 drop it and kill the node.");
			Scanner s = new Scanner(System.in);
			String input = s.nextLine();
			try{
				decision = Integer.parseInt(input);
			}
			catch(Exception e){
				System.out.println("Invalid input.");
				continue;
			}
			if(decision<1||decision>3){
				System.out.println("Invalid input.");
			}
			else
				break;
		}
		switch(decision){
		case 1:
//			node.sendMessageInternal(receiver, msg);
			break;
		case 2:
			break;
		case 3:
			terminated.add(node);
			break;
		}
		
		
	}

}
