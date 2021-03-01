package de.bigamgamen.java.telegrambots.hertlhendl.controller;

import java.util.Arrays;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.User;

import de.bigamgamen.java.telegrambots.hertlhendl.HertlHendlBot;
import de.bigamgamen.java.telegrambots.hertlhendl.api.RightController;
import de.bigamgamen.java.telegrambots.hertlhendl.api.RoleController;

public class HertlRoleController implements RoleController {

	private static List<String> ADMIN_SET = Arrays.asList(HertlHendlBot.ABILTY_NAME_LOCATION_PHOTO,
			HertlHendlBot.ABILTY_NAME_PRICES_PHOTO, HertlHendlBot.ABILTY_NAME_ITEM,
			HertlHendlBot.ABILTY_NAME_LIST_MY_ORDERS, HertlHendlBot.ABILTY_NAME_MY_ORDERS_AS_KEYBOARD,
			HertlHendlBot.ABILTY_NAME_NEW_ORDER, HertlHendlBot.ABILTY_NAME_MY_OPEN_ORDERS,HertlHendlBot.ABILTY_NAME_ADMIN_OPEN_ORDERS);
	private static List<String> USER_SET = Arrays.asList(HertlHendlBot.ABILTY_NAME_LOCATION_PHOTO,
			HertlHendlBot.ABILTY_NAME_PRICES_PHOTO, HertlHendlBot.ABILTY_NAME_ITEM,
			HertlHendlBot.ABILTY_NAME_LIST_MY_ORDERS, HertlHendlBot.ABILTY_NAME_MY_ORDERS_AS_KEYBOARD,
			HertlHendlBot.ABILTY_NAME_NEW_ORDER, HertlHendlBot.ABILTY_NAME_MY_OPEN_ORDERS);

	private final User user;
	private final RightController rightController;

	public HertlRoleController(User user) {
		this.user = user;
		this.rightController = new HertlRightController( user.getId());
	}

	@Override
	public List<String> getKeyBoardShortcutsForUser(User user) {
		if (rightController.isAdmin(user)) {
			return ADMIN_SET;
		} else {
			return USER_SET;
		}
	}

}
