package br.edu.faculdadedelta.modelo.test;

import static br.edu.faculdadedelta.util.DateUtil.toDate;
import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;

import br.edu.faculdadedelta.modelo.Agendamento;
import br.edu.faculdadedelta.modelo.Aluno;
import br.edu.faculdadedelta.modelo.Instrutor;
import br.edu.faculdadedelta.modelo.Veiculo;
import br.edu.faculdadedelta.test.base.BaseCrudTest;
import br.edu.faculdadedelta.test.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.test.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.test.base.FuncaoValidaAlteracaoEntidade;
import br.edu.faculdadedelta.test.util.FabricaTeste;

public class AgendamentoTest extends BaseCrudTest<String, Agendamento> {

	private static final LocalDateTime DATA_HORA_PROVA = LocalDateTime.of(2018, 9, 20, 10, 00);
	private static final Date DATA_HORA_PROVA_ALTERACAO = toDate(LocalDateTime.of(2018, 9, 20, 11, 00));

	@Override
	public Agendamento getEntidadeParaTeste() {

		return FabricaTeste.criaAgendamento(DATA_HORA_PROVA);

	}

	public FuncaoAlteraEntidade<String, Agendamento> alteracaoEntidadeDeTeste() {

		return (agendamento) -> agendamento.setDataHoraProva(DATA_HORA_PROVA_ALTERACAO);
	}

	@Override
	public FuncaoValidaAlteracaoEntidade<String, Agendamento> validaAlteracaoEntidadeDeTeste() {

		return (agendamento) -> assertEquals(DATA_HORA_PROVA_ALTERACAO, agendamento.getDataHoraProva());
	}

	@Override
	public FuncaoCriterioParaBuscaDeEntidade<String, Agendamento> getCriterioBuscaEntidadesTeste() {
		return () -> Restrictions.eq(Agendamento.Atributos.DATA_HORA_PROVA, toDate(DATA_HORA_PROVA));
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(Agendamento.class);
		deveLimparBase(Veiculo.class);
		deveLimparBase(Instrutor.class);
		deveLimparBase(Aluno.class);
	}
}