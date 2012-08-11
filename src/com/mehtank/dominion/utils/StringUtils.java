package com.mehtank.dominion.utils;

import com.mehtank.dominion.engine.Card;

public class StringUtils {
	public static String getCanonicalName(String orig) {
		String s = new String(orig);
		return s.replaceAll("\\W","").toLowerCase();
	}

	public static String plays(Card card) {
        return "plays " + card.getName() + ", getting " + addsString(card, ", ");
	}
	public static String played(Card card) {
        return "had played " + card.getName() + ", and gets " + addsStringNext(card, ", ");
	}
	
	private static String addsString(int addCards, int addActions, int addBuys, int addGold, int addVP, String sep) {
		String s = "";
		
		if (addCards > 1) s += sep + "+" + addCards + " cards";
		else if (addCards > 0) s += sep + "+" + addCards + " card";

		if (addActions > 1) s += sep + "+" + addActions + " actions";
		else if (addActions > 0) s += sep + "+" + addActions + " action";
    		
		if (addBuys > 1) s += sep + "+" + addBuys + " buys";
		else if (addBuys > 0) s += sep + "+" + addBuys + " buy";
		
		if (addGold > 0) s += sep + "+ ¤" + addGold;
		if (addVP > 0) s += sep + "+<" + addVP + "> VP";
		
		if (s.length() > 0)
			s = s.substring(sep.length());
		
		return s;
	}

	public static String addsString(Card c, String sep) {
		return addsString(c.getAddCards(), c.getAddActions(), c.getAddBuys(), c.getAddCoin() + c.getCoin(), c.getAddVP(), sep);
	}
	public static String addsStringNext(Card c, String sep) {
		return addsString(c.getAddCardsNext(), c.getAddActionsNext(), c.getAddBuysNext(), c.getAddCoinNext(), c.getAddVPNext(), sep);
	}
	
}
