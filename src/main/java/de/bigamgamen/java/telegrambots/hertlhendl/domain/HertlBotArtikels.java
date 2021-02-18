package de.bigamgamen.java.telegrambots.hertlhendl.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.bigamgamen.java.telegrambots.hertlhendl.dal.HertlBotRootDao;
import one.microstream.persistence.types.Persister;

public class HertlBotArtikels {

	private final Map<String, HertlBotArtikel> artikels = new HashMap<>();

	public HertlBotArtikels() {
		super();
	}
	
	
	
	public void add(
		final HertlBotArtikel artikel,
		final Persister persister
	)
	{
		this.artikels.put(artikel.getName(), artikel);
		persister.store(this.artikels);
	}

	public void addAll(final Collection<? extends HertlBotArtikel> HertlBotArtikels)
	{
		this.addAll(HertlBotArtikels, HertlBotRootDao.storageManager());
	}
	
	public void addAll(
		final Collection<? extends HertlBotArtikel> artikels,
		final Persister persister
	)
	{
		this.artikels.putAll(
				artikels.stream().collect(
				Collectors.toMap(HertlBotArtikel::getName, Function.identity())
			)
		);
		persister.store(this.artikels);
	}
	
	public int HertlBotArtikelCount()
	{
		return this.artikels.size();
	}

	public List<HertlBotArtikel> all()
	{
		return new ArrayList<>(this.artikels.values());
	}

	public HertlBotArtikel ofName(final String artikelName)
	{
		return this.artikels.get(artikelName);
	}
	
	

}
