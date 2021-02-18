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
	
	public HertlBotBestellung(final HertlBotUser user, final List<HertlBotPosition> positionen) {
		this.bestellDatum = LocalDate.now();
		this.user = user;
		this.positionen = positionen;
	}
	
	public synchronized void addPosition(HertlBotPosition position, final Persister persister)
	{
		positionen.add(position);	
		persister.store(position);
		persister.store(positionen);
	}
	

	public String getBestellDatumFormated() {
		return this.bestellDatum.format(DateTimeFormatter.ofPattern(DD_MM_YYYY));
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(BESTELLUNG_TITLE + " " +this.index);
		sb.append(" Vom: "+this.getBestellDatumFormated()+System.lineSeparator());
		this.positionen.forEach(pos -> sb.append(pos.toString()+System.lineSeparator()));
		sb.append("Summe: "+ this.getSumme());
		return sb.toString();
	}
	
	private String getSumme() {
		 BigInteger summe = new BigInteger("0");
		for(HertlBotPosition pos : positionen)
		{
			summe = summe.add(pos.getPositionPrice());
		}
		return Pricehelper.getPriceAsEuroString(summe);
	}
	
	private void sumToBigInteger(BigInteger addingTo, final BigInteger addition)
	{
		addingTo = addingTo.add(addition);
	}

	public HertlBotUser getUser() {
		return this.user;
	}
	public void setUser(final HertlBotUser user) {
		this.user = user;
	}
	public List<HertlBotPosition> getPositionen() {
		return this.positionen;
	}
	public void setPositionen(final List<HertlBotPosition> positionen) {
		this.positionen = positionen;
	}

	public LocalDate getBestellDatum() {
		return this.bestellDatum;
	}

	public void setBestellDatum(final LocalDate bestellDatum) {
		this.bestellDatum = bestellDatum;
	}

	public int getIndex() {
		return this.index;
	}

	public void setIndex(final int index) {
		this.index = index;
	}
	
	
	
}
