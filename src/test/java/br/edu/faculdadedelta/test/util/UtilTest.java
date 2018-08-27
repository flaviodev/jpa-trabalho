package br.edu.faculdadedelta.test.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.junit.Test;

import br.edu.faculdadedelta.test.base.BaseTest;
import br.edu.faculdadedelta.util.DateUtil;
import br.edu.faculdadedelta.util.StringUtil;
import br.edu.faculdadedelta.util.ValidadorUtil;

public class UtilTest extends BaseTest {

	/**
	 * Teste validação LocalDate nulo
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deveLancarExcecaoAoPassarLocalDateNuloNaConversaoParaDate() {

		DateUtil.toDate((LocalDate) null);

		fail("Devia ter lançado exceção ao passar data nula");
	}

	/**
	 * Teste validação LocalDateTime nulo
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deveLancarExcecaoAoPassarLocalDateTimeNuloNaConversaoParaDate() {

		DateUtil.toDate((LocalDateTime) null);

		fail("Devia ter lançado exceção ao passar data nula");
	}

	/**
	 * Teste validação Date nulo
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deveLancarExcecaoAoPassarDateNuloNaConversaoParaLocalDate() {

		DateUtil.toLocalDate(null);

		fail("Devia ter lançado exceção ao passar data nula");
	}

	/**
	 * Teste validação LocalDateTime nulo
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deveLancarExcecaoAoPassarDateNuloNaConversaoParaLocalDateTime() {

		DateUtil.toLocalDateTime((Date) null);

		fail("Devia ter lançado exceção ao passar data nula");
	}

	/**
	 * Teste validação LocalDateTime nulo
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deveLancarExcecaoAoPassarDateSQLNuloNaConversaoParaLocalDateTime() {

		DateUtil.toLocalDateTime((java.sql.Date) null);

		fail("Devia ter lançado exceção ao passar data nula");
	}

	/**
	 * Teste conversão localdatetime
	 */
	@Test
	public void deveConverterDateParaLocalDateTime() {

		Date data = null;

		try {
			data = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("20/08/2018 10:25");
		} catch (ParseException e) {
			fail("falhou na criação da data para conversão");
		}

		LocalDateTime dataHoraConvertida = DateUtil.toLocalDateTime(data);

		assertNotNull("Data/hora convertida não deve ser nula", dataHoraConvertida);
		assertTrue("Ano deve ser 2018", dataHoraConvertida.getYear() == 2018);
		assertTrue("Mês deve ser 8", dataHoraConvertida.getMonth().getValue() == 8);
		assertTrue("Dia deve ser 20", dataHoraConvertida.getDayOfMonth() == 20);
		assertTrue("Hora deve ser 10", dataHoraConvertida.getHour() == 10);
		assertTrue("Minuto deve ser 25", dataHoraConvertida.getMinute() == 25);
	}

	/**
	 * Teste conversão localdatetime
	 */
	@Test
	public void deveConverterDateSQLParaLocalDateTime() {

		Date data = null;

		try {
			data = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("20/08/2018 10:25");
		} catch (ParseException e) {
			fail("falhou na criação da data para conversão");
		}

		java.sql.Date dataSQL = new java.sql.Date(data.getTime());

		LocalDateTime dataHoraConvertida = DateUtil.toLocalDateTime(dataSQL);

		assertNotNull("Data/hora convertida não deve ser nula", dataHoraConvertida);
		assertTrue("Ano deve ser 2018", dataHoraConvertida.getYear() == 2018);
		assertTrue("Mês deve ser 8", dataHoraConvertida.getMonth().getValue() == 8);
		assertTrue("Dia deve ser 20", dataHoraConvertida.getDayOfMonth() == 20);
		assertTrue("Hora deve ser 10", dataHoraConvertida.getHour() == 10);
		assertTrue("Minuto deve ser 25", dataHoraConvertida.getMinute() == 25);
	}

	/**
	 * Teste validação String nula na concatenação
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deveLancarExcecaoAoPassarNullParaConcatenacao() {

		String[] valorNulo = null;
		StringUtil.concat(valorNulo);

		fail("Devia ter lançado exceção ao passar string nula na concatenação");
	}

	/**
	 * Teste validação String vazia na concatenação
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deveLancarExcecaoAoPassarColecaoVaziaNaConcatenacao() {

		String[] colecaoVazia = new String[] {};
		StringUtil.concat(colecaoVazia);

		fail("Devia ter lançado exceção ao passar coleção vazia na concatenação");
	}

	/**
	 * Teste concatenação
	 */
	@Test
	public void deveConcatenarStrings() {

		String valor1 = "strings";
		String valor2 = " ";
		String valor3 = "concatenadas";

		String resultado = StringUtil.concat(valor1, valor2, valor3).toString();

		assertTrue("Resultado de ser 'strings concatenadas'", resultado.equals("strings concatenadas"));
	}

	/**
	 * Teste validação cpf
	 */
	@Test
	public void deveValidarCpf() {

		assertTrue("CPF 121.714.750-05 deve ser válido", ValidadorUtil.isCPFValido("121.714.750-05"));
	}
	
	/**
	 * Teste validação cpf vazio
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deveLancarExcecaoCpfVazio() {

		ValidadorUtil.isCPFValido("");

		fail("Devia ter lançado exceção ao passar cpf vazio");
	}
	
	/**
	 * Teste validação cpf nulo
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deveLancarExcecaoCpfNulo() {

		ValidadorUtil.isCPFValido(null);

		fail("Devia ter lançado exceção ao passar cpf nulo");
	}
	
	/**
	 * Teste validação cpf com mais de 11 numeros
	 */
	@Test
	public void deveSerInvalidoCpfMaiorQueOnzeCaracteres() {

		assertFalse("CPF 111.111.111-111 deve ser inválido", ValidadorUtil.isCPFValido("111.111.111-111"));
	}
	
	/**
	 * Teste validação cpf invalido
	 */
	@Test
	public void deveSerInvalidoCpf() {

		assertFalse("CPF 123.456.789-00 deve ser inválido", ValidadorUtil.isCPFValido("123.456.789-00"));
	}
	
	/**
	 * Teste validação placa vazia
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deveLancarExcecaoPlacaVazia() {

		ValidadorUtil.isPlacaDeVeiculoValida("");

		fail("Devia ter lançado exceção ao passar placa vazia");
	}
	
	/**
	 * Teste validação placa nula
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deveLancarExcecaoPlacaNula() {

		ValidadorUtil.isPlacaDeVeiculoValida(null);

		fail("Devia ter lançado exceção ao passar placa nula");
	}
}
