package br.edu.faculdadedelta.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

	private DateUtil() {

		throw new IllegalStateException("Classe Utilitária");
	}

	public static LocalDate toLocalDate(Date data) {

		if (data == null)
			throw new IllegalArgumentException("Data não pode ser nula");

		if (data instanceof java.sql.Date)
			return ((java.sql.Date) data).toLocalDate();

		return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date toDate(LocalDate data) {

		if (data == null)
			throw new IllegalArgumentException("Data não pode ser nula");

		return Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
