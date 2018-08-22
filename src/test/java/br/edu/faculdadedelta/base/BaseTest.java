package br.edu.faculdadedelta.base;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;

import br.edu.faculdadedelta.modelo.BaseEntity;
import br.edu.faculdadedelta.util.JPAUtil;

public abstract class BaseTest {

	EntityManager em;

	@Before
	public void instanciarEntityManager() {

		em = JPAUtil.INSTANCE.getEntityManager();
	}

	@After
	public void fecharEntityManager() {

		if (em.isOpen())
			em.close();
	}

	protected EntityManager getEntityManager() {

		return this.em;
	}

	protected <I extends Serializable, E extends BaseEntity<I>> Criteria  createCriteria(Class<E> classeEntidade, String alias) {

		return ((Session) getEntityManager().getDelegate()).createCriteria(classeEntidade, alias);
	}
	
	protected <I extends Serializable, E extends BaseEntity<I>> Criteria  createCriteria(Class<E> classeEntidade) {
		
		return ((Session) getEntityManager().getDelegate()).createCriteria(classeEntidade);
	}
}
