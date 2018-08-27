package br.edu.faculdadedelta.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {
	
	private static final String DATA_NAO_PODE_SER_NULA = "Data não pode ser nula";

	private DateUtil() {

	}

	public static LocalDate toLocalDate(Date data) {

		if (data == null)
			throw new IllegalArgumentException(DATA_NAO_PODE_SER_NULA);

		if (data instanceof java.sql.Date)
			return ((java.sql.Date) data).toLocalDate();

		return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime toLocalDateTime(Date data) {

		if (data == null)
			throw new IllegalArgumentException(DATA_NAO_PODE_SER_NULA);

		if (data instanceof java.sql.Date)
			return toLocalDateTime(new Date(((java.sql.Date) data).getTime()));

		return LocalDateTime.ofInstant(data.toInstant(), ZoneId.systemDefault());
	}

	public static Date toDate(LocalDate data) {

		if (data == null)
			throw new IllegalArgumentException(DATA_NAO_PODE_SER_NULA);

		return Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static Date toDate(LocalDateTime dataHora) {

		if (dataHora == null)
			throw new IllegalArgumentException("Data/hora não pode ser nula");

		return Date.from(dataHora.atZone(ZoneId.systemDefault()).toInstant());
	}

}
