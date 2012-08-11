package com.mehtank.dominion.cards.base;

import com.mehtank.dominion.engine.Card;
import com.mehtank.dominion.engine.SelectCardOptions;
import com.mehtank.dominion.engine.TurnContext;

public class Chapel extends Card {
	
	@Override
	public void playAction(TurnContext context) {
		super.playAction(context);
		
		SelectCardOptions sco = new SelectCardOptions()
			.to(getText("q1"))
			.fromHand()
			.atMost(Math.min(4, currentPlayer.getHandSize()));
		Card[] cards = currentPlayer.pickCards(sco, currentPlayer.getHand());
        
		if (cards != null) {
            if (cards.length > 4) {
                context.game.error(currentPlayer, "Chapel trash error, trying to trash too many cards, ignoring.");
            } else {
                for (Card card : cards) {
                    for (int i = 0; i < currentPlayer.getHandSize(); i++) {
                        Card playersCard = currentPlayer.getHand(i);
                        if (playersCard.equals(card)) {
                            Card thisCard = currentPlayer.getHand(i);
                    		context.game.trash(thisCard, currentPlayer, context);
                            break;
                        }
                    }
                }
            }
        }
	}
}
