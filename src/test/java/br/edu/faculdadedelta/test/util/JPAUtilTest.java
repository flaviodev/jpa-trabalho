package br.edu.faculdadedelta.test.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.edu.faculdadedelta.test.base.BaseTest;

public class JPAUtilTest extends BaseTest {

	/**
	 *  Teste verifica se o método que retorna o Entity Manager não está vindo nulo
	 */
	@Test
	public void deveTerInstanciaDoEntityManagerDefinida() {
		
		assertNotNull("instância do EntityManager não deve ser nula", getEntittyManager());
	}
	
	/**
	 *  Teste verifica se o EntityManager não está aberto
	 */
	@Test
	public void deveFecharEntityManager() {
	
		getEntittyManager().close();
		
		assertFalse("instância do EntityManager deve estar fechada", getEntittyManager().isOpen());
	}
	
	/**
	 *  Teste verifica o estado da transação (se está ativa ou não) antes e após disparar o método begin()
	 */
	@Test
	public void deveAbrirUmaTransacao(){
		
		assertFalse("Transação deve estar fechada", getEntittyManager().getTransaction().isActive());	
		
		getEntittyManager().getTransaction().begin();
		
		assertTrue("Transação deve estar aberta", getEntittyManager().getTransaction().isActive());
	}
}
