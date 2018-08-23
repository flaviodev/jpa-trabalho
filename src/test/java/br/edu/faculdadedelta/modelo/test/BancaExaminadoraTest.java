package br.edu.faculdadedelta.modelo.test;

import static br.edu.faculdadedelta.util.DateUtil.toDate;
import static br.edu.faculdadedelta.util.StringUtil.concat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Date;

import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.BancaExaminadora;
import br.edu.faculdadedelta.modelo.Examinador;
import br.edu.faculdadedelta.test.base.BaseCrudTest;
import br.edu.faculdadedelta.test.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.test.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.test.base.FuncaoValidaAlteracaoEntidade;
import br.edu.faculdadedelta.util.DateUtil;

public class BancaExaminadoraTest extends BaseCrudTest<String, BancaExaminadora> {

	private static final LocalDate DATA_PADRAO = LocalDate.of(2018, 9, 20);
	private static final Date DATA_ALTERACAO = DateUtil.toDate(LocalDate.of(2018, 9, 30));

	@Override
	public BancaExaminadora getEntidadeParaTeste() {

		return new BancaExaminadora(DATA_PADRAO);
	}

	public FuncaoAlteraEntidade<String, BancaExaminadora> alteracaoEntidadeDeTeste() {
		return (banca) -> banca.setData(DATA_ALTERACAO);
	}

	@Override
	public FuncaoValidaAlteracaoEntidade<String, BancaExaminadora> validaAlteracaoEntidadeDeTeste() {
		return (banca) -> assertTrue(concat("valor esperado <", DATA_ALTERACAO.toString(), "> : retornado <",
				banca.getData().toString(), ">"), banca.getData().equals(DATA_ALTERACAO));
	}

	@Override
	public FuncaoCriterioParaBuscaDeEntidade<String, BancaExaminadora> getCriterioBuscaEntidadesTeste() {
		return () -> Restrictions.eq(BancaExaminadora.Atributos.DATA, toDate(DATA_PADRAO));
	}

	@Test
	public void deveAdicionarESalvarExaminador() {

		BancaExaminadora banca = getEntidadeParaTeste();
		Examinador examinador = new Examinador("José Pio de Alcântara").setCpf("777.777.777-77");
		banca.adicionaExaminador(examinador);

		assertNotNull("Banca examinadora não pode ser nula", banca);
		assertTrue("Banca examinadora deve estar no estado transient antes de ser persistido", banca.isTransient());
		assertTrue("Examinador deve estar no estado transient antes de ser persistido", examinador.isTransient());

		banca.persiste();

		assertFalse("Banca examinadora não deve estar no estado transient após ter sido persistido",
				banca.isTransient());
		assertFalse("Examinador não deve estar no estado transient após ter sido persistido", examinador.isTransient());

		assertTrue("Banca deve ter examinador adicionado a ela", banca.getExaminadores().size() > 0);
	}

	@Test
	public void deveRetirarExaminador() {

		BancaExaminadora banca = getEntidadeParaTeste();
		Examinador examinador = new Examinador("José Pio de Alcântara").setCpf("777.777.777-77");
		banca.adicionaExaminador(examinador);

		assertNotNull("Banca examinadora não pode ser nula", banca);
		assertTrue("Banca examinadora deve estar no estado transient antes de ser persistido", banca.isTransient());
		assertTrue("Examinador deve estar no estado transient antes de ser persistido", examinador.isTransient());

		banca.persiste();

		assertFalse("Banca examinadora não deve estar no estado transient após ter sido persistido",
				banca.isTransient());
		assertFalse("Examinador não deve estar no estado transient após ter sido persistido", examinador.isTransient());

		assertTrue("Banca deve ter examinador adicionado a ela", banca.getExaminadores().size() > 0);
		
		banca.retiraExaminador(examinador);
		banca.altera();
		
		assertFalse("Banca examinadora não deve estar no estado transient após ter sido persistido",
				banca.isTransient());
		assertFalse("Examinador não deve estar no estado transient após ter sido persistido", examinador.isTransient());

		assertTrue("Banca não deve ter examinadores associados a ela", banca.getExaminadores().size() == 0);
		
	}
	
	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(BancaExaminadora.class);
		deveLimparBase(Examinador.class);
	}
}