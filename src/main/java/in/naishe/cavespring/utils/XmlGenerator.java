package in.naishe.cavespring.utils;

import java.util.List;

public class XmlGenerator {
	
	public static String createHostedZoneXml(String domainName, String uniqueCallerRef, String comment){
		return 
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
		"<CreateHostedZoneRequest xmlns=\"https://route53.amazonaws.com/doc/2010-10-01/\">"+
			"<Name>"+domainName+".</Name>"+
			"<CallerReference>"+uniqueCallerRef+"</CallerReference>"+
		    "<HostedZoneConfig>"+
		       "<Comment>"+comment+"</Comment>"+
		    "</HostedZoneConfig>"+
		"</CreateHostedZoneRequest>";
		
	}
	
	/**
	 * @see <a href="http://docs.amazonwebservices.com/Route53/latest/APIReference/index.html?API_ChangeResourceRecordSets.html">ChangeResourceRecordSets XML</a>
	 * @param changeResourceRecordSets
	 * @param comment
	 * @return
	 */
	public static String ChangeResourceRecordSetsRequest(List<ChangeResourceRecordSet> changeResourceRecordSets, String comment){
		String pre = 
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
		"<ChangeResourceRecordSetsRequest xmlns=\"https://route53.amazonaws.com/doc/2010-10-01/\">"+
		   "<ChangeBatch>"+
		      "<Comment>"+
		      	comment +
		      "</Comment>"+
		      "<Changes>";
		
		String changeSets = "";
		for(ChangeResourceRecordSet set:changeResourceRecordSets)
			changeSets = changeSets + set.toString();
		
		String post =
		      "</Changes>"+
		   "</ChangeBatch>"+
		"</ChangeResourceRecordSetsRequest>";
		
		return pre+changeSets+post;
	}
}
