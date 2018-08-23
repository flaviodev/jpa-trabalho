package br.edu.faculdadedelta.modelo.test;

import static br.edu.faculdadedelta.util.DateUtil.toDate;
import static br.edu.faculdadedelta.util.StringUtil.concat;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;

import br.edu.faculdadedelta.modelo.AgendamentoDeExame;
import br.edu.faculdadedelta.modelo.Aluno;
import br.edu.faculdadedelta.modelo.BancaExaminadora;
import br.edu.faculdadedelta.modelo.Examinador;
import br.edu.faculdadedelta.modelo.Instrutor;
import br.edu.faculdadedelta.modelo.ProcessoHabilitacao;
import br.edu.faculdadedelta.modelo.Veiculo;
import br.edu.faculdadedelta.test.base.BaseCrudTest;
import br.edu.faculdadedelta.test.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.test.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.test.base.FuncaoValidaAlteracaoEntidade;
import br.edu.faculdadedelta.tipo.Sexo;
import br.edu.faculdadedelta.tipo.StatusInstrutor;
import br.edu.faculdadedelta.tipo.TipoVeiculo;

public class AgendamentoDeExameTest extends BaseCrudTest<String, AgendamentoDeExame> {

	private static final LocalDateTime DATA_PADRAO = LocalDateTime.of(2018, 9, 20, 10, 00);
	private static final Date DATA_ALTERACAO = toDate(LocalDateTime.of(2018, 9, 20, 11, 00));

	@Override
	public AgendamentoDeExame getEntidadeParaTeste() {

		Instrutor instrutor = new Instrutor("João Vieira").setCpf("111.111.111-11").setStatus(StatusInstrutor.ATIVO)
				.persiste();

		Aluno aluno = new Aluno("Flávio de Souza").setDataNascimento(LocalDate.of(1980, 8, 10)).setCpf("222.222.222-22")
				.setSexo(Sexo.MASCULINO).persiste();

		Veiculo veiculo = new Veiculo("GM").setModelo("Spin LTZ").setCor("Preta").setPlaca("AAA-1111")
				.setTipo(TipoVeiculo.CARRO).setAno(2015).persiste();

		ProcessoHabilitacao processo = new ProcessoHabilitacao(LocalDate.of(2018, 7, 20)).setAluno(aluno)
				.setInstrutor(instrutor).setVeiculo(veiculo).persiste();

		Examinador examinador = new Examinador("Maria do Carmo").setCpf("999.999.999-99").persiste();
		
		BancaExaminadora banca = new BancaExaminadora(LocalDate.of(2018, 9, 20))
				.adicionaExaminador(examinador).persiste();

		return new AgendamentoDeExame(DATA_PADRAO).setBanca(banca).setProcesso(processo);
	}

	public FuncaoAlteraEntidade<String, AgendamentoDeExame> alteracaoEntidadeDeTeste() {
		return (agendamento) -> agendamento.setDataHora(DATA_ALTERACAO);
	}

	@Override
	public FuncaoValidaAlteracaoEntidade<String, AgendamentoDeExame> validaAlteracaoEntidadeDeTeste() {
		return (agendamento) -> assertTrue(concat("valor esperado <", DATA_ALTERACAO.toString(), "> : retornado <",
				agendamento.getDataHora().toString(), ">"), agendamento.getDataHora().equals(DATA_ALTERACAO));
	}

	@Override
	public FuncaoCriterioParaBuscaDeEntidade<String, AgendamentoDeExame> getCriterioBuscaEntidadesTeste() {
		return () -> Restrictions.eq(AgendamentoDeExame.Atributos.DATA_HORA, toDate(DATA_PADRAO));
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(AgendamentoDeExame.class);
		deveLimparBase(BancaExaminadora.class);
		deveLimparBase(Examinador.class);
		deveLimparBase(ProcessoHabilitacao.class);
		deveLimparBase(Veiculo.class);
		deveLimparBase(Instrutor.class);
		deveLimparBase(Aluno.class);
	}
}