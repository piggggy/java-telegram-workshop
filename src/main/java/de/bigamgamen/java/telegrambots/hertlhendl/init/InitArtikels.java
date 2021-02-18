package de.bigamgamen.java.telegrambots.hertlhendl.init;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.bigamgamen.java.telegrambots.hertlhendl.dal.HertlBotRootDao;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotArtikel;

public class InitArtikels {
	
	private InitArtikels() {
		
	}
	
	public synchronized static void initArtikels(final HertlBotRootDao dao)
	{
		final List<HertlBotArtikel> artikelList= new ArrayList<>();
		
		artikelList.add(new HertlBotArtikel(1,"1/2-Hähnchen", BigInteger.valueOf(380L)));
		artikelList.add(new HertlBotArtikel(2,"Hähnchen-Schenkel", BigInteger.valueOf(200L)));
		artikelList.add(new HertlBotArtikel(3,"Krautsalat", BigInteger.valueOf(150L)));	
		artikelList.add(new HertlBotArtikel(4,"Brezel-klein", BigInteger.valueOf(80L)));
		artikelList.add(new HertlBotArtikel(5,"Brezel-groß", BigInteger.valueOf(160L)));
		
		dao.root().artikels().addAll(artikelList);
	
	}
	
}
