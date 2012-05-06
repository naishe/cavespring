package in.naishe.cavespring.action;

import in.naishe.cavespring.utils.CaveSpringRestUtils;
import in.naishe.cavespring.utils.ChangeResourceRecordSet;
import in.naishe.cavespring.utils.XmlGenerator;
import in.naishe.cavespring.utils.ChangeResourceRecordSet.Type;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;



public class Route53Commands {

	/**
	 * Creates new hosted zone for a given domain-name
	 * @param domainName
	 * @param uniqueCallerRef
	 * @param comment
	 * @return
	 * @throws InvalidKeyException
	 * @throws MalformedURLException
	 * @throws NoSuchAlgorithmException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String createHostedZone(String domainName, String uniqueCallerRef, String comment) 
		throws 
			InvalidKeyException, 
			MalformedURLException, 
			NoSuchAlgorithmException, 
			IllegalStateException, 
			IOException
	{
		return 
			CaveSpringRestUtils
				.postXmlData(
						CaveSpringRestUtils.R53_CREATE_HOSTED_ZONE, 
						XmlGenerator.createHostedZoneXml(
										domainName, 
										uniqueCallerRef, 
										comment));
	}
	
	
	
	/**
	 * 
	 * @param hostedZoneId
	 * @return list of hosted zone as returned from R53
	 * @throws InvalidKeyException
	 * @throws MalformedURLException
	 * @throws NoSuchAlgorithmException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String getNameServers(String hostedZoneId) 
		throws 
			InvalidKeyException, 
			MalformedURLException, 
			NoSuchAlgorithmException, 
			IllegalStateException, 
			IOException
	{
		return 
			CaveSpringRestUtils
				.doGetRequest(
						CaveSpringRestUtils.R53_CREATE_HOSTED_ZONE+
						"/"+
						hostedZoneId);
	}
	
	/**
	 * @param marker (can be null)
	 * @param maxItems (0 = All)
	 * @return All hosted zones for the given account
	 * @throws InvalidKeyException
	 * @throws MalformedURLException
	 * @throws NoSuchAlgorithmException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String getHostedZones(String marker, int maxItems) 
		throws 
			InvalidKeyException, 
			MalformedURLException, 
			NoSuchAlgorithmException, 
			IllegalStateException, 
			IOException
	{
		return 
		CaveSpringRestUtils
			.doGetRequest(CaveSpringRestUtils.R53_CREATE_HOSTED_ZONE + "?"+
					((CaveSpringRestUtils.isValid(marker))?"marker="+marker+"&":"")+
					((maxItems>0)?"maxitems="+maxItems:""));
	}
	
	
	public static String getHostedZones() throws InvalidKeyException, MalformedURLException, NoSuchAlgorithmException, IllegalStateException, IOException
	{return getHostedZones(null, 0);}
	
	/**
	 * GET /2010-10-01/hostedzone/hosted_zone_ID/rrset?type=NS&name=example.com&maxitems=10
	 * @param hostedZoneId (must)
	 * @param type (can be null)
	 * @param name (can be null)
	 * @param maxItem (can be null)
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws MalformedURLException 
	 * @throws InvalidKeyException 
	 */
	public static String getResourceRecordSets(String hostedZoneId, Type type, String name, int maxItems) throws InvalidKeyException, MalformedURLException, NoSuchAlgorithmException, IllegalStateException, IOException{
		return 
			CaveSpringRestUtils
				.doGetRequest(
						CaveSpringRestUtils.R53_CREATE_HOSTED_ZONE + 
						"/"+
						hostedZoneId+
						"/"+
						"rrset?"+
						((type!=null)?"type="+type+"&":"")+
						((CaveSpringRestUtils.isValid(name)?"name="+name+"&":""))+
						((maxItems>0)?"maxitems="+maxItems:""));
	}

	public static String getResourceRecordSets(String hostedZoneId) throws InvalidKeyException, MalformedURLException, NoSuchAlgorithmException, IllegalStateException, IOException{
		return getResourceRecordSets(hostedZoneId, null, null,0);
	}
	
	/**
	 * 2010-10-01/hostedzone/<hosted Zone ID>/rrset 
	 * @param hostedZoneId
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws MalformedURLException 
	 * @throws InvalidKeyException 
	 */
	public static String changeResourceRecordSets(String hostedZoneId, List<ChangeResourceRecordSet> changeResourceRecordSets, String comment) throws InvalidKeyException, MalformedURLException, NoSuchAlgorithmException, IllegalStateException, IOException{
		return
			CaveSpringRestUtils
				.postXmlData(
						CaveSpringRestUtils.R53_CREATE_HOSTED_ZONE + 
						"/"+
						hostedZoneId+
						"/"+
						"rrset", XmlGenerator.ChangeResourceRecordSetsRequest(changeResourceRecordSets, comment));
	}
	
	/**
	 * GET /2010-10-01/change/C2682N5HXP0BZ4
	 * @param changeId
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws MalformedURLException 
	 * @throws InvalidKeyException 
	 */
	public static String getChangeStatus(String changeId) throws InvalidKeyException, MalformedURLException, NoSuchAlgorithmException, IllegalStateException, IOException{
		return
			CaveSpringRestUtils.doGetRequest(CaveSpringRestUtils.R53_CHANGE_STATUS+"/"+changeId);
	}
	
}
