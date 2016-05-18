package osu.planisphere;

/**
 * The identifier of a node. It is composed
 * of a role and an ID, such as CLIENT.1, SERVER.2, etc. 
 * @author Yang Wang
 *
 */
public class NodeIdentifier {

	private int hashCode;
	
	public NodeIdentifier(Role role, int id){
		if (id>(1<<24))
			throw new RuntimeException("id too large: "+id);
		hashCode = (role.ordinal() << 24) | id;
	}
	
	public Role getRole(){
		return Role.values()[hashCode>>24];
	}
	
	public int getID(){
		return hashCode & 0x0FFF;
	}
	
	@Override
	public int hashCode(){
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o){
		NodeIdentifier n = (NodeIdentifier)o;
		return this.hashCode == n.hashCode;
	}
	
	@Override
	public String toString(){
		return getRole().toString() + "." + getID();
	}

}
