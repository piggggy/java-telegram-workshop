package de.bigamgamen.java.telegrambots.hertlhendl.dal;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotBestellung;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotRoot;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotUser;
import one.microstream.storage.configuration.Configuration;
import one.microstream.storage.types.EmbeddedStorageFoundation;
import one.microstream.storage.types.EmbeddedStorageManager;

public class HertlBotRootDao {

	private final static EmbeddedStorageManager storageManager = createStorageManager();

	public HertlBotRootDao() {

	}

	private static EmbeddedStorageManager createStorageManager() {
		final Configuration configuration = Configuration.Default();
		configuration.setBaseDirectory(Paths.get("data", "storage").toString());
		configuration.setChannelCount(2);

		final EmbeddedStorageFoundation<?> foundation = configuration.createEmbeddedStorageFoundation();

		final EmbeddedStorageManager storageManager = foundation.createEmbeddedStorageManager().start();

		if (storageManager.root() == null) {
			final HertlBotRoot root = new HertlBotRoot();
			storageManager.setRoot(root);
			storageManager.storeRoot();
		}

		return storageManager;
	}

	public static EmbeddedStorageManager storageManager() {
		return storageManager;
	}

	public HertlBotRoot root() {
		return (HertlBotRoot) storageManager().root();
	}

	public HertlBotBestellung loadBestellung(Long chatId, int bestellId) {
		HertlBotUser user = loadUser(chatId);

		return user.getBestellungen().stream().filter(bestellung -> bestellung.getIndex() == bestellId).findFirst()
				.get();
	}

	public HertlBotUser loadUser(Long chatId) {

		Optional<HertlBotUser> userOpt = root().users().ofId(chatId);

		if (userOpt.isPresent()) {
			return userOpt.get();
		}

		HertlBotUser user = new HertlBotUser(chatId);
		root().users().add(user, storageManager());
		return user;
	}

	public HertlBotBestellung createNewBestellungForUser(Long chatId) {
		HertlBotUser user = this.loadUser(chatId);
		HertlBotBestellung bestellung = new HertlBotBestellung(user, new ArrayList<>());
		user.addBestellung(bestellung, storageManager);

		return bestellung;
	}

	public void shutDown() {

		storageManager.shutdown();
	}

}
