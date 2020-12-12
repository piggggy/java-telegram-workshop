package de.bigamgamen.java.telegrambots.hertlhendl.domain;

import java.math.BigInteger;

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
