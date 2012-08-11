package com.mehtank.dominion.cards.base;

import com.mehtank.dominion.engine.Card;
import com.mehtank.dominion.engine.GameEvent;
import com.mehtank.dominion.engine.Player;
import com.mehtank.dominion.engine.TurnContext;

public class Moat extends Card {
	
	@Override
	public boolean handleEvent(Player p, GameEvent e) { // for reactions
		if (e.getType() == GameEvent.Type.PlayerAttacking)
			return (p != e.getPlayer());
		return false;
	}
	@Override
	public void react(Player p, TurnContext context) {
		super.react(p, context);
		p.immune();
	}
}
