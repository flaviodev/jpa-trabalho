package br.edu.faculdadedelta.modelo.test;

import static br.edu.faculdadedelta.util.StringUtil.concat;
import static org.junit.Assert.assertTrue;

import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;

import br.edu.faculdadedelta.modelo.Veiculo;
import br.edu.faculdadedelta.test.base.BaseCrudTest;
import br.edu.faculdadedelta.test.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.test.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.test.base.FuncaoValidaAlteracaoEntidade;
import br.edu.faculdadedelta.tipo.TipoVeiculo;

public class VeiculoTest extends BaseCrudTest<String, Veiculo> {

	private static final String MARCA_PADRAO = "Chevrolet";
	private static final TipoVeiculo TIPO_PADRAO = TipoVeiculo.CARRO;
	private static final String COR_ALTERACAO = "José de Arimatéia";

	@Override
	public Veiculo getEntidadeParaTeste() {

		return new Veiculo(MARCA_PADRAO).setModelo("Spin LTZ").setCor("Preta").setPlaca("AAA-1111")
				.setTipo(TIPO_PADRAO).setAno(2015);
	}

	public FuncaoAlteraEntidade<String, Veiculo> alteracaoEntidadeDeTeste() {
		return (veiculo) -> veiculo.setCor(COR_ALTERACAO);
	}

	@Override
	public FuncaoValidaAlteracaoEntidade<String, Veiculo> validaAlteracaoEntidadeDeTeste() {
		return (veiculo) -> assertTrue(
				concat("valor esperado <", COR_ALTERACAO, "> : retornado <", veiculo.getCor(), ">"),
				veiculo.getCor().equals(COR_ALTERACAO));
	}

	@Override
	public FuncaoCriterioParaBuscaDeEntidade<String, Veiculo> getCriterioBuscaEntidadesTeste() {
		return () -> Restrictions.eq(Veiculo.Atributos.TIPO, TIPO_PADRAO);
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(Veiculo.class);
	}
}