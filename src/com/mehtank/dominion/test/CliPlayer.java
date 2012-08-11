package com.mehtank.dominion.test;

import java.io.IOException;

import com.mehtank.dominion.comms.GameQuery;
import com.mehtank.dominion.engine.Player;

public class CliPlayer extends Player {
	boolean printQueries = true;

	public CliPlayer(String string, boolean print) {
		setName(string);
		printQueries = print;
	}
	public CliPlayer(String string) {
		setName(string);
	}

	public void output(String s) {
		System.out.println(s);
	}
	
    protected static String readString() {
        StringBuilder sb = new StringBuilder();
        try {
            do {
                int input = System.in.read();
                if (input != -1 && input != 10 && input != 13) {
                    sb.append((char) input);
                }
            } while (System.in.available() != 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

	@Override
	public GameQuery query(GameQuery p) {
		if (printQueries) 
			output("~~~ " + getName() + " ~~~ " + p.t.toString() + "\n");
		
		if (p.r == null)
			return null;

		GameQuery resp = new GameQuery(p.r, null);
		if (p.r == GameQuery.QueryType.CARD) {
			output(getFullStateStr());
			output(p.toString());
			String s = readString();
			resp.setInteger(1);
			resp.setObject(new String[]{s});
		}
		return resp;
	}
}