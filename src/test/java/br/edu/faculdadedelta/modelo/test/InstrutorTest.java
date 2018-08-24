package br.edu.faculdadedelta.modelo.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.hibernate.LazyInitializationException;
import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.Aluno;
import br.edu.faculdadedelta.modelo.Instrutor;
import br.edu.faculdadedelta.test.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.test.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.test.base.FuncaoValidaAlteracaoEntidade;
import br.edu.faculdadedelta.test.util.FabricaTeste;
import br.edu.faculdadedelta.tipo.StatusInstrutor;

public class InstrutorTest extends BasePessoaCrudTest<String, Instrutor> {

	private static final String CPF = "222.222.222-22";
	private static final String NOME = "José De Almeida Prado";
	private static final String NOME_ALTERACAO = "José de Arimatéia";
	private static final String NOME_FILTRO = "José";

	@Override
	public Instrutor getEntidadeParaTeste() {

		return FabricaTeste.criaInstrutor(NOME, CPF);
	}

	@Override
	public FuncaoAlteraEntidade<String, Instrutor> alteracaoEntidadeDeTeste() {

		return (instrutor) -> instrutor.setNome(NOME_ALTERACAO);
	}

	@Override
	public FuncaoValidaAlteracaoEntidade<String, Instrutor> validaAlteracaoEntidadeDeTeste() {

		return (instrutor) -> assertEquals(NOME_ALTERACAO, instrutor.getNome());
	}

	@Override
	public FuncaoCriterioParaBuscaDeEntidade<String, Instrutor> getCriterioBuscaEntidadesTeste() {

		return () -> Restrictions.eq(Instrutor.Atributos.CPF, CPF);
	}

	/**
	 * Teste tentar acessar alunos fora do escopo do EntityManager
	 */
	@Test(expected = LazyInitializationException.class)
	public void naoDeveAcessarAtributoLazyForaEscopoEntityManager() {

		Instrutor instrutor = getEntidadeParaTeste().persiste();
		fecharEntityManager();
		instanciarEntityManager();

		instrutor = getEntittyManager().find(Instrutor.class, instrutor.getId());

		assertNotNull("Retorno da busca do instrutor não pode ser nulo", instrutor);

		getEntittyManager().detach(instrutor);
		instrutor.getAlunos().size();

		fail("devia ter lançado LazyInitializationException ao tentar acessar alunos (lazy) fora do escopo do EntityManager");
	}

	/**
	 * Teste acessar alunos dentro do escopo do EntityManager
	 */
	@Test
	public void deveAcessarAtributoLazy() {

		Instrutor instrutor = getEntidadeParaTeste().persiste();
		instrutor = getEntittyManager().find(Instrutor.class, instrutor.getId());

		assertNotNull("Retorno da busca do instrutor não pode ser nulo", instrutor);
		assertNotNull("Lista de alunos (lazy) não pode ser nula", instrutor.getAlunos());
	}

	/**
	 * Teste consulta atributos do instrutor
	 */
	@Test
	public void deveConsultarIdENomeDosInstrutoresPeloCpf() {

		deveConsultarIdENomeDasPessoasPeloCpf(Instrutor.class, CPF);
	}

	/**
	 * Teste consulta objetos com apenas alguns atributos do instrutor
	 */
	@Test
	public void deveConsultarObjetosApenasComIdENomeDoInstrutorPeloCpf() {

		deveConsultarObjetosApenasComIdENomeDaPessoaPeloCpf(Instrutor.class, CPF);
	}

	/**
	 * Teste consulta instrutores pelo nome
	 */
	@Test
	public void deveConsultarInstrutoresPeloNome() {

		deveConsultarPessoasPeloNome(Instrutor.class, NOME_FILTRO);
	}

	/**
	 * Teste validação de status nulo
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComStatusNulo() {

		Instrutor instrutor = getEntidadeParaTeste();
		instrutor.setStatus(null);
		instrutor.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir instrutor com status null");
	}

	/**
	 * Teste validação de status inativo na inclusao
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComStatusInativo() {

		Instrutor instrutor = getEntidadeParaTeste();
		instrutor.setStatus(StatusInstrutor.INATIVO);
		instrutor.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir instrutor com status inativo");
	}

	/**
	 * Teste validação alteracao de status
	 */
	@Test
	public void deveAlterarParaStatusInativo() {

		Instrutor instrutor = getEntidadeParaTeste();
		instrutor.setStatus(StatusInstrutor.ATIVO);
		instrutor.persiste();
		assertFalse("Instrutor não deve estar no estado transient após ter sido persistido", instrutor.isTransient());

		int versao = instrutor.getVersion();
		assertNotNull("Instrutor deve possuir versão", versao);

		instrutor.setStatus(StatusInstrutor.INATIVO);
		instrutor = instrutor.altera();
		assertTrue("Instrutor deve estar com status inativo", instrutor.getStatus() == StatusInstrutor.INATIVO);
		assertNotEquals(instrutor.getVersion().intValue(), versao);
	}

	/**
	 * Teste validação adicionar aluno nulo
	 */
	@Test(expected = IllegalArgumentException.class)
	public void naoDeveAdicionarAlunoNulo() {

		Instrutor instrutor = getEntidadeParaTeste();
		instrutor.adicionaAluno(null);

		fail("devia ter lançado IllegalArgumentException ao tentar adicionar aluno nulo ao instrutor");
	}

	/**
	 * Teste validação adicionar aluno não persistido
	 */
	@Test(expected = IllegalArgumentException.class)
	public void naoDeveAdicionarAlunoNaoPersistido() {

		Instrutor instrutor = getEntidadeParaTeste();
		instrutor.adicionaAluno(FabricaTeste.criaAluno());

		fail("devia ter lançado IllegalArgumentException ao tentar adicionar ao instrutor aluno não persistido");
	}

	/**
	 * Teste validação adicionar aluno já pertencente
	 */
	@Test(expected = IllegalArgumentException.class)
	public void naoDeveAdicionarAlunoJaPertencenteAoInstrutor() {

		Instrutor instrutor = getEntidadeParaTeste();
		Aluno aluno = FabricaTeste.criaAluno().persiste();
		instrutor.adicionaAluno(aluno);
		instrutor.adicionaAluno(aluno);

		fail("devia ter lançado IllegalArgumentException ao tentar adicionar aluno já pertencente ao instrutor");
	}

	/**
	 * Teste validação retirar aluno nulo
	 */
	@Test(expected = IllegalArgumentException.class)
	public void naoDeveRetirarAlunoNulo() {

		Instrutor instrutor = getEntidadeParaTeste();
		instrutor.retiraAluno(null);

		fail("devia ter lançado IllegalArgumentException ao tentar retirar aluno nulo ao instrutor");
	}

	/**
	 * Teste validação retirar aluno não persistido
	 */
	@Test(expected = IllegalArgumentException.class)
	public void naoDeveRetirarAlunoNaoPersistido() {

		Instrutor instrutor = getEntidadeParaTeste();
		instrutor.retiraAluno(FabricaTeste.criaAluno());

		fail("devia ter lançado IllegalArgumentException ao tentar retirar do instrutor aluno não persistido");
	}

	/**
	 * Teste validação retirar aluno não pertencente
	 */
	@Test(expected = IllegalArgumentException.class)
	public void naoDeveRetirarAlunoNaoPertencenteAoInstrutor() {

		Instrutor instrutor = getEntidadeParaTeste();
		Aluno veiculo = FabricaTeste.criaAluno().persiste();
		instrutor.retiraAluno(veiculo);

		fail("devia ter lançado IllegalArgumentException ao tentar retirar aluno não pertencente ao instrutor");
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(Instrutor.class);
	}
}