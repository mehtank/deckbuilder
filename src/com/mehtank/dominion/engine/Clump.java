package com.mehtank.dominion.engine;

import java.util.HashMap;

public class Clump {
    HashMap<String, CardPile> piles = new HashMap<String, CardPile>();
    HashMap<String, Integer> tokens = new HashMap<String, Integer>(); 

    CardPile addPile(String name) {
    	CardPile temp = new CardPile(name);
    	piles.put(name, temp);
    	return temp;
    }
    Integer addToken(String name) {
    	Integer temp = new Integer(0);
    	tokens.put(name, temp);
    	return temp;
    }

    CardPile getPile(String name) {
    	return piles.get(name);
    }
    Integer getToken(String name) {
    	return tokens.get(name);
    }

    Card takeFromPile(String string) {
		CardPile p = piles.get(string);

		if (p == null)
			return null;
		if (p.size() > 0)
			return p.remove(0);
		return null;
	}
}