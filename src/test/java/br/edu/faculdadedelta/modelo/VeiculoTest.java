package br.edu.faculdadedelta.modelo;

import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;

import br.edu.faculdadedelta.base.BaseCrudTest;
import br.edu.faculdadedelta.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.tipo.TipoVeiculo;

public class VeiculoTest extends BaseCrudTest<String, Veiculo> {

	@Override
	public Veiculo getInstanciaDaEntidade() {

		return new Veiculo("GM").setModelo("Spin LTZ").setAno(2015).setCor("Preta").setPlaca("AAA-1111")
				.setTipo(TipoVeiculo.CARRO);
	}

	public FuncaoAlteraEntidade<String, Veiculo> alteracaoEntidade() {
		return (veiculo) -> veiculo.setCor("Branca");
	}

	@Override
	public FuncaoCriterioParaBuscaDeEntidade<String, Veiculo> getCriterioBuscaEntidades() {
		return () -> Restrictions.eq(Veiculo.Atributos.TIPO, TipoVeiculo.CARRO);
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(Veiculo.class);
	}
}