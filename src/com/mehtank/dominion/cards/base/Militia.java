package com.mehtank.dominion.cards.base;

import java.util.ArrayList;
import java.util.Arrays;

import com.mehtank.dominion.engine.Card;
import com.mehtank.dominion.engine.Player;
import com.mehtank.dominion.engine.SelectCardOptions;
import com.mehtank.dominion.engine.TurnContext;

public class Militia extends Card {
	@Override
	public void playAction(TurnContext context) {
		super.playAction(context);

		attack(context, ONLY_OTHERS);
	}
	
	@Override
	public void doAttack(Player player, TurnContext context) {
		if (player.getHandSize() > 3) {
			SelectCardOptions sco = new SelectCardOptions()
				.to(getText("q1"))
				.fromHand()
				.exactly(3);
			Card[] selectedCards = player.pickCards(sco, player.getHand());            		

			boolean bad = false;
			ArrayList<Card> handCopy = new ArrayList<Card>(Arrays.asList(player.getHand()));

			if (selectedCards == null || selectedCards.length != 3) {
				bad = true;
			} else {
				for (Card cardToKeep : selectedCards) {
					for (Card c : handCopy) {
						if (bad)
							break;
						bad = true;
						if (c.equals(cardToKeep)) {
							handCopy.remove(c);
							bad = false;
							continue;
						}
					}
				}
			}

			if (bad) {
				context.game.error(player, "Militia discard error, just keeping first 3.");
				handCopy.remove(0);
				handCopy.remove(0);
				handCopy.remove(0);
			}

			// Discard all of the cards 
			for (Card card : handCopy) {
				player.discard(card, context);
			}
		}
	}
}
