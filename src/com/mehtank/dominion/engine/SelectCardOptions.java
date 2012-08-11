package com.mehtank.dominion.engine;

import java.io.Serializable;
import java.util.ArrayList;

public class SelectCardOptions implements Serializable {
	private static final long serialVersionUID = -1473106875075390348L;

//	public enum SelectType {
//		GETACTION, GETBUY, DOACTION, ATTACK
//	};
//
//	public SelectType selectType;
	public String header = "";
	public boolean fromHand = false;
	public boolean fromTable = false;
	public int maxCost = -1;
	public int minCost = -1;
	public int count = 1;
	public boolean exactly = true;
	public boolean isAction = false;
	public boolean isReaction = false;
	public boolean isTreasure = false;
	public boolean isVictory = false;
	public boolean isPassable = false;
	public boolean ordered = false;

//	public SelectCardOptions setType(SelectType s) {selectType = s; return this;}
	public SelectCardOptions to(String s) {header = s; return this;}
	public SelectCardOptions fromHand() {fromHand = true; return this;}
	public SelectCardOptions fromTable() {fromTable = true; return this;}
	public SelectCardOptions isAction() {isAction = true; return this;}
	public SelectCardOptions isReaction() {isReaction = true; return this;}
	public SelectCardOptions isTreasure() {isTreasure = true; return this;}
	public SelectCardOptions isVictory() {isVictory = true; return this;}
	public SelectCardOptions isPassable() {isPassable = true; return this;}
	public SelectCardOptions ordered() {ordered = true; return this;}
	public SelectCardOptions maxCost(int c) {maxCost = c; return this;}
	public SelectCardOptions minCost(int c) {minCost = c; return this;}
	public SelectCardOptions exactly(int c) {count = c; return this;}
	public SelectCardOptions atMost(int c) {count = c; exactly = false; return this;}

	public boolean checkValid(Card c) {
		if ((maxCost >= 0) && (c.getCost() > maxCost)) return false;
		if ((minCost >= 0) && (c.getCost() < minCost)) return false;
		if (isAction && !c.isAction()) return false;
		if (isReaction && !c.isReaction()) return false;
		if (isTreasure && !c.isTreasure()) return false;
		if (isVictory && !c.isVictory()) return false;
		if (isReaction && !c.isReaction()) return false;
		return true;
	}
	
	public String toString() {
		String str = "Select ";

		if (exactly) str += "exactly ";
		else str += "up to ";
		
		str += count + " ";
		
		if (isAction) str += "action ";
		if (isReaction) str += "reaction ";
		if (isTreasure) str += "treasure ";
		if (isVictory) str += "victory ";

		str += "cards ";
		
		if (fromHand) str += "from your hand ";
		if (fromTable) str += "from the table ";
		
		if (ordered) str += "in order ";
		
		if (minCost >= 0 || maxCost >= 0) {
			str += "that costs ";
			if (minCost >= 0) str += "at least ¤" + minCost + " ";
			if (maxCost >= 0) str += "at most ¤" + maxCost + " ";
		}
		
		str += "to " + header;
		if (isPassable) str += " (or pass)";
		return str;
	}
	
	boolean isValid(Card c) {
		if (isAction && !(c.isAction()))
			return false;
		if (isReaction && !(c.isReaction()))
			return false;
		if (isTreasure && !(c.isTreasure()))
			return false;
		if (isVictory && !(c.isVictory()))
			return false;
		
		if ((minCost >= 0) && (c.getCost() < minCost))
			return false;
		if ((maxCost >= 0) && (c.getCost() > maxCost))
			return false;

		return true;
	}
	
	Card[] filter(Card[] input) {
		ArrayList<Card> output = new ArrayList<Card>();
		for (Card c : input)
			if (isValid(c)) 
				output.add(c);
		return output.toArray(new Card[0]);
	}
}
