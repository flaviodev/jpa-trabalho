package br.edu.faculdadedelta.modelo;

import static br.edu.faculdadedelta.util.DateUtil.toDate;
import static br.edu.faculdadedelta.util.DateUtil.toLocalDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import br.edu.faculdadedelta.modelo.base.BaseEntity;
import br.edu.faculdadedelta.tipo.base.TipoEdicaoCRUD;

@Entity
@Table(name = "tb_agendamento")
public class AgendamentoDeExame extends BaseEntity<String> {

	private static final long serialVersionUID = 123447920782851623L;

	public static class Atributos {
		private Atributos() {
		}

		public static final String ID = "id";
		public static final String DATA_HORA = "dataHora";
		public static final String BANCA = "banca";
		public static final String PROCESSO = "processo";
	}

	@Id
	@GeneratedValue(generator = "UUIDGenerator")
	@GenericGenerator(name = "UUIDGenerator", strategy = "br.edu.faculdadedelta.modelo.base.UUIDGenerator")
	@Column(name = "id_agendamento", length = 32)
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	@Basic(fetch = FetchType.LAZY, optional = false)
	@Column(name = "dh_agendamento")
	private Date dataHora;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private BancaExaminadora banca;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private ProcessoHabilitacao processo;

	public AgendamentoDeExame() {

	}

	public AgendamentoDeExame(Date dataHora) {

		setDataHora(dataHora);
	}

	public AgendamentoDeExame(String id, Date dataHora) {

		this.id = id;
		setDataHora(dataHora);
	}

	public AgendamentoDeExame(LocalDateTime dataHora) {

		setDataHora(dataHora);
	}

	public AgendamentoDeExame(String id, LocalDateTime dataHora) {

		this.id = id;
		setDataHora(dataHora);
	}

	@Override
	public String getId() {

		return id;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {

		setDataHora(toDate(dataHora));
	}

	public BancaExaminadora getBanca() {
		return banca;
	}

	public AgendamentoDeExame setBanca(BancaExaminadora banca) {
		this.banca = banca;
		return this;
	}

	public ProcessoHabilitacao getProcesso() {
		return processo;
	}

	public AgendamentoDeExame setProcesso(ProcessoHabilitacao processo) {
		this.processo = processo;
		return this;
	}

	@Override
	public void validaDados(TipoEdicaoCRUD tipoEdicao) {

		if (dataHora == null)
			throw new IllegalStateException("Data de agendamento deve ser informada");

		if (toLocalDate(dataHora).isBefore(LocalDate.now()))
			throw new IllegalStateException("Data do agendamento não pode ser menor que hoje");
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