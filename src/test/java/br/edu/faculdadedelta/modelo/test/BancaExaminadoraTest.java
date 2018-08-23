package br.edu.faculdadedelta.modelo.test;

import static br.edu.faculdadedelta.util.DateUtil.toDate;
import static br.edu.faculdadedelta.util.StringUtil.concat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.AgendamentoDeExame;
import br.edu.faculdadedelta.modelo.BancaExaminadora;
import br.edu.faculdadedelta.modelo.Examinador;
import br.edu.faculdadedelta.modelo.ProcessoHabilitacao;
import br.edu.faculdadedelta.test.base.BaseCrudTest;
import br.edu.faculdadedelta.test.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.test.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.test.base.FuncaoValidaAlteracaoEntidade;
import br.edu.faculdadedelta.test.util.FabricaTeste;
import br.edu.faculdadedelta.util.DateUtil;

public class BancaExaminadoraTest extends BaseCrudTest<String, BancaExaminadora> {

	private static final LocalDate DATA = LocalDate.of(2018, 9, 20);
	private static final Date DATA_ALTERACAO = DateUtil.toDate(LocalDate.of(2018, 9, 30));

	@Override
	public BancaExaminadora getEntidadeParaTeste() {

		return FabricaTeste.criaBanca(DATA);
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
		
		return () -> Restrictions.eq(BancaExaminadora.Atributos.DATA, toDate(DATA));
	}

	private Examinador adicionaExaminadorABanca(BancaExaminadora banca) {

		assertNotNull("Banca examinadora não pode ser nula", banca);
		assertTrue("Banca examinadora deve estar no estado transient antes de ser persistido", banca.isTransient());

		Examinador examinador = FabricaTeste.criaExaminador().persiste();
		assertFalse("Examinador não deve estar no estado transient após ter sido persistido", examinador.isTransient());

		banca.adicionaExaminador(examinador);

		return examinador;
	}

	@Test
	public void deveAdicionarExaminador() {

		BancaExaminadora banca = getEntidadeParaTeste();
		adicionaExaminadorABanca(banca);

		banca.persiste();
		assertFalse("Banca examinadora não deve estar no estado transient após ter sido persistido",
				banca.isTransient());

		assertTrue("Banca deve ter examinador adicionado a ela", banca.getExaminadores().size() > 0);
	}

	@Test
	public void deveRetirarExaminador() {

		BancaExaminadora banca = getEntidadeParaTeste();
		Examinador examinador = adicionaExaminadorABanca(banca);

		banca.persiste();

		assertFalse("Banca examinadora não deve estar no estado transient após ter sido persistido",
				banca.isTransient());

		assertTrue("Banca deve ter examinador adicionado a ela", banca.getExaminadores().size() > 0);

		banca.retiraExaminador(examinador);
		banca.altera();

		assertFalse("Banca examinadora não deve estar no estado transient após ter sido persistido",
				banca.isTransient());

		assertTrue("Banca não deve ter examinadores associados a ela", banca.getExaminadores().size() == 0);
	}

	private AgendamentoDeExame agendaExame(BancaExaminadora banca) {

		assertNotNull("Banca não pode ser nula", banca);
		assertFalse("Banca examinadora não deve estar no estado transient após ter sido persistido",
				banca.isTransient());

		ProcessoHabilitacao processo = FabricaTeste.criaProcesso(LocalDate.of(2018, 8, 1)).persiste();

		assertFalse("Processo habilitação não deve estar no estado transient após ter sido persistido",
				processo.isTransient());

		AgendamentoDeExame agendamento = banca.agendaExame(LocalDateTime.of(2018, 9, 1, 10, 00), processo);

		assertFalse("Agendamento de Exame não deve estar no estado transient após ter sido persistido",
				agendamento.isTransient());

		return agendamento;
	}

	@Test
	public void deveAgendarExame() {

		agendaExame(FabricaTeste.criaBanca(LocalDate.of(2018, 9, 1)).persiste());
	}

	@Test
	public void deveCancelarAgendamentoDeExame() {
		
		BancaExaminadora banca = FabricaTeste.criaBanca(LocalDate.of(2018, 9, 2)).persiste();

		AgendamentoDeExame agendamento = agendaExame(banca);
		assertNotNull("Agendamento não pode ser nulo", agendamento);

		banca.cancelaAgendamentoDeExame(agendamento.getProcesso());

		assertTrue("Banca não deve possuir agendamentos", banca.getAgendamentos().isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void naoDeveAgendarMaisDeUmExameParaOMesmoHorario() {

		BancaExaminadora banca = FabricaTeste.criaBanca(LocalDate.of(2018, 9, 2)).persiste();

		agendaExame(banca);
		agendaExame(banca);

		fail("Deveria lançar IllegalArgumentException ao tentar agendar duas vezes o mesmo horário em uma banca");
	}

	@Override
	public void deveExluirTodasEntidade() {

		deveLimparBase(AgendamentoDeExame.class);
		super.deveExluirTodasEntidade();
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(AgendamentoDeExame.class);
		deveLimparBase(BancaExaminadora.class);
		deveLimparBase(ProcessoHabilitacao.class);
		deveLimparBase(Examinador.class);
	}
}