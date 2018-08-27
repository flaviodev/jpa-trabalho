package br.edu.faculdadedelta.modelo.test;

import static br.edu.faculdadedelta.util.DateUtil.toDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.PersistenceException;

import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.Aluno;
import br.edu.faculdadedelta.test.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.test.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.test.base.FuncaoValidaAlteracaoEntidade;
import br.edu.faculdadedelta.test.util.FabricaTeste;
import br.edu.faculdadedelta.tipo.Sexo;

public class AlunoTest extends BasePessoaCrudTest<String, Aluno> {

	private static final String CPF = "111.111.111-11";
	private static final String NOME = "Joaquim Bragança";
	private static final LocalDate DATA_NASCIMENTO = LocalDate.of(1982, 10, 14);
	private static final Sexo SEXO = Sexo.MASCULINO;
	private static final String NOME_ALTERACAO = "Joaquim Albuquerque";
	private static final String NOME_FILTRO = "Joaquim";

	@Override
	public Aluno getEntidadeParaTeste() {

		return FabricaTeste.criaAluno(NOME, CPF).setDataNascimento(toDate(DATA_NASCIMENTO)).setSexo(SEXO);
	}

	@Override
	public FuncaoAlteraEntidade<String, Aluno> alteracaoEntidadeDeTeste() {
		
		return (aluno) -> aluno.setNome(NOME_ALTERACAO);
	}

	@Override
	public FuncaoValidaAlteracaoEntidade<String, Aluno> validaAlteracaoEntidadeDeTeste() {

		return (aluno) -> { 
			assertEquals(NOME_ALTERACAO, aluno.getNome()); 
			assertEquals(CPF, aluno.getCpf());
			assertEquals(toDate(DATA_NASCIMENTO), aluno.getDataNascimento());
			assertEquals(SEXO, aluno.getSexo());
		};
	}

	@Override
	public FuncaoCriterioParaBuscaDeEntidade<String, Aluno> getCriterioBuscaEntidadesTeste() {
		
		return () -> Restrictions.eq(Aluno.Atributos.CPF, CPF);
	}

	/**
	 * Teste validação de data de nascimento nula
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComDataDeNascimentoNula() {

		Aluno aluno = getEntidadeParaTeste();
		aluno.setDataNascimento((Date) null);
		aluno.persiste();
		
		fail("devia ter lançado IllegalStateException ao tentar persistir aluno com data de nascimento null");
	}

	/**
	 * Teste validação de data de nascimento maior que data correte
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComDataDeNascimentoMaiorQueDataCorrente() {

		Aluno aluno = getEntidadeParaTeste();
		aluno.setDataNascimento(LocalDate.now().plusDays(1));
		aluno.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir aluno com data de nascimento mair que a data corrente");
	}

	/**
	 * Teste validação de aluno com menos de 17 anos
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirAlunoMenoDeDezesseteAnos() {

		Aluno aluno = getEntidadeParaTeste();
		aluno.setDataNascimento(LocalDate.now().minusYears(15));
		aluno.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir aluno com 15 anos");
	}

	/**
	 * Teste validação de sexo nulo
	 */
	@Test(expected = PersistenceException.class)
	public void naoDevePersistirComSexoNulo() {

		Aluno aluno = getEntidadeParaTeste();
		aluno.setSexo(null);
		aluno.persiste();

		fail("devia ter lançado PersistenceException ao tentar persistir aluno com sexo nulo");
	}

	/**
	 * Teste consulta atributos do aluno
	 */
	@Test
	public void deveConsultarIdENomeDosAlunosPeloCpf() {

		deveConsultarIdENomeDasPessoasPeloCpf(Aluno.class, CPF);
	}
	
	/**
	 * Teste consulta objetos com apenas alguns atributos do aluno
	 */
	@Test
	public void deveConsultarObjetosComApenasIdeNomeDoAlunoPeloCpf() {

		deveConsultarObjetosApenasComIdENomeDaPessoaPeloCpf(Aluno.class, CPF);
	}

	/**
	 * Teste consulta alunos pelo nome
	 */
	@Test
	public void deveConsultarAlunosPeloNome() {

		deveConsultarPessoasPeloNome(Aluno.class, NOME_FILTRO);
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(Aluno.class);
	}
}