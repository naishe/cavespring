package in.naishe.cavespring.utils;

import in.naishe.cavespring.pojo.ResourceRecordSet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;


public class XMLParser {

	public static List<ResourceRecordSet> parseResourceRecordSet(String xml) throws ValidityException, ParsingException, IOException{
		ArrayList<ResourceRecordSet> list = new ArrayList<ResourceRecordSet>(); 
		Builder b = new Builder();
		Document d = b.build(new ByteArrayInputStream(xml.getBytes()));
		Elements firstLevelChilds = d.getRootElement().getChildElements();
		Element resourceRecordSets = null;
		for(int i=0; i<firstLevelChilds.size(); i++)
			if(firstLevelChilds.get(i).getLocalName().equalsIgnoreCase("ResourceRecordSets")){
				resourceRecordSets = firstLevelChilds.get(i);
				break;
			}
		if(resourceRecordSets == null)
			return list;
		
		Elements es = resourceRecordSets.getChildElements();
		for(int i=0; i<es.size(); i++){
			ResourceRecordSet rrs = new ResourceRecordSet();
			Element e = es.get(i);
			rrs.name = e.getChild(0).getValue();
			rrs.type = e.getChild(1).getValue();
			rrs.ttl = e.getChild(2).getValue();
			Elements records = ((Element)e.getChild(3)).getChildElements();
			ArrayList<String> recordList = new ArrayList<String>();
			for(int j=0; j<records.size(); j++)
				recordList.add(records.get(j).getValue());
			rrs.resourceRecords = recordList;
			list.add(rrs);
		}
		return list;
	}
}
