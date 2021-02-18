package de.bigamgamen.java.telegrambots.hertlhendl.domain;

import java.math.BigInteger;

import de.bigamgamen.java.helper.Pricehelper;

public class HertlBotArtikel {
	private int id;
	private String name;
	private BigInteger priceInCent;

	public HertlBotArtikel() {

	}

	public HertlBotArtikel(int id, String name, BigInteger priceInCent) {
		super();
		this.id = id;
		this.name = name;
		this.priceInCent = priceInCent;
	}
	
	@Override
	public String toString() {		
		return this.name + ": " + Pricehelper.getPriceAsEuroString(priceInCent);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigInteger getPriceInCent() {
		return priceInCent;
	}

	public void setPriceInCent(BigInteger priceInCent) {
		this.priceInCent = priceInCent;
	}

	public int getId() {
		return id;
	}

}
