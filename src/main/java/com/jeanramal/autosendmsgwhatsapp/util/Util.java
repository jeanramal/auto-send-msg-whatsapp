package com.jeanramal.autosendmsgwhatsapp.util;

import java.util.Calendar;
import java.util.Date;

public class Util {
	public static long nuTran = 0;

	public static String getTransaccion() {
		// return UUID.randomUUID().toString().replace("-", "").toUpperCase() +
		// Util.getDate().getTime();
		try {
			return String.valueOf(getDate().getTime()) + (nuTran++);
		} catch (Exception e) {
			return String.valueOf(getDate().getTime());
		}
	}

	public static Date getDate() {
		return Calendar.getInstance().getTime();
	}
}
