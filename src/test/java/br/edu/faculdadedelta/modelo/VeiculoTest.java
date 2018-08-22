package br.edu.faculdadedelta.modelo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.Test;

import br.edu.faculdadedelta.base.BaseCrudTest;

public class VeiculoTest extends BaseCrudTest<String, Veiculo> {

	@Override
	public Veiculo getInstanciaDaEntidade() {

		return new Veiculo("GM").setModelo("Spin LTZ").setAno(2015).setCor("Preta").setPlaca("AAA-1111");
	}

	@Test
	public void deveAlterarEntidade() {

		Veiculo veiculo = salvaEntidade();
		assertFalse("Deve possuir id", veiculo.isTransient());

		Criteria criteria = createCriteria(Veiculo.class);
		criteria.add(Restrictions.eq(Veiculo.Atributos.ID, veiculo.getId()));

		veiculo = (Veiculo) criteria.uniqueResult();

		assertNotNull("Dever ter encontrado veículo", veiculo);

		Integer versao = veiculo.getVersion();

		assertNotNull("Deve possuir versão", versao);

		getEntityManager().getTransaction().begin();

		veiculo.setCor("Branca");
		veiculo = getEntityManager().merge(veiculo);
		getEntityManager().getTransaction().commit();

		assertNotEquals("Versão deve ser diferente", versao.intValue(), veiculo.getVersion().intValue());
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(Veiculo.class);
	}
}