/*
 * ChatBot Workshop
 * Copyright (C) 2020 Arne Meier
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.bigamgamen.java.telegrambots.hertlhendl;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.xml.sax.SAXException;

import com.google.common.annotations.VisibleForTesting;

import de.bigamgamen.java.helper.IOHelper;
import de.bigamgamen.java.telegrambots.hertlhendl.dal.HertlBotRootDao;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotArtikel;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotBestellung;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotPosition;
import de.bigamgamen.java.telegrambots.hertlhendl.init.InitArtikels;


public class HertlHendlBot extends AbilityBot
{
	
	private static final String ABILTY_NAME_KEYBOARD = "keyboard";
	private static final String ABILTY_NAME_STANDORTEFOTO = "standortefoto";
	private static final String ABILTY_NAME_PREISEFOTO = "preisefoto";
	private static final String ABILTY_NAME_PREISE = "preise";
	private static final String ABILTY_NAME_BESTELLUNG = "bestellung";
	private static final String ABILTY_NAME_BESTELLUNGEN = "bestellungen";
	private static final String ABILTY_NAME_ARTIKEL = "artikel";
	private static final String ABILTY_NAME_ADD_POSITION = "addposition";
	private static final String ABILTY_NAME_MY_BESTELLUNGEN = "mybestellungen";
	private static final String ABILTY_NAME_MY_BESTELLUNGEN_KEYBOARD = "mybestellungenkeyboard";
	private static final String ABILTY_NAME_NEUE_BESTELLUNG = "neuebestellung";
	private static final String ABILTY_NAME_OFFENE_BESTELLUNG = "offnenebestellungen";
	private static final List<String> abilities = Arrays.asList(
		ABILTY_NAME_KEYBOARD,
		ABILTY_NAME_STANDORTEFOTO,
		ABILTY_NAME_PREISEFOTO,
		ABILTY_NAME_PREISE,
		ABILTY_NAME_ARTIKEL,
		ABILTY_NAME_BESTELLUNGEN,
		ABILTY_NAME_MY_BESTELLUNGEN_KEYBOARD,
		ABILTY_NAME_BESTELLUNG,
		ABILTY_NAME_MY_BESTELLUNGEN,
		ABILTY_NAME_NEUE_BESTELLUNG,
		ABILTY_NAME_OFFENE_BESTELLUNG);
	private static final String KEY_PRE_SYMBOL = "/";
	
	private static final String PRICE_AS_TEXT = "1/2 Hähnchen 3,80€\n"
		+ "Schenkel     2,00€\n"
		+ "Brezel       0,80€\n"
		+ "Salat        1,50€\n";
	private static final String HENDL_PREISE_JPG = "hendl_preise.jpg";
	private final static Logger LOG = LoggerFactory.getLogger(HertlHendlBot.class);
	private final static String BOT_TOKEN = "";
	private final static String BOT_USERNAME = "";
	private static int CREATOR_ID = 929115416;
	private static String HERTL_URL = "https://hertel-haehnchen.de/standplatzsuche?search=92637";
	private static HertlBotRootDao hertlBotDao;
	
	public static void main(final String[] args) throws ParserConfigurationException,
		SAXException, IOException, URISyntaxException, TelegramApiException
	{
		LOG.info("HertlHendlBot starting");

		// final DBContext db = MapDBContext.onlineInstance("bot.db");
		final String token = args[0] != null ? args[0] : BOT_TOKEN;
		final String username = args[1] != null ? args[1] : BOT_USERNAME;
		final HertlHendlBot bot = new HertlHendlBot(token, username);
		 TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
		api.registerBot(bot);
		LOG.info("HertlHendlBot successfull started");
	}
	
	public HertlHendlBot(final String botToken, final String botUsername)
		throws ParserConfigurationException, SAXException, IOException, URISyntaxException
	{
		super(botToken, botUsername);
		hertlBotDao = new HertlBotRootDao();
		InitArtikels.initArtikels(hertlBotDao);
	}
	
	@Override
	public int creatorId()
	{
		return CREATOR_ID;
	}
	
	private String getFilePath(final PhotoSize photo)
	{
		if(photo.getFilePath() != null)
		{
			return photo.getFilePath();
		}
		final GetFile getFileMethod = new GetFile();
		getFileMethod.setFileId(photo.getFileId());
		try
		{
			final org.telegram.telegrambots.meta.api.objects.File file = this.execute(getFileMethod);
			return file.getFilePath();
		}
		catch(final TelegramApiException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private File downloadPhoto(final String filePath)
	{
		try
		{
			return this.downloadFile(filePath);
		}
		catch(final TelegramApiException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private void sendPhotoFromUpload(final String filePath, final  Long chatId)
	{
		final SendPhoto sendPhotoRequest = new SendPhoto(); // 1
		sendPhotoRequest.setChatId(Long.toString(chatId)); // 2
		try
		{
			sendPhotoRequest.setPhoto(new InputFile(IOHelper.findResource(filePath), filePath));
		}
		catch(final FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch(final IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // 3
		try
		{
			this.execute(sendPhotoRequest); // 4
		}
		catch(final TelegramApiException e)
		{
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({"unused", "WeakerAccess"})
	public Ability showHelp()
	{
		
		return Ability.builder().name("help").info("shows help").locality(ALL).privacy(PUBLIC).action(context ->
		{
			final SendMessage message = new SendMessage();
			message.setChatId(Long.toString(context.chatId()));
			message.setText(this.createAbilityListForHelp());
			this.silent.execute(message);
		}).build();
	}

	@SuppressWarnings({ "unused", "WeakerAccess" })
	public Ability showBestellung() {

		return Ability
				.builder()
				.name(ABILTY_NAME_BESTELLUNG)
				.info("zeigt eine bestimmte Bestellung")
				.locality(ALL)
				.privacy(PUBLIC)
				.input(1)
				.action(context -> {
					final int bestellId = Integer.parseInt(context.firstArg());
					final Long chatId = context.chatId();
					final HertlBotBestellung bestellung = hertlBotDao.loadBestellung(chatId, bestellId);
					final SendMessage message = new SendMessage();
					message.setChatId(Long.toString(context.chatId()));
					final String messageText = bestellung.toString() + System.lineSeparator() + "Füge Positionen zu deiner Bestellung hinzu";
					
					message.setText(messageText);
					
					final ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
					final List<KeyboardRow> keyboard = this.loadAndShowAllArtikelForBestellung(context.chatId(), bestellId);

					// activate the keyboard
					keyboardMarkup.setKeyboard(keyboard);
					message.setReplyMarkup(keyboardMarkup);
					
					this.silent.execute(message);
				}).build();
	}
	
	@SuppressWarnings({ "unused", "WeakerAccess" })
	public Ability showArtikel() {

		return Ability
				.builder()
				.name(ABILTY_NAME_ARTIKEL)
				.info("Listet alle Artikel auf")
				.locality(ALL)
				.privacy(PUBLIC)
				.action(context -> {
					final SendMessage message = new SendMessage();
					message.setChatId(Long.toString(context.chatId()));
					message.setText(this.loadAndShowAllArtikel());
					this.silent.execute(message);
				}).build();
	}
	
	@SuppressWarnings({"unused", "WeakerAccess"})
	public Ability showMyBestellungen()
	{
		
		return Ability.builder().name(ABILTY_NAME_MY_BESTELLUNGEN).info("Zeigt die eigenen Bestellungen").locality(
			ALL).privacy(PUBLIC).action(context ->
			{
				final SendMessage message = new SendMessage();
				message.setChatId(Long.toString(context.chatId()));
				message.setText(this.loadAndShowMyBestellungen(context.chatId()));
				
				this.silent.execute(message);
			}).build();
	}
	
	@SuppressWarnings({"unused", "WeakerAccess"})
	public Ability showMyBestellungenKeyBoard()
	{
		
		return Ability.builder().name(ABILTY_NAME_MY_BESTELLUNGEN_KEYBOARD).info(
			"Zeigt die eigenen Bestellungen als keyboard").locality(ALL).privacy(PUBLIC).action(context ->
			{
				final SendMessage message = new SendMessage();
				message.setChatId(Long.toString(context.chatId()));
				message.setText("Öffne die Bestellungen über die Tastatur: ");
				
				final ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				final List<KeyboardRow> keyboard = this.loadAndShowMyBestellungenAsKeyBoard(context.chatId());
				
				// activate the keyboard
				keyboardMarkup.setKeyboard(keyboard);
				message.setReplyMarkup(keyboardMarkup);
				
				this.silent.execute(message);
			}).build();
	}
	
	@SuppressWarnings({"unused", "WeakerAccess"})
	public Ability createNewBestellung()
	{
		
		return Ability.builder().name(ABILTY_NAME_NEUE_BESTELLUNG).info("Erstellt eine neue Bestellung").locality(
			ALL).privacy(PUBLIC).action(context ->
			{
				final SendMessage message = new SendMessage();
				message.setChatId(Long.toString(context.chatId()));
				message.setText(this.createAndShowNewBestellung(context.chatId()));
				this.silent.execute(message);
			}).build();
	}
	
	@SuppressWarnings({"unused", "WeakerAccess"})
	public Ability addPositionToBestellung()
	{
		
		return Ability.builder()
				.name(ABILTY_NAME_ADD_POSITION)
				.info("Fügt eine Position zu einer Bestellung hinzu")
				.locality(ALL)
				.privacy(PUBLIC)
				.input(2)
				.action(context ->
			{
				final SendMessage message = new SendMessage();
				message.setChatId(Long.toString(context.chatId()));
				message.setText(this.createPositionForBestellung(context.firstArg(), context.chatId(), Integer.valueOf(context.secondArg())));
				this.silent.execute(message);
			}).build();
	}
	
	@SuppressWarnings({"unused", "WeakerAccess"})
	public Ability sendKeyboard()
	{
		return Ability.builder().name("keyboard").info("send a custom keyboard").locality(ALL).privacy(PUBLIC).action(
			context ->
			{
				final SendMessage message = new SendMessage();
				message.setChatId(Long.toString(context.chatId()));
				message.setText("Enjoy this wonderful keyboard!");
				
				final ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				final List<KeyboardRow> keyboard = new ArrayList<>();
				
				// row 1
				final KeyboardRow row = new KeyboardRow();
				row.add(this.createKeyForAbility(ABILTY_NAME_PREISE));
				row.add(this.createKeyForAbility(ABILTY_NAME_PREISEFOTO));
				row.add(this.createKeyForAbility(ABILTY_NAME_STANDORTEFOTO));
				keyboard.add(row);
				
				// activate the keyboard
				keyboardMarkup.setKeyboard(keyboard);
				message.setReplyMarkup(keyboardMarkup);
				
				this.silent.execute(message);
			}).build();
	}
	
	@SuppressWarnings({"unused", "WeakerAccess"})
	public Ability showPreise()
	{
		return Ability.builder().name(ABILTY_NAME_PREISE).info("Preisliste").locality(ALL).privacy(PUBLIC).action(
			context ->
			{
				final SendMessage message = new SendMessage();
				message.setChatId(Long.toString(context.chatId()));
				message.setText(PRICE_AS_TEXT);
				this.silent.execute(message);
			}).build();
	}
	
	@SuppressWarnings({"unused", "WeakerAccess"})
	public Ability showPreiseFoto()
	{
		return Ability.builder().name(ABILTY_NAME_PREISEFOTO).info("send Preisfoto").locality(ALL).privacy(
			PUBLIC).action(context -> this.sendPhotoFromUpload(HENDL_PREISE_JPG, context.chatId())).build();
	}
	
	@SuppressWarnings({"unused", "WeakerAccess"})
	public Ability showstandorteFoto()
	{
		return Ability.builder().name(ABILTY_NAME_STANDORTEFOTO).info("standorteFoto Weiden").locality(ALL).privacy(
			PUBLIC).action(context -> this.makeScreenshotSenditDeleteit(context.chatId())).build();
	}
	
	@SuppressWarnings({"unused", "WeakerAccess"})
	public Ability showstandorteWeiden()
	{
		return Ability.builder().name("standorte").info("standorte Weiden").locality(ALL).privacy(PUBLIC).action(
			context -> this.sendPhotoFromUpload("", context.chatId())).build();
	}
	
	private void makeScreenshotSenditDeleteit(final Long chatId)
	{
		final String fileName = this.makingScreenshotOfHertlHomepage();
		this.sendPhotoFromUpload(fileName, chatId);
		final File fileToDelete = new File(fileName);
		fileToDelete.delete();
	}
	
	private String makingScreenshotOfHertlHomepage()
	{
		final ProcessBuilder processBuilder = new ProcessBuilder();
		
		final String hertlTimeStampFileName = "hertl_standorteFoto"
			+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd__HH-mm-ss-SSS"))
			+ ".png";
		// Run a shell script
		// processBuilder.command("path/to/hello.sh");
		
		// -- Windows --
		
		// Run a command
		// processBuilder.command("cmd.exe", "/c", "dir C:\\Users\\mkyong");
		
		// Run a bat file
		// processBuilder.command("C:\\Users\\mkyong\\hello.bat");
		
		try
		{
			
			// -- Linux --
			final String command = "sudo docker run --rm -v $PWD:/srv lifenz/docker-screenshot "
				+ HERTL_URL
				+ " "
				+ hertlTimeStampFileName
				+ " 1500px 2000 1";
			System.out.println(command);
			// Run a shell command
			processBuilder.command("bash", "-c", command);
			
			final Process process = processBuilder.start();
			System.out.println("Docker gestartet");
			
			final StringBuilder output = new StringBuilder();
			
			final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			String line;
			while((line = reader.readLine()) != null)
			{
				output.append(line + "\n");
			}
			
			final int exitVal = process.waitFor();
			System.out.println("exitvalue: " + exitVal);
			if(exitVal == 0)
			{
				System.out.println("Success!");
				System.out.println(output);
				return hertlTimeStampFileName;
			}
			else
			{
				return null;
			}
			
		}
		catch(final IOException e)
		{
			e.printStackTrace();
		}
		catch(final InterruptedException e)
		{
			e.printStackTrace();
		}
		return "";
		
	}
	
	@VisibleForTesting
	void setSender(final MessageSender sender)
	{
		this.sender = sender;
	}
	
	@VisibleForTesting
	void setSilent(final SilentSender silent)
	{
		this.silent = silent;
	}
	
	private String createPositionForBestellung(
		final String artikelName,
		final Long chatId,
		final Integer bestellungId)
	{
		final HertlBotArtikel artikel = hertlBotDao.root().artikels().ofName(artikelName);
		final HertlBotBestellung bestellung = hertlBotDao.loadBestellung(chatId, bestellungId);
		final Predicate<HertlBotPosition> positionAlreadyExist =
			position -> position.getArtikel().getName().equals(artikel.getName());
		
		HertlBotPosition position;
		
		final Optional<HertlBotPosition> positionOpt =
			bestellung.getPositionen().stream().filter(positionAlreadyExist).findFirst();
		if(positionOpt.isPresent())
		{
			position = positionOpt.get();
			position.getMenge().add(
				BigInteger.valueOf(1L));
			hertlBotDao.storageManager().store(position);
		}
		else
		{
			position = new HertlBotPosition();
			position.setArtikel(artikel);
			bestellung.addPosition(position, HertlBotRootDao.storageManager());
		}
		
		return this.loadAndShowBestellung(chatId, bestellungId);
		
	}
	
	private String formatStandorte(final List<String> standorte)
	{
		String alle = "";
		for(final String standort : standorte)
		{
			alle += standort;
		}
		
		return alle;
	}
	
	private String loadAndShowBestellung(final Long chatId, final int bestellId)
	{
		final HertlBotBestellung bestellung = hertlBotDao.loadBestellung(chatId, bestellId);
		return bestellung.toString();
	}
	
	public String loadAndShowMyBestellungen(final Long chatId)
	{
		
		final StringBuilder sb = new StringBuilder("Ihre Bestellungen:" + System.lineSeparator());
		HertlHendlBot.hertlBotDao.loadUser(chatId).getBestellungen().forEach(
			bestellung -> sb.append(this.createBestellungLink(bestellung)));
		return sb.toString();
	}
	
	public List<KeyboardRow> loadAndShowMyBestellungenAsKeyBoard(final Long chatId)
	{
		final List<KeyboardRow> keyboard = new ArrayList<>();
		final KeyboardRow row = new KeyboardRow();
		
		HertlHendlBot.hertlBotDao.loadUser(chatId).getBestellungen().forEach(
			bestellung -> row.add(this.createBestellungLink(bestellung)));
		
		keyboard.add(row);
		
		return keyboard;
	}
	
	public String createAndShowNewBestellung(final Long chatId)
	{
		
		final HertlBotBestellung bestellung = hertlBotDao.createNewBestellungForUser(chatId);
		
		return this.createBestellungLink(bestellung);
	}
	
	private String createAbilityListForHelp()
	{
		final StringBuilder sb = new StringBuilder();
		abilities.forEach(ability -> sb.append(this.createKeyForAbility(ability) + System.lineSeparator()));
		return sb.toString();
	}
	
	private String createKeyForAbility(final String ability)
	{
		return KEY_PRE_SYMBOL + ability;
	}
	
	private String createBestellungLink(final HertlBotBestellung bestellung)
	{
		return this.createKeyForAbility(ABILTY_NAME_BESTELLUNG)
			+ " "
			+ Integer.toString(bestellung.getIndex())
			+ System.lineSeparator();
	}
	
	private String loadAndShowAllArtikel()
	{
		final StringBuilder sb = new StringBuilder();
		hertlBotDao.root().artikels().all().forEach(
			artikel -> sb.append(artikel.toString()).append(System.lineSeparator()));
		return sb.toString();
	}
	
	private List<KeyboardRow> loadAndShowAllArtikelForBestellung(final Long chatId, final Integer bestellungId)
	{
		final List<KeyboardRow> keyboard = new ArrayList<>();
		final KeyboardRow row = new KeyboardRow();
		
				hertlBotDao.root().artikels().all().forEach(artikel  -> row.add(this.createAddPositiontoBestellungLink(artikel, bestellungId)) );
		
		keyboard.add(row);
		
		return keyboard;
	}
	
	private String createAddPositiontoBestellungLink(final HertlBotArtikel artikel, final Integer bestellungId)
	{
		return this.createKeyForAbility(ABILTY_NAME_ADD_POSITION)
			+ " "
			+ artikel.getName()
			+ " "
			+ bestellungId
			+ System.lineSeparator();
	}
	
}
