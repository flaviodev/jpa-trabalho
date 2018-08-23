package br.edu.faculdadedelta.modelo.test;

import static br.edu.faculdadedelta.util.DateUtil.toDate;
import static br.edu.faculdadedelta.util.StringUtil.concat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.Date;

import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.Aluno;
import br.edu.faculdadedelta.modelo.Instrutor;
import br.edu.faculdadedelta.modelo.ProcessoHabilitacao;
import br.edu.faculdadedelta.modelo.Veiculo;
import br.edu.faculdadedelta.test.base.BaseCrudTest;
import br.edu.faculdadedelta.test.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.test.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.test.base.FuncaoValidaAlteracaoEntidade;
import br.edu.faculdadedelta.tipo.Sexo;
import br.edu.faculdadedelta.tipo.StatusInstrutor;
import br.edu.faculdadedelta.tipo.TipoVeiculo;
import br.edu.faculdadedelta.util.DateUtil;

public class ProcessoHabilitacaoTest extends BaseCrudTest<String, ProcessoHabilitacao> {

	private static final LocalDate DATA_ABERTURA_PADRAO = LocalDate.of(2018, 8, 10);
	private static final Date DATA_ABERTURA_ALTERACAO = DateUtil.toDate(LocalDate.of(2018, 8, 15));

	@Override
	public ProcessoHabilitacao getEntidadeParaTeste() {

		return criaProcessoDeHabilitacao();
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
		return () -> Restrictions.eq(ProcessoHabilitacao.Atributos.DATA_ABERTURA, toDate(DATA_ABERTURA_PADRAO));
	}

	private ProcessoHabilitacao criaProcessoDeHabilitacao() {
		getDao().getTransaction().begin();

		Veiculo veiculo = new Veiculo("Honda").setModelo("GC 125 Fan").setCor("Vermelha")
				.setPlaca("CCC-3333").setTipo(TipoVeiculo.MOTO).setAno(2016);
		getDao().persist(veiculo);

		Instrutor instrutor = new Instrutor("João da Silveira").setCpf("444.444.444-44")
				.setStatus(StatusInstrutor.ATIVO);
		getDao().persist(instrutor);
		getDao().getTransaction().commit();

		Aluno aluno = new Aluno("Sebastião Antônio").setCpf("333.333.333-333")
				.setDataNascimento(LocalDate.of(1964, 2, 12)).setSexo(Sexo.MASCULINO);

		return new ProcessoHabilitacao(DATA_ABERTURA_PADRAO).setAluno(aluno).setVeiculo(veiculo)
				.setInstrutor(instrutor);
	}

	@Test
	public void deveSalvarAlunoAoIncluirProcesso() {
		ProcessoHabilitacao processo = criaProcessoDeHabilitacao();

		assertNotNull("Processo de Habilitação não pode ser nulo", processo);
		assertTrue("Aluno deve estar no estado transient antes de ser persistido", processo.getAluno().isTransient());

		processo.persiste();

		assertFalse("Processo não deve estar no estado transient após ter sido persistido", processo.isTransient());
		assertFalse("Aluno não deve estar no estado transient após ter sido persistido",
				processo.getAluno().isTransient());
	}

	@Test(expected = IllegalStateException.class)
	public void naoDeveSalvarInstrutorAoIncluirProcesso() {
		ProcessoHabilitacao processo = criaProcessoDeHabilitacao();

		assertNotNull("Processo de Habilitação não pode ser nulo", processo);

		Instrutor novoInstrutor = new Instrutor("Inácio de Coimbra").setCpf("555.555.555-55")
				.setStatus(StatusInstrutor.ATIVO);

		processo.setInstrutor(novoInstrutor);

		assertTrue("Instrutor deve estar no estado transient antes de ser persistido",
				processo.getInstrutor().isTransient());

		getDao().getTransaction().begin();
		getDao().persist(processo);
		getDao().getTransaction().commit();

		fail("IllegalStateException deveria ser lançada pois não há persist cascade para Instrutor");
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(ProcessoHabilitacao.class);
		deveLimparBase(Aluno.class);
		deveLimparBase(Veiculo.class);
		deveLimparBase(Instrutor.class);
	}
}