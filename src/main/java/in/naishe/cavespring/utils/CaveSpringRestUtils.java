package in.naishe.cavespring.utils;

import in.naishe.cavespring.pojo.ResourceRecordSet;
import in.naishe.cavespring.utils.ChangeResourceRecordSet.Type;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.apache.commons.codec.binary.Base64;

/**
 * Null safe utility methods
 * @author naishe
 *
 */
public class CaveSpringRestUtils {
	
	public static final String R53_HOSTNAME = "route53.amazonaws.com";
    public static final String R53_BASE_URL = "https://route53.amazonaws.com";
	public static final String R53_DATE_PATH = "/date";
	public static final String R53_CREATE_HOSTED_ZONE ="/2010-10-01/hostedzone";
	public static final String R53_CHANGE_STATUS ="/2010-10-01/change";
	
	public static Map<String, List<String>> getHeader(String urlstr) throws IOException{
		URL url = new URL(urlstr);
        URLConnection connection = url.openConnection();
        Map<String, List<String>> mp = connection.getHeaderFields();
        return mp!=null?mp:new HashMap<String,List<String>>();
	}
	
	public static String getServerDate() throws IOException{
		List<String> l = getHeader(getDateService()).get("Date");
		return l!=null&&l.size()>0?l.get(0):"";
	}

	public static String getDateService() {
		return R53_BASE_URL+R53_DATE_PATH;
	}
	
	/**
	 * X-Amzn-Authorization: AWS3-HTTPS AWSAccessKeyId=MyAccessKey,Algorithm=ALGORITHM,Signature=Base64( Algorithm((ValueOfDateHeader), SigningKey) )
	 * The algorithm can be HmacSHA1 or HmacSHA256.
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public static String getAmznAuthorizationHeader(String amznDate) throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, IOException{
		return
			"AWS3-HTTPS " +
			"AWSAccessKeyId="+ CaveSpringConfig.getAccessKey() +"," +
			"Algorithm=HmacSHA1," +
			"Signature="+getSignature(amznDate);
		
	}

	/**
	 * Base64( Algorithm((ValueOfDateHeader), SigningKey) )
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public static String getSignature(String amznDate) throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, IOException {
		byte[] keyBytes = CaveSpringConfig.getSecretKey().getBytes();
		SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA1");
		Mac m = Mac.getInstance("HmacSHA1");
		m.init(key);
		byte[] mac = m.doFinal(amznDate.getBytes());
		return new String(Base64.encodeBase64(mac));
	}
	
	/**
	 * Sends post request to the path as text/xml with authorized header, path must start with "/" 
	 * @param path
	 * @param xmlData
	 * @return response from AWS
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws IllegalStateException
	 */
	public static String postXmlData(String  path, String xmlData) 
	  throws 
		MalformedURLException, 
		IOException, 
		InvalidKeyException, 
		NoSuchAlgorithmException, 
		IllegalStateException
	{
		HttpURLConnection conn = (HttpURLConnection)new URL(R53_BASE_URL+path).openConnection();
		String amznDate = CaveSpringRestUtils.getServerDate();
	    String authhdr = CaveSpringRestUtils.getAmznAuthorizationHeader(amznDate);
	    conn.setRequestMethod("POST");
		conn.setRequestProperty("Host", R53_HOSTNAME);
		conn.setRequestProperty("X-Amzn-Authorization", authhdr);
		conn.setRequestProperty("Content-Length", xmlData.length()+"");
		conn.setRequestProperty("Content-Type", "text/xml");
		conn.setRequestProperty("x-amz-date",amznDate);
		conn.setDoOutput(true);
		
		if(CaveSpringConfig.verbose){
			System.out.println("URL: "+R53_BASE_URL+path);
			System.out.println("Method: POST");
			System.out.println("Host: " + R53_HOSTNAME);
			System.out.println("X-Amzn-Authorization: "+ authhdr);
			System.out.println("Content-Length: "+ xmlData.length()+"");
			System.out.println("Content-Type: "+ "text/xml");
			System.out.println("x-amz-date: "+amznDate);
			System.out.println("XML Data: \n"+ xmlData);
		}
		
		PrintWriter pw = new PrintWriter(conn.getOutputStream());
		pw.write(xmlData);
		pw.flush();
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String str = "";
		String line;
	    while((line = rd.readLine()) != null)
	    	str = str + line;
	    return str;
	}
	
	/**
	 * <p>performs get request, it expects you to pass path with query String like <pre>/2010-10-01/hostedzone/Z1PA6795UKMFR9/rrset?type=NS&name=example.com&maxitems=10</pre></p>
	 * @param pathWithQueryString
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws IllegalStateException
	 */
	public static String doGetRequest(String pathWithQueryString) throws MalformedURLException, IOException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException{
		HttpURLConnection conn = (HttpURLConnection)new URL(R53_BASE_URL+pathWithQueryString).openConnection();
		String amznDate = CaveSpringRestUtils.getServerDate();
	    String authhdr = CaveSpringRestUtils.getAmznAuthorizationHeader(amznDate);
	    conn.setRequestMethod("GET");
		conn.setRequestProperty("Host", R53_HOSTNAME);
		conn.setRequestProperty("X-Amzn-Authorization", authhdr);
		conn.setRequestProperty("x-amz-date",amznDate);
		conn.setDoOutput(true);

		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String str = "";
		String line;
	    while((line = rd.readLine()) != null)
	    	str = str + line;
	    return str;
	}
	
	// --- Formatting and general purpose utilities -- 
	
	public static boolean isValid(String s){
		if(s!=null && s.trim().length()>0)
			return true;
		return false;
	}
	
	public static String prettyPrintXML(String xml){
		return new PrettyXML().format(xml);
	}
	
	/**
	 * Looks into ResourceRequest's response XML for given domain name
	 * If exists returns the existing ResourceRecordSet, else returns null.
	 * @param name
	 * @param xml
	 * @return existingResourceRecord or null
	 * @throws ValidityException
	 * @throws ParsingException
	 * @throws IOException
	 */
	public static ResourceRecordSet isMatchingResourceRecordSetExist(String name, String xml) throws ValidityException, ParsingException, IOException{
		List<ResourceRecordSet> rrss = XMLParser.parseResourceRecordSet(xml);
		for(ResourceRecordSet rrs: rrss)
			if(rrs.name.equalsIgnoreCase(name+"."))
				return rrs;
		return null;
	}
	
	

	
	public static Type stringToType(String type){
		String[] types = {"A", "AAAA", "CNAME", "MX", "NS", "PTR", "SOA", "SPF", "SRV", "TXT"};
		int index = 0;
		for(String s: types)
			if(s.equalsIgnoreCase(type))
				break;
			else
				index++;
		switch (index) {
			case 0:
				return Type.A;
			case 1:
				return Type.AAAA;
			case 2:
				return Type.CNAME;
			case 3:
				return Type.MX;
			case 4:
				return Type.NS;
			case 5:
				return Type.PTR;
			case 6:
				return Type.SOA;
			case 7:
				return Type.SPF;
			case 8:
				return Type.SRV;
			case 9:
				return Type.TXT;
			default:
				return Type.A;
		}
	}
}
