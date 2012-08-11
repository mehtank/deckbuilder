package com.mehtank.dominion.cards.base;

import java.util.ArrayList;

import com.mehtank.dominion.engine.Card;
import com.mehtank.dominion.engine.CardPile;
import com.mehtank.dominion.engine.Player;
import com.mehtank.dominion.engine.TurnContext;

public class Thief extends Card {
	CardPile trashed = new CardPile("Trashed");

	@Override
	public void playAction(TurnContext context) {
		super.playAction(context);
		
		attack(context, ONLY_OTHERS);
		
        while (trashed.size() > 0) {
        	ArrayList<String> strs = new ArrayList<String>();
        	for (Card c : trashed) 
        		strs.add(c.getTitle());
        	strs.add(getText("q3"));
        	String choice = currentPlayer.selectString(getText("q2"), strs.toArray(new String[0]));
        	if (choice.equals(getText("q3")))
        		break;
        	
            for (Card c : trashed) {
            	if (choice.equals(c.getTitle())) {
            		currentPlayer.gain(c, context);
            		break;
                }
            }
        }
	}
	
	@Override
	public void doAttack(Player player, TurnContext context) {
		CardPile thief = new CardPile("Thief");

		Card card = player.drawFromDeck(context);
		if (card != null) {
			player.revealCard(card, context);
			if (!card.isTreasure())
				player.discard(card, context);
			else
				card.moveTo(thief, context);
		}
		
		card = player.drawFromDeck(context);
		if (card != null) {
			player.revealCard(card, context);
			if (!card.isTreasure())
				player.discard(card, context);
			else
				card.moveTo(thief, context);
		}

		Card cardToTrashOrKeep = null;
		if (thief.size() == 1)
			cardToTrashOrKeep = thief.get(0);
		if (thief.size() == 2) {
			boolean takeFirst = currentPlayer.selectBoolean(getText("q1"), thief.get(0).getTitle(), thief.get(1).getTitle());
			player.discard(takeFirst ? thief.get(1) : thief.get(0), context);
			cardToTrashOrKeep = thief.get(0);
		}

		if (cardToTrashOrKeep != null) {
			context.game.trash(cardToTrashOrKeep, player, context);
			trashed.add(cardToTrashOrKeep);
		}
	}
}
