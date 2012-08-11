package com.mehtank.dominion.comms;

import java.io.Serializable;

import com.mehtank.dominion.engine.GameEvent;

public class SerializableGameEvent implements Serializable {
	private static final long serialVersionUID = 4321881301938446390L;

	String eventType;
	int playerNum;
	String card = "";
	String str;
	
	public SerializableGameEvent(GameEvent event) {
		eventType = event.getType().toString();
		playerNum = event.getPlayer().getIndex();
		if (event.getCard() != null)
			card = event.getCard().getName();
		str = event.getStr();
	}
	
	public String toString() {
		String s = "Game event: " + eventType;
		s += " from player " + playerNum;
		if (card != "")
			s += " (card: " + card + ")";
		s += "\n" + str;
		return s;
	}

}
