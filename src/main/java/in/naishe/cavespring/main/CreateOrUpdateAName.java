package in.naishe.cavespring.main;

import in.naishe.cavespring.action.Route53Commands;
import in.naishe.cavespring.pojo.ResourceRecordSet;
import in.naishe.cavespring.utils.CaveSpringConfig;
import in.naishe.cavespring.utils.CaveSpringRestUtils;
import in.naishe.cavespring.utils.ChangeResourceRecordSet;
import in.naishe.cavespring.utils.PrettyXML;
import in.naishe.cavespring.utils.ChangeResourceRecordSet.Type;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import nu.xom.ParsingException;
import nu.xom.ValidityException;


public class CreateOrUpdateAName {

	private static final String usage = "" +
			"usage: Create Resource Record\n" +
			"\tcrr [-t TYPE] -z hostedZone -n name -i ip [-m \"some comment\"]\n" +
			"\tTYPE can be any of {A(default), AAAA, CNAME, MX, NS, PTR, SOA, SPF, SRV, TXT}";
	/**
	 * Creates a New A-Name record for the given IP and domainName
	 * If there already exists a A-name record, it deletes it and 
	 * overwrites with the new one
	 * @param args
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws MalformedURLException 
	 * @throws InvalidKeyException 
	 * @throws ParsingException 
	 * @throws ValidityException 
	 */
	public static void main(String[] args) throws InvalidKeyException, MalformedURLException, NoSuchAlgorithmException, IllegalStateException, IOException, ValidityException, ParsingException {
		HashMap<String, String> params = ConsoleHelper.getParam(args);
		if(params.get("-n")==null || params.get("-i")==null || params.get("-z")==null){
			System.out.println(usage);
			System.exit(1);
			return;
		}
		
		String name = params.get("-n");
		String ip = params.get("-i");
		String hostedZone = params.get("-z");
		String comment = params.get("-m")!=null?params.get("-m"): "Create on "+new Date().toString();
		Type type = params.get("-t")!=null?CaveSpringRestUtils.stringToType(params.get("-t")):Type.A;
		CaveSpringConfig.enableVerbose();
		String XML = Route53Commands.getResourceRecordSets(hostedZone, null, name, 0);
		//Check if domain name already allocated to some IP
		ResourceRecordSet rrs = CaveSpringRestUtils.isMatchingResourceRecordSetExist(name, XML);
		ArrayList<ChangeResourceRecordSet> changeRequest = new ArrayList<ChangeResourceRecordSet>();
		//if record already added, add to request list to delete
		if(rrs!=null){
			changeRequest.add(
					new ChangeResourceRecordSet(
						ChangeResourceRecordSet.Action.DELETE, 
						rrs.name, 
						CaveSpringRestUtils.stringToType(rrs.type), 
						rrs.ttl!=null?Integer.parseInt(rrs.ttl):300, 
						rrs.resourceRecords)
					);
			System.out.println(
					"Resource for domain name: '"+name+"', already exits.\n"+rrs.toString() + "\nDeleting this record!\n"
					);
		}
		changeRequest.add(
				new ChangeResourceRecordSet(
					ChangeResourceRecordSet.Action.CREATE, 
					name, 
					type, 
					300, 
					ip)
				);
		String output = "";
		output = Route53Commands.changeResourceRecordSets(hostedZone, changeRequest, comment);
		System.out.println(new PrettyXML().format(output) + "\n");
		System.out.println("Request to add domain ["+name+" --> "+ip+"] is sent to Route53.");
	}

}
