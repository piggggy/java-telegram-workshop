/*
 * ChatBot Workshop
 * Copyright (C) 2018 Marcus Fihlon
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.db.MapDBContext;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import com.google.common.annotations.VisibleForTesting;

public class HertlHendlBot extends AbilityBot {

	private final static String BOT_TOKEN = "";
	private final static String BOT_USERNAME = "";
	private static int CREATOR_ID = 929115416;
	private static String HERTL_URL = "https://hertel-haehnchen.de/standplatzsuche?search=92637";

	public static void main(String[] args) throws TelegramApiRequestException {
		ApiContextInitializer.init();
		final DBContext db = MapDBContext.onlineInstance("bot.db");
		String token = args[0] != null ? args[0] : BOT_TOKEN;
		String username = args[1] != null ? args[1] : BOT_USERNAME;
		final HertlHendlBot bot = new HertlHendlBot(db,token,username);
		final TelegramBotsApi api = new TelegramBotsApi();
		api.registerBot(bot);
		
	}

	HertlHendlBot(final DBContext db, String botToken, String botUsername) {
		super(botToken, botUsername, db);
	}

	@Override
	public int creatorId() {
		return CREATOR_ID;
	}

	private String getFilePath(final PhotoSize photo) {
		if (photo.hasFilePath()) {
			return photo.getFilePath();
		}
		final GetFile getFileMethod = new GetFile();
		getFileMethod.setFileId(photo.getFileId());
		try {
			final org.telegram.telegrambots.meta.api.objects.File file = execute(getFileMethod);
			return file.getFilePath();
		} catch (final TelegramApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	private File downloadPhoto(final String filePath) {
		try {
			return downloadFile(filePath);
		} catch (final TelegramApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void sendPhotoFromUrl(final String url, final Long chatId) {
		final SendPhoto sendPhotoRequest = new SendPhoto(); // 1
		sendPhotoRequest.setChatId(chatId); // 2
		sendPhotoRequest.setPhoto(url); // 3
		try {
			execute(sendPhotoRequest); // 4
		} catch (final TelegramApiException e) {
			e.printStackTrace();
		}
	}

	private void sendPhotoFromFileId(final String fileId, final Long chatId) {
		final SendPhoto sendPhotoRequest = new SendPhoto(); // 1
		sendPhotoRequest.setChatId(chatId); // 2
		sendPhotoRequest.setPhoto(fileId); // 3
		try {
			execute(sendPhotoRequest); // 4
		} catch (final TelegramApiException e) {
			e.printStackTrace();
		}
	}

	private void sendPhotoFromUpload(final String filePath, final Long chatId) {
		final SendPhoto sendPhotoRequest = new SendPhoto(); // 1
		sendPhotoRequest.setChatId(chatId); // 2
		sendPhotoRequest.setPhoto(new File(filePath)); // 3
		try {
			execute(sendPhotoRequest); // 4
		} catch (final TelegramApiException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unused", "WeakerAccess" })
	public Ability showHelp() {
		return Ability.builder().name("help").info("shows help").locality(ALL).privacy(PUBLIC).action(context -> {
			final SendMessage message = new SendMessage();
			message.setChatId(context.chatId());
			message.setText("/preise\n"
					+ "/preiseFoto\n"
					+ "/standorte\n"
					+ "/keyboard");
			silent.execute(message);
		}).build();
	}

	@SuppressWarnings({ "unused", "WeakerAccess" })
	public Ability sendKeyboard() {
		return Ability.builder().name("keyboard").info("send a custom keyboard").locality(ALL).privacy(PUBLIC)
				.action(context -> {
					final SendMessage message = new SendMessage();
					message.setChatId(context.chatId());
					message.setText("Enjoy this wonderful keyboard!");

					final ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
					final List<KeyboardRow> keyboard = new ArrayList<>();

					// row 1
					KeyboardRow row = new KeyboardRow();
					row.add("/preise");
					row.add("/preiseFoto");
					row.add("/standorte");
					keyboard.add(row);

					// activate the keyboard
					keyboardMarkup.setKeyboard(keyboard);
					message.setReplyMarkup(keyboardMarkup);

					silent.execute(message);
				}).build();
	}

	@SuppressWarnings({ "unused", "WeakerAccess" })
	public Ability showPreise() {
		return Ability.builder().name("preise").info("Preisliste").locality(ALL).privacy(PUBLIC).action(context -> {
			final SendMessage message = new SendMessage();
			message.setChatId(context.chatId());
			message.setText(
					"1/2 Hähnchen 3,80€\n"
				  + "Schenkel     2,00€\n"
				  + "Brezel       0,80€\n"
				  + "Salat        1,50€\n");
			silent.execute(message);
		}).build();
	}
	
	 @SuppressWarnings({"unused", "WeakerAccess"})
	    public Ability showPreiseFoto() {
	        return Ability
	                .builder()
	                .name("preiseFoto")
	                .info("send Preisfoto")
	                .locality(ALL)
	                .privacy(PUBLIC)
	                .action(context -> sendPhotoFromUpload("src/main/resources/chatbot.jpg", context.chatId()))
	                .build();
	    }

	@SuppressWarnings({ "unused", "WeakerAccess" })
	public Ability showStandorte() {
		return Ability.builder().name("standorte").info("Standorte Weiden").locality(ALL).privacy(PUBLIC)
				.action(context -> sendPhotoFromUpload(makingScreenshotOfHertlHomepage(), context.chatId())).build();
	}

	private String makingScreenshotOfHertlHomepage() {
		ProcessBuilder processBuilder = new ProcessBuilder();

	
		String hertlTimeStampFileName = "hertl_standorte"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd__HH-mm-ss-SSS")) + ".png";
		// Run a shell script
		// processBuilder.command("path/to/hello.sh");

		// -- Windows --

		// Run a command
		// processBuilder.command("cmd.exe", "/c", "dir C:\\Users\\mkyong");

		// Run a bat file
		// processBuilder.command("C:\\Users\\mkyong\\hello.bat");

		try {
			
			
			// -- Linux --
			String command = "docker run --rm -v $PWD:/srv lifenz/docker-screenshot " + HERTL_URL + " "
					+ hertlTimeStampFileName + " 1920px 2000 1";
			System.out.println(command);
			// Run a shell command
			processBuilder.command("bash", "-c", command);

			Process process = processBuilder.start();

			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println("Success!");
				System.out.println(output);
				return hertlTimeStampFileName;
			} else {
				// abnormal...
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";

	}

//    @SuppressWarnings({"unused", "WeakerAccess"})
//    public Ability sayNo() {
//        return Ability.builder()
//                .name(DEFAULT)
//                .privacy(PUBLIC)
//                .locality(ALL)
//                .action(context -> silent.send("Sorry, I have no answer for you today.", context.chatId()))
//                .build();
//    }

	@VisibleForTesting
	void setSender(final MessageSender sender) {
		this.sender = sender;
	}

	@VisibleForTesting
	void setSilent(final SilentSender silent) {
		this.silent = silent;
	}

}
