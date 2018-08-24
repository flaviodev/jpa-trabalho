package br.edu.faculdadedelta.test.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.edu.faculdadedelta.modelo.Agendamento;
import br.edu.faculdadedelta.modelo.Aluno;
import br.edu.faculdadedelta.modelo.Instrutor;
import br.edu.faculdadedelta.modelo.Veiculo;
import br.edu.faculdadedelta.tipo.CategoriaExame;
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

	public static Veiculo criaVeiculo() {

		return new Veiculo("Chevrolet").setModelo("Spin LTZ").setCor(COR_VEICULO).setPlaca("AAA-1111")
				.setTipo(TIPO_VEICULO).setAno(2015);
	}

	public static Agendamento criaAgendamento(LocalDateTime dataHoraProva) {

		return new Agendamento(dataHoraProva).setAluno(criaAluno().persiste()).setCategoria(CategoriaExame.CARRO)
				.setNomeExaminador(NOME_EXAMINADOR).adicionaVeiculo(criaVeiculo().persiste());
	}

}
