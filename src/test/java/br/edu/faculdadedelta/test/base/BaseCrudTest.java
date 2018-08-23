package br.edu.faculdadedelta.test.base;

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

import static br.edu.faculdadedelta.util.StringUtil.concat;

/**
 * @author Flávio de Souza
 *
 * @param <I>
 *            tipo de chave (id) da entidade a ser testada
 * @param <E>
 *            tipo de entidade a ser testada
 * 
 *            Classe responsável por testar as operações CRUD de classes de
 *            teste voltadas a testes de unidades das entidades de modelo
 *            (@Entity)
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

		EntityManager dao = JPAUtil.INSTANCE.getEntityManager();
		final String nomeEntidade = classeEntidade.getSimpleName();

		dao.getTransaction().begin();
		Query query = dao.createQuery(concat("DELETE FROM ", nomeEntidade));
		query.executeUpdate();
		dao.getTransaction().commit();

		query = dao.createQuery(concat("SELECT e FROM ", nomeEntidade, " e"));
		assertTrue(concat("Tabela ", nomeEntidade, " não deve possuir registros"), query.getResultList().size() == 0);
	}

	/**
	 * Teste genérico de inclusão de uma entidade: Após instanciar entidade, testa
	 * se é transient, persiste, testa se já não é mais transient (inclusive
	 * verifiando se o id da entidade não é nulo)
	 * 
	 */
	@Test
	public void deveSalvarEntidade() {

		E entidade = getEntidadeParaTeste();
		assertTrue(concat(nomeEntidade, " deve estar no estado transient antes de ser pesistido"),
				entidade.isTransient());

		entidade.persiste();
		assertFalse(concat(nomeEntidade, " não deve no estado transient após ter sido persistido"),
				entidade.isTransient());

		assertNotNull(concat(nomeEntidade, " deve ter um id definido após ter sido persistido"), entidade.getId());
	}

	/**
	 * Teste genérico de alteração de uma entidade: instancia e persiste uma
	 * entidade (verificando se não está transient após inclusão), aplica a
	 * alteração dos dados e verifica se os dados foram alterados e versão da
	 * entidade mudou
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void deveAlterarEntidade() {

		FuncaoAlteraEntidade<I, E> funcaoAltera = alteracaoEntidadeDeTeste();

		if (funcaoAltera == null)
			throw new IllegalStateException("Não foi possível carregar a função de alteração da entidade");

		E entidade = getEntidadeParaTeste().persiste();
		assertFalse("Deve possuir id", entidade.isTransient());

		Criteria criteria = createCriteria(tipoEntidade);
		criteria.add(Restrictions.eq("id", entidade.getId()));

		entidade = (E) criteria.uniqueResult();

		assertNotNull(concat("Deve ter encontrado ", nomeEntidade), entidade);

		Integer versao = entidade.getVersion();
		assertNotNull(concat(nomeEntidade, " deve possuir versão"), versao);

		funcaoAltera.altera(entidade);
		entidade = entidade.altera();

		validaAlteracaoEntidadeDeTeste().validaAlteracao(entidade);

		assertNotEquals(concat("Versão ", nomeEntidade, " deve ser diferente"), versao.intValue(),
				entidade.getVersion().intValue());
	}

	/** continuar aqui
	 * Teste genérico de exclusão de uma entidade: testa se uma entidade foi
	 * excluída corretamente
	 */
	@Test
	public void deveExluirEntidade() {

		E entidade = getEntidadeParaTeste().persiste();
		assertFalse(concat(nomeEntidade, " não deve estar no estado transient após ter sido pesistido"),
				entidade.isTransient());

		EntityManager dao = getDao();

		I id = entidade.getId();
		entidade = dao.find(tipoEntidade, id);
		assertNotNull(concat("Deve encontrar ", nomeEntidade), entidade);

		entidade.exclui();

		dao.clear();
		entidade = dao.find(tipoEntidade, id);

		assertNull(concat("Não deve encontrar ", nomeEntidade), entidade);
	}

	/**
	 * Teste genérico de exclusão de todos os elementos de uma entidade: testa a
	 * exclusão de todos os itens de uma tabela
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void deveExluirTodasEntidades() {

		IntStream.range(0, 2).forEach(i -> deveSalvarEntidade());
		Criteria critera = createCriteria(tipoEntidade);
		List<E> entidades = (List<E>) critera.list();

		assertFalse(concat("Tabela de ", nomeEntidade, " não deve estar vazia"), entidades.isEmpty());

		EntityManager dao = getDao();

		dao.getTransaction().begin();
		Query query = dao.createQuery(concat("DELETE FROM ", nomeEntidade));
		query.executeUpdate();
		dao.getTransaction().commit();

		entidades = (List<E>) critera.list();

		assertTrue(concat("Tabela de ", nomeEntidade, " deve estar vazia"), entidades.isEmpty());
	}

	/**
	 * Teste genérico para consultar todos os elementos de uma entidade: testa a
	 * exclusão de todos os itens de uma tabela
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void deveRetornaUmElementoOuMaisDaEntidadeNaConsulta() {

		IntStream.range(0, 2).forEach(i -> deveSalvarEntidade());
		Criteria critera = createCriteria(tipoEntidade);
		List<E> entidades = (List<E>) critera.list();

		assertFalse(concat(nomeEntidade, " não deve estar vazia"), entidades.isEmpty());
		assertTrue(concat(nomeEntidade, " deve ter itens"), entidades.size() > 0);
	}

	@Test(expected = NoResultException.class)
	public void naoDeveFuncionarSingleResultComNenhumRegistro() {

		deveSalvarEntidade();
		Query query = getDao().createQuery(concat("SELECT e FROM ", nomeEntidade, " e WHERE e.id = :id"));
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
		Query query = getDao().createQuery(concat("SELECT e FROM ", nomeEntidade, " e"));
		query.getSingleResult();

		fail("metodo getSingleResult deve lançar exception NonUniqueResultException");
	}

	@Test
	public void devePesquisarEntidadesPorCriterio() {

		FuncaoCriterioParaBuscaDeEntidade<I, E> funcaoCriterio = getCriterioBuscaEntidadesTeste();

		if (funcaoCriterio == null)
			throw new IllegalStateException(
					"Não foi possível carregar a função contendo o critério de busca da entidade");

		Criterion criterio = funcaoCriterio.getCriterio();

		if (criterio == null)
			throw new IllegalStateException("A função contendo o critério de busca da entidade não pode ser nula");

		IntStream.range(0, 2).forEach(i -> deveSalvarEntidade());

		Criteria critera = createCriteria(tipoEntidade);
		critera.add(criterio);

		assertTrue(concat("Quantidade de itens retornada da busca de ", nomeEntidade, " deve ser maior que zero"),
				critera.list().size() > 0L);
	}

	public abstract E getEntidadeParaTeste();

	public abstract FuncaoAlteraEntidade<I, E> alteracaoEntidadeDeTeste();

	public abstract FuncaoValidaAlteracaoEntidade<I, E> validaAlteracaoEntidadeDeTeste();

	public abstract FuncaoCriterioParaBuscaDeEntidade<I, E> getCriterioBuscaEntidadesTeste();
}
