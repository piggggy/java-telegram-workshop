package de.bigamgamen.java.telegrambots.hertlhendl.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HertlBotUser {

	private Long chatId;
	private List<HertlBotBestellung> bestellungen = new ArrayList<>();

	public HertlBotUser() {
		
	}
	
	public synchronized void addBestellung(HertlBotBestellung bestellung)
	{
		bestellung.setUser(this);
		this.bestellungen.add(bestellung);
		bestellung.setIndex(this.bestellungen.indexOf(bestellung));
	}
	
	public boolean isRightUser(Long chatId)
	{
		return this.chatId.equals(chatId);
	}
	
	public HertlBotUser(Long chatId) {
		this.chatId = chatId;
	}

	public List<HertlBotBestellung> getBestellungen() {
		return new ArrayList<>(bestellungen);
	}
	
	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

	public Long getChatId() {
		return chatId;
	}

}
