package br.edu.faculdadedelta.test.base;

import static br.edu.faculdadedelta.util.StringUtil.concat;
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

import br.edu.faculdadedelta.modelo.base.BaseEntity;
import br.edu.faculdadedelta.modelo.base.JPAUtil;

/**
 * @author Flávio de Souza
 *
 * @param <I>
 *            tipo de chave (id) da entidade a ser testada
 * @param <E>
 *            tipo de entidade a ser testada
 * 
 *            Classe responsável por testar as operações CRUD comuns a todas as
 *            entiades (@Entity)
 */
public abstract class BaseCrudTest<I extends Serializable, E extends BaseEntity<I>> extends BaseTest {

	private Class<E> tipoEntidade;
	private String nomeEntidade;

	@SuppressWarnings("unchecked")
	public BaseCrudTest() {

		this.tipoEntidade = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[1];

		if (tipoEntidade == null)
			throw new IllegalStateException("Não foi possíve obter o tipo (classe) da entidade");

		nomeEntidade = tipoEntidade.getSimpleName();
	}

	public static <I extends Serializable, B extends BaseEntity<I>> void deveLimparBase(Class<B> classeEntidade) {

		EntityManager entityManager = JPAUtil.INSTANCE.getEntityManager();
		final String nomeEntidade = classeEntidade.getSimpleName();

		entityManager.getTransaction().begin();
		Query query = entityManager.createQuery(concat("DELETE FROM ", nomeEntidade));
		query.executeUpdate();
		entityManager.getTransaction().commit();

		query = entityManager.createQuery(concat("SELECT e FROM ", nomeEntidade, " e"));
		assertTrue(concat(nomeEntidade, " não deve possuir registros"), query.getResultList().isEmpty());
	}

	/**
	 * Teste genérico de inclusão de uma entidade
	 */
	@Test
	public void devePersistirEntidade() {

		E entidade = getEntidadeParaTeste();
		assertTrue(concat(nomeEntidade, " deve estar no estado transient antes de ser pesistido"),
				entidade.isTransient());

		entidade.persiste();
		assertFalse(concat(nomeEntidade, " não deve estar no estado transient após ter sido persistido"),
				entidade.isTransient());

		assertNotNull(concat(nomeEntidade, " deve ter um id definido após ter sido persistido"), entidade.getId());
	}

	/**
	 * Teste genérico de alteração de uma entidade
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void deveAlterarEntidade() {

		FuncaoAlteraEntidade<I, E> funcaoAltera = alteracaoEntidadeDeTeste();

		if (funcaoAltera == null)
			throw new IllegalStateException("Não foi possível carregar a função de alteração da entidade");

		E entidade = getEntidadeParaTeste().persiste();
		assertFalse(concat(nomeEntidade, " não deve estar no estado transient após ter sido persistido"),
				entidade.isTransient());

		Criteria criteria = createCriteria(tipoEntidade);
		criteria.add(Restrictions.eq("id", entidade.getId()));

		entidade = (E) criteria.uniqueResult();
		assertNotNull(concat("Retorno da busca de ", nomeEntidade, " não pode ser nulo"), entidade);

		int versao = entidade.getVersion();
		assertNotNull(concat(nomeEntidade, " deve possuir versão"), versao);

		funcaoAltera.altera(entidade);
		entidade = entidade.altera();
		assertNotEquals(entidade.getVersion().intValue(), versao);
		
		criteria = createCriteria(tipoEntidade);
		criteria.add(Restrictions.eq("id", entidade.getId()));

		entidade = (E) criteria.uniqueResult();
		assertNotNull(concat("Retorno da busca de ", nomeEntidade, " não pode ser nulo"), entidade);

		validaAlteracaoEntidadeDeTeste().validaAlteracao(entidade);

	}

	/**
	 * Teste genérico de exclusão de uma entidade
	 */
	@Test
	public void deveExluirEntidade() {

		E entidade = getEntidadeParaTeste().persiste();
		assertFalse(concat(nomeEntidade, " não deve estar no estado transient após ter sido persistido"),
				entidade.isTransient());

		EntityManager entityManager = getEntittyManager();

		I id = entidade.getId();
		entidade = entityManager.find(tipoEntidade, id);
		assertNotNull(concat("Retorno da busca de ", nomeEntidade, " não pode ser nulo"), entidade);

		entidade.exclui();

		entityManager.clear();
		entidade = entityManager.find(tipoEntidade, id);

		assertNull(concat("Retorno da busca de ", nomeEntidade, " deve ser nulo"), entidade);
	}

	/**
	 * Teste genérico de exclusão de todos os registros de uma entidade
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void deveExluirTodosRegistrosDaEntidade() {

		IntStream.range(0, 2).forEach(i -> devePersistirEntidade());
		Criteria critera = createCriteria(tipoEntidade);
		List<E> entidades = (List<E>) critera.list();

		assertFalse(concat(nomeEntidade, " deve possuir registros"), entidades.isEmpty());

		EntityManager entityManager = getEntittyManager();

		entityManager.getTransaction().begin();
		Query query = entityManager.createQuery(concat("DELETE FROM ", nomeEntidade));
		query.executeUpdate();
		entityManager.getTransaction().commit();

		entidades = (List<E>) critera.list();
		assertTrue(concat(nomeEntidade, " não deve possuir registros"), entidades.isEmpty());
	}

	/**
	 * Teste genérico para consultar todos os registros de uma entidade
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void deveRetornaUmOuMaisRegistrosDaEntidade() {

		IntStream.range(0, 2).forEach(i -> devePersistirEntidade());
		Criteria critera = createCriteria(tipoEntidade);
		List<E> entidades = (List<E>) critera.list();

		assertFalse(concat(nomeEntidade, " deve possuir registros"), entidades.isEmpty());
	}

	/**
	 * Teste genérico para lançar exceção para resultado vazio quando o esperado era
	 * um resultado simples
	 */
	@Test(expected = NoResultException.class)
	public void deveLancarExcecaoAoRetornarResultadoSemRegistrosEnquantoEraEsperadoUmResultadoSimples() {

		devePersistirEntidade();
		Query query = getEntittyManager().createQuery(concat("SELECT e FROM ", nomeEntidade, " e WHERE e.id = :id"));
		query.setParameter("id", "0");
		query.getSingleResult();

		fail("devia ter lançado NoResultException ao getSingleResult() não trazer resultado");
	}

	/**
	 * Teste genérico para lançar exceção para resultado com mais de um registro
	 * quando o esperado era um resultado único
	 */
	@Test(expected = HibernateException.class)
	public void deveLancarExcecaoAoRetornarResultadoComMaisDeUmRegistroEnquantoEraEsperadoUmResultadoUnico() {

		IntStream.range(0, 2).forEach(i -> devePersistirEntidade());
		Criteria critera = createCriteria(tipoEntidade);
		critera.uniqueResult();

		fail("devia ter lançado HibernateException ao uniqueResult() trazer mais de um resultado");
	}

	/**
	 * Teste genérico para lançar exceção para resultado com mais de um registro
	 * quando o esperado era um resultado simples
	 */
	@Test(expected = NonUniqueResultException.class)
	public void deveLancarExcecaoAoRetornarResultadoComMaisDeUmRegistroEnquantoEraEsperadoUmResultadoSimples() {

		IntStream.range(0, 2).forEach(i -> devePersistirEntidade());
		Query query = getEntittyManager().createQuery(concat("SELECT e FROM ", nomeEntidade, " e"));
		query.getSingleResult();

		fail("devia ter lançado NonUniqueResultException ao getSingleResult() trazer mais de um resultado");
	}

	/**
	 * Teste genérico para validar resultado de uma consulta dos registros de um uma
	 * entidade com base em um critério
	 */
	@Test
	public void deveConsultarRegistrosDaEntidadePorCriterio() {

		FuncaoCriterioParaBuscaDeEntidade<I, E> funcaoCriterio = getCriterioBuscaEntidadesTeste();

		if (funcaoCriterio == null)
			throw new IllegalStateException(
					"Não foi possível carregar a função contendo o critério de busca da entidade");

		Criterion criterio = funcaoCriterio.getCriterio();

		if (criterio == null)
			throw new IllegalStateException("A função contendo o critério de busca da entidade não pode ser nula");

		IntStream.range(0, 2).forEach(i -> devePersistirEntidade());

		Criteria critera = createCriteria(tipoEntidade);
		critera.add(criterio);
	
		assertFalse(concat(nomeEntidade, " deve possuir registros"), critera.list().isEmpty());
	}

	public abstract E getEntidadeParaTeste();

	public abstract FuncaoAlteraEntidade<I, E> alteracaoEntidadeDeTeste();

	public abstract FuncaoValidaAlteracaoEntidade<I, E> validaAlteracaoEntidadeDeTeste();

	public abstract FuncaoCriterioParaBuscaDeEntidade<I, E> getCriterioBuscaEntidadesTeste();
}
