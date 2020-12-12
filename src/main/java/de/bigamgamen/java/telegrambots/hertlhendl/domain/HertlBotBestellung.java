package de.bigamgamen.java.telegrambots.hertlhendl.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HertlBotBestellung {
	
	private LocalDate bestellDatum;
	private HertlBotUser user;	
	private List<HertlBotPosition> positionen;
	
	public HertlBotBestellung() {
		
	}
	
	public HertlBotBestellung(HertlBotUser user, List<HertlBotPosition> positionen) {
		this.bestellDatum = LocalDate.now();
		this.user = user;
		this.positionen = positionen;
	}
	
	@Override
	public String toString() {		
		return this.bestellDatum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}
	
	public HertlBotUser getUser() {
		return user;
	}
	public void setUser(HertlBotUser user) {
		this.user = user;
	}
	public List<HertlBotPosition> getPositionen() {
		return positionen;
	}
	public void setPositionen(List<HertlBotPosition> positionen) {
		this.positionen = positionen;
	}

	public LocalDate getBestellDatum() {
		return bestellDatum;
	}

	public void setBestellDatum(LocalDate bestellDatum) {
		this.bestellDatum = bestellDatum;
	}
	
	
	
}
