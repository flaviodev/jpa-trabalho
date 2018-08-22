package br.edu.faculdadedelta.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.BaseEntity;
import br.edu.faculdadedelta.util.JPAUtil;

public abstract class BaseCrudTest<I extends Serializable, E extends BaseEntity<I>> extends BaseTest {

	private Class<E> tipoEntidade;

	@SuppressWarnings("unchecked")
	public BaseCrudTest() {

		this.tipoEntidade = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[1];

		if (tipoEntidade == null)
			throw new IllegalStateException("Não foi possíve obter o tipo (classe) da entidade");
	}

	public static <I extends Serializable, B extends BaseEntity<I>> void deveLimparBase(Class<B> classeEntidade) {

		EntityManager entityManager = JPAUtil.INSTANCE.getEntityManager();

		entityManager.getTransaction().begin();
		Query query = entityManager
				.createQuery(new StringBuilder("DELETE FROM ").append(classeEntidade.getSimpleName()).toString());
		query.executeUpdate();
		entityManager.getTransaction().commit();

		StringBuilder stringQuery = new StringBuilder("SELECT e FROM ");
		stringQuery.append(classeEntidade.getSimpleName());
		stringQuery.append(" e");

		query = entityManager.createQuery(stringQuery.toString());
		assertTrue(new StringBuilder("Tabela ").append(classeEntidade.getSimpleName())
				.append(" não deve possuir registros").toString(), query.getResultList().size() == 0);
	}

	@Test
	public void deveSalvarEntidade() {

		E entidade = getInstanciaDaEntidade();
		assertTrue(
				new StringBuilder(tipoEntidade.getSimpleName())
						.append(" deve estar no estado transient antes de ser pesistido").toString(),
				entidade.isTransient());

		salvaEntidade(entidade);
		assertFalse(
				new StringBuilder(tipoEntidade.getSimpleName())
						.append(" não deve no estado transient após ter sido persistido").toString(),
				entidade.isTransient());

		assertNotNull(new StringBuilder(tipoEntidade.getSimpleName())
				.append(" deve ter um id definido após ter sido persistido").toString(), entidade.getId());
	}

	public E salvaEntidade(E entidade) {

		em.getTransaction().begin();
		em.persist(entidade);
		em.getTransaction().commit();

		return entidade;
	}

	public E salvaEntidade() {
		return salvaEntidade(getInstanciaDaEntidade());
	}

	@Test(expected = NoResultException.class)
	public void naoDeveFuncionarSingleResultComNenhumRegistro() {

		deveSalvarEntidade();

		StringBuilder stringQuery = new StringBuilder("SELECT e FROM ");
		stringQuery.append(tipoEntidade.getSimpleName());
		stringQuery.append(" e WHERE e.id = :id");

		Query query = getEntityManager().createQuery(stringQuery.toString());
		query.setParameter("id", "0");
		query.getSingleResult();

		fail("metodo getSingleResult deve lançar exception NoResultException");
	}

	@Test(expected = HibernateException.class)
	public void naoDeveFuncionarUniqueResultComMaisDeUmRegistro() {

		IntStream.range(0, 2).forEach(i -> deveSalvarEntidade());

		Criteria critera = createCriteria(tipoEntidade);
		critera.uniqueResult();

		fail("metodo critera.uniqueResult() deve lançar exception HibernateException");
	}

	@Test(expected = NonUniqueResultException.class)
	public void naoDeveFuncionarSingleResultComMuitosRegistros() {

		IntStream.range(0, 2).forEach(i -> deveSalvarEntidade());

		StringBuilder stringQuery = new StringBuilder("SELECT e FROM ");
		stringQuery.append(tipoEntidade.getSimpleName());
		stringQuery.append(" e");

		Query query = getEntityManager().createQuery(stringQuery.toString());
		query.getSingleResult();

		fail("metodo getSingleResult deve lançar exception NonUniqueResultException");
	}

	@Test
	public void devePesquisarEntidades() {

		IntStream.range(0, 2).forEach(i -> deveSalvarEntidade());

		Criteria critera = createCriteria(tipoEntidade);
		@SuppressWarnings("unchecked")
		List<E> entidades = (List<E>) critera.list();

		assertFalse(new StringBuilder(tipoEntidade.getSimpleName()).append(" não deve estar vazia").toString(),
				entidades.isEmpty());
		assertTrue(new StringBuilder(tipoEntidade.getSimpleName()).append(" deve ter itens").toString(),
				entidades.size() > 0);
	}

	@Test
	public void deveExluirEntidade() {

		E entidade = salvaEntidade();
		assertFalse(
				new StringBuilder(tipoEntidade.getSimpleName())
						.append(" não deve estar no estado transient após ter sido pesistido").toString(),
				entidade.isTransient());

		I id = entidade.getId();
		entidade = getEntityManager().find(tipoEntidade, id);
		assertNotNull(new StringBuilder("Deve encontrar ").append(tipoEntidade.getSimpleName()).toString(), entidade);

		getEntityManager().getTransaction().begin();
		getEntityManager().remove(entidade);
		getEntityManager().getTransaction().commit();

		entidade = getEntityManager().find(tipoEntidade, id);

		assertNull(new StringBuilder("Não deve encontrar ").append(tipoEntidade.getSimpleName()).toString(), entidade);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void deveExluirTodasEntidade() {

		IntStream.range(0, 2).forEach(i -> deveSalvarEntidade());

		Criteria critera = createCriteria(tipoEntidade);
		List<E> entidades = (List<E>) critera.list();

		assertFalse(new StringBuilder("Tabela de ").append(tipoEntidade.getSimpleName()).append(" não deve estar vazia")
				.toString(), entidades.isEmpty());

		getEntityManager().getTransaction().begin();
		Query query = getEntityManager()
				.createQuery(new StringBuilder("DELETE FROM ").append(tipoEntidade.getSimpleName()).toString());
		query.executeUpdate();
		getEntityManager().getTransaction().commit();

		entidades = (List<E>) critera.list();

		assertTrue(new StringBuilder("Tabela de ").append(tipoEntidade.getSimpleName()).append(" deve estar vazia")
				.toString(), entidades.isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void deveAlterarEntidade() {

		FuncaoAlteraEntidade<I, E> funcaoAltera = alteracaoEntidade();

		if (funcaoAltera == null)
			throw new IllegalStateException("Não foi possível carregar a função de alteração da entidade");

		E entidade = salvaEntidade();
		assertFalse("Deve possuir id", entidade.isTransient());

		Criteria criteria = createCriteria(tipoEntidade);
		criteria.add(Restrictions.eq("id", entidade.getId()));

		entidade = (E) criteria.uniqueResult();

		assertNotNull(new StringBuilder("Deve ter encontrado ").append(tipoEntidade.getSimpleName()).toString(),
				entidade);

		Integer versao = entidade.getVersion();
		assertNotNull(new StringBuilder(tipoEntidade.getSimpleName()).append(" deve possuir versão").toString(),
				versao);

		getEntityManager().getTransaction().begin();

		funcaoAltera.altera(entidade);

		entidade = getEntityManager().merge(entidade);
		getEntityManager().getTransaction().commit();

		assertNotEquals(new StringBuilder("Versão ").append(tipoEntidade.getSimpleName()).append(" deve ser diferente")
				.toString(), versao.intValue(), entidade.getVersion().intValue());
	}

	@Test
	public void devePesquisarEntidadesPorCriterio() {

		FuncaoCriterioParaBuscaDeEntidade<I, E> funcaoCriterio = getCriterioBuscaEntidades();

		if (funcaoCriterio == null)
			throw new IllegalStateException(
					"Não foi possível carregar a função contendo o critério de busca da entidade");

		Criterion criterio = funcaoCriterio.getCriterio();

		if (criterio == null)
			throw new IllegalStateException("A função contendo o critério de busca da entidade não pode ser nula");

		IntStream.range(0, 2).forEach(i -> deveSalvarEntidade());

		Criteria critera = createCriteria(tipoEntidade);
		critera.add(criterio);

		assertTrue(new StringBuilder("Quantidade de itens retornada da busca de ").append(tipoEntidade.getSimpleName())
				.append(" deve ser maior que zero").toString(), critera.list().size() > 0L);
	}

	public abstract E getInstanciaDaEntidade();

	public abstract FuncaoAlteraEntidade<I, E> alteracaoEntidade();

	public abstract FuncaoCriterioParaBuscaDeEntidade<I, E> getCriterioBuscaEntidades();
}
