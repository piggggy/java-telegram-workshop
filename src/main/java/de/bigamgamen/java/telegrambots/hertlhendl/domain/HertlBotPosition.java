package de.bigamgamen.java.telegrambots.hertlhendl.domain;

import java.math.BigInteger;

public class HertlBotPosition {
	private BigInteger menge;
	private HertlBotArtikel artikel; 
	
	public HertlBotPosition() {
		
	}
	
	public BigInteger getPositionPrice ()
	{
		return artikel.getPriceInCent().multiply(menge);
	}
	
	public BigInteger getMenge() {
		return menge;
	}

	public void setMenge(BigInteger menge) {
		this.menge = menge;
	}

	public HertlBotArtikel getArtikel() {
		return artikel;
	}

	public void setArtikel(HertlBotArtikel artikel) {
		this.artikel = artikel;
	}

	
	
}
