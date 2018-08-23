package br.edu.faculdadedelta.test.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.edu.faculdadedelta.modelo.AgendamentoDeExame;
import br.edu.faculdadedelta.modelo.Aluno;
import br.edu.faculdadedelta.modelo.BancaExaminadora;
import br.edu.faculdadedelta.modelo.Examinador;
import br.edu.faculdadedelta.modelo.Instrutor;
import br.edu.faculdadedelta.modelo.ProcessoHabilitacao;
import br.edu.faculdadedelta.modelo.Veiculo;
import br.edu.faculdadedelta.tipo.Sexo;
import br.edu.faculdadedelta.tipo.StatusInstrutor;
import br.edu.faculdadedelta.tipo.TipoVeiculo;

public class FabricaTeste {

	public static final String NOME_ALUNO = "Paulo Teixeira";
	public static final String PRIMEIRO_NOME_ALUNO = "Paulo";
	public static final String CPF_ALUNO = "111.111.111-11";

	public static final String NOME_INSTRUTOR = "Tiago Campos";
	public static final String PRIMEIRO_NOME_INSTRUTOR = "Tiago";
	public static final String CPF_INSTRUTOR = "222.222.222-22";

	public static final String NOME_EXAMINADOR = "Marcos dos Santos";
	public static final String PRIMEIRO_NOME_EXAMINADOR = "Marcos";
	public static final String CPF_EXAMINADOR = "333.333.333-33";

	public static final String COR_VEICULO = "Prata";
	public static final TipoVeiculo TIPO_VEICULO = TipoVeiculo.CARRO;

	private FabricaTeste() {
		throw new IllegalStateException("Classe utilitária");
	}

	public static Aluno criaAluno(String nome, String cpf) {

		return new Aluno(nome).setCpf(cpf).setSexo(Sexo.MASCULINO).setDataNascimento(LocalDate.of(1980, 8, 12));
	}

	public static Aluno criaAluno() {

		return new Aluno(NOME_ALUNO).setCpf(CPF_ALUNO).setSexo(Sexo.MASCULINO)
				.setDataNascimento(LocalDate.of(1980, 8, 12));
	}

	public static Instrutor criaInstrutor(String nome, String cpf) {

		return new Instrutor(nome).setCpf(cpf).setStatus(StatusInstrutor.ATIVO);
	}

	public static Instrutor criaInstrutor() {

		return new Instrutor(NOME_INSTRUTOR).setCpf(CPF_INSTRUTOR).setStatus(StatusInstrutor.ATIVO);
	}

	public static Examinador criaExaminador(String nome, String cpf) {

		return new Examinador(nome).setCpf(cpf);
	}

	public static Examinador criaExaminador() {

		return new Examinador(NOME_EXAMINADOR).setCpf(CPF_EXAMINADOR);
	}

	public static Veiculo criaVeiculo(String cor, TipoVeiculo tipo) {

		return new Veiculo("Chevrolet").setModelo("Spin LTZ").setCor(cor).setPlaca("AAA-1111").setTipo(tipo)
				.setAno(2015);
	}

	public static Veiculo criaVeiculo() {

		return new Veiculo("Chevrolet").setModelo("Spin LTZ").setCor(COR_VEICULO).setPlaca("AAA-1111")
				.setTipo(TIPO_VEICULO).setAno(2015);
	}

	public static ProcessoHabilitacao criaProcesso(LocalDate dataAbertura, Aluno aluno, Instrutor instrutor,
			Veiculo veiculo) {

		if (aluno.isTransient())
			aluno.persiste();

		if (instrutor.isTransient())
			instrutor.persiste();

		if (veiculo.isTransient())
			veiculo.persiste();

		return new ProcessoHabilitacao(dataAbertura).setAluno(aluno).setInstrutor(instrutor).setVeiculo(veiculo);
	}

	public static ProcessoHabilitacao criaProcesso(LocalDate dataAbertura) {

		return criaProcesso(dataAbertura, criaAluno(), criaInstrutor(), criaVeiculo());
	}

	public static BancaExaminadora criaBanca(LocalDate data) {

		return new BancaExaminadora(data);
	}

	public static AgendamentoDeExame criaAgendamento(LocalDateTime dataHoraAgendamento, 
			LocalDate dataAberturaProcesso, BancaExaminadora banca) {

		if (banca.isTransient())
			banca.persiste();
		
		return new AgendamentoDeExame(dataHoraAgendamento).setBanca(banca)
				.setProcesso(criaProcesso(dataAberturaProcesso).persiste());
	}
	
	public static AgendamentoDeExame criaAgendamento(LocalDateTime dataHoraAgendamento, LocalDate dataDaBanca,
			LocalDate dataAberturaProcesso) {

		return new AgendamentoDeExame(dataHoraAgendamento).setBanca(criaBanca(dataDaBanca).persiste())
				.setProcesso(criaProcesso(dataAberturaProcesso).persiste());
	}
	
	public static AgendamentoDeExame criaAgendamento(LocalDateTime dataHoraAgendamento, LocalDate dataDaBanca,
			LocalDate dataAberturaProcesso, Aluno aluno) {

		if(aluno == null)
			throw new IllegalArgumentException("aluno não pode ser nulo");
		
		if(aluno.isTransient())
			aluno.persiste();
		
		return new AgendamentoDeExame(dataHoraAgendamento).setBanca(criaBanca(dataDaBanca).persiste())
				.setProcesso(criaProcesso(dataAberturaProcesso).setAluno(aluno).persiste());
	}
}
