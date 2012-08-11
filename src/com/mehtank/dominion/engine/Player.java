package com.mehtank.dominion.engine;

import java.util.ArrayList;
import java.util.Collections;
import com.mehtank.dominion.comms.GameQuery;

public abstract class Player extends PlayerUtils implements GameEventListener {
    String name;
    
    CardPile hand;
    CardPile deck;
    CardPile played;
    CardPile nextTurnCards;
    CardPile discard;
    
    Integer vps;

    Game game;
    int index;

    boolean attackable = true;

    public Player() {
    	super();
    	
    	hand = addPile("Hand");
    	deck = addPile("Deck");
    	played = addPile("Played");
    	nextTurnCards = addPile("Duration");
    	discard = addPile("Discard");
    	
    	vps = addToken("VPs");
    }    
    
    void setGame(Game game) {
    	this.game = game;
    }
    void setIndex(int index) {
    	this.index = index;
    }
	public int getIndex() {
		return index;
	}
	
	protected void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

    public int getDeckSize() { return deck.size(); }
    public int getHandSize() { return hand.size(); }
    public Card[] getPlayed() { return played.toArray(); }
    public Card[] getNextTurnCards() { return nextTurnCards.toArray(); }
    public int getDiscardSize() { return discard.size(); }

    public Card[] getHand() {
        return hand.toArray();
    }
    
    int getActionsInHand() {
    	int num = 0;
    	for (Card c : hand) 
    		if (c.isAction())
    			num++;
    	return num;
    }
    
    public int getTreasuresInHand() {
    	int num = 0;
    	for (Card c : hand) 
    		if (c.isTreasure())
    			num++;
    	return num;
    }
    
    Card[] getMatContents(String mat) {
    	CardPile p = getPile(mat);
    	if (p == null)
    		return null;
    	return p.toArray();
    }    
    public int getNumCards() { return getAllCards().size(); }
    
    public int getVPs() { return vps; }

    ArrayList<Card> getAllCards() {
        ArrayList<Card> allCards = new ArrayList<Card>();

        for (CardPile cardlist : piles.values()) {
        	for (Card card : cardlist) {
        		allCards.add(card);
        	}
        }
        Collections.sort(allCards);
        return allCards;
    }

    void replenishDeck(TurnContext context) {
        while (discard.size() > 0) {
        	int i = Game.rand.nextInt(discard.size());
            discard.get(i).moveTo(deck, context);
        }

        GameEvent event = new GameEvent(GameEvent.Type.RESHUFFLES, this, context);
        game.broadcastEvent(event);
    }

    public Card drawFromDeck(TurnContext context) {
    	if (getDeckSize() == 0) 
    		replenishDeck(context);
    	if (getDeckSize() == 0)
    		return null;

    	return deck.get(0);
    }

    public void drawToHand(TurnContext context) {
    	Card c = drawFromDeck(context);
    	if (c != null)
    		c.moveTo(hand, context);
    }
    
    Card peekAtDeckBottom() {
        return deck.get(deck.size() - 1);
    }

    public void putOnTopOfDeck(Card card, TurnContext context) {
    	card.moveTo(deck, 0, context);
    }
    
    public void discard(Card card, TurnContext context) {
    	card.moveTo(discard, context);
    }
    
    public void gain(Card card, TurnContext context) {
    	discard(card, context);
    }
    
	public Card getHand(int i) {
		return hand.get(i);
	}

	public boolean hasReaction = false;
	
	public boolean attackable() {
		return attackable;
	}
	public void immune() {
		attackable = false;
	}
	public void resetImmunity() {
		attackable = true;
	}

	public void revealHand(TurnContext context) {
		for (Card card : hand) {
			revealCard(card, context);
		}
	}
	public void revealCard(Card card, TurnContext context) {
		GameEvent event = new GameEvent(GameEvent.Type.REVEALS, this, context);
		event.setCard(card);
		context.game.broadcastEvent(event);                            
	}

	private void reactFromHand(GameEvent event) {
		while(true) {
			boolean doReact = false;
			
			for (Card c: hand)
				doReact |= c.handleEvent(this, event);
			if (!doReact)
				break;
			SelectCardOptions sco = new SelectCardOptions()
				.fromHand()
				.isReaction()
				.isPassable()
				.to("react");
			Card r = pickACard(sco, hand.toArray());
			if (r == null)
				break;
			if (r.handleEvent(this, event))
				r.react(this, event.getContext());
		}
	}
	
	public String getStateStr() {
		String str = "Player " + getName() + " state: \n";
		str += "  Deck (" + getDeckSize() + "), Hand (" + getHandSize() + "), ";
		str += "Discard (" + getDiscardSize() + "); Total (" + getAllCards().size() + ")\n";

		return str;
	}
	
	public String getFullStateStr() {
		String str = getStateStr();
		for (Card c : getHand()) {
			str += "  " + c.getTitle();
		}

		return str;
	}
	@Override
	public void handleGameEvent(GameEvent event) {
		GameQuery p = event.toQuery();
		query(p);

		reactFromHand(event);
		for (Card c: played)
			c.handleEvent(this, event);
		for (Card c: nextTurnCards)
			c.handleEvent(this, event);
	}	
}