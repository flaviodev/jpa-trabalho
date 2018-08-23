package br.edu.faculdadedelta.modelo.test;

import static br.edu.faculdadedelta.util.StringUtil.concat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.Examinador;
import br.edu.faculdadedelta.test.base.BaseCrudTest;
import br.edu.faculdadedelta.test.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.test.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.test.base.FuncaoValidaAlteracaoEntidade;

public class ExaminadorTest extends BaseCrudTest<String, Examinador> {

	private static final String CPF_PADRAO = "222.222.222-22";
	private static final String NOME_ALTERACAO = "Pedro Claver";

	@Override
	public Examinador getEntidadeParaTeste() {

		return new Examinador("Pedro Antônio").setCpf(CPF_PADRAO);
	}

	@Override
	public FuncaoAlteraEntidade<String, Examinador> alteracaoEntidadeDeTeste() {

		return (examinador) -> examinador.setNome(NOME_ALTERACAO);
	}

	@Override
	public FuncaoValidaAlteracaoEntidade<String, Examinador> validaAlteracaoEntidadeDeTeste() {

		return (examinador) -> assertTrue(
				concat("valor esperado <", NOME_ALTERACAO, "> : retornado <", examinador.getNome(), ">"),
				examinador.getNome().equals(NOME_ALTERACAO));
	}

	@Override
	public FuncaoCriterioParaBuscaDeEntidade<String, Examinador> getCriterioBuscaEntidadesTeste() {

		return () -> Restrictions.eq(Examinador.Atributos.CPF, CPF_PADRAO);
	}

	@Test
	public void deveVerificarExistenciaExaminador() {

		deveSalvarEntidade();

		Criteria critera = createCriteria(Examinador.class);
		critera.add(Restrictions.eq(Examinador.Atributos.CPF, CPF_PADRAO));

		assertTrue("Verifica se há registros na lista", critera.list().size() > 0L);
	}

	private String montaHqlParaObterIdENomePeloCpf() {

		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(Examinador.Atributos.ID);
		hql.append(',');
		hql.append(Examinador.Atributos.NOME);
		hql.append(" FROM ");
		hql.append(Examinador.class.getSimpleName());
		hql.append(" WHERE ");
		hql.append(Examinador.Atributos.CPF);
		hql.append(" = :cpf ");

		return hql.toString();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void deveConsultarIdNomeForeach() {
		deveSalvarEntidade();

		Query query = getDao().createQuery(montaHqlParaObterIdENomePeloCpf());
		query.setParameter("cpf", CPF_PADRAO);

		List<Object[]> resultado = query.getResultList();

		assertFalse("Verifica se há registros na lista", resultado.isEmpty());

		resultado.forEach(linha -> {
			assertTrue("Verifica que o id deve estar nulo", linha[0] instanceof String);
			assertTrue("Verifica que o cpf deve estar nulo", linha[1] instanceof String);

			Examinador examinador = new Examinador((String) linha[0], (String) linha[1]);

			assertNotNull("Verifica que o examinador não deve estar nulo", examinador);
		});
	}

	@Test
	@SuppressWarnings("unchecked")
	public void deveConsultarIdNome() {

		deveSalvarEntidade();

		Query query = getDao().createQuery(montaHqlParaObterIdENomePeloCpf());
		query.setParameter("cpf", CPF_PADRAO);

		List<Object[]> resultado = query.getResultList();

		assertFalse("Verifica se há registros na lista", resultado.isEmpty());

		for (Object[] linha : resultado) {
			assertTrue("Verifica que o id deve estar nulo", linha[0] instanceof String);
			assertTrue("Verifica que o cpf deve estar nulo", linha[1] instanceof String);

			Examinador examinador = new Examinador((String) linha[0], (String) linha[1]);

			assertNotNull("Verifica que o examinador não deve estar nulo", examinador);
		}
	}

	private String montaHqlParaObterEntidadeComIdENomePeloCpf() {

		StringBuilder hql = new StringBuilder("SELECT new ");
		hql.append(Examinador.class.getSimpleName());
		hql.append('(');
		hql.append(Examinador.Atributos.ID);
		hql.append(',');
		hql.append(Examinador.Atributos.NOME);
		hql.append(')');
		hql.append(" FROM ");
		hql.append(Examinador.class.getSimpleName());
		hql.append(" WHERE ");
		hql.append(Examinador.Atributos.CPF);
		hql.append(" = :cpf ");

		return hql.toString();
	}

	@Test
	public void deveConsultarApenasIdNome() {

		deveSalvarEntidade();

		Query query = getDao().createQuery(montaHqlParaObterEntidadeComIdENomePeloCpf());
		query.setParameter("cpf", CPF_PADRAO);

		@SuppressWarnings("unchecked")
		List<Examinador> examinadores = query.getResultList();

		assertFalse("Verifica se há registros na lista", examinadores.isEmpty());

		examinadores.forEach(instrutor -> assertNull("Verifica que o cpf deve estar nulo", instrutor.getCpf()));
	}

	@Test
	public void deveConsultarInstrutorComIdNome() {
		deveSalvarEntidade();

		Query query = getDao().createQuery(montaHqlParaObterEntidadeComIdENomePeloCpf());
		query.setParameter("cpf", CPF_PADRAO);

		@SuppressWarnings("unchecked")
		List<Examinador> examinadores = query.getResultList();

		assertFalse("Verifica se há registros na lista", examinadores.isEmpty());

		examinadores.forEach(examinador -> {
			assertNull("Verifica que o cpf deve estar nulo", examinador.getCpf());
			examinador.setCpf(CPF_PADRAO);
		});
	}

	@Test
	@SuppressWarnings("unchecked")
	public void deveConsultarCpf() {
		deveSalvarEntidade();

		String filtro = "%Pedro%";

		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(Examinador.Atributos.CPF);
		hql.append(" FROM ");
		hql.append(Examinador.class.getSimpleName());
		hql.append(" WHERE ");
		hql.append(Examinador.Atributos.NOME);
		hql.append(" LIKE :nome ");

		Query query = getDao().createQuery(hql.toString());
		query.setParameter("nome", filtro);

		List<String> listaCpf = query.getResultList();

		assertFalse("Deve possuir itens", listaCpf.isEmpty());
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(Examinador.class);
	}
}