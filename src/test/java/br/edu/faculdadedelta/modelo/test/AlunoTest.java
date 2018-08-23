package br.edu.faculdadedelta.modelo.test;

import static br.edu.faculdadedelta.util.StringUtil.concat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.LazyInitializationException;
import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.Aluno;
import br.edu.faculdadedelta.test.base.BaseCrudTest;
import br.edu.faculdadedelta.test.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.test.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.test.base.FuncaoValidaAlteracaoEntidade;
import br.edu.faculdadedelta.test.util.FabricaTeste;

public class AlunoTest extends BaseCrudTest<String, Aluno> {

	private static final String CPF = "111.111.111-11";
	private static final String NOME = "Joaquim Bragança";
	private static final String NOME_ALTERACAO = "Joaquim Albuquerque";
	private static final String NOME_FILTRO = "Joaquim";

	@Override
	public Aluno getEntidadeParaTeste() {

		return FabricaTeste.criaAluno(NOME, CPF);
	}

	@Override
	public FuncaoAlteraEntidade<String, Aluno> alteracaoEntidadeDeTeste() {
		return (aluno) -> aluno.setNome(NOME_ALTERACAO);
	}

	@Override
	public FuncaoValidaAlteracaoEntidade<String, Aluno> validaAlteracaoEntidadeDeTeste() {
		return (aluno) -> assertTrue(
				concat("valor esperado <", NOME_ALTERACAO, "> : retornado <", aluno.getNome(), ">"),
				aluno.getNome().equals(NOME_ALTERACAO));
	}

	@Override
	public FuncaoCriterioParaBuscaDeEntidade<String, Aluno> getCriterioBuscaEntidadesTeste() {
		return () -> Restrictions.eq(Aluno.Atributos.CPF, CPF);
	}

	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComDataDeNascimentoNula() {

		Aluno aluno = getEntidadeParaTeste();
		aluno.setDataNascimento((Date) null);
		aluno.persiste();

		fail("deveria disparar PersistenceException porque o campo dataDeNascimento é optional = false");
	}

	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComDataDeNascimentoMaiorQueDataCorrente() {

		Aluno aluno = getEntidadeParaTeste();
		aluno.setDataNascimento(LocalDate.now().plusDays(1));
		aluno.persiste();

		fail("deveria disparar IllegalStateException no validaDados");
	}

	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirAlunoMenoDeDezesseteAnos() {

		Aluno aluno = getEntidadeParaTeste();
		aluno.setDataNascimento(LocalDate.of(2010, 1, 1));
		aluno.persiste();

		fail("deveria disparar IllegalStateException no validaDados");
	}

	@Test(expected = PersistenceException.class)
	public void naoDevePersistirComSexoNulo() {

		Aluno aluno = getEntidadeParaTeste();
		aluno.setSexo(null);
		aluno.persiste();

		fail("deveria disparar PersistenceException porque o campo sexo é optional = false");
	}

	@Test(expected = LazyInitializationException.class)
	public void naoDeveAcessarAtributoLazyForaEscopoEntityManager() {

		Aluno aluno = getEntidadeParaTeste().persiste();
		fecharEntityManager();
		instanciarEntityManager();

		aluno = getDao().find(Aluno.class, aluno.getId());

		assertNotNull("Verifica se encontrou um registro", aluno);

		getDao().detach(aluno);
		aluno.getProcessos().size();

		fail("deveria lançar LazyInitializationException ao Acessar Atributo Lazy Fora do Escopo EntityManager");
	}

	@Test
	public void deveAcessarAtributoLazy() {

		Aluno aluno = getEntidadeParaTeste().persiste();
		aluno = getDao().find(Aluno.class, aluno.getId());

		assertNotNull("Verifica se encontrou um registro", aluno);
		assertNotNull("Lista lazy não deve ser nula", aluno.getProcessos());
	}

	private String montaHqlParaObterIdENomePeloCpf() {

		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(Aluno.Atributos.ID);
		hql.append(',');
		hql.append(Aluno.Atributos.NOME);
		hql.append(" FROM ");
		hql.append(Aluno.class.getSimpleName());
		hql.append(" WHERE ");
		hql.append(Aluno.Atributos.CPF);
		hql.append(" = :cpf ");

		return hql.toString();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void deveConsultarIdNomeForeach() {
		deveSalvarEntidade();

		Query query = getDao().createQuery(montaHqlParaObterIdENomePeloCpf());
		query.setParameter("cpf", CPF);

		List<Object[]> resultado = query.getResultList();

		assertFalse("Verifica se há registros na lista", resultado.isEmpty());

		resultado.forEach(linha -> {
			assertTrue("Verifica que o id deve estar nulo", linha[0] instanceof String);
			assertTrue("Verifica que o cpf deve estar nulo", linha[1] instanceof String);

			Aluno aluno = new Aluno((String) linha[0], (String) linha[1]);

			assertNotNull("Verifica que o aluno não deve estar nulo", aluno);
		});
	}

	@Test
	@SuppressWarnings("unchecked")
	public void deveConsultarIdNome() {
		deveSalvarEntidade();

		Query query = getDao().createQuery(montaHqlParaObterIdENomePeloCpf());
		query.setParameter("cpf", CPF);

		List<Object[]> resultado = query.getResultList();

		assertFalse("Verifica se há registros na lista", resultado.isEmpty());

		for (Object[] linha : resultado) {
			assertTrue("Verifica que o id deve estar nulo", linha[0] instanceof String);
			assertTrue("Verifica que o cpf deve estar nulo", linha[1] instanceof String);

			Aluno aluno = new Aluno((String) linha[0], (String) linha[1]);

			assertNotNull("Verifica que o aluno não deve estar nulo", aluno);
		}
	}

	private String montaHqlParaObterEntidadeComIdENomePeloCpf() {

		StringBuilder hql = new StringBuilder("SELECT new ");
		hql.append(Aluno.class.getSimpleName());
		hql.append('(');
		hql.append(Aluno.Atributos.ID);
		hql.append(',');
		hql.append(Aluno.Atributos.NOME);
		hql.append(')');
		hql.append(" FROM ");
		hql.append(Aluno.class.getSimpleName());
		hql.append(" WHERE ");
		hql.append(Aluno.Atributos.CPF);
		hql.append(" = :cpf ");

		return hql.toString();
	}

	@Test
	public void deveConsultarApenasIdNome() {

		deveSalvarEntidade();

		Query query = getDao().createQuery(montaHqlParaObterEntidadeComIdENomePeloCpf());
		query.setParameter("cpf", CPF);

		@SuppressWarnings("unchecked")
		List<Aluno> alunos = query.getResultList();

		assertFalse("Verifica se há registros na lista", alunos.isEmpty());

		alunos.forEach(aluno -> assertNull("Verifica que o cpf deve estar nulo", aluno.getCpf()));
	}

	@Test
	public void deveConsultarAlunoComIdNome() {
		deveSalvarEntidade();

		Query query = getDao().createQuery(montaHqlParaObterEntidadeComIdENomePeloCpf());
		query.setParameter("cpf", CPF);

		@SuppressWarnings("unchecked")
		List<Aluno> alunos = query.getResultList();

		assertFalse("Verifica se há registros na lista", alunos.isEmpty());

		alunos.forEach(aluno -> {
			assertNull("Verifica que o cpf deve estar nulo", aluno.getCpf());
			aluno.setCpf(CPF);
		});
	}

	@Test
	@SuppressWarnings("unchecked")
	public void deveConsultarCpf() {
		deveSalvarEntidade();

		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(Aluno.Atributos.CPF);
		hql.append(" FROM ");
		hql.append(Aluno.class.getSimpleName());
		hql.append(" WHERE ");
		hql.append(Aluno.Atributos.NOME);
		hql.append(" LIKE :nome ");

		Query query = getDao().createQuery(hql.toString());
		query.setParameter("nome", concat("%", NOME_FILTRO, "%"));

		List<String> listaCpf = query.getResultList();

		assertFalse("Deve possuir itens", listaCpf.isEmpty());
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(Aluno.class);
	}
}