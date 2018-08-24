package br.edu.faculdadedelta.modelo.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.stream.IntStream;

import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.Veiculo;
import br.edu.faculdadedelta.test.base.BaseCrudTest;
import br.edu.faculdadedelta.test.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.test.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.test.base.FuncaoValidaAlteracaoEntidade;
import br.edu.faculdadedelta.test.util.FabricaTeste;
import br.edu.faculdadedelta.tipo.TipoVeiculo;

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

	/**
	 * Teste validação de marca nula
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComMarcaNula() {

		Veiculo veiculo = getEntidadeParaTeste();
		veiculo.setMarca(null);
		veiculo.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir veículo com marca null");
	}

	/**
	 * Teste validação de marca vazia
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComMarcaVazia() {

		Veiculo veiculo = getEntidadeParaTeste();
		veiculo.setMarca("");
		veiculo.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir veículo com marca vazia");
	}

	/**
	 * Teste validação de quantidade de caracteres da marca
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComMarcaComMaisDe100Caracteres() {

		Veiculo veiculo = getEntidadeParaTeste();

		StringBuilder marcaComMaisDe100Caracteres = new StringBuilder(veiculo.getMarca());
		IntStream.range(0, 101).forEach(i -> marcaComMaisDe100Caracteres.append("-"));
		veiculo.setMarca(marcaComMaisDe100Caracteres.toString());
		veiculo.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir veículo com marca com mais de 100 caracteres");
	}

	/**
	 * Teste validação de modelo nulo
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComModeloNulo() {

		Veiculo veiculo = getEntidadeParaTeste();
		veiculo.setModelo(null);
		veiculo.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir veículo com modelo null");
	}

	/**
	 * Teste validação de modelo vazio
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComModeloVazio() {

		Veiculo veiculo = getEntidadeParaTeste();
		veiculo.setModelo("");
		veiculo.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir veículo com modelo vazio");
	}

	/**
	 * Teste validação de quantidade de caracteres do modelo
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComModeloComMaisDe150Caracteres() {

		Veiculo veiculo = getEntidadeParaTeste();

		StringBuilder modeloComMaisDe150Caracteres = new StringBuilder(veiculo.getModelo());
		IntStream.range(0, 151).forEach(i -> modeloComMaisDe150Caracteres.append("-"));
		veiculo.setModelo(modeloComMaisDe150Caracteres.toString());
		veiculo.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir veículo com modelo com mais de 150 caracteres");
	}

	/**
	 * Teste validação de cor nula
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComCorNula() {

		Veiculo veiculo = getEntidadeParaTeste();
		veiculo.setCor(null);
		veiculo.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir veículo com cor null");
	}

	/**
	 * Teste validação de cor vazia
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComCorVazia() {

		Veiculo veiculo = getEntidadeParaTeste();
		veiculo.setCor("");
		veiculo.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir veículo com marca vazia");
	}

	/**
	 * Teste validação de quantidade de caracteres da marca
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComCorComMaisDe50Caracteres() {

		Veiculo veiculo = getEntidadeParaTeste();

		StringBuilder corComMaisDe50Caracteres = new StringBuilder(veiculo.getCor());
		IntStream.range(0, 51).forEach(i -> corComMaisDe50Caracteres.append("-"));
		veiculo.setCor(corComMaisDe50Caracteres.toString());
		veiculo.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir veículo com cor com mais de 50 caracteres");
	}

	/**
	 * Teste validação de tipo nulo
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComTipoNulo() {

		Veiculo veiculo = getEntidadeParaTeste();
		veiculo.setTipo(null);
		veiculo.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir veículo com tipo null");
	}

	/**
	 * Teste validação de placa invalida
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComPlacaInvalida() {

		Veiculo veiculo = getEntidadeParaTeste();
		veiculo.setPlaca("AA-1234");
		veiculo.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir veículo com placa inválida");
	}

	/**
	 * Teste validação de ano nulo
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDeveSetarAnoNulo() {

		Veiculo veiculo = getEntidadeParaTeste();
		veiculo.setAno(null);

		fail("devia ter lançado IllegalStateException ao tentar setar ano null");
	}

	/**
	 * Teste validação de ano sem tipo
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDeveSetarAnoComTipoNulo() {

		Veiculo veiculo = getEntidadeParaTeste();
		veiculo.setTipo(null);
		veiculo.setAno(2018);

		fail("devia ter lançado IllegalStateException ao tentar setar ano com tipo nulo");
	}

	/**
	 * Teste validação idade carro
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDeveSetarAnoParaCarroComMaisDe8AnosDeUso() {

		Veiculo veiculo = getEntidadeParaTeste();
		veiculo.setTipo(TipoVeiculo.CARRO);
		veiculo.setAno(LocalDate.now().minusYears(10).getYear());

		fail("devia ter lançado IllegalStateException ao tentar setar ano de carro com mais de 8 anos de uso");
	}

	/**
	 * Teste validação idade carro
	 */
	@Test
	public void devePersistirCarroCom8AnosDeUso() {

		Veiculo veiculo = getEntidadeParaTeste();
		veiculo.setTipo(TipoVeiculo.CARRO);
		veiculo.setAno(LocalDate.now().minusYears(8).getYear());

		veiculo.persiste();
		assertFalse("Veículo não deve estar no estado transient após ter sido persistido", veiculo.isTransient());
	}

	/**
	 * Teste validação idade moto
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDeveSetarAnoParaMotoComMaisDe5AnosDeUso() {

		Veiculo veiculo = getEntidadeParaTeste();
		veiculo.setTipo(TipoVeiculo.MOTO);
		veiculo.setAno(LocalDate.now().minusYears(6).getYear());

		fail("devia ter lançado IllegalStateException ao tentar setar ano de moto com mais de 6 anos de uso");
	}

	/**
	 * Teste validação idade moto
	 */
	@Test
	public void devePersistirMotoCom5AnosDeUso() {

		Veiculo veiculo = getEntidadeParaTeste();
		veiculo.setTipo(TipoVeiculo.MOTO);
		veiculo.setAno(LocalDate.now().minusYears(5).getYear());

		veiculo.persiste();
		assertFalse("Veículo não deve estar no estado transient após ter sido persistido", veiculo.isTransient());
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(Veiculo.class);
	}
}