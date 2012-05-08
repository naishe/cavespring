package in.naishe.cavespring.main;

import in.naishe.cavespring.action.Route53Commands;
import in.naishe.cavespring.utils.CaveSpringRestUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;



public class GetNameServer {

	private static String usage = 
		"uses: \n" +
		"\trns -id hostedZoneId";
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
		if(ConsoleHelper.isHelp(args) || param.get("-id")==null){
			System.out.println(usage);
			System.exit(1);
		}

		System.out.println(CaveSpringRestUtils.prettyPrintXML(Route53Commands.getNameServers(param.get("-id"))));
	}

}
