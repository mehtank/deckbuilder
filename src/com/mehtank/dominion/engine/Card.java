package com.mehtank.dominion.engine;

import java.lang.reflect.InvocationTargetException;

import org.w3c.dom.Element;

import com.mehtank.dominion.utils.StringUtils;
import com.mehtank.dominion.utils.XMLUtils;

public class Card implements Comparable<Card>{
	Element xmlDefinition;
	String name;
	
	public String getName() { return name; }
	
	public String getStr(String s) { return xmlDefinition.getAttribute(s); };
	public String getText(String s) { return XMLUtils.getElementValueByTagName(xmlDefinition, s);}
	public boolean hasElement(String name, String value) { return XMLUtils.getElementValuesByTagName(xmlDefinition, name).contains(value); }
	public int getInt(String s) { 
		Integer i = XMLUtils.getIntFromXML(xmlDefinition, s); 
		if (i == null)
			return 0;
		return i;
	}
	
	public String getTitle() { return getText("name");}
	public String getDescription() { return getText("desc");}

	public boolean isAction() { return hasElement("type", "action"); }
	public boolean isVictory() { return hasElement("type", "victory"); }
	public boolean isTreasure() { return hasElement("type", "treasure"); }
	public boolean isCurse() { return hasElement("type", "curse"); }
	public boolean isReaction() { return hasElement("type", "reaction"); }
	public boolean isAttack() { return hasElement("type", "attack"); }
	public boolean isDuration() { return hasElement("type", "duration"); }

	public int getCost() { return getInt("cost"); }
	public int getPotionCost() { return getInt("potioncost"); }
	public int getCount() { return getInt("count"); }

	public int getVP() { return getInt("vp"); } 
	public int getCoin() { return getInt("coin"); } 
	public int getPotion() { return getInt("potion"); } 
	
	public int getAddCards() { return getInt("addCards"); } 
	public int getAddActions() { return getInt("addActions"); } 
	public int getAddBuys() { return getInt("addBuys"); } 
	public int getAddCoin() { return getInt("addCoin"); } 
	public int getAddVP() { return getInt("addVP"); } 
	
	public int getAddCardsNext() { return getInt("addCardsNext"); } 
	public int getAddActionsNext() { return getInt("addActionsNext"); } 
	public int getAddBuysNext() { return getInt("addBuysNext"); } 
	public int getAddCoinNext() { return getInt("addCoinNext"); } 
	public int getAddVPNext() { return getInt("addVPNext"); } 
	
	public Card () {}
	public Card (Element e) { setXML(e); }
	
	public void setXML(Element e) {
		xmlDefinition = e;
		name = e.getAttribute("name");		
	}
	public boolean equals(Card c) {
		return (name.equals(c.getName()));
	}
	public boolean equals(String s) {
		String cName = StringUtils.getCanonicalName(name);
		return (cName.equals(StringUtils.getCanonicalName(s)));
	}
	
	@Override
	public int compareTo(Card c) {
		if (getCost() != c.getCost())
			return getCost() - c.getCost();
		if (getPotionCost() != c.getPotionCost())
			return getPotionCost() - c.getPotionCost();
		return name.compareTo(c.getName());
	}
	
	public Card copy() {
		Card c = null;
		try {
			c = this.getClass().getDeclaredConstructor().newInstance();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.setXML(xmlDefinition);
		return c;
	}

	public void addAttribute(String name, String value) {
		xmlDefinition.setAttribute(name, value);
	}

	private CardPile parent = null;

	protected Player currentPlayer;
		
	public void moveTo(CardPile cardlist, TurnContext context) {
		if (parent != null)
			parent.remove(this);
		parent = cardlist;
		parent.add(this);
	}
	public void moveTo(CardPile cardlist, int index, TurnContext context) {
		if (parent != null)
			parent.remove(this);
		parent = cardlist;
		parent.add(index, this);
	}
		
	//
	// Card behaviors
	// 
	
	public void setupGame(Game game, Player[] players) {
		return;
	}
	public void playAction(TurnContext context) {
		if (!isAction())
			return;
		
		currentPlayer = context.currentPlayer;

        if (isDuration())
        	moveTo(context.currentPlayer.nextTurnCards, context);
        else
        	moveTo(context.currentPlayer.played, context);
        
		GameEvent event = new GameEvent(GameEvent.Type.PlayingAction, context.currentPlayer, context);
        event.setCard(this);
        event.setString(StringUtils.plays(this));
        context.game.broadcastEvent(event);
        
        if (isAttack()) {
            for (Player player : context.game.getPlayersInTurnOrder())
            	player.resetImmunity();
        	event = new GameEvent(GameEvent.Type.PlayerAttacking, context.currentPlayer, context);
        	event.setCard(this);
        	context.game.broadcastEvent(event);
        }
        
        context.actionsPlayedSoFar++;

        context.actions += getAddActions();
        context.buys += getAddBuys();
        context.coins += getAddCoin();
        context.currentPlayer.vps += getAddVP();
        
        int cardsToDraw = getAddCards();

        while (cardsToDraw > 0) {
            cardsToDraw--;
            context.currentPlayer.drawToHand(context);
        }
	}
	public void playActionNext(TurnContext context) {
		if (!isDuration())
			return;
		
		currentPlayer = context.currentPlayer;

    	moveTo(context.currentPlayer.played, context);

        GameEvent event = new GameEvent(GameEvent.Type.PlayingDurationAction, context.currentPlayer, context);
        event.setCard(this);
        event.setString(StringUtils.played(this));
        context.game.broadcastEvent(event);
        
        context.actions += getAddActionsNext();
        context.buys += getAddBuysNext();
        context.coins += getAddCoinNext();
        context.currentPlayer.vps += getAddVPNext();
                
        int cardsToDraw = getAddCardsNext();

        while (cardsToDraw > 0) {
            cardsToDraw--;
            context.currentPlayer.drawToHand(context);
        }
	}
	public void playTreasure(TurnContext context) {
		if (!isTreasure())
			return;
		
		currentPlayer = context.currentPlayer;
		
    	moveTo(context.currentPlayer.played, context);

        GameEvent event = new GameEvent(GameEvent.Type.PlayingTreasure, context.currentPlayer, context);
        event.setCard(this);
        event.setString(StringUtils.plays(this));
        context.game.broadcastEvent(event);

        context.buys += getAddBuys();
        context.coins += getCoin();
        context.coins += getAddCoin();
        context.currentPlayer.vps += getAddVP();
	}
	
	public boolean handleEvent(Player p, GameEvent e) { // True if can react
		return false;
	}
	public void react(Player p, TurnContext context) {
		GameEvent e = new GameEvent(GameEvent.Type.PlayerReacting, p, context);
		e.setCard(this);
		context.game.broadcastEvent(e);
		return;
	}
	
	public static final boolean ONLY_OTHERS = false;
	public static final boolean ME_TOO = true;
	
	public void attack(TurnContext context, boolean attackSelf) {
        for (Player player : context.game.getPlayersInTurnOrder())
            if (((player == currentPlayer) && attackSelf) ||
            	((player != currentPlayer) && player.attackable()))
            	doAttack(player, context);
	}
	public void doAttack(Player player, TurnContext context) {
		return;
	}
	public int getVPs(Player p) {
		return getVP();
	}
}
