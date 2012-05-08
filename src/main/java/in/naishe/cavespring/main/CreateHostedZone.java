package in.naishe.cavespring.main;

import in.naishe.cavespring.action.Route53Commands;
import in.naishe.cavespring.utils.CaveSpringRestUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;



public class CreateHostedZone {

	private static String usage = 
		"uses: Create Hosted Zone\n" +
		"\tchz -d domainName -cr uniqueCallerReference [-m \"some comment\"]";
	/**
	 * @param args
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws MalformedURLException 
	 * @throws InvalidKeyException 
	 */
	public static void main(String[] args) throws InvalidKeyException, MalformedURLException, NoSuchAlgorithmException, IllegalStateException, IOException {
		HashMap<String, String> param = ConsoleHelper.getParam(args);
				
		if(ConsoleHelper.isHelp(args) || param.get("-d")==null ||param.get("-cr")==null){
			System.out.println(usage);
			System.exit(1);
		}
		
		String domain = param.get("-d").trim();
		String callerRef = param.get("-cr").trim();
		String comment = (param.get("-m")!=null)?param.get("-m").trim():"Created on "+new Date().toString();
		
		System.out.println(domain);
		System.out.println(callerRef);
		System.out.println(comment);
		
		System.out.println(CaveSpringRestUtils.prettyPrintXML(Route53Commands.createHostedZone(domain, callerRef, comment)));
		
	}

}
