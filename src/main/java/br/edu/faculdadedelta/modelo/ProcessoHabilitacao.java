package br.edu.faculdadedelta.modelo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ent_processo_habilitacao")
public class ProcessoHabilitacao extends BaseEntity<String> {

	private static final long serialVersionUID = 123447920782851987L;

	public static class Atributos {
		private Atributos() {
		}

		public static final String ID = "id";
		public static final String DATA_ABERTURA = "dataAbertura";
		public static final String ALUNO = "aluno";
		public static final String VEICULO = "veiculo";
		public static final String INSTRUTOR = "instrutor";
	}

	@Id
	@GeneratedValue(generator = "UUIDGenerator")
	@GenericGenerator(name = "UUIDGenerator", strategy = "br.edu.faculdadedelta.util.UUIDGenerator")
	@Column(name = "id_processo", length = 32)
	private String id;

	@Temporal(TemporalType.DATE)
	@Basic(fetch = FetchType.LAZY, optional = false)
	@Column(name = "dt_abertura")
	private Date dataAbertura;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Aluno aluno;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Veiculo veiculo;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Instrutor instrutor;

	public ProcessoHabilitacao() {

	}

	public ProcessoHabilitacao(Date dataAbertura) {

		setDataAbertura(dataAbertura);
	}

	public ProcessoHabilitacao(String id, Date dataAbertura) {

		this.id = id;
		setDataAbertura(dataAbertura);
	}

	public ProcessoHabilitacao(LocalDate dataAbertura) {

		setDataAbertura(dataAbertura);
	}

	public ProcessoHabilitacao(String id, LocalDate dataAbertura) {

		this.id = id;
		setDataAbertura(dataAbertura);
	}

	@Override
	public String getId() {

		return id;
	}

	public Date getDataAbertura() {

		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {

		if (dataAbertura == null)
			throw new IllegalArgumentException("Data de abertura não pode ser nula");

		this.dataAbertura = dataAbertura;
	}

	public void setDataAbertura(LocalDate dataAbertura) {

		if (dataAbertura == null)
			throw new IllegalArgumentException("Data de abertura não pode ser nula");

		setDataAbertura(Date.from(dataAbertura.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}

	public Aluno getAluno() {

		return aluno;
	}

	public ProcessoHabilitacao setAluno(Aluno aluno) {

		this.aluno = aluno;
		return this;
	}

	public Veiculo getVeiculo() {

		return veiculo;
	}

	public ProcessoHabilitacao setVeiculo(Veiculo veiculo) {

		this.veiculo = veiculo;
		return this;
	}

	public Instrutor getInstrutor() {

		return instrutor;
	}

	public ProcessoHabilitacao setInstrutor(Instrutor instrutor) {

		this.instrutor = instrutor;
		return this;
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