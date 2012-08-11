package com.mehtank.dominion.cards.base;

import com.mehtank.dominion.engine.Card;
import com.mehtank.dominion.engine.SelectCardOptions;
import com.mehtank.dominion.engine.TurnContext;

public class Cellar extends Card {
	@Override
	public void playAction(TurnContext context) {
		super.playAction(context);
		
		SelectCardOptions sco = new SelectCardOptions()
			.to(getText("q1"))
			.fromHand()
			.atMost(currentPlayer.getHandSize());
        Card[] cards = currentPlayer.pickCards(sco, currentPlayer.getHand());
        if (cards != null) {
            int numberOfCards = 0;
            for (Card card : cards) {
                for (int i = 0; i < currentPlayer.getHandSize(); i++) {
                    Card playersCard = currentPlayer.getHand(i);
                    if (playersCard.equals(card)) {
                        currentPlayer.discard(currentPlayer.getHand(i), context);
                        numberOfCards++;
                        break;
                    }
                }
            }

            while (numberOfCards > 0) {
                numberOfCards--;
                currentPlayer.drawToHand(context);
            }
        }
	}
}
