package br.edu.faculdadedelta.test.base;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import br.edu.faculdadedelta.modelo.base.BaseEntity;
import br.edu.faculdadedelta.modelo.base.JPAUtil;
import br.edu.faculdadedelta.repositorio.RepositorioAgendamento;
import br.edu.faculdadedelta.repositorio.RepositorioAgendamentoImpl;
import br.edu.faculdadedelta.repositorio.base.FabricaDeRepositorios;

public abstract class BaseTest {

	EntityManager dao;

	@Before
	public void instanciarEntityManager() {

		dao = JPAUtil.INSTANCE.getEntityManager();
	}

	@After
	public void fecharEntityManager() {

		if (dao.isOpen())
			dao.close();
	}

	@BeforeClass
	public static void iniciaRepositorios() {
		
		if(FabricaDeRepositorios.getRepositorio(RepositorioAgendamento.class) == null)
			FabricaDeRepositorios.registraRepositorio(RepositorioAgendamento.class, new RepositorioAgendamentoImpl());
	}
	
	protected EntityManager getDao() {

		return this.dao;
	}

	protected <I extends Serializable, E extends BaseEntity<I>> Criteria  createCriteria(Class<E> classeEntidade, String alias) {

		return ((Session) getDao().getDelegate()).createCriteria(classeEntidade, alias);
	}
	
	protected <I extends Serializable, E extends BaseEntity<I>> Criteria  createCriteria(Class<E> classeEntidade) {
		
		return ((Session) getDao().getDelegate()).createCriteria(classeEntidade);
	}
	
}
