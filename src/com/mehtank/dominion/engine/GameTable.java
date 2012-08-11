package com.mehtank.dominion.engine;

import java.util.ArrayList;
import java.util.Collections;

public class GameTable extends Clump {

	CardPile trash;
	ArrayList<Card> supplyArray = new ArrayList<Card>();

	GameTable() {
		trash = addPile("Trash");
	}
	
	void addSupplyPile(Card c, int count) {
		supplyArray.add(c);
		
		CardPile pile = addPile(c.getName());

		for (int i = 0; i < count; i++) {
			Card thisCard = c.copy();
			pile.add(thisCard);
		}
	}

	void makePiles(Card[] cards, int numPlayers) {
		for (Card c: cards) {
			int count = 10;

			// TODO: Special cases -- handle elegantly somewhere else?
			if (c.isCurse())
				count = (numPlayers - 1) * 10;
			if (c.isVictory()) 
				count = (numPlayers < 3) ? 8 : 12;
			
			if (c.equals("Estate"))
				count += numPlayers * 3;
			if (c.equals("Province") && numPlayers > 4)
				count += 3;
			if (c.equals("Province") && numPlayers > 5)
				count += 3;
			
			if (c.equals("Copper"))
				count = 60;
			if (c.equals("Silver"))
				count = 40;
			if (c.equals("Gold"))
				count = 30;
			
			addSupplyPile(c, count);
		}
		Collections.sort(supplyArray);
	}
	
	boolean gameOver() {
    	if (piles.containsKey("Colony"))
    		if (piles.get("Colony").size() == 0)
    			return true;
    	if (piles.containsKey("Province"))
    		if (piles.get("Province").size() == 0)
    			return true;

    	int empty = 0;
    	for (CardPile p : piles.values())
    		if (p.size() == 0)
    			empty++;
    	
    	return (empty >= 3);
	}

	Card[] getSupplyArray() {
		return supplyArray.toArray(new Card[0]);
	}
	
	Card takeFromPile(Card card) {
		return takeFromPile(card.getName());
	}

	int getCount(Card c) {
		CardPile p = getPile(c.getName());
		if (p == null) 
			return 0;
		return p.size();
	}

	String getState() {
		String str = "Supply:";

		for (Card c : supplyArray) {
			str += "\n    " + getCount(c) + ": " + c.getTitle();
			str += " (" + c.getCost() + ")";
			// str += "\n        --" + c.getDescription();
		}
		return str;
	}
}
