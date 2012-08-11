package com.mehtank.dominion.test;

import com.mehtank.dominion.cards.base.Cards;
import com.mehtank.dominion.engine.Card;
import com.mehtank.dominion.engine.Game;
import com.mehtank.dominion.engine.Player;

public class GameTest {
	
	public static void main(String[] args) {
		CliPlayer p1 = new CliPlayer("Player A");
		CliPlayer p2 = new CliPlayer("Player B", false);
		Player[] players = {p1, p2};
		
		Card[] cards = new Card[10];
        System.arraycopy(new Cards().getCards(), 0, cards, 0, 10);
        new GameTest().go(players, cards);
	}
	
	public void go(Player[] players, Card[] cards) {
		Game g = new Game(players, cards);
		g.play();
	}
}
