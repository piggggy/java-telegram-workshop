package de.bigamgamen.java.telegrambots.hertlhendl.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HertlBotUser {

	private Long chatId;
	private List<HertlBotBestellung> bestellungen = new ArrayList<>();

	public HertlBotUser() {
		
	}
	
	public boolean isRightUser(Long chatId)
	{
		return this.chatId.equals(chatId);
	}
	
	public HertlBotUser(Long chatId) {
		this.chatId = chatId;
	}

	public List<HertlBotBestellung> getBestellungen() {
		return bestellungen;
	}

	public void setBestellungen(List<HertlBotBestellung> bestellungen) {
		this.bestellungen = bestellungen;
	}
	
	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

	public Long getChatId() {
		return chatId;
	}

}
