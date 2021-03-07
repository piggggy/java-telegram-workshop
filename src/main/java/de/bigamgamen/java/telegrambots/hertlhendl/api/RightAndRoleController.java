package de.bigamgamen.java.telegrambots.hertlhendl.api;

import java.util.List;

import org.telegram.telegrambots.meta.api.objects.User;

public interface RightAndRoleController {
	
	/**
	 * Checks if the User is the Admin
	 * @param user
	 * @return
	 */
	public boolean isAdmin(User user);
	
	/**
	 * Get the KeyboardShortcuts which this User can use.
	 * @param user
	 * @return
	 */
	public List<String> getKeyBoardShortcutsForUser(User user);
	
	/**
	 * Checks if the user can use the Ability
	 * @param ability
	 * @param user
	 * @return
	 */
	public Boolean canUseAbility(User user, String ability);
}
