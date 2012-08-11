package com.mehtank.dominion.engine;

public class TurnContext {
	enum TurnPhase { DURATION, ACTION, BUY, CLEANUP };
	TurnPhase phase = TurnPhase.DURATION;
	
	public int actions = 1;
    public int buys = 1;
    public int coins = 0;

    int actionsPlayedSoFar = 0;

    public Player currentPlayer;
    public Game game;
    
    public String toString() {
    	return "" + actions + " / " + buys + " / (" + coins + ")\n";
    }
}
