package br.edu.faculdadedelta.test.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.edu.faculdadedelta.test.base.BaseTest;

public class JPAUtilTest extends BaseTest {

	@Test
	public void deveTerInstanciaDoEntityManagerDefinida() {
		
		assertNotNull("instância do EntityManager não deve ser nula", getDao());
	}
	
	@Test
	public void deveFecharEntityManager() {
	
		getDao().close();
		
		assertFalse("instância do EntityManager deve estar fechada", getDao().isOpen());
	}
	
	@Test
	public void deveAbrirUmaTransacao(){
		
		assertFalse("Transação deve estar fechada", getDao().getTransaction().isActive());	
		
		getDao().getTransaction().begin();
		
		assertTrue("Transação deve estar aberta", getDao().getTransaction().isActive());
	}
}
