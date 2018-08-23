package br.edu.faculdadedelta.modelo.test;

import static br.edu.faculdadedelta.util.DateUtil.toDate;
import static br.edu.faculdadedelta.util.StringUtil.concat;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Date;

import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;

import br.edu.faculdadedelta.modelo.Aluno;
import br.edu.faculdadedelta.modelo.Instrutor;
import br.edu.faculdadedelta.modelo.ProcessoHabilitacao;
import br.edu.faculdadedelta.modelo.Veiculo;
import br.edu.faculdadedelta.test.base.BaseCrudTest;
import br.edu.faculdadedelta.test.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.test.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.test.base.FuncaoValidaAlteracaoEntidade;
import br.edu.faculdadedelta.test.util.FabricaTeste;
import br.edu.faculdadedelta.util.DateUtil;

public class ProcessoHabilitacaoTest extends BaseCrudTest<String, ProcessoHabilitacao> {

	private static final LocalDate DATA_ABERTURA = LocalDate.of(2018, 8, 10);
	private static final Date DATA_ABERTURA_ALTERACAO = DateUtil.toDate(LocalDate.of(2018, 8, 15));

	@Override
	public ProcessoHabilitacao getEntidadeParaTeste() {

		return FabricaTeste.criaProcesso(DATA_ABERTURA);
	}

	public FuncaoAlteraEntidade<String, ProcessoHabilitacao> alteracaoEntidadeDeTeste() {

		return (processo) -> processo.setDataAbertura(DATA_ABERTURA_ALTERACAO);
	}

	@Override
	public FuncaoValidaAlteracaoEntidade<String, ProcessoHabilitacao> validaAlteracaoEntidadeDeTeste() {

		return (processo) -> assertTrue(
				concat("valor esperado <", DATA_ABERTURA_ALTERACAO.toString(), "> : retornado <",
						processo.getDataAbertura().toString(), ">"),
				processo.getDataAbertura().equals(DATA_ABERTURA_ALTERACAO));
	}

	@Override
	public FuncaoCriterioParaBuscaDeEntidade<String, ProcessoHabilitacao> getCriterioBuscaEntidadesTeste() {
		return () -> Restrictions.eq(ProcessoHabilitacao.Atributos.DATA_ABERTURA, toDate(DATA_ABERTURA));
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(ProcessoHabilitacao.class);
		deveLimparBase(Aluno.class);
		deveLimparBase(Veiculo.class);
		deveLimparBase(Instrutor.class);
	}
}