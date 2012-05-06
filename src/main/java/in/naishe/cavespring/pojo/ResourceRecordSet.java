package in.naishe.cavespring.pojo;

import java.util.List;

public class ResourceRecordSet {
	public String name;
	public String type;
	public List<String> resourceRecords;
	public String ttl;
	
	@Override
	public String toString() {
		return "{\n" +
				"           name: "+name+"\n" +
				"           type: "+type+"\n" +
				"            ttl: "+ttl+"\n" +
				"ResourceRecords: "+resourceRecords+"\n" +
				"}";
	}
}
