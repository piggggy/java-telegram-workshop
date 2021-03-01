package de.bigamgamen.java.telegrambots.hertlhendl.controller;

import org.telegram.telegrambots.meta.api.objects.User;

import de.bigamgamen.java.telegrambots.hertlhendl.api.RightController;

public class HertlRightController implements RightController{

	private final Integer id; 

    
     public HertlRightController(Integer idAdmin) {
		this.id = idAdmin;
	}
	
	@Override
	public boolean isAdmin(User user) {
		return user.getId().equals(id);
	}

}
