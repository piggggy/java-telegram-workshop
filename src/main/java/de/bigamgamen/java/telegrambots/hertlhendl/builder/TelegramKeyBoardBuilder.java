package de.bigamgamen.java.telegrambots.hertlhendl.builder;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import de.bigamgamen.java.telegrambots.hertlhendl.HertlHendlBot;
import de.bigamgamen.java.telegrambots.hertlhendl.dal.HertlBotRootDao;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotArtikel;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotBestellung;

public class TelegramKeyBoardBuilder {

	private static final int MAX_BUTTON_PER_ROW = 1;

	private static final String KEY_PRE_SYMBOL = "/";

	private final HertlBotRootDao hertlBotDao;

	public TelegramKeyBoardBuilder(HertlBotRootDao hertlBotDao) {
		this.hertlBotDao = hertlBotDao;
	}

	public List<KeyboardRow> loadAndShowMyBestellungenAsKeyBoard(final Long chatId) {
		final List<KeyboardRow> keyboard = new ArrayList<>();

		hertlBotDao.loadUser(chatId).getBestellungen()
				.forEach(bestellung -> addButtonToKeyBoard(keyboard, this.createBestellungLink(bestellung)));

		return keyboard;
	}
	
	public List<KeyboardRow> loadAndShowAllArtikelForBestellung(final Long chatId, final Integer bestellungId)
	{
		final List<KeyboardRow> keyboard = new ArrayList<>();
	
		
				hertlBotDao.root().artikels().all().forEach(artikel  -> addButtonToKeyBoard(keyboard,createAddPositiontoBestellungLink(artikel, bestellungId)) );
		
		
		
		return keyboard;
	}

	private void addButtonToKeyBoard(List<KeyboardRow> keyboard, String buttonString) {
		KeyboardRow rowToUse = null;

		for (KeyboardRow row : keyboard) {
			if (row.size() < MAX_BUTTON_PER_ROW) {
				rowToUse = row;
			}
		}

		if (rowToUse == null) {
			rowToUse = new KeyboardRow();
			keyboard.add(rowToUse);
		}

		rowToUse.add(buttonString);

	}

	public String createAndShowNewBestellung(final Long chatId) {

		final HertlBotBestellung bestellung = hertlBotDao.createNewBestellungForUser(chatId);

		return this.createBestellungLink(bestellung);
	}

	public String createBestellungLink(final HertlBotBestellung bestellung) {
		return this.createKeyForAbility(HertlHendlBot.ABILTY_NAME_BESTELLUNG) + " "
				+ Integer.toString(bestellung.getIndex()) + System.lineSeparator();
	}

	public String createAddPositiontoBestellungLink(final HertlBotArtikel artikel, final Integer bestellungId) {
		return this.createKeyForAbility(HertlHendlBot.ABILTY_NAME_ADD_POSITION) + " " + artikel.getName() + " "
				+ bestellungId + System.lineSeparator();
	}

	public String createAbilityListForHelp(List<String> abilities) {
		final StringBuilder sb = new StringBuilder();
		abilities.forEach(ability -> sb.append(this.createKeyForAbility(ability) + System.lineSeparator()));
		return sb.toString();
	}

	public String createKeyForAbility(final String ability) {
		return KEY_PRE_SYMBOL + ability;
	}
	
	private void addClearButtonBestellung(List<KeyboardRow> keyboard, HertlBotBestellung bestellung)
	{
		
	}
}
