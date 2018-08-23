package br.edu.faculdadedelta.modelo;

import static br.edu.faculdadedelta.util.DateUtil.toDate;
import static br.edu.faculdadedelta.util.DateUtil.toLocalDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import br.edu.faculdadedelta.modelo.base.BaseEntity;
import br.edu.faculdadedelta.repositorio.RepositorioAgendamento;
import br.edu.faculdadedelta.tipo.base.TipoEdicaoCRUD;

@Entity
@Table(name = "tb_banca")
public class BancaExaminadora extends BaseEntity<String> {

	private static final long serialVersionUID = 123447920782851623L;
	
	public static class Atributos {
		private Atributos() {
		}

		public static final String ID = "id";
		public static final String DATA = "data";
		public static final String EXAMINADORES = "examinadores";
	}
	
	@Transient
	private RepositorioAgendamento repositorioAgendamento = getRepositorio(RepositorioAgendamento.class);

	@Id
	@GeneratedValue(generator = "UUIDGenerator")
	@GenericGenerator(name = "UUIDGenerator", strategy = "br.edu.faculdadedelta.modelo.base.UUIDGenerator")
	@Column(name = "id_banca", length = 32)
	private String id;

	@Temporal(TemporalType.DATE)
	@Basic(fetch = FetchType.LAZY, optional = false)
	@Column(name = "dt_exame")
	private Date data;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "banca_examinador", joinColumns = @JoinColumn(name = "id_banca"), inverseJoinColumns = @JoinColumn(name = "id_examinador"))
	private List<Examinador> examinadores;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "banca_agendamento", joinColumns = @JoinColumn(name = "id_banca"), inverseJoinColumns = @JoinColumn(name = "id_agendamento"))
	private List<AgendamentoDeExame> agendamentos;

	public BancaExaminadora() {

	}

	public BancaExaminadora(Date data) {

		setData(data);
	}

	public BancaExaminadora(String id, Date data) {

		this.id = id;
		setData(data);
	}

	public BancaExaminadora(LocalDate data) {

		setData(data);
	}

	public BancaExaminadora(String id, LocalDate data) {

		this.id = id;
		setData(data);
	}

	@Override
	public String getId() {

		return id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BancaExaminadora setData(LocalDate data) {

		setData(toDate(data));
		return this;
	}

	public List<Examinador> getExaminadores() {

		if (examinadores == null)
			examinadores = new ArrayList<>();

		return examinadores;
	}

	public void setExaminadores(List<Examinador> examinadores) {

		this.examinadores = examinadores;
	}

	public BancaExaminadora adicionaExaminador(Examinador examinador) {

		if (examinador == null)
			throw new IllegalArgumentException("Examinador não pode ser nulo");

		if (getExaminadores().contains(examinador))
			throw new IllegalArgumentException("Examinador já pertence a banca");

		getExaminadores().add(examinador);

		return this;
	}

	public BancaExaminadora retiraExaminador(Examinador examinador) {

		if (examinador == null)
			throw new IllegalArgumentException("Examinador não pode ser nulo");

		if (!getExaminadores().contains(examinador))
			throw new IllegalArgumentException("Examinador não pertence a banca");

		getExaminadores().remove(examinador);

		return this;
	}

	public List<AgendamentoDeExame> getAgendamentos() {

		if (agendamentos == null)
			agendamentos = new ArrayList<>();

		return agendamentos;
	}

	public void setAgendamentos(List<AgendamentoDeExame> agendamentos) {

		this.agendamentos = agendamentos;
	}

	public AgendamentoDeExame agendaExame(LocalDateTime dataHora, ProcessoHabilitacao processo) {

		if (isTransient())
			throw new IllegalArgumentException("Banca examinadora deve estar persistida para efetuar agendamento");

		if (processo == null)
			throw new IllegalArgumentException("Processo habilitação não pode ser nulo");

		AgendamentoDeExame agendamentoJaExistente = repositorioAgendamento.getAgendamentoPelaBancaEDataHora(this, toDate(dataHora));
		
		if(agendamentoJaExistente != null)
			throw new IllegalArgumentException("Já existe agendamento para o horário informado");
				
		AgendamentoDeExame agendamento = new AgendamentoDeExame(dataHora).setBanca(this).setProcesso(processo)
				.persiste();

		getAgendamentos().add(agendamento);

		return agendamento;
	}

	public AgendamentoDeExame cancelaAgendamentoDeExame(ProcessoHabilitacao processo) {

		if (isTransient())
			throw new IllegalArgumentException("Banca examinadora deve estar persistida para efetuar cancelamento de agendamento");
		
		if (processo == null)
			throw new IllegalArgumentException("Processo habilitação não pode ser nulo");

		AgendamentoDeExame agendamentoACancelar = repositorioAgendamento.getAgendamentoPelaBancaEProcesso(this, processo);
		
		if(agendamentoACancelar == null)
			throw new IllegalArgumentException("Não há agendamento nesta banca para o processo informado");
		
		getAgendamentos().remove(agendamentoACancelar);
		
		agendamentoACancelar.exclui();
		
		return agendamentoACancelar;
	}

	@Override
	public void validaDados(TipoEdicaoCRUD tipoEdicao) {

		if (data == null)
			throw new IllegalStateException("Data da banca deve ser informada");

		if (toLocalDate(data).isBefore(LocalDate.now()))
			throw new IllegalStateException("Data da banca não pode ser menor que hoje");
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