package osu.planisphere;

import java.util.HashSet;
import java.util.Scanner;

public class StepRunning implements EventHook {

	@Override
	public HookAction handleEvent(Timing timing, Action action, Message msg, NodeIdentifier node) {
		if(timing == Timing.before) {
			if(action == Action.handle)
				System.out.println(node.getID()+" tries to handle " + msg);
			else
				System.out.println(node.getID()+" tries to send " + msg);
			int decision = -1;
			while(true){
				System.out.println("Enter your choice: 1 do it; 2 drop it; 3 kill the node.");
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
				return new HookAction(ActionResponse.doit, null);
			case 2:
				return new HookAction(ActionResponse.dropit, null);
			case 3:
				return new HookAction(ActionResponse.killnode, null);
			}
		}
		else {
			if(action == Action.handle)
				System.out.println("after " + node.getID()+" handles " + msg);
			else
				System.out.println("after " + node.getID()+" sends " + msg);
			int decision = -1;
			while(true){
				System.out.println("Enter your choice: 1 continue; 2 kill the node.");
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
				return new HookAction(ActionResponse.doit, null);
			case 2:
				return new HookAction(ActionResponse.killnode, null);
			}
		}
		return new HookAction(ActionResponse.doit, null);
	}

}
