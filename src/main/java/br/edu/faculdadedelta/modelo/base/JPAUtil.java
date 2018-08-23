package br.edu.faculdadedelta.modelo.base;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public enum JPAUtil {
	
	INSTANCE;
	
	private EntityManagerFactory factory;
	
	private JPAUtil() {
		
		this.factory = Persistence.createEntityManagerFactory("DeltaPU");
	}
	
	public EntityManager getEntityManager() {
		
		return this.factory.createEntityManager();
	}
	
	public static String getAlias(String... aliases) {
		
	    if (aliases == null || aliases.length == 0)
	      return null;

	    StringBuilder aliasesContatenados = new StringBuilder(aliases[0]);
	    boolean primeiro = true;

	    for (String alias : aliases) {
	    	
	      if (primeiro) {
	    	  
	        primeiro = false;
	      } else {
	    	  
	        aliasesContatenados.append('.');
	        aliasesContatenados.append(alias);
	      }
	    }

	    return aliasesContatenados.toString();
	  }
}
