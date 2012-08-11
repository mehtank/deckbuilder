package com.mehtank.dominion.cards.base;

import com.mehtank.dominion.engine.Card;
import com.mehtank.dominion.engine.Player;
import com.mehtank.dominion.engine.SelectCardOptions;
import com.mehtank.dominion.engine.TurnContext;

public class Bureaucrat extends Card {

	@Override
	public void playAction(TurnContext context) {
		super.playAction(context);
		
        Card silver = context.game.takeFromPile("silver");
        if (silver != null)
        	currentPlayer.putOnTopOfDeck(silver, context);
        
        attack(context, ONLY_OTHERS);
	}
	
	@Override
	public void doAttack(Player player, TurnContext context) {
		Card hasVictory = null;
		for (int i = 0; i < player.getHandSize(); i++)
			if (player.getHand(i).isVictory())
				hasVictory = player.getHand(i);
		if (hasVictory != null) {
			SelectCardOptions sco = new SelectCardOptions()
				.isVictory()
				.fromHand()
				.to(getText("q1"));

			Card card = player.pickACard(sco, player.getHand());
			// XXX check if card is valid
			player.putOnTopOfDeck(card, context);
		} else {
			player.revealHand(context);
		}
	}
}
