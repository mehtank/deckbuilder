package com.mehtank.dominion.cards.base;

import com.mehtank.dominion.engine.Card;
import com.mehtank.dominion.engine.TurnContext;

public class Moneylender extends Card {
	@Override
	public void playAction(TurnContext context) {
		super.playAction(context);

        for (int i = 0; i < currentPlayer.getHandSize(); i++) {
            Card card = currentPlayer.getHand(i);
            if (card.equals("copper")) {
            	context.game.trash(card, currentPlayer, context);
            	context.coins += 3;
                return;
            }
        }
	}
}