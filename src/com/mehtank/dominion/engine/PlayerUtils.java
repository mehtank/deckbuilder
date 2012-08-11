package com.mehtank.dominion.engine;

import java.util.ArrayList;
import java.util.Arrays;

import com.mehtank.dominion.comms.GameQuery;
import com.mehtank.dominion.comms.GameQuery.QueryType;

public abstract class PlayerUtils extends Clump {
    
    public Card pickACard(SelectCardOptions sco, Card[] allcards) {
    	sco.exactly(1);
    	
    	Card[] cs = pickCards(sco, allcards);
    	if (cs == null)
    		return null;
    	if (cs.length > 0)
    		return cs[0];
    	return null;
    }
    
    public Card[] pickCards(SelectCardOptions sco, Card[] allcards) {
        GameQuery p = new GameQuery(QueryType.GETCARD, QueryType.CARD)
        	.setInteger(sco.count)
        	.setBoolean(sco.exactly)
        	.setString(sco.header)
        	.setObject(sco);
        
        p = query(p);
        if (p == null)
        	return null;
        else if (p.i == 0)
        	return null;
		else if (p.o instanceof Card[] || p.o instanceof String[]) {
			String[] selected = new String[0];
			if (p.o instanceof Card[]) {
				ArrayList<String> a = new ArrayList<String>();
				for (Card c : (Card[]) p.o) 
					a.add(c.getName());
				selected = a.toArray(new String[0]);
			} else
				selected = (String[])p.o;
			
			ArrayList<Card> ret = new ArrayList<Card>();
			ArrayList<Card> all = new ArrayList<Card>(Arrays.asList(allcards));
			for (int i = 0; i < selected.length; i++) {
				for (int j = 0; j < all.size(); j++) {
					if (all.get(j).equals(selected[i])) {
						ret.add(all.get(j));
						all.remove(j);
						break;
					}
				}
			}
			return ret.toArray(new Card[0]);
		}
        
        return null;
    }
    
    public String selectString(String header, String[] s) {
    	GameQuery p = new GameQuery(QueryType.GETSTRING, QueryType.STRING)
    					.setString(header)
    					.setObject(s);
    	p = query(p);
    	if (p == null)
    		return null;
    	else
    		return p.s;
    }	
	public Integer[] orderCards(Card[] cards) {
		GameQuery p = new GameQuery(QueryType.ORDERCARDS, QueryType.CARDORDER)
			.setString("Return cards to deck:")
			.setObject(cards);
    
		p = query(p);
		if (p == null)
			return null;
		else
			try {
				return (Integer[])p.o;
			} catch (ClassCastException e) {
				return null;
			}
	}
	
    public boolean selectBoolean(String header, String strTrue, String strFalse) {
    	String [] s = new String [] {strTrue, strFalse};
    	String r = selectString(header, s);
    	if (strTrue.equals(r))
    		return true;
    	return false;
    }

    
    /*
    public Card pickACard(TurnContext context, String header, Card[] cards, boolean allowNull) {
    	SelectCardOptions sco = new SelectCardOptions();
    	if (allowNull)
    		sco.isPassable();
    	for (Card card : cards)
    		sco.addValidCard(card);
    	
    	return pickACard(context, header, sco);
    }

    final int ANYFROMHAND = 0;
    final int ACTIONFROMHAND = 1;
    final int TREASUREFROMHAND = 2;
    
    final boolean PASSABLE = true;
    final boolean NOTPASSABLE = false;
    
    public Card[] getFromHand(TurnContext context, String header, boolean isPassable, int type, int count, boolean exact, boolean ordered) {
    	Card[] hand = context.currentPlayer.getHand();
        if (hand.length == 0)
            return null;

		SelectCardOptions sco = new SelectCardOptions()
			.fromHand();
		
		if (isPassable)
			sco.isPassable();
		if (ordered)
			sco.ordered();

		ArrayList<Card> handList = new ArrayList<Card>();
        for (Card card : hand) {
        	handList.add(card);
        	switch (type) {
        	case ACTIONFROMHAND:
        		if (card.isAction())
        			sco.isAction().addValidCard(card);
            	break;
        	case TREASUREFROMHAND:
        		if (card.isTreasure())
        			sco.isTreasure().addValidCard(card);
            	break;
            default:
        		sco.addValidCard(card);
        	}
        }
        
        if (sco.allowedCards.size() == 0)
        	return null;

        String str = "Pick ";
        if (count == 1) 
        	str += "a card ";
        else if (exact)
        	str += "exactly " + count + " cards ";
        else
        	str += "up to " + count + " cards ";
        		
        Card[] tempCards = pickCards(context, str + "from your hand to " + header, sco, count, exact);
                
        if (tempCards == null)
        	return null;
        
        for (int i=0; i<tempCards.length; i++)
        	for (Card c : handList) 
        		if (c.equals(tempCards[i])) {
                	tempCards[i] = c;
                	handList.remove(c);
        			break;
        		}

        return tempCards;
    }
    
    public Card[] getAnyFromHand(TurnContext context, String header, boolean isPassable, int count, boolean exact) {
    	return getFromHand(context, header, isPassable, ANYFROMHAND, count, exact, false);
    }
    public Card[] getOrderedFromHand(TurnContext context, String header, boolean isPassable, int count, boolean exact) {
    	return getFromHand(context, header, isPassable, ANYFROMHAND, count, exact, true);
    }
    public Card getAnyFromHand(TurnContext context, String header, boolean isPassable) {    	
    	Card[] cs = getFromHand(context, header, isPassable, ANYFROMHAND, 1, true, false);
    	if (cs == null)
    		return null;
    	return cs[0];

    }
    public Card getActionFromHand(TurnContext context, String header, boolean isPassable) {
    	Card[] cs = getFromHand(context, header, isPassable, ACTIONFROMHAND, 1, true, false);
    	if (cs == null)
    		return null;
    	return cs[0];
    }
    public Card getTreasureFromHand(TurnContext context, String header, boolean isPassable) {
    	Card[] cs = getFromHand(context, header, isPassable, TREASUREFROMHAND, 1, true, false);
    	if (cs == null)
    		return null;
    	return cs[0];
    }

    public Card getFromTable(TurnContext context, String header, int maxCost, int minCost, boolean isBuy) {
        Card[] cards = context.game.getCardsInPlay();
        SelectCardOptions sco = new SelectCardOptions()
        	.fromTable()
        	.isPassable();
        for (Card card : cards)
        	if ((!isBuy && card.getCost() <= maxCost) || 
        		(isBuy && context.canBuy(card, maxCost)))
        		if (card.getCost() >= minCost)
        			sco.addValidCard(card);

        if (sco.allowedCards.size() == 0)
        	return null;
        
        if (maxCost < Integer.MAX_VALUE)
        	maxCost += context.bridgesPlayed;
        if (minCost > 0)
        	minCost += context.bridgesPlayed;

        sco.maxCost(maxCost)
           .minCost(minCost);
        
        String cost = "";
        if (minCost == maxCost)
        	cost = "that costs exactly (" + maxCost + ") ";
        else if ((minCost <= 0) && (maxCost < Integer.MAX_VALUE))
        	cost = "that costs at most (" + maxCost + ") ";
        else if (maxCost < Integer.MAX_VALUE)
        	cost = "that costs between (" + minCost + ") and (" + maxCost + ") ";
        else if (minCost > 0)
        	cost = "that costs at least (" + minCost + ") ";

        return pickACard(context, "Select a card from the table " + cost + "to " + header, sco);
    }
    public Card getFromTable(TurnContext context, String header, int maxCost, int minCost) {
    	return getFromTable(context, header, maxCost, minCost, false);
    }
    public Card getFromTable(TurnContext context, String header, int maxCost, boolean isBuy) {
    	return getFromTable(context, header, maxCost, Integer.MIN_VALUE, isBuy);
    }
    public Card getFromTable(TurnContext context, String header, int maxCost) {
    	return getFromTable(context, header, maxCost, Integer.MIN_VALUE, false);
    }
        */
    
    /*
    public int selectInt(String header, int maxInt, int errVal) {
    	ArrayList<String> options = new ArrayList<String>();
    	for (int i=0; i<=maxInt; i++)
    		options.add("" + i);

    	String o = selectString(header, options.toArray(new String[0]));

		try {
			return Integer.parseInt(o);
		} catch (NumberFormatException e) { 
			return errVal;
		}
    }
    public boolean selectBooleanWithCard(String header, Card c, String strTrue, String strFalse) {
    	return selectBoolean(header + c.getName(), strTrue, strFalse);
    }
    public boolean selectBooleanWithCardAndPlayer(String header, Card c, Player p, String strTrue, String strFalse) {
    	return selectBooleanWithCard("From " + p.getName() + "\n" + header, c, strTrue, strFalse);
    }    
	*/
    
    public abstract GameQuery query(GameQuery p);
}