package de.bigamgamen.java.telegrambots.hertlhendl.dal;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;

import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotBestellung;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotRoot;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotUser;
import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;

public class HertlBotRootDao 
{
	private final Path DEFAULT_PATH;
	
	// Initialize a storage manager ("the database") with purely defaults.
	private final EmbeddedStorageManager storageManager;

	public HertlBotRootDao() throws URISyntaxException {
		DEFAULT_PATH = Paths.get("microStream.db");
		storageManager = EmbeddedStorage.start(DEFAULT_PATH);
	}

	public HertlBotRoot getRoot() {
		HertlBotRoot root = (HertlBotRoot) this.storageManager.root(); 
		if(root == null)
		{
			root = new HertlBotRoot();
			this.storageManager.setRoot(root);
			this.storageManager.storeRoot();
		}
		return root;
	}
	
	public HertlBotUser loadUser(Long chatId) {
		
		Optional<HertlBotUser> userOpt = getRoot()
				.getUsers()
				.stream()
				.filter(user -> user.getChatId().equals(chatId)).findFirst();
		
		if(userOpt.isPresent())
		{
			return userOpt.get();
		}
		
		HertlBotUser user = new HertlBotUser(
				chatId);
		getRoot()
		.getUsers()
		.add(user);
		storageManager.storeRoot();
		return user;
	}
	
	public HertlBotBestellung createNewBestellungForUser(Long chatId)
	{
		HertlBotUser user = this.loadUser(chatId);
		HertlBotBestellung bestellung = new HertlBotBestellung(user, Collections.emptyList());
		user.getBestellungen().add(bestellung);
		this.storageManager.storeRoot();
		
		return bestellung;
		
	}
	public void shutDown() {
		
		this.storageManager.shutdown();
	}

}
