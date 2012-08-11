package com.mehtank.dominion.engine;

import java.util.ArrayList;
import java.util.Random;

public class GameUtils {
	public static Random rand = new Random(System.currentTimeMillis());

	ArrayList<GameEventListener> listeners = new ArrayList<GameEventListener>();
    ArrayList<Player> players;
    GameTable table;
    
    GameUtils() {
    	table = new GameTable();
    }

    public void debug(String s) {
    	System.err.println(s);
    }
	public void error(Player p, String string) {
		debug("Player error (" + p.getName() + "): " + string);
	}
	
	public Card[] getSupplyArray() {
		return table.getSupplyArray();
	}
	public Card takeFromPile(Card c) {
		return table.takeFromPile(c);
	}
	public Card takeFromPile(String s) {
		return table.takeFromPile(s);
	}
	
	public void trash(Card card, Player player, TurnContext context) {
		card.moveTo(table.trash, context);
		
        GameEvent event = new GameEvent(GameEvent.Type.TRASHES, player, context);
        event.setCard(card);
        broadcastEvent(event);
	}
	
	public Player[] getPlayersInTurnOrder() {
		return players.toArray(new Player[0]);
	}

	public void broadcastEvent(GameEvent event) {
	    for (GameEventListener listener : listeners) {
	        listener.handleGameEvent(event);
	    }
	}

	protected void addGameListener(GameEventListener listener) {
	    if (listener != null && !listeners.contains(listener)) {
	        listeners.add(listener);
	    }
	}
}