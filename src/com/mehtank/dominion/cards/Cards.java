package com.mehtank.dominion.cards;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mehtank.dominion.engine.Card;
import com.mehtank.dominion.utils.XMLUtils;

public class Cards {
	private String set = "";
	private ArrayList<Card> cards = new ArrayList<Card>();
	
	public String getSet() {
		return set;
	}
	public Card[] getCards() {
		return cards.toArray(new Card[0]);
	}

	public Cards() {
		loadCards();
	}
	
	public Card getCard(String name) {
		for (Card c : cards)
			if (c.equals(name))
				return c.copy();
		return null;
	}
	
	private void loadCards() {
		InputStream xmlStream = this.getClass().getResourceAsStream("cards.xml");
		InputStream textStream = this.getClass().getResourceAsStream("text.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		Document doc;
		Document text;
		try {
			doc = dBuilder.parse(xmlStream);
			text = dBuilder.parse(textStream);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		Element root = doc.getDocumentElement();
		root.normalize();
		Element textroot = text.getDocumentElement();
		textroot.normalize();

		String rootname = root.getNodeName();

		if (!rootname.equals("cards"))
			return;

		set = root.getAttribute("set");

		for (Element e : XMLUtils.getElementListByTagName(root, "card")) {
			String name = e.getAttribute("name");
			NodeList textData = null;
			for (Element t : XMLUtils.getElementListByTagName(textroot, "card")) {
				if (t.getAttribute("name").equals(name)) {
					textData = t.getChildNodes();
					break;
				}
			}
			if (textData != null) 
			    for (int i = 0; i < textData.getLength(); i++) 
					e.appendChild(doc.importNode(textData.item(i), true));
			makeCard(e);
		}
	}

	private void makeCard(Element element) {
		String name = element.getAttribute("name");
		String classname = this.getClass().getPackage().getName() + "." + name;
		Class<?> c = null;
		try {
			c = Class.forName(classname);
		} catch (ClassNotFoundException e) {
			return;
		}
		
		if (c == null)
			return;

		Card card;
		try {
			card = (Card) c.newInstance();
		} catch (InstantiationException e) {
			return;
		} catch (IllegalAccessException e) {
			return;
		}
		
		card.setXML(element);
		cards.add(card);
	}
	
}
