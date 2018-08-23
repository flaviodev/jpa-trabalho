package br.edu.faculdadedelta.modelo.test;

import static br.edu.faculdadedelta.modelo.base.JPAUtil.getAlias;
import static br.edu.faculdadedelta.util.DateUtil.toDate;
import static br.edu.faculdadedelta.util.StringUtil.concat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.junit.AfterClass;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.AgendamentoDeExame;
import br.edu.faculdadedelta.modelo.Aluno;
import br.edu.faculdadedelta.modelo.BancaExaminadora;
import br.edu.faculdadedelta.modelo.Examinador;
import br.edu.faculdadedelta.modelo.Instrutor;
import br.edu.faculdadedelta.modelo.ProcessoHabilitacao;
import br.edu.faculdadedelta.modelo.Veiculo;
import br.edu.faculdadedelta.test.base.BaseCrudTest;
import br.edu.faculdadedelta.test.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.test.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.test.base.FuncaoValidaAlteracaoEntidade;
import br.edu.faculdadedelta.test.util.FabricaTeste;

public class AgendamentoDeExameTest extends BaseCrudTest<String, AgendamentoDeExame> {

	private static final LocalDateTime DATA_HORA_AGENDAMENTO = LocalDateTime.of(2018, 9, 20, 10, 00);
	private static final Date DATA_HORA_AGENDAMENTO_ALTERACAO = toDate(LocalDateTime.of(2018, 9, 20, 11, 00));

	private static final LocalDate DATA_BANCA = LocalDate.of(2018, 9, 20);
	private static final LocalDate DATA_ABERTURA_PROCESSO = LocalDate.of(2018, 8, 20);

	@Override
	public AgendamentoDeExame getEntidadeParaTeste() {

		return FabricaTeste.criaAgendamento(DATA_HORA_AGENDAMENTO, DATA_BANCA, DATA_ABERTURA_PROCESSO);

	}

	public FuncaoAlteraEntidade<String, AgendamentoDeExame> alteracaoEntidadeDeTeste() {
		return (agendamento) -> agendamento.setDataHora(DATA_HORA_AGENDAMENTO_ALTERACAO);
	}

	@Override
	public FuncaoValidaAlteracaoEntidade<String, AgendamentoDeExame> validaAlteracaoEntidadeDeTeste() {
		return (agendamento) -> assertTrue(
				concat("valor esperado <", DATA_HORA_AGENDAMENTO_ALTERACAO.toString(), "> : retornado <",
						agendamento.getDataHora().toString(), ">"),
				agendamento.getDataHora().equals(DATA_HORA_AGENDAMENTO_ALTERACAO));
	}

	@Override
	public FuncaoCriterioParaBuscaDeEntidade<String, AgendamentoDeExame> getCriterioBuscaEntidadesTeste() {
		return () -> Restrictions.eq(AgendamentoDeExame.Atributos.DATA_HORA, toDate(DATA_HORA_AGENDAMENTO));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void deveConsultarAgendamentoPelaDataDaBanca() {

		IntStream.range(0, 2).forEach(i -> FabricaTeste
				.criaAgendamento(DATA_HORA_AGENDAMENTO, DATA_BANCA, DATA_ABERTURA_PROCESSO).persiste());

		FabricaTeste.criaAgendamento(DATA_HORA_AGENDAMENTO, DATA_BANCA.minusDays(1), DATA_ABERTURA_PROCESSO).persiste();

		Criteria criteria = createCriteria(AgendamentoDeExame.class);
		criteria.createAlias(AgendamentoDeExame.Atributos.BANCA, AgendamentoDeExame.Atributos.BANCA);
		criteria.add(Restrictions.eq(getAlias(AgendamentoDeExame.Atributos.BANCA, BancaExaminadora.Atributos.DATA),
				toDate(DATA_BANCA)));

		List<AgendamentoDeExame> agendamentos = criteria.list();

		assertTrue("Devem ser retornados 2 agendamentos", agendamentos.size() == 2);

		agendamentos.forEach(agendamento -> assertFalse("Agendamento não pode estar no estado transient",
				agendamento.isTransient()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void deveConsultarAlunosAgendadosPorNome() {
		
		IntStream.range(0, 3).forEach(i -> FabricaTeste.criaAluno().setNome("Bernardo de Claravau").persiste());

		Criteria criteria = createCriteria(Aluno.class);
		criteria.add(Restrictions.ilike(Aluno.Atributos.NOME, "Bernardo", MatchMode.START));
		
		List<Aluno> alunos = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

		assertEquals(1, alunos.size());

		alunos.forEach(aluno -> assertFalse("Aluno não pode estar no estado transient",
				aluno.isTransient()));
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(AgendamentoDeExame.class);
		deveLimparBase(BancaExaminadora.class);
		deveLimparBase(Examinador.class);
		deveLimparBase(ProcessoHabilitacao.class);
		deveLimparBase(Veiculo.class);
		deveLimparBase(Instrutor.class);
		deveLimparBase(Aluno.class);
	}
}