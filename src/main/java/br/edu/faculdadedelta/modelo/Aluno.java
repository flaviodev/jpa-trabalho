package br.edu.faculdadedelta.modelo;

import static br.edu.faculdadedelta.util.DateUtil.toDate;
import static br.edu.faculdadedelta.util.DateUtil.toLocalDate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.edu.faculdadedelta.tipo.Sexo;
import br.edu.faculdadedelta.tipo.base.TipoEdicaoCRUD;

@Entity
public class Aluno extends Pessoa {

	private static final long serialVersionUID = 5896447923572856584L;

	public static class Atributos {
		private Atributos() {
		}

		public static final String ID = "id";
		public static final String NOME = "nome";
		public static final String CPF = "cpf";
		public static final String DATA_NASCIMENTO = "dataNascimento";
		public static final String SEXO = "sexo";
	}

	@Temporal(TemporalType.DATE)
	@Basic(fetch = FetchType.LAZY, optional = false)
	@Column(name = "dt_nascimento")
	private Date dataNascimento;

	@Enumerated(EnumType.STRING)
	@Basic(fetch = FetchType.LAZY, optional = false)
	@Column(length = 9)
	private Sexo sexo;

	public Aluno() {

	}

	public Aluno(String id, String nome) {

		super(id, nome);
	}

	public Aluno(String nome) {

		super(nome);
	}

	public Date getDataNascimento() {

		return dataNascimento;
	}

	public Aluno setDataNascimento(Date dataNascimento) {

		this.dataNascimento = dataNascimento;
		return this;
	}

	public Aluno setDataNascimento(LocalDate dataNascimento) {

		return setDataNascimento(toDate(dataNascimento));
	}

	public Sexo getSexo() {

		return sexo;
	}

	public Aluno setSexo(Sexo sexo) {

		this.sexo = sexo;
		return this;
	}

	@Override
	public void validaDados(TipoEdicaoCRUD tipo) {
		super.validaDados(tipo);
		
		if (dataNascimento == null)
			throw new IllegalStateException("Data de nascimento deve ser informada");

		if (dataNascimento.after(new Date()))
			throw new IllegalStateException("Data de nascimento não pode ser maior que hoje");
			
		long diferencaEmAnos = ChronoUnit.YEARS.between(toLocalDate(getDataNascimento()), LocalDate.now());

		if (diferencaEmAnos < 17)
			throw new IllegalStateException("Aluno não pode ter menos de 17 anos");
	}

	@Override
	public Aluno setNome(String nome) {

		return (Aluno) super.setNome(nome);
	}

	@Override
	public Aluno setCpf(String cpf) {

		return (Aluno) super.setCpf(cpf);
	}

	@Override
	public boolean equals(Object obj) {

		return super.equals(obj);
	}

	@Override
	public int hashCode() {

		return super.hashCode();
	}
}