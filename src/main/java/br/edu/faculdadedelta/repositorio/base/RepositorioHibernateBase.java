package br.edu.faculdadedelta.repositorio.base;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;

import br.edu.faculdadedelta.modelo.base.BaseEntity;
import br.edu.faculdadedelta.modelo.base.JPAUtil;

public abstract class RepositorioHibernateBase implements Repositorio {

	private static final long serialVersionUID = 3690971225880951071L;
	
	protected EntityManager getDao() {

		return JPAUtil.INSTANCE.getEntityManager();
	}
	
	protected <I extends Serializable, E extends BaseEntity<I>> Criteria  createCriteria(Class<E> classeEntidade, String alias) {

		return ((Session) getDao().getDelegate()).createCriteria(classeEntidade, alias);
	}
	
	protected <I extends Serializable, E extends BaseEntity<I>> Criteria  createCriteria(Class<E> classeEntidade) {
		
		return ((Session) getDao().getDelegate()).createCriteria(classeEntidade);
	}
	
	protected Query createQuery(String sql) {
		
		if(sql == null || sql.isEmpty())
			throw new IllegalArgumentException("Sql deve ser informada");
		
		return getDao().createQuery(sql);
	}
	
}
