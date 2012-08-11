package com.mehtank.dominion.cards.base;

import com.mehtank.dominion.engine.Card;
import com.mehtank.dominion.engine.SelectCardOptions;
import com.mehtank.dominion.engine.TurnContext;

public class Workshop extends Card {
	@Override
	public void playAction(TurnContext context) {
		super.playAction(context);

		SelectCardOptions sco = new SelectCardOptions()
			.to(getText("q1"))
			.fromTable()
			.maxCost(4);
		Card card = currentPlayer.pickACard(sco, context.game.getSupplyArray());
		if (card != null) {
            // check cost
            if (card.getCost() <= 4) {
                card = context.game.takeFromPile(card);
                // could still be null here if the pile is empty.
                if (card != null) {
                    currentPlayer.gain(card, context);
                }
            }
        }

	}
}
