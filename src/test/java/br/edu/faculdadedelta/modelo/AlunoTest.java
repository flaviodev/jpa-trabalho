package br.edu.faculdadedelta.modelo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.Test;

import br.edu.faculdadedelta.base.BaseCrudTest;
import br.edu.faculdadedelta.tipo.Sexo;

public class AlunoTest extends BaseCrudTest<String, Aluno> {

	private static final String CPF_PADRAO = "111.111.111-11";

	@Override
	public Aluno getInstanciaDaEntidade() {

		return new Aluno("Joaquim Barcelos").setCpf(CPF_PADRAO).setSexo(Sexo.MASCULINO)
				.setDataNascimento(LocalDate.of(1980, 8, 12));
	}

	@Test
	public void deveAlterarEntidade() {

		Aluno aluno = salvaEntidade();
		assertFalse("Deve possuir id", aluno.isTransient());

		Criteria criteria = createCriteria(Aluno.class);
		criteria.add(Restrictions.eq(Aluno.Atributos.ID, aluno.getId()));
		
		aluno = (Aluno) criteria.uniqueResult();
		
		assertNotNull("Deve ter encontrado aluno", aluno);

		Integer versao = aluno.getVersion();
		assertNotNull("Deve possuir versão", versao);

		getEntityManager().getTransaction().begin();

		aluno.setNome("Joaquim Bragança");
		aluno = getEntityManager().merge(aluno);
		getEntityManager().getTransaction().commit();

		assertNotEquals("Versão deve ser diferente", versao.intValue(), aluno.getVersion().intValue());
	}
	
	// @Test(expected = LazyInitializationException.class)
	public void naoDeveAcessarAtributoLazyForaEscopoEntityManager() {

		Aluno clienteInserido = salvaEntidade();
		fecharEntityManager();
		instanciarEntityManager();

		Aluno cliente = getEntityManager().find(Aluno.class, clienteInserido.getId());

		assertNotNull("Verifica se encontrou um registro", cliente);

		getEntityManager().detach(cliente);
		// cliente.getCompras().size();

		fail("deve disparar LazyInitializationException ao Acessar Atributo Lazy Fora do Escopo EntityManager");
	}

	// @Test
	public void deveAcessarAtributoLazy() {

		Aluno clienteInserido = salvaEntidade();
		Aluno cliente = getEntityManager().find(Aluno.class, clienteInserido.getId());

		assertNotNull("Verifica se encontrou um registro", cliente);
		// assertNotNull("Lista lazy não deve ser nula", cliente.getCompras());
	}

	@Test
	public void deveVerificarExistenciaAluno() {

		deveSalvarEntidade();
		
		Criteria critera = createCriteria(Aluno.class);
		critera.add(Restrictions.eq(Aluno.Atributos.CPF, CPF_PADRAO));

		assertTrue("Verifica se há registros na lista", critera.list().size() > 0L);
	}

	private String montaHqlParaObterIdENomePeloCpf() {
		
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(Aluno.Atributos.ID);
		hql.append(',');
		hql.append(Aluno.Atributos.NOME);
		hql.append(" FROM ");
		hql.append(Aluno.class.getSimpleName());
		hql.append(" WHERE ");
		hql.append(Aluno.Atributos.CPF);
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

			Aluno aluno = new Aluno((String) linha[0], (String) linha[1]);

			assertNotNull("Verifica que o aluno não deve estar nulo", aluno);
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

			Aluno aluno = new Aluno((String) linha[0], (String) linha[1]);

			assertNotNull("Verifica que o aluno não deve estar nulo", aluno);
		}
	}

	private String montaHqlParaObterEntidadeComIdENomePeloCpf() {
		
		StringBuilder hql = new StringBuilder("SELECT new ");
		hql.append(Aluno.class.getSimpleName());
		hql.append('(');
		hql.append(Aluno.Atributos.ID);
		hql.append(',');
		hql.append(Aluno.Atributos.NOME);
		hql.append(')');
		hql.append(" FROM ");
		hql.append(Aluno.class.getSimpleName());
		hql.append(" WHERE ");
		hql.append(Aluno.Atributos.CPF);
		hql.append(" = :cpf ");
		
		return hql.toString();
	}
	
	@Test
	public void deveConsultarApenasIdNome() {

		deveSalvarEntidade();

		Query query = getEntityManager().createQuery(montaHqlParaObterEntidadeComIdENomePeloCpf());
		query.setParameter("cpf", CPF_PADRAO);

		@SuppressWarnings("unchecked")
		List<Aluno> alunos = query.getResultList();

		assertFalse("Verifica se há registros na lista", alunos.isEmpty());

		alunos.forEach(aluno -> assertNull("Verifica que o cpf deve estar nulo", aluno.getCpf()));
	}

	@Test
	public void deveConsultarAlunoComIdNome() {
		deveSalvarEntidade();

		Query query = getEntityManager().createQuery(montaHqlParaObterEntidadeComIdENomePeloCpf());
		query.setParameter("cpf", CPF_PADRAO);

		@SuppressWarnings("unchecked")
		List<Aluno> alunos = query.getResultList();

		assertFalse("Verifica se há registros na lista", alunos.isEmpty());

		alunos.forEach(aluno -> {
			assertNull("Verifica que o cpf deve estar nulo", aluno.getCpf());
			aluno.setCpf(CPF_PADRAO);
		});
	}

	@Test
	@SuppressWarnings("unchecked")
	public void deveConsultarCpf() {
		deveSalvarEntidade();

		String filtro = "%Joaquim%";

		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(Aluno.Atributos.CPF);
		hql.append(" FROM ");
		hql.append(Aluno.class.getSimpleName());
		hql.append(" WHERE ");
		hql.append(Aluno.Atributos.NOME);
		hql.append(" LIKE :nome ");
		
		Query query = getEntityManager().createQuery(hql.toString());
		query.setParameter("nome", filtro);

		List<String> listaCpf = query.getResultList();

		assertFalse("Deve possuir itens", listaCpf.isEmpty());
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(Aluno.class);
	}
}