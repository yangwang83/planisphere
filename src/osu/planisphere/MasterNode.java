package osu.planisphere;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

import osu.planisphere.EventHook.HookAction;
import osu.planisphere.messages.*;

public class MasterNode extends Node {
	
	private HashMap<NodeIdentifier, InetSocketAddress> nodes = new HashMap<NodeIdentifier, InetSocketAddress> ();
	private Network network;
	private EventHook hook = null;
	private int debugMode = 0;

	public MasterNode(int debugMode, EventHook hook) {
		super(new NodeIdentifier(Role.MASTER,1), 5000);
		this.network = new Network();
		this.network.init(Configuration.masterAddr.getPort(), this);
		this.debugMode = debugMode;
		this.hook = hook;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void sendMessage(NodeIdentifier receiver, Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleTimer() {
		//System.out.println("master handle timer");

	}

	@Override
	public void handleMessage(Message msg, ObjectOutputStream out) {
		System.out.println("master got "+msg);
		if(msg instanceof RegisterMessage) {
			RegisterMessage rMsg = (RegisterMessage) msg;
			nodes.put(rMsg.getSender(), rMsg.getAddress());
		}
		else if(msg instanceof GetRegistryMessage) {
			GetRegistryMessage gMsg = (GetRegistryMessage)msg;
			if(!nodes.containsKey(gMsg.getTarget())) {
				try {
					out.writeObject(new GetRegistryReplyMessage(this.getID(), gMsg.getTarget(), null));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				InetSocketAddress addr = nodes.get(gMsg.getTarget());
				ObjectOutputStream oos;
				try {
					out.writeObject(new GetRegistryReplyMessage(this.getID(), gMsg.getTarget(), addr));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		else if(msg instanceof ReportActionMessage) {
			ReportActionMessage action = (ReportActionMessage)msg;
			if(debugMode==0)
				System.out.println("master debug mode=0. Should not receive ReportActionMessage");
			else if(debugMode==1)
				System.out.println(action);
			else if(debugMode==2) {
				if(hook==null)
					System.out.println("master debug mode=2, but no hook");
				else {
					HookAction response = hook.handleEvent(action.getTiming(), action.getAction(), action.getMessage(), action.getSender());
					ReportActionResponseMessage reply = new ReportActionResponseMessage(this.getID(), response.response, response.message);
					try {
						out.writeObject(reply);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}
	
	public static void main(String []args){
		MasterNode master = new MasterNode(0, null);
		master.start();
	}

}
