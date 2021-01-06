package de.bigamgamen.java.telegrambots.hertlhendl.domain;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import de.bigamgamen.java.helper.Pricehelper;
import one.microstream.persistence.types.Persister;

public class HertlBotBestellung {
	
	private static final String BESTELLUNG_TITLE = "Ihre Bestellung:";
	private static final String DD_MM_YYYY = "dd-MM-yyyy";
	private int index;
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
	
	public synchronized void addPosition(HertlBotPosition position, final Persister persister)
	{
		positionen.add(position);		
		persister.store(positionen);
	}
	

	public String getBestellDatumFormated() {		
		return this.bestellDatum.format(DateTimeFormatter.ofPattern(DD_MM_YYYY));
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(BESTELLUNG_TITLE + " " +this.index);
		sb.append("Vom: "+getBestellDatumFormated()+System.lineSeparator());
		this.positionen.forEach(pos -> sb.append(pos.toString()+System.lineSeparator()));
		sb.append("Summe: "+ getSumme());
		return sb.toString();
	}
	
	private String getSumme() {
		BigInteger summe = new BigInteger("0");
		this.positionen.forEach(pos -> summe.add(pos.getPositionPrice()));
		return Pricehelper.getPriceAsEuroString(summe);
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	
	
}
