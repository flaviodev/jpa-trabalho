package br.edu.faculdadedelta.modelo.test;

import static br.edu.faculdadedelta.util.StringUtil.concat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;

import br.edu.faculdadedelta.modelo.Pessoa;
import br.edu.faculdadedelta.modelo.base.BaseEntity;
import br.edu.faculdadedelta.test.base.BaseCrudTest;

public abstract class BasePessoaCrudTest<I extends Serializable, E extends BaseEntity<I>> extends BaseCrudTest<I, E> {

	@SuppressWarnings("unchecked")
	protected void deveConsultarIdENomeDasPessoasPeloCpf(Class<E> classePessoa, String cpf) {
		devePersistirEntidade();

		StringBuilder consulta = new StringBuilder("SELECT ");
		consulta.append(Pessoa.Atributos.ID);
		consulta.append(',');
		consulta.append(Pessoa.Atributos.NOME);
		consulta.append(" FROM ");
		consulta.append(classePessoa.getSimpleName());
		consulta.append(" WHERE ");
		consulta.append(Pessoa.Atributos.CPF);
		consulta.append(" = :cpf ");

		Query query = getEntittyManager().createQuery(consulta.toString());
		query.setParameter("cpf", cpf);

		List<Object[]> resultado = query.getResultList();

		assertFalse("Verifica se há registros na lista", resultado.isEmpty());

		resultado.forEach(linha -> {
			assertTrue("Verifica que o id deve estar nulo", linha[0] instanceof String);
			assertTrue("Verifica que o cpf deve estar nulo", linha[1] instanceof String);
		});
	}

	protected void deveConsultarObjetosApenasComIdENomeDaPessoaPeloCpf(Class<E> classePessoa, String cpf) {

		devePersistirEntidade();

		StringBuilder consulta = new StringBuilder("SELECT new ");
		consulta.append(classePessoa.getSimpleName());
		consulta.append('(');
		consulta.append(Pessoa.Atributos.ID);
		consulta.append(',');
		consulta.append(Pessoa.Atributos.NOME);
		consulta.append(')');
		consulta.append(" FROM ");
		consulta.append(classePessoa.getSimpleName());
		consulta.append(" WHERE ");
		consulta.append(Pessoa.Atributos.CPF);
		consulta.append(" = :cpf ");
		
		Query query = getEntittyManager().createQuery(consulta.toString());
		query.setParameter("cpf", cpf);

		@SuppressWarnings("unchecked")
		List<Pessoa> pessoas = query.getResultList();

		assertFalse("Verifica se há registros na lista", pessoas.isEmpty());

		pessoas.forEach(pessoa -> assertNull("Verifica que o cpf deve estar nulo", pessoa.getCpf()));
	}

	@SuppressWarnings("unchecked")
	public void deveConsultarPessoasPeloNome(Class<E> classePessoa, String nome) {
		devePersistirEntidade();

		StringBuilder consulta = new StringBuilder("SELECT ");
		consulta.append(Pessoa.Atributos.CPF);
		consulta.append(" FROM ");
		consulta.append(classePessoa.getSimpleName());
		consulta.append(" WHERE ");
		consulta.append(Pessoa.Atributos.NOME);
		consulta.append(" LIKE :nome ");

		Query query = getEntittyManager().createQuery(consulta.toString());
		query.setParameter("nome", concat("%", nome, "%"));

		List<String> listaCpf = query.getResultList();

		assertFalse("Deve possuir itens", listaCpf.isEmpty());
	}

	
}
