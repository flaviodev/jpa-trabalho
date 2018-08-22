package br.edu.faculdadedelta.modelo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.Test;

import br.edu.faculdadedelta.base.BaseCrudTest;
import br.edu.faculdadedelta.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.tipo.StatusInstrutor;

public class InstrutorTest extends BaseCrudTest<String, Instrutor> {

	private static final String CPF_PADRAO = "222.222.222-22";

	@Override
	public Instrutor getInstanciaDaEntidade() {

		return new Instrutor("José De Almeida Prado").setCpf(CPF_PADRAO).setStatus(StatusInstrutor.ATIVO);
	}

	@Override
	public FuncaoAlteraEntidade<String, Instrutor> alteracaoEntidade() {
		return (instrutor) -> instrutor.setNome("José de Arimatéia");
	}
	
	@Override
	public FuncaoCriterioParaBuscaDeEntidade<String, Instrutor> getCriterioBuscaEntidades() {
		return () -> Restrictions.eq(Instrutor.Atributos.CPF, CPF_PADRAO);
	}

	// @Test(expected = LazyInitializationException.class)
	public void naoDeveAcessarAtributoLazyForaEscopoEntityManager() {

		Instrutor instrutorInserido = salvaEntidade();
		fecharEntityManager();
		instanciarEntityManager();

		Instrutor instrutor = getEntityManager().find(Instrutor.class, instrutorInserido.getId());

		assertNotNull("Verifica se encontrou um registro", instrutor);

		getEntityManager().detach(instrutor);
		// cliente.getCompras().size();

		fail("deve disparar LazyInitializationException ao Acessar Atributo Lazy Fora do Escopo EntityManager");
	}

	// @Test
	public void deveAcessarAtributoLazy() {

		Instrutor InstrutorInserido = salvaEntidade();
		Instrutor instrutor = getEntityManager().find(Instrutor.class, InstrutorInserido.getId());

		assertNotNull("Verifica se encontrou um registro", instrutor);
		// assertNotNull("Lista lazy não deve ser nula", cliente.getCompras());
	}

	@Test
	public void deveVerificarExistenciaInstrutor() {

		deveSalvarEntidade();

		Criteria critera = createCriteria(Instrutor.class);
		critera.add(Restrictions.eq(Instrutor.Atributos.CPF, CPF_PADRAO));

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

		Query query = getEntityManager().createQuery(montaHqlParaObterIdENomePeloCpf());
		query.setParameter("cpf", CPF_PADRAO);

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

		Query query = getEntityManager().createQuery(montaHqlParaObterIdENomePeloCpf());
		query.setParameter("cpf", CPF_PADRAO);

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

		Query query = getEntityManager().createQuery(montaHqlParaObterEntidadeComIdENomePeloCpf());
		query.setParameter("cpf", CPF_PADRAO);

		@SuppressWarnings("unchecked")
		List<Instrutor> instrutores = query.getResultList();

		assertFalse("Verifica se há registros na lista", instrutores.isEmpty());

		instrutores.forEach(instrutor -> assertNull("Verifica que o cpf deve estar nulo", instrutor.getCpf()));
	}

	@Test
	public void deveConsultarInstrutorComIdNome() {
		deveSalvarEntidade();

		Query query = getEntityManager().createQuery(montaHqlParaObterEntidadeComIdENomePeloCpf());
		query.setParameter("cpf", CPF_PADRAO);

		@SuppressWarnings("unchecked")
		List<Instrutor> instrutores = query.getResultList();

		assertFalse("Verifica se há registros na lista", instrutores.isEmpty());

		instrutores.forEach(instrutor -> {
			assertNull("Verifica que o cpf deve estar nulo", instrutor.getCpf());
			instrutor.setCpf(CPF_PADRAO);
		});
	}

	@Test
	@SuppressWarnings("unchecked")
	public void deveConsultarCpf() {
		deveSalvarEntidade();

		String filtro = "%José%";

		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(Instrutor.Atributos.CPF);
		hql.append(" FROM ");
		hql.append(Instrutor.class.getSimpleName());
		hql.append(" WHERE ");
		hql.append(Instrutor.Atributos.NOME);
		hql.append(" LIKE :nome ");

		Query query = getEntityManager().createQuery(hql.toString());
		query.setParameter("nome", filtro);

		List<String> listaCpf = query.getResultList();

		assertFalse("Deve possuir itens", listaCpf.isEmpty());
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(Instrutor.class);
	}
}