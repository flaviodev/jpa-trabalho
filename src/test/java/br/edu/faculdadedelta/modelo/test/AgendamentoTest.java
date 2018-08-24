package br.edu.faculdadedelta.modelo.test;

import static br.edu.faculdadedelta.util.DateUtil.toDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.IntStream;

import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.Agendamento;
import br.edu.faculdadedelta.modelo.Aluno;
import br.edu.faculdadedelta.modelo.Instrutor;
import br.edu.faculdadedelta.modelo.Veiculo;
import br.edu.faculdadedelta.test.base.BaseCrudTest;
import br.edu.faculdadedelta.test.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.test.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.test.base.FuncaoValidaAlteracaoEntidade;
import br.edu.faculdadedelta.test.util.FabricaTeste;
import br.edu.faculdadedelta.tipo.CategoriaExame;
import br.edu.faculdadedelta.tipo.TipoVeiculo;

public class AgendamentoTest extends BaseCrudTest<String, Agendamento> {

	private static final LocalDateTime DATA_HORA_PROVA = LocalDateTime.of(2018, 9, 20, 10, 00);
	private static final Date DATA_HORA_PROVA_ALTERACAO = toDate(LocalDateTime.of(2018, 9, 20, 11, 00));

	@Override
	public Agendamento getEntidadeParaTeste() {

		return FabricaTeste.criaAgendamento(DATA_HORA_PROVA);
	}

	public FuncaoAlteraEntidade<String, Agendamento> alteracaoEntidadeDeTeste() {

		return (agendamento) -> agendamento.setDataHoraProva(DATA_HORA_PROVA_ALTERACAO);
	}

	@Override
	public FuncaoValidaAlteracaoEntidade<String, Agendamento> validaAlteracaoEntidadeDeTeste() {

		return (agendamento) -> assertEquals(DATA_HORA_PROVA_ALTERACAO, agendamento.getDataHoraProva());
	}

	@Override
	public FuncaoCriterioParaBuscaDeEntidade<String, Agendamento> getCriterioBuscaEntidadesTeste() {

		return () -> Restrictions.eq(Agendamento.Atributos.DATA_HORA_PROVA, toDate(DATA_HORA_PROVA));
	}

	/**
	 * Teste validação de data hora prova nula
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComDataHoraProvaNula() {

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.setDataHoraProva((Date) null);
		agendamento.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir agendamento com data hora prova null");
	}

	/**
	 * Teste validação de data hora prova anterior data corrente
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComDataHoraProvaAnteriorADataCorrete() {

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.setDataHoraProva(LocalDateTime.now().minusDays(1));
		agendamento.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir agendamento com data hora prova anterior à data corrente");
	}

	/**
	 * Teste validação de nome examinador nulo
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComNomeExaminadorNulo() {

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.setNomeExaminador(null);
		agendamento.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir agendamento com nome do examinador null");
	}

	/**
	 * Teste validação de modelo vazio
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComNomeExaminadorVazio() {

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.setNomeExaminador("");
		agendamento.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir agendamento com nome do examinador vazio");
	}

	/**
	 * Teste validação de quantidade de caracteres do modelo
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComNomeExaminadorComMaisDe150Caracteres() {

		Agendamento agendamento = getEntidadeParaTeste();

		StringBuilder nomeExaminadorComMaisDe150Caracteres = new StringBuilder(agendamento.getNomeExaminador());
		IntStream.range(0, 151).forEach(i -> nomeExaminadorComMaisDe150Caracteres.append("-"));
		agendamento.setNomeExaminador(nomeExaminadorComMaisDe150Caracteres.toString());
		agendamento.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir agendamento com nome do examinador com mais de 150 caracteres");
	}

	/**
	 * Teste validação de categoria nula
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComCategoriaNula() {

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.setCategoria(null);
		agendamento.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir agendamento com categoria null");
	}

	/**
	 * Teste validação de categoria ambos
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComCategoriaAmbosSemInformarDoisVeiculos() {

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.setCategoria(CategoriaExame.AMBOS);
		agendamento.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir agendamento com categoria ambos sem informar dois veiculos");
	}

	/**
	 * Teste validação de categoria diferente de ambos
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComCategoriaDiferenteDeAmbosInformandoQuantidadeDeVeiculosDiferenteDeUm() {

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.setCategoria(CategoriaExame.CARRO);
		agendamento.adicionaVeiculo(FabricaTeste.criaVeiculo().persiste());
		agendamento.adicionaVeiculo(FabricaTeste.criaVeiculo().persiste());
		agendamento.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir agendamento com categoria diferente de ambos informando quantidade de veiculos diferente de um");
	}

	/**
	 * Teste validação de categoria ambos
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComCategoriaAmbosInformandoDoisVeiculosDoMesmoTipo() {

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.setCategoria(CategoriaExame.AMBOS);
		agendamento.getVeiculos().clear();
		agendamento.adicionaVeiculo(FabricaTeste.criaVeiculo().persiste());
		agendamento.adicionaVeiculo(FabricaTeste.criaVeiculo().persiste());
		agendamento.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir agendamento com categoria ambos informando dois veiculos do mesmo tipo");
	}

	/**
	 * Teste validação de categoria carro
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComCategoriaCarroInformandoMoto() {

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.setCategoria(CategoriaExame.CARRO);
		agendamento.getVeiculos().clear();
		agendamento.adicionaVeiculo(FabricaTeste.criaVeiculo().setTipo(TipoVeiculo.MOTO).persiste());
		agendamento.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir agendamento com categoria carro informando moto");
	}

	/**
	 * Teste validação de categoria moto
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDevePersistirComCategoriaMotoInformandoCarro() {

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.setCategoria(CategoriaExame.MOTO);
		agendamento.getVeiculos().clear();
		agendamento.adicionaVeiculo(FabricaTeste.criaVeiculo().setTipo(TipoVeiculo.CARRO).persiste());
		agendamento.persiste();

		fail("devia ter lançado IllegalStateException ao tentar persistir agendamento com categoria moto informando carro");
	}

	/**
	 * Teste validação de categoria ambos
	 */
	public void devePersistirComCategoriaAmbos() {

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.setCategoria(CategoriaExame.AMBOS);
		agendamento.getVeiculos().clear();
		agendamento.adicionaVeiculo(FabricaTeste.criaVeiculo().setTipo(TipoVeiculo.CARRO).persiste());
		agendamento.adicionaVeiculo(FabricaTeste.criaVeiculo().setTipo(TipoVeiculo.MOTO).persiste());
		agendamento.persiste();

		assertFalse("Agendamento não deve estar no estado transient após ter sido persistido",
				agendamento.isTransient());
		assertTrue("Agendamento deve ter dois veiculos", agendamento.getVeiculos().size() == 2);
		assertTrue("Agendamento deve ter dois veiculos diferentes",
				agendamento.getVeiculos().get(0).getTipo() != agendamento.getVeiculos().get(1).getTipo());
	}

	/**
	 * Teste validação de categoria Carro
	 */
	public void devePersistirComCategoriaCarro() {

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.setCategoria(CategoriaExame.CARRO);
		agendamento.getVeiculos().clear();
		agendamento.adicionaVeiculo(FabricaTeste.criaVeiculo().setTipo(TipoVeiculo.CARRO).persiste());
		agendamento.persiste();

		assertFalse("Agendamento não deve estar no estado transient após ter sido persistido",
				agendamento.isTransient());
		assertTrue("Agendamento deve ter um veículo", agendamento.getVeiculos().size() == 1);
		assertTrue("Agendamento deve ter um carro", agendamento.getVeiculos().get(0).getTipo() == TipoVeiculo.CARRO);
	}

	/**
	 * Teste validação de categoria moto
	 */
	public void devePersistirComCategoriaMoto() {

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.setCategoria(CategoriaExame.MOTO);
		agendamento.getVeiculos().clear();
		agendamento.adicionaVeiculo(FabricaTeste.criaVeiculo().setTipo(TipoVeiculo.MOTO).persiste());
		agendamento.persiste();

		assertFalse("Agendamento não deve estar no estado transient após ter sido persistido",
				agendamento.isTransient());
		assertTrue("Agendamento deve ter um veículo", agendamento.getVeiculos().size() == 1);
		assertTrue("Agendamento deve ter uma moto", agendamento.getVeiculos().get(0).getTipo() == TipoVeiculo.MOTO);
	}

	/**
	 * Teste validação adicionar veículo nulo
	 */
	@Test(expected = IllegalArgumentException.class)
	public void naoDeveAdicionarVeiculoNulo() {

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.adicionaVeiculo(null);

		fail("devia ter lançado IllegalArgumentException ao tentar adicionar veículo nulo ao agendamento");
	}

	/**
	 * Teste validação adicionar veículo não persistido
	 */
	@Test(expected = IllegalArgumentException.class)
	public void naoDeveAdicionarVeiculoNaoPersistido() {

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.adicionaVeiculo(FabricaTeste.criaVeiculo());

		fail("devia ter lançado IllegalArgumentException ao tentar adicionar ao agendamento veículo não persistido");
	}

	/**
	 * Teste validação adicionar veículo já pertencente
	 */
	@Test(expected = IllegalArgumentException.class)
	public void naoDeveAdicionarVeiculoJaPertencenteAoAgendamento() {

		Agendamento agendamento = getEntidadeParaTeste();
		Veiculo veiculo = FabricaTeste.criaVeiculo().persiste();
		agendamento.adicionaVeiculo(veiculo);
		agendamento.adicionaVeiculo(veiculo);

		fail("devia ter lançado IllegalArgumentException ao tentar adicionar veículo já pertencente ao agendamento");
	}

	/**
	 * Teste validação retirar veículo nulo
	 */
	@Test(expected = IllegalArgumentException.class)
	public void naoDeveRetirarVeiculoNulo() {

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.retiraVeiculo(null);

		fail("devia ter lançado IllegalArgumentException ao tentar retirar veículo nulo ao agendamento");
	}

	/**
	 * Teste validação retirar veículo não persistido
	 */
	@Test(expected = IllegalArgumentException.class)
	public void naoDeveRetirarVeiculoNaoPersistido() {

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.retiraVeiculo(FabricaTeste.criaVeiculo());

		fail("devia ter lançado IllegalArgumentException ao tentar retirar do agendamento veículo não persistido");
	}

	/**
	 * Teste validação retirar veículo não pertencente
	 */
	@Test(expected = IllegalArgumentException.class)
	public void naoDeveRetirarVeiculoNaoPertencenteAoAgendamento() {

		Agendamento agendamento = getEntidadeParaTeste();
		Veiculo veiculo = FabricaTeste.criaVeiculo().persiste();
		agendamento.retiraVeiculo(veiculo);

		fail("devia ter lançado IllegalArgumentException ao tentar retirar veículo não pertencente ao agendamento");
	}

	/**
	 * Teste validação de categoria alteracao do veículo
	 */
	@Test(expected = IllegalStateException.class)
	public void naoDeveSalvarAoTrocarTipoDeVeiculoSemTrocarCategoriaDaProva() {

		Veiculo carro = FabricaTeste.criaVeiculo().setTipo(TipoVeiculo.CARRO).persiste();

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.setCategoria(CategoriaExame.CARRO);
		agendamento.getVeiculos().clear();
		agendamento.adicionaVeiculo(carro);
		agendamento.persiste();

		assertFalse("Agendamento não deve estar no estado transient após ter sido persistido",
				agendamento.isTransient());
		assertTrue("Agendamento deve ter um veículo", agendamento.getVeiculos().size() == 1);
		assertTrue("Agendamento deve ter um carro", agendamento.getVeiculos().get(0).getTipo() == TipoVeiculo.CARRO);

		agendamento.retiraVeiculo(carro);
		agendamento.adicionaVeiculo(FabricaTeste.criaVeiculo().setTipo(TipoVeiculo.MOTO).persiste());
		agendamento.altera();

		fail("devia ter lançado IllegalStateException ao tentar alterar tipo do veículo do agendamento sem alterar também a categoria");
	}

	/**
	 * Teste validação de categoria alteracao do veículo
	 */
	public void deveSalvarAoTrocarVeiculoPorOutroDoMesmoTipo() {

		Veiculo carro = FabricaTeste.criaVeiculo().setTipo(TipoVeiculo.CARRO).persiste();

		Agendamento agendamento = getEntidadeParaTeste();
		agendamento.setCategoria(CategoriaExame.CARRO);
		agendamento.getVeiculos().clear();
		agendamento.adicionaVeiculo(carro);
		agendamento.persiste();

		assertFalse("Agendamento não deve estar no estado transient após ter sido persistido",
				agendamento.isTransient());

		assertTrue("Agendamento deve ter um veículo", agendamento.getVeiculos().size() == 1);
		assertTrue("Agendamento deve ter um carro", agendamento.getVeiculos().get(0).getTipo() == TipoVeiculo.CARRO);

		int versao = agendamento.getVersion();
		assertNotNull("Agendamento deve possuir versão", versao);

		Veiculo novoCarro = FabricaTeste.criaVeiculo().setTipo(TipoVeiculo.CARRO).persiste();
		String idNovoCarro = novoCarro.getId();

		agendamento.retiraVeiculo(carro);
		agendamento.adicionaVeiculo(novoCarro);
		agendamento = agendamento.altera();

		assertTrue("Agendamento deve estar com o novo carro",
				agendamento.getVeiculos().get(0).getId().equals(idNovoCarro));
		assertNotEquals(agendamento.getVersion().intValue(), versao);
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(Agendamento.class);
		deveLimparBase(Veiculo.class);
		deveLimparBase(Instrutor.class);
		deveLimparBase(Aluno.class);
	}
}