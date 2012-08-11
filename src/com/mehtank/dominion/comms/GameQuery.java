package com.mehtank.dominion.comms;

import java.io.Serializable;

public class GameQuery implements Serializable{
	private static final long serialVersionUID = -590316466543954582L;
	
	public enum QueryType {
		HELLO, GAMESTATS, NEWGAME,
		
		STARTGAME, JOINGAME,
		
		STATUS,
		
		GETNAME, SETNAME, SETHOST,

		GETCARD, CARD,
		GETSTRING, STRING, 
		ORDERCARDS, CARDORDER,
		
		GAMEEVENT,
		SAY, CHAT, 
		
		Success,
		QUIT,
		
		DEBUG, 
		PING, PONG, DISCONNECT, 
	}
	public QueryType t; // type of query
	public QueryType r; // type of response requested; null if no response necessary

	public String s;
	public boolean b;
	public int i;
	public Serializable o;
	
	public GameQuery(QueryType t, QueryType r) {
		this.t = t;
		this.r = r;
	}
	
	public GameQuery setType(QueryType r) {
		this.t = r;
		return this;
	}
	public GameQuery setString(String s) {
		this.s = s;
		return this;
	}
	public GameQuery setBoolean(boolean b) {
		this.b = b;
		return this;
	}
	public GameQuery setInteger(int i) {
		this.i = i;
		return this;
	}
	public GameQuery setObject(Serializable o) {
		this.o = o;
		return this;
	}
	
	public String toString() {
		String str = "GameQuery type " + t.toString();
		
		str += "\n  int = " + i;
		str += "\n  str =  " + s;
		str += "\n  bool = " + b;
		if (r != null)
			str += "\nRequesting response of type " + r.toString();
		if (o != null) {
			str += "\n\nObject:\n";
			str += o.toString();
		}
		
		return str;
	}
}