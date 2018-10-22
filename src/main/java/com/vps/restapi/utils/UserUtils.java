package com.vps.restapi.utils;

import com.vps.restapi.model.User;

public class UserUtils {
	public static final boolean checkIfEqual(User user1, User user2) {
		if (!user1.getUid().equals(user2.getUid())) {
			return false;
		} else if (!user1.getEmail().equals(user2.getEmail())) {
			return false;
		} else if (!user1.getFirstName().equals(user2.getFirstName())) {
			return false;
		} else if (!user1.getLastName().equals(user2.getLastName())) {
			return false;
		} else if (!user1.getPassword().equals(user2.getPassword())) {
			return false;
		}

		return true;
	}

}