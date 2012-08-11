package com.mehtank.dominion.engine;

import com.mehtank.dominion.comms.GameQuery;
import com.mehtank.dominion.comms.SerializableGameEvent;

public class GameEvent {
    public enum Type {
        GameStarting, // A new game is starting, called at the start of each game when multiple are played

        TurnBegin, // Player begins a turn

        PlayingAction, // Action card is about to be played by a player.
        PlayingDurationAction, // Duration action card's next turn effects are about to occur for a player.
        PlayerAttacking, // Player is attacking another player
        PlayerReacting, // Player defended an attack
        CardEvent, // Card specific event

        PlayingTreasure, // Action card is about to be played by a player.
        BUYS, // Buying a card in the buy phase.

        RESHUFFLES, // Discard pile shuffled to create a new deck for one of the players

        OBTAINS, // Card was obtained by a player through an effect of an action
        TRASHES, // Card removed from the game
        REVEALS, // Card revealed by an action

        TurnEnd, // Player's turn ends
        
        GameOver, // Game completed
    }

    private Type type;  // type of event
    private Player who; // Player who generated the event
    private Card card;  // optional, depending on event type
    private String str; // optional, depending on event type

    private TurnContext context;

	public GameEvent(Type type, Player who, TurnContext context) {
        this.type = type;
        this.who = who;
        this.context = context;
    }
	public void setString(String str) {
		this.str = str;
	}
	public void setCard(Card card) {
		this.card = card;
	}

    public Type getType() {
        return type;
    }
    public Player getPlayer() {
        return who;
    }
	public TurnContext getContext() {
		return context;
	}
    public Card getCard() {
        return card;
    }
    public String getStr() {
    	return str;
    }
    public GameQuery toQuery() {
    	GameQuery p = new GameQuery(GameQuery.QueryType.GAMEEVENT, null);
    	p.setObject(new SerializableGameEvent(this));
    	return p;
    }
}
