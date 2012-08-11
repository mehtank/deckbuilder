package com.mehtank.dominion.cards.base;

import com.mehtank.dominion.engine.Card;
import com.mehtank.dominion.engine.Player;
import com.mehtank.dominion.engine.TurnContext;

public class Spy extends Card {
	@Override
	public void playAction(TurnContext context) {
		super.playAction(context);
		attack(context, ME_TOO);
	}
	
	@Override
	public void doAttack(Player player, TurnContext context) {
		Card card = player.drawFromDeck(context);

		if (card != null) {
			player.revealCard(card, context);

			boolean discard = currentPlayer.selectBoolean(player.getName() + ": " + card.getTitle() + " " + getText("q1"), getText("q2"), getText("q3"));

			if (discard)
				player.discard(card, context);
			// else it's already on the top of the deck.
		}
	}
}
