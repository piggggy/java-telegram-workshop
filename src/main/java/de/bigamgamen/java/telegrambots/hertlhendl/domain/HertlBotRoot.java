package de.bigamgamen.java.telegrambots.hertlhendl.domain;

import java.util.HashSet;
import java.util.Set;

public class HertlBotRoot {
	private HertlBotUsers users = new HertlBotUsers();
	private HertlBotArtikels artikels = new HertlBotArtikels();
	
	public HertlBotRoot()
	{
		
	}

	public HertlBotUsers users() {
		return users;
	}

	public HertlBotArtikels artikels() {
		return artikels;
	}

}
