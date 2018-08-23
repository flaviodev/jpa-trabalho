package br.edu.faculdadedelta.modelo.test;

import static br.edu.faculdadedelta.util.StringUtil.concat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.LazyInitializationException;
import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.Instrutor;
import br.edu.faculdadedelta.test.base.BaseCrudTest;
import br.edu.faculdadedelta.test.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.test.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.test.base.FuncaoValidaAlteracaoEntidade;
import br.edu.faculdadedelta.test.util.FabricaTeste;

public class InstrutorTest extends BaseCrudTest<String, Instrutor> {

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

		return (instrutor) -> assertTrue(
				concat("valor esperado <", NOME_ALTERACAO, "> : retornado <", instrutor.getNome(), ">"),
				instrutor.getNome().equals(NOME_ALTERACAO));
	}

	@Override
	public FuncaoCriterioParaBuscaDeEntidade<String, Instrutor> getCriterioBuscaEntidadesTeste() {

		return () -> Restrictions.eq(Instrutor.Atributos.CPF, CPF);
	}

	@Test(expected = LazyInitializationException.class)
	public void naoDeveAcessarAtributoLazyForaEscopoEntityManager() {

		Instrutor instrutor = getEntidadeParaTeste().persiste();
		fecharEntityManager();
		instanciarEntityManager();

		instrutor = getDao().find(Instrutor.class, instrutor.getId());

		assertNotNull("Verifica se encontrou um registro", instrutor);

		getDao().detach(instrutor);
		instrutor.getProcessos().size();

		fail("deveria lançar LazyInitializationException ao Acessar Atributo Lazy Fora do Escopo EntityManager");
	}

	@Test
	public void deveAcessarAtributoLazy() {

		Instrutor instrutor = getEntidadeParaTeste().persiste();
		instrutor = getDao().find(Instrutor.class, instrutor.getId());

		assertNotNull("Verifica se encontrou um registro", instrutor);
		assertNotNull("Lista lazy não deve ser nula", instrutor.getProcessos());
	}

	@Test
	public void deveVerificarExistenciaInstrutor() {

		deveSalvarEntidade();

		Criteria critera = createCriteria(Instrutor.class);
		critera.add(Restrictions.eq(Instrutor.Atributos.CPF, CPF));

		assertTrue("Verifica se há registros na lista", critera.list().size() > 0L);
	}

	private String montaHqlParaObterIdENomePeloCpf() {

		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(Instrutor.Atributos.ID);
		hql.append(',');
		hql.append(Instrutor.Atributos.NOME);
		hql.append(" FROM ");
		hql.append(Instrutor.class.getSimpleName());
		hql.append(" WHERE ");
		hql.append(Instrutor.Atributos.CPF);
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

			Instrutor instrutor = new Instrutor((String) linha[0], (String) linha[1]);

			assertNotNull("Verifica que o instrutor não deve estar nulo", instrutor);
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

			Instrutor instrutor = new Instrutor((String) linha[0], (String) linha[1]);

			assertNotNull("Verifica que o instrutor não deve estar nulo", instrutor);
		}
	}

	private String montaHqlParaObterEntidadeComIdENomePeloCpf() {

		StringBuilder hql = new StringBuilder("SELECT new ");
		hql.append(Instrutor.class.getSimpleName());
		hql.append('(');
		hql.append(Instrutor.Atributos.ID);
		hql.append(',');
		hql.append(Instrutor.Atributos.NOME);
		hql.append(')');
		hql.append(" FROM ");
		hql.append(Instrutor.class.getSimpleName());
		hql.append(" WHERE ");
		hql.append(Instrutor.Atributos.CPF);
		hql.append(" = :cpf ");

		return hql.toString();
	}

	@Test
	public void deveConsultarApenasIdNome() {

		deveSalvarEntidade();

		Query query = getDao().createQuery(montaHqlParaObterEntidadeComIdENomePeloCpf());
		query.setParameter("cpf", CPF);

		@SuppressWarnings("unchecked")
		List<Instrutor> instrutores = query.getResultList();

		assertFalse("Verifica se há registros na lista", instrutores.isEmpty());

		instrutores.forEach(instrutor -> assertNull("Verifica que o cpf deve estar nulo", instrutor.getCpf()));
	}

	@Test
	public void deveConsultarInstrutorComIdNome() {
		deveSalvarEntidade();

		Query query = getDao().createQuery(montaHqlParaObterEntidadeComIdENomePeloCpf());
		query.setParameter("cpf", CPF);

		@SuppressWarnings("unchecked")
		List<Instrutor> instrutores = query.getResultList();

		assertFalse("Verifica se há registros na lista", instrutores.isEmpty());

		instrutores.forEach(instrutor -> {
			assertNull("Verifica que o cpf deve estar nulo", instrutor.getCpf());
			instrutor.setCpf(CPF);
		});
	}

	@Test
	@SuppressWarnings("unchecked")
	public void deveConsultarCpf() {
		deveSalvarEntidade();

		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(Instrutor.Atributos.CPF);
		hql.append(" FROM ");
		hql.append(Instrutor.class.getSimpleName());
		hql.append(" WHERE ");
		hql.append(Instrutor.Atributos.NOME);
		hql.append(" LIKE :nome ");

		Query query = getDao().createQuery(hql.toString());
		query.setParameter("nome", concat("%", NOME_FILTRO, "%"));

		List<String> listaCpf = query.getResultList();

		assertFalse("Deve possuir itens", listaCpf.isEmpty());
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(Instrutor.class);
	}
}