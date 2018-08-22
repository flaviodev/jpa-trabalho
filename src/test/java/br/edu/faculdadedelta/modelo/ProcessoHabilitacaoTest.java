package br.edu.faculdadedelta.modelo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.Test;

import br.edu.faculdadedelta.base.BaseCrudTest;
import br.edu.faculdadedelta.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.tipo.Sexo;
import br.edu.faculdadedelta.tipo.StatusInstrutor;
import br.edu.faculdadedelta.tipo.TipoVeiculo;

public class ProcessoHabilitacaoTest extends BaseCrudTest<String, ProcessoHabilitacao> {

	private static final LocalDate DATA_ABERTURA_PADARO = LocalDate.of(2018, 8, 10);

	@Override
	public ProcessoHabilitacao getInstanciaDaEntidade() {

		return criaProcessoDeHabilitacao();
	}

	public FuncaoAlteraEntidade<String, ProcessoHabilitacao> alteracaoEntidade() {
		return (processo) -> processo.setDataAbertura(LocalDate.of(2018, 8, 15));
	}

	@Override
	public FuncaoCriterioParaBuscaDeEntidade<String, ProcessoHabilitacao> getCriterioBuscaEntidades() {
		return () -> Restrictions.eq(ProcessoHabilitacao.Atributos.DATA_ABERTURA,
				Date.from(DATA_ABERTURA_PADARO.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}

	
	private ProcessoHabilitacao criaProcessoDeHabilitacao() {
		getEntityManager().getTransaction().begin();

		Veiculo veiculo = new Veiculo("Honda").setModelo("GC 125 Fan").setAno(2016).setCor("Vermelha")
				.setPlaca("CCC-3333").setTipo(TipoVeiculo.MOTO);
		getEntityManager().persist(veiculo);

		Instrutor instrutor = new Instrutor("João da Silveira").setCpf("444.444.444-44")
				.setStatus(StatusInstrutor.ATIVO);
		getEntityManager().persist(instrutor);
		getEntityManager().getTransaction().commit();

		Aluno aluno = new Aluno("Sebastião Antônio").setCpf("333.333.333-333")
				.setDataNascimento(LocalDate.of(1964, 2, 12)).setSexo(Sexo.MASCULINO);

		return new ProcessoHabilitacao(DATA_ABERTURA_PADARO).setAluno(aluno).setVeiculo(veiculo)
				.setInstrutor(instrutor);
	}
	
	@Test
	public void deveSalvarAlunoAoIncluirProcesso() {
		ProcessoHabilitacao processo = criaProcessoDeHabilitacao();
		
		assertNotNull("Processo de Habilitação não pode ser nulo", processo);
		assertTrue("Aluno deve estar no estado transient antes de ser persistido", processo.getAluno().isTransient());
		
		getEntityManager().getTransaction().begin();
		getEntityManager().persist(processo);
		getEntityManager().getTransaction().commit();
		
		assertFalse("Processo não deve estar no estado transient após ter sido persistido", processo.isTransient());
		assertFalse("Aluno não deve estar no estado transient após ter sido persistido", processo.getAluno().isTransient());
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

		getEntityManager().getTransaction().begin();
		getEntityManager().persist(processo);
		getEntityManager().getTransaction().commit();

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