package de.bigamgamen.java.telegrambots.hertlhendl.domain;

import java.math.BigInteger;

import de.bigamgamen.java.helper.Pricehelper;

public class HertlBotArtikel {
	private String name;
	private BigInteger priceInCent;

	public HertlBotArtikel() {

	}

	public HertlBotArtikel(String name, BigInteger priceInCent) {
		super();
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

}
