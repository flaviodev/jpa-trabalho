package br.edu.faculdadedelta.modelo;

import static br.edu.faculdadedelta.util.DateUtil.toDate;
import static br.edu.faculdadedelta.util.DateUtil.toLocalDate;

import java.time.LocalDate;
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

import org.hibernate.annotations.GenericGenerator;

import br.edu.faculdadedelta.modelo.base.BaseEntity;
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

	public void setData(LocalDate data) {

		setData(toDate(data));
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