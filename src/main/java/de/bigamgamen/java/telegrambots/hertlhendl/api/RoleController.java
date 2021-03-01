package de.bigamgamen.java.telegrambots.hertlhendl.api;

import java.util.List;

import org.telegram.telegrambots.meta.api.objects.User;

public interface RoleController {
	
	/**
	 * Get the KeyboardShortcuts which this User can use.
	 * @param user
	 * @return
	 */
	public List<String> getKeyBoardShortcutsForUser(User user);
}
