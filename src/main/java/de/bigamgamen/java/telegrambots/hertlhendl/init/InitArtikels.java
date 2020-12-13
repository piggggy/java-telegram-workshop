package de.bigamgamen.java.telegrambots.hertlhendl.init;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.bigamgamen.java.telegrambots.hertlhendl.dal.HertlBotRootDao;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotArtikel;

public class InitArtikels {
	
	private InitArtikels() {
		
	}
	
	public synchronized static void initArtikels(HertlBotRootDao dao)
	{
		List<HertlBotArtikel> artikelList= new ArrayList<>();
		
		artikelList.add(new HertlBotArtikel("1/2 Hähnchen", BigInteger.valueOf(380L)));
		artikelList.add(new HertlBotArtikel("Schenkel", BigInteger.valueOf(200L)));
		artikelList.add(new HertlBotArtikel("Krautsalat", BigInteger.valueOf(150L)));	
		artikelList.add(new HertlBotArtikel("Brezel klein", BigInteger.valueOf(80L)));
		artikelList.add(new HertlBotArtikel("Brezel groß", BigInteger.valueOf(160L)));
		
		dao.root().artikels().addAll(artikelList);
	
	}
	
}
