package in.naishe.cavespring.main;

import java.util.HashMap;

public class ConsoleHelper {

	
	public static HashMap<String, String> getParam(String[] args) {
		HashMap<String, String> hm = new HashMap<String, String>();
		if(args==null || args.length == 0)
			return hm;
		for(int i=0; i<args.length; i++){
			if(args[i].startsWith("-")){
				if(i<args.length-1)
					hm.put(args[i], args[++i]);
				else
					hm.put(args[i], "");
			}
			else
				hm.put(""+i, args[i]);
		}
		return hm;
	}

}
