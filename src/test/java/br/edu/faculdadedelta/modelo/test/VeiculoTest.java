package br.edu.faculdadedelta.modelo.test;

import static org.junit.Assert.assertEquals;

import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;

import br.edu.faculdadedelta.modelo.Veiculo;
import br.edu.faculdadedelta.test.base.BaseCrudTest;
import br.edu.faculdadedelta.test.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.test.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.test.base.FuncaoValidaAlteracaoEntidade;
import br.edu.faculdadedelta.test.util.FabricaTeste;

public class VeiculoTest extends BaseCrudTest<String, Veiculo> {

	private static final String COR = "Preta";
	private static final String COR_ALTERACAO = "Branca";

	@Override
	public Veiculo getEntidadeParaTeste() {

		return FabricaTeste.criaVeiculo().setCor(COR);
	}

	public FuncaoAlteraEntidade<String, Veiculo> alteracaoEntidadeDeTeste() {
		return (veiculo) -> veiculo.setCor(COR_ALTERACAO);
	}

	@Override
	public FuncaoValidaAlteracaoEntidade<String, Veiculo> validaAlteracaoEntidadeDeTeste() {

		return (veiculo) -> assertEquals(COR_ALTERACAO, veiculo.getCor());
	}

	@Override
	public FuncaoCriterioParaBuscaDeEntidade<String, Veiculo> getCriterioBuscaEntidadesTeste() {
		return () -> Restrictions.eq(Veiculo.Atributos.COR, COR);
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(Veiculo.class);
	}
}