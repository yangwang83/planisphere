package osu.planisphere.demo;

import osu.planisphere.Message;
import osu.planisphere.NodeIdentifier;

public class TestMessage extends Message{
	
	private int value;
	public TestMessage(NodeIdentifier sender, int value){
		super(sender);
		this.value = value;
	}
	
	@Override
	public String toString(){
		return value + " from "+sender;
	}
}
