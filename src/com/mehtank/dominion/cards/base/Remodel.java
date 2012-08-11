package com.mehtank.dominion.cards.base;

import com.mehtank.dominion.engine.Card;
import com.mehtank.dominion.engine.SelectCardOptions;
import com.mehtank.dominion.engine.TurnContext;

public class Remodel extends Card {
	@Override
	public void playAction(TurnContext context) {
		super.playAction(context);

		SelectCardOptions sco = new SelectCardOptions()
			.fromHand()
			.to(getText("q1"));
		Card cardToTrash = currentPlayer.pickACard(sco, currentPlayer.getHand());

        if (cardToTrash == null) {
            return;
        }

        int cost = -1;
        for (int i = 0; i < currentPlayer.getHandSize(); i++) {
            Card playersCard = currentPlayer.getHand(i);
            if (playersCard.equals(cardToTrash)) {
                cost = playersCard.getCost();
        		context.game.trash(playersCard, currentPlayer, context);
                break;
            }
        }

        if (cost == -1) {
            context.game.error(currentPlayer, "Remodel returned invalid card, ignoring.");
            return;
        }

        cost += 2;
        boolean foundCard = false;

		sco = new SelectCardOptions()
			.fromTable()
			.to(getText("q2"))
			.maxCost(cost);
		Card card = currentPlayer.pickACard(sco, context.game.getSupplyArray());

        if (card != null) {
            // check cost
            if (card.getCost() <= cost) {
                card = context.game.takeFromPile(card);
                // could still be null here if the pile is empty.
                if (card != null) {
                    currentPlayer.gain(card, context);
                    foundCard = true;
                }
            }
        }

        if (!foundCard) {
            // This could happen if the player returns null for the new card, if the new card costs
            // too
            // much or if the pile is empty (or if the pile is not even in the game).
        	context.game.error(currentPlayer, "Remodel new card is invalid, ignoring.");
            return;
        }


	}
}