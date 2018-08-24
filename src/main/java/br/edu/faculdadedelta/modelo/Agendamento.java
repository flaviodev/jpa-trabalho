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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import br.edu.faculdadedelta.modelo.base.BaseEntity;
import br.edu.faculdadedelta.tipo.CategoriaExame;
import br.edu.faculdadedelta.tipo.base.TipoEdicaoCRUD;
import br.edu.faculdadedelta.util.StringUtil;

@Entity
public class Agendamento extends BaseEntity<String> {

	private static final long serialVersionUID = 123447920782851623L;

	public static class Atributos {
		private Atributos() {
		}

		public static final String ID = "id";
		public static final String DATA_HORA_PROVA = "dataHoraProva";
		public static final String ALUNO = "aluno";
		public static final String CATEGORIA = "categoria";
		public static final String NOME_EXAMINADOR = "nomeExaminador";
		public static final String VEICULOS = "veiculos";
	}

	@Id
	@GeneratedValue(generator = "UUIDGenerator")
	@GenericGenerator(name = "UUIDGenerator", strategy = "br.edu.faculdadedelta.modelo.base.UUIDGenerator")
	@Column(name = "id_agendamento", length = 32)
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	@Basic(optional = false)
	@Column(name = "dh_prova")
	private Date dataHoraProva;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Aluno aluno;

	@Enumerated(EnumType.STRING)
	@Basic(fetch = FetchType.LAZY, optional = false)
	@Column(length = 6)
	private CategoriaExame categoria;

	@Basic(fetch = FetchType.LAZY, optional = false)
	@Column(length = 150)
	private String nomeExaminador;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "agendamento_veiculo", joinColumns = @JoinColumn(name = "id_agendamento"), inverseJoinColumns = @JoinColumn(name = "id_veiculo"))
	private List<Veiculo> veiculos;

	public Agendamento() {

	}

	public Agendamento(Date dataHoraProva) {

		setDataHoraProva(dataHoraProva);
	}

	public Agendamento(String id, Date dataHoraProva) {

		this.id = id;
		setDataHoraProva(dataHoraProva);
	}

	public Agendamento(LocalDateTime dataHoraProva) {

		setDataHoraProva(dataHoraProva);
	}

	public Agendamento(String id, LocalDateTime dataHoraProva) {

		this.id = id;
		setDataHoraProva(dataHoraProva);
	}

	@Override
	public String getId() {

		return id;
	}

	public Date getDataHoraProva() {

		return dataHoraProva;
	}

	public Agendamento setDataHoraProva(Date dataHoraProva) {

		this.dataHoraProva = dataHoraProva;
		return this;
	}

	public Agendamento setDataHoraProva(LocalDateTime dataHoraProva) {

		if (dataHoraProva == null)
			throw new IllegalArgumentException("Data/hora da prova deve ser informada");

		return setDataHoraProva(toDate(dataHoraProva));
	}

	public Aluno getAluno() {

		return aluno;
	}

	public Agendamento setAluno(Aluno aluno) {

		this.aluno = aluno;
		return this;
	}

	public CategoriaExame getCategoria() {

		return categoria;
	}

	public Agendamento setCategoria(CategoriaExame categoria) {

		this.categoria = categoria;
		return this;
	}

	public String getNomeExaminador() {

		return nomeExaminador;
	}

	public Agendamento setNomeExaminador(String nomeExaminador) {

		this.nomeExaminador = nomeExaminador;
		return this;
	}

	public List<Veiculo> getVeiculos() {

		if (veiculos == null)
			veiculos = new ArrayList<>();

		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {

		this.veiculos = veiculos;
	}

	public Agendamento adicionaVeiculo(Veiculo veiculo) {

		if (veiculo == null)
			throw new IllegalArgumentException("Veículo deve ser informado");

		if (veiculo.isTransient())
			throw new IllegalArgumentException("Veículo informado não está salvo");

		if (getVeiculos().contains(veiculo))
			throw new IllegalArgumentException("Veículo já pertence ao agendamento");

		getVeiculos().add(veiculo);

		return this;
	}

	public Agendamento retiraVeiculo(Veiculo veiculo) {

		if (veiculo == null)
			throw new IllegalArgumentException("Veículo deve ser informado");

		if (veiculo.isTransient())
			throw new IllegalArgumentException("Veículo informado não está salvo");

		if (!getVeiculos().contains(veiculo))
			throw new IllegalArgumentException("Veículo não pertence ao agendamento");

		getVeiculos().remove(veiculo);

		return this;
	}

	@Override
	public void validaDados(TipoEdicaoCRUD tipoEdicao) {

		if (dataHoraProva == null)
			throw new IllegalStateException("Data/hora da prova deve ser informada");

		if (toLocalDate(dataHoraProva).isBefore(LocalDate.now()))
			throw new IllegalStateException("Data da prova não pode ser anterior a hoje");

		if (nomeExaminador == null || nomeExaminador.trim().isEmpty())
			throw new IllegalStateException("Nome do examinador deve ser informado");

		if (nomeExaminador.length() > 150)
			throw new IllegalStateException("Nome do examinador não pode exceder 150 caracteres");

		validaCategoriaEVeiculos();
	}

	private void validaCategoriaEVeiculos() {

		if (categoria == null)
			throw new IllegalStateException("Categoria deve ser informada");

		if (categoria == CategoriaExame.AMBOS && getVeiculos().size() != 2)
			throw new IllegalStateException(
					"Para agendamento de prova de ambas categorias é preciso informar 2 veículos no agendamento");

		if (categoria != CategoriaExame.AMBOS && getVeiculos().size() != 1)
			throw new IllegalStateException(StringUtil.concat("Para prova de ", categoria.toString(),
					" deve-se informar somente o veículo correspondente à categoria no agendamento"));

		if (categoria == CategoriaExame.AMBOS && getVeiculos().get(0).getTipo() == getVeiculos().get(1).getTipo())
			throw new IllegalStateException(
					"Para agendamento de prova de ambas categorias deve-se informar uma moto e um carro nos veículos");

		if (categoria != CategoriaExame.AMBOS && getVeiculos().get(0).getTipo().getCategoria() != categoria)
			throw new IllegalStateException(
					"O tipo de veículo deve ser correspondente à categoria informada no agendamente da prova");

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