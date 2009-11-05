package pl.edu.agh.iptv.commons;

import java.util.List;

import com.thoughtworks.xstream.XStream;

public class Serializator {
	public static XStream xstream = new XStream();
	public static String createXmlFromList(List<CommonMovie> messageCreatorList) {
		return xstream.toXML(messageCreatorList);
	}
	
	@SuppressWarnings("unchecked")
	public static List<CommonMovie> createListFromXml(String xml) {
		return  (List<CommonMovie>)xstream.fromXML(xml);
	}
}
