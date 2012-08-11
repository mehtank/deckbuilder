package com.mehtank.dominion.engine;

import java.util.ArrayList;

public class CardPile extends ArrayList<Card> {
	private static final long serialVersionUID = 3476703878599183216L;

	String name;

    String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	public CardPile(String name) {
    	super();
        this.name = name;
    }

    Card get(Card card) {
        for (Card c : this) {
            if (c.equals(card)) {
                return c;
            }
        }

        return null;
    }

    public Card[] toArray() {
        return toArray(new Card[0]);
    }
}
