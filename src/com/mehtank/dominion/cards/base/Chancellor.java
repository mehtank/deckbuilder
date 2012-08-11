package com.mehtank.dominion.cards.base;

import com.mehtank.dominion.engine.Card;
import com.mehtank.dominion.engine.TurnContext;

public class Chancellor extends Card {
	@Override
	public void playAction(TurnContext context) {
		super.playAction(context);

        boolean discard = currentPlayer.selectBoolean(getTitle(), getText("q1"), getText("q2"));
        if (discard) {
            while (currentPlayer.getDeckSize() > 0) {
                currentPlayer.discard(currentPlayer.drawFromDeck(context), context);
            }
        }

	}
}
