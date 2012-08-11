package com.mehtank.dominion.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Game extends GameUtils {
    int turnCount = 0;
    
    public Game(Player[] players, Card[] cards) {
    	setupPlayers(players);
    	setupCards(cards);
    	
    	debug(toString());
    	debug("*** SET UP! ***");
    }
    
    public void play() {
    	turnCount = 0;
    	startup();
    	debug(toString());

    	while(!gameOver()) {
    		playTurn();
    		turnCount++;
    	}
    }

	// Setup
    void setupPlayers(Player[] ps) {
    	players = new ArrayList<Player>(Arrays.asList(ps));
    	Collections.shuffle(players);
    	for (int i = 0; i < players.size(); i++) {
    		Player p = players.get(i);
    		p.setGame(this);
    		p.setIndex(i);
    		addGameListener(p);
    	}
    }
    void setupCards(Card[] cards) {
    	// TODO: how many of each treasure card?
    	Card[] common = new com.mehtank.dominion.cards.common.Cards().getCards();
    	table.makePiles(common, players.size());	
    	table.makePiles(cards, players.size());
    }
    void startup() {
    	for (Player p : players) {
    		for (int i = 0; i < 7; i++) p.gain(takeFromPile("Copper"), null);
    		for (int i = 0; i < 3; i++) p.gain(takeFromPile("Estate"), null);
        	for (int i = 0; i < 5; i++) p.drawToHand(null);
    	}
    }
    
    boolean gameOver() {
    	return table.gameOver();
    }
    
    void playTurn() {
    	int curPlayer = turnCount % players.size();
    	Player currentPlayer = players.get(curPlayer);
    	TurnContext context = new TurnContext();
    	context.game = this;
    	context.currentPlayer = currentPlayer;
    	
    	doActions(context);
    	doBuys(context);
    	doCleanup(context);
	}

	void doActions(TurnContext context) {
    	while(context.actions > 0) {
    		if (context.currentPlayer.getActionsInHand() < 1)
    			return;
    		
			SelectCardOptions sco = new SelectCardOptions()
				.isAction()
				.fromHand()
				.isPassable()
				.to("play");

			Card card = context.currentPlayer.pickACard(sco, context.currentPlayer.hand.toArray());
			if (card != null) {
		        GameEvent event = new GameEvent(GameEvent.Type.PlayingAction, context.currentPlayer, null);
		        event.setCard(card);
		        broadcastEvent(event);
		        
				card.playAction(context);
				context.actions--;
			} else
				return;
    	}
    }
	void doBuys(TurnContext context) {
		while (context.currentPlayer.getTreasuresInHand() > 0) {
			SelectCardOptions sco = new SelectCardOptions()
				.isTreasure()
				.fromHand()
				.isPassable()
				.to("play");
	
			Card card = context.currentPlayer.pickACard(sco, context.currentPlayer.hand.toArray());
			if (card != null)
				card.playTreasure(context);
			else
				break;
		}

    	while(context.buys > 0) {
			SelectCardOptions sco = new SelectCardOptions()
				.fromTable()
				.maxCost(context.coins)
				.isPassable()
				.to("buy");

			Card card = context.currentPlayer.pickACard(sco, getSupplyArray());
			if (card == null)
				return;
			
			// TODO allowed?
			// TODO price changers
			Card toBuy = takeFromPile(card);
			if (toBuy != null) {
				context.currentPlayer.gain(toBuy, context);
				context.buys--;
				context.coins -= card.getCost();
				
		        GameEvent event = new GameEvent(GameEvent.Type.BUYS, context.currentPlayer, context);
		        event.setCard(toBuy);
		        broadcastEvent(event);
			} else
				return;
    	}
	}
	
    void doCleanup(TurnContext context) {
    	Player currentPlayer = context.currentPlayer;
    	ArrayList<Card> toDiscard = new ArrayList<Card>();
    	
    	for (Card c : currentPlayer.hand)
    		toDiscard.add(c);
    	for (Card c : currentPlayer.played)
    		toDiscard.add(c);    
    	for (Card c : toDiscard)
    		currentPlayer.discard(c, context);
    	for (int i = 0; i < 5; i++)
    		currentPlayer.drawToHand(context);
	}
    
	public String toString() {
		String str = "Game State:  Turn " + turnCount + "\n";
		for (Player p : getPlayersInTurnOrder())
			str += p.getStateStr() + "\n";
		
		str += table.getState();
				
		return str;
	}
}