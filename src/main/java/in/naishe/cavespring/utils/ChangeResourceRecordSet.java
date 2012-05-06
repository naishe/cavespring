package in.naishe.cavespring.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Pojo representation of 
 		<Change>
            <Action>CREATE</Action>
            <ResourceRecordSet>
               <Name>www.example.com.</Name>
               <Type>A</Type>
               <TTL>300</TTL>
               <ResourceRecords>
                  <ResourceRecord>
                     <Value>192.0.2.1</Value>
                  </ResourceRecord>
               </ResourceRecords>
            </ResourceRecordSet>
        </Change>

 * @author naishe
 *
 */
public class ChangeResourceRecordSet {

	public enum Action{CREATE, DELETE};
	public enum Type{A, AAAA, CNAME, MX, NS, PTR, SOA, SPF, SRV, TXT}

	private Action action;
	private String name;
	private Type type;
	private int ttl;
	private List<String> value;
	
	/**
	 * Use it for single value
	 * @param action
	 * @param name
	 * @param type
	 * @param ttl
	 * @param value
	 */
	public ChangeResourceRecordSet(Action action, String name, Type type, int ttl, String value){
		this(action, name, type, ttl, convertStringToList(value));
	}
	
	/**
	 * Use it if ResourceRecord has more than one values
	 * @param action
	 * @param name
	 * @param type
	 * @param ttl
	 * @param values
	 */
	public ChangeResourceRecordSet(Action action, String name, Type type, int ttl, List<String> values){
		this.action = action;
		this.name = name.endsWith(".")?name.substring(0, name.length()-1):name;
		this.type = type;
		this.ttl = ttl;
		this.value = values;
	}
	
	public Action getAction() {
		return action;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public int getTtl() {
		return ttl;
	}

	public List<String> getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		String values = "";
		for(String v: this.value)
			values+="<Value>"+v+"</Value>";
		if(!CaveSpringRestUtils.isValid(values))
			values = "<Value>"+""+"</Value>";
		return 
		"<Change>"+
	        "<Action>"+this.action+"</Action>"+
	        "<ResourceRecordSet>"+
	           "<Name>"+this.name+".</Name>"+
	           "<Type>"+this.type+"</Type>"+
	           "<TTL>"+this.ttl+"</TTL>"+
	           "<ResourceRecords>"+
	              "<ResourceRecord>"+
	                 values +
	              "</ResourceRecord>"+
	           "</ResourceRecords>"+
	        "</ResourceRecordSet>"+
	    "</Change>";
	}
	
	private static List<String> convertStringToList(String singleVal) {
		ArrayList<String> al = new ArrayList<String>();
		al.add(singleVal);
		return al;
	}
}
