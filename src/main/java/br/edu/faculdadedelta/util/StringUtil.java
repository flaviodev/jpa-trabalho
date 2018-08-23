package br.edu.faculdadedelta.util;

import java.util.stream.IntStream;

public class StringUtil {

	private StringUtil() {

		throw new IllegalStateException("Classe Utilitária");
	}

	public static String concat(String... valoresTextuais) {

		if (valoresTextuais == null || valoresTextuais.length == 0)
			throw new IllegalArgumentException("Coleção de valores textuais não pode ser nula ou vazia");

		StringBuilder valoresConcatenados = new StringBuilder(valoresTextuais.length);

		IntStream.range(0, valoresTextuais.length).forEach(i -> valoresConcatenados.append(valoresTextuais[i]));

		return valoresConcatenados.toString();
	}
}
