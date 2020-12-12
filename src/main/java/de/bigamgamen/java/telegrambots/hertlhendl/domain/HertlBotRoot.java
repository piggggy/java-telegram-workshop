package de.bigamgamen.java.telegrambots.hertlhendl.domain;

import java.util.HashSet;
import java.util.Set;

public class HertlBotRoot {
	private Set<HertlBotUser> users = new HashSet<>();
	
	public HertlBotRoot()
	{
		
	}

	public Set<HertlBotUser> getUsers() {
		return users;
	}

	public void setUsers(Set<HertlBotUser> users) {
		this.users = users;
	}

}
