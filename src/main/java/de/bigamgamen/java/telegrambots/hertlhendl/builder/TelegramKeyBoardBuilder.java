package de.bigamgamen.java.telegrambots.hertlhendl.builder;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import de.bigamgamen.java.telegrambots.hertlhendl.HertlHendlBot;
import de.bigamgamen.java.telegrambots.hertlhendl.dal.HertlBotRootDao;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotArtikel;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotBestellung;

public class TelegramKeyBoardBuilder {
	
	private static final String KEY_PRE_SYMBOL = "/";
	
	private final HertlBotRootDao hertlBotDao;
	
	public TelegramKeyBoardBuilder(HertlBotRootDao hertlBotDao) {
		this.hertlBotDao = hertlBotDao;
	}
	
	public List<KeyboardRow> loadAndShowMyBestellungenAsKeyBoard(final Long chatId)
	{
		final List<KeyboardRow> keyboard = new ArrayList<>();
		final KeyboardRow row = new KeyboardRow();
		
		hertlBotDao.loadUser(chatId).getBestellungen().forEach(
			bestellung -> row.add(this.createBestellungLink(bestellung)));
		
		keyboard.add(row);
		
		return keyboard;
	}
	
	public String createAndShowNewBestellung(final Long chatId)
	{
		
		final HertlBotBestellung bestellung = hertlBotDao.createNewBestellungForUser(chatId);
		
		return this.createBestellungLink(bestellung);
	}
	
	public String createBestellungLink(final HertlBotBestellung bestellung)
	{
		return this.createKeyForAbility(HertlHendlBot.ABILTY_NAME_BESTELLUNG)
			+ " "
			+ Integer.toString(bestellung.getIndex())
			+ System.lineSeparator();
	}
	
	public String createAddPositiontoBestellungLink(final HertlBotArtikel artikel, final Integer bestellungId)
	{
		return this.createKeyForAbility(HertlHendlBot.ABILTY_NAME_ADD_POSITION)
			+ " "
			+ artikel.getName()
			+ " "
			+ bestellungId
			+ System.lineSeparator();
	}
	
	public String createAbilityListForHelp(List<String> abilities)
	{
		final StringBuilder sb = new StringBuilder();
		abilities.forEach(ability -> sb.append(this.createKeyForAbility(ability) + System.lineSeparator()));
		return sb.toString();
	}
	
	public String createKeyForAbility(final String ability)
	{
		return KEY_PRE_SYMBOL + ability;
	}
}
