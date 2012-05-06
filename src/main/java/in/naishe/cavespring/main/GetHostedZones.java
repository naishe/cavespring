package in.naishe.cavespring.main;

import in.naishe.cavespring.action.Route53Commands;
import in.naishe.cavespring.utils.CaveSpringRestUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;



public class GetHostedZones {

	private static String usage = 
		"uses: Retrieve Hosted Zone\n" +
		"\trhz [-m marker] [-n  maxItems]";
	
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
		
		int maxItems = param.get("-n")!=null?Integer.parseInt(param.get("-n")):0;
		System.out.println(
			CaveSpringRestUtils.prettyPrintXML(Route53Commands.getHostedZones(param.get("-m"), maxItems))
		);
	}

}
