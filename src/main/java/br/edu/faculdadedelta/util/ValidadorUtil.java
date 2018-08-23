package br.edu.faculdadedelta.util;

import static br.edu.faculdadedelta.util.StringUtil.concat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorUtil {
	
	private ValidadorUtil() {
		throw new IllegalStateException("Classe Utiliária");
	}

	private static final int[] PESO_CPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

	private static int calcularDigitoCpf(String str, int[] peso) {
		int soma = 0;
		int digito = 0;
		for (int indice = str.length() - 1; indice >= 0; indice--) {
			digito = Integer.parseInt(str.substring(indice, indice + 1));
			soma += digito * peso[peso.length - str.length() + indice];
		}
		soma = 11 - soma % 11;
		return soma > 9 ? 0 : soma;
	}

	public static boolean isCPFValido(String cpf) {
		if (cpf == null || cpf.isEmpty())
			throw new IllegalArgumentException("CPF deve ser informado");

		Pattern padrao = Pattern.compile("^([0-9]{3}\\.?){3}-?[0-9]{2}$");
		Matcher matcher = padrao.matcher(cpf.trim());
		
		if(!matcher.matches())
			return false;
		
		cpf = cpf.replace(".", "").replaceAll("-", "");

		if (cpf.length() != 11)
			return false;

		String corpoCPF = cpf.substring(0, 9);
		int digito1 = Integer.parseInt(cpf.substring(9, 10));
		int digito2 = Integer.parseInt(cpf.substring(10, 11));
		
		int digito1Calculado = calcularDigitoCpf(corpoCPF, PESO_CPF);
		int digito2Calculado = calcularDigitoCpf(concat(corpoCPF, String.valueOf(digito1)), PESO_CPF);
		
		return digito1 == digito1Calculado && digito2 == digito2Calculado;
	}
	
	public static boolean isPlacaDeVeiculoValida(String placa) {
		if (placa == null || placa.isEmpty())
			throw new IllegalArgumentException("Placa deve ser informada");

		Pattern padrao = Pattern.compile("^[A-Z]{3}-?[0-9]{4}$");
		Matcher matcher = padrao.matcher(placa.toUpperCase().trim());
		
		return  matcher.matches();
	}
}