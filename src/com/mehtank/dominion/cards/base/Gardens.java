package com.mehtank.dominion.cards.base;

import com.mehtank.dominion.engine.Card;
import com.mehtank.dominion.engine.Player;

public class Gardens extends Card {
	@Override
	public int getVPs(Player p) {
		int vps = super.getVPs(p);
		vps += (p.getNumCards() % 10);
		return vps;
	}
}
