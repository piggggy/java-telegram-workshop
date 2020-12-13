package de.bigamgamen.java.helper;

import java.math.BigInteger;

public class Pricehelper {
	private Pricehelper() {

	}

	public static String getPriceAsEuroString(BigInteger priceInCent) {
		return priceInCent.doubleValue() / 100 + "€";
	}
}
