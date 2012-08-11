package com.mehtank.dominion.utils;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtils {
	public static Integer getIntFromXML(Element e, String attribute) {
		String s = e.getAttribute(attribute);
		if (s == null || s.isEmpty())
			return null;
		return Integer.parseInt(s);
	}

	public static Element[] getElementListByTagName(Element e, String name) {
		NodeList nodes = e.getElementsByTagName(name);
		ArrayList<Element> elements = new ArrayList<Element>();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				elements.add(element);
			}
		}
		return elements.toArray(new Element[0]);
	}	
	
	public static ArrayList<String> getElementValuesByTagName(Element e, String name) {
		Element[] elements = getElementListByTagName(e, name);
		ArrayList<String> strings = new ArrayList<String>();
		for (Element elem : elements) {
			strings.add(elem.getTextContent());
		}
		return strings;
	}	
	
	public static String getElementValueByTagName(Element e, String name) {
		ArrayList<String> strings = getElementValuesByTagName(e, name);
		if (strings == null)
			return null;
		if (strings.size() > 0)
			return strings.get(0);
		return null;
	}	
}
