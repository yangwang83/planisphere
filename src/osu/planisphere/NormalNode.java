package osu.planisphere;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

import osu.planisphere.messages.GetRegistryMessage;
import osu.planisphere.messages.GetRegistryReplyMessage;
import osu.planisphere.messages.RegisterMessage;
import osu.planisphere.messages.ReportActionMessage;
import osu.planisphere.messages.ReportActionResponseMessage;

public abstract class NormalNode extends Node {

	private Network network;
	private Socket masterSocket;
	private ObjectOutputStream masterOut;
	ObjectInputStream masterIn;
	private final HashMap<NodeIdentifier, InetSocketAddress> nodes = new HashMap<NodeIdentifier, InetSocketAddress>();
	private int debugMode = 0; //0 means no debugging; 1 means logging; 2 means using hooks
	
	public NormalNode(NodeIdentifier id, int timerInterval, int debugMode) {
		super(id, timerInterval);
		if(debugMode<0||debugMode>2)
			throw new RuntimeException("Invalid debugMode "+debugMode);
		this.debugMode = debugMode;
		this.network = new Network();
		this.network.init(-1, this);
		
		try {
			masterSocket = new Socket();
			masterSocket.connect(Configuration.masterAddr);
			
			masterOut = new ObjectOutputStream(masterSocket.getOutputStream());
			masterOut.writeObject(new RegisterMessage(this.getID(), this.network.getLocalAddress()));
			
			masterIn = new ObjectInputStream(masterSocket.getInputStream());
			
			
		}
		catch(IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public abstract void handleMessage(Message msg);
	
	
	public void handleMessage(Message msg, ObjectOutputStream out) {
		if(debugMode > 0){
			try {
				masterOut.writeObject(new ReportActionMessage(this.getID(), Timing.before, Action.handle, msg));
				if(debugMode==2){
					ReportActionResponseMessage response = (ReportActionResponseMessage)masterIn.readObject();
					if(response.getResponse() == ActionResponse.dropit)
						return;
					else if(response.getResponse() == ActionResponse.killnode)
						System.exit(0);
					else if(response.getResponse() == ActionResponse.replace)
						msg = response.getMessage();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		handleMessage(msg);
		if(debugMode == 2){
			try {
				masterOut.writeObject(new ReportActionMessage(this.getID(), Timing.after, Action.handle, msg));
				ReportActionResponseMessage response = (ReportActionResponseMessage)masterIn.readObject();
				if(response.getResponse() == ActionResponse.killnode)
					System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	
	@Override
	public void sendMessage(NodeIdentifier receiver, Message msg) {
		InetSocketAddress target = null;
		synchronized(nodes) {
			if(nodes.containsKey(receiver)) {
				target = nodes.get(receiver);
			}
		}
		if(target == null) {
			
			try {
				masterOut.writeObject(new GetRegistryMessage(this.getID(), receiver));
				GetRegistryReplyMessage reply = (GetRegistryReplyMessage) masterIn.readObject();
				target = reply.getAddress();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized(nodes) {
				nodes.put(receiver, target);
			}
		}
		if(debugMode > 0){
			try {
				masterOut.writeObject(new ReportActionMessage(this.getID(), Timing.before, Action.send, msg));
				if(debugMode==2){
					ReportActionResponseMessage response = (ReportActionResponseMessage)masterIn.readObject();
					if(response.getResponse() == ActionResponse.dropit)
						return;
					else if(response.getResponse() == ActionResponse.killnode)
						System.exit(0);
					else if(response.getResponse() == ActionResponse.replace)
						msg = response.getMessage();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		network.sendMessage(target, msg);
		if(debugMode == 2){
			try {
				masterOut.writeObject(new ReportActionMessage(this.getID(), Timing.after, Action.send, msg));
				ReportActionResponseMessage response = (ReportActionResponseMessage)masterIn.readObject();
				if(response.getResponse() == ActionResponse.killnode)
					System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

}
