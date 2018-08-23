package br.edu.faculdadedelta.modelo;

import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.edu.faculdadedelta.modelo.base.BaseEntity;
import br.edu.faculdadedelta.tipo.TipoVeiculo;
import br.edu.faculdadedelta.tipo.base.TipoEdicaoCRUD;
import br.edu.faculdadedelta.util.ValidadorUtil;

@Entity
@Table(name = "tb_veiculo")
public class Veiculo extends BaseEntity<String> {

	private static final long serialVersionUID = 775447920782851623L;

	public static class Atributos {
		private Atributos() {}

		public static final String ID = "id";
		public static final String MARCA = "marca";
		public static final String MODELO = "modelo";
		public static final String ANO = "ano";
		public static final String COR = "cor";
		public static final String PLACA = "placa";
		public static final String TIPO = "tipo";
	}
	
	@Id
	@GeneratedValue(generator = "UUIDGenerator")
	@GenericGenerator(name = "UUIDGenerator", strategy = "br.edu.faculdadedelta.modelo.base.UUIDGenerator")
	@Column(name = "id_veiculo", length = 32)
	private String id;

	@Column(name = "nm_marca", nullable = false, length = 100)
	private String marca;

	@Column(name = "nm_modelo", nullable = false, length = 150)
	private String modelo;

	@Basic(fetch = FetchType.LAZY, optional = false)
	@Column(name = "nu_ano")
	private Integer ano;

	@Basic(fetch = FetchType.LAZY, optional = false)
	@Column(name = "nm_cor", length = 50)
	private String cor;

	@Basic(fetch = FetchType.LAZY, optional = false)
	@Column(name = "nm_placa", length = 8)
	private String placa;

	@Enumerated(EnumType.STRING)
	@Basic(fetch = FetchType.LAZY, optional = false)
	@Column(length = 5)
	private TipoVeiculo tipo;
	
	public Veiculo() {

	}
	
	public Veiculo(String marca) {

		this.marca = marca;
	}
	
	public Veiculo(String id, String marca) {
		
		this.id = id;
		this.marca = marca;
	}
	
	@Override
	public String getId() {

		return id;
	}
	
	public String getMarca() {

		return marca;
	}

	public Veiculo setMarca(String marca) {

		this.marca = marca;
		return this;
	}

	public String getModelo() {

		return modelo;
	}

	public Veiculo setModelo(String modelo) {

		this.modelo = modelo;
		return this;
	}

	public Integer getAno() {

		return ano;
	}

	public Veiculo setAno(Integer ano) {

		if(ano == null)
			throw new IllegalStateException("Ano de fabricação deve ser informado");
		
		if(tipo == null) 
			throw new IllegalStateException("O tipo de veículo deve ser informado previamente");
			
		if(tipo == TipoVeiculo.CARRO && calculaIdade(ano) > 8) 
			throw new IllegalStateException("Carros não devem ter mais que 8 anos uso");
		
		if(tipo == TipoVeiculo.MOTO && calculaIdade(ano) > 5) 
			throw new IllegalStateException("Motos não devem ter mais que 5 anos uso");
		
		this.ano = ano;
		return this;
	}

	public String getCor() {

		return cor;
	}

	public Veiculo setCor(String cor) {

		this.cor = cor;
		return this;
	}

	public String getPlaca() {

		return placa;
	}

	public Veiculo setPlaca(String placa) {

		this.placa = placa;
		return this;
	}
	
	public TipoVeiculo getTipo() {

		return tipo;
	}

	public Veiculo setTipo(TipoVeiculo tipo) {

		this.tipo = tipo;
		return this;
	}
	
	@Override
	public void validaDados(TipoEdicaoCRUD tipoEdicao) {
		
		if (marca == null || marca.isEmpty())
			throw new IllegalStateException("Marca deve ser informada");

		if (marca.length() > 100)
			throw new IllegalStateException("Marca não pode exceder 100 caracteres");

		if (modelo == null || modelo.isEmpty())
			throw new IllegalStateException("Modelo deve ser informado");

		if (modelo.length() > 150)
			throw new IllegalStateException("Modelo não pode exceder 150 caracteres");
		
		if (cor == null || cor.isEmpty())
			throw new IllegalStateException("Cor deve ser informada");

		if (cor.length() > 50)
			throw new IllegalStateException("Cor não pode exceder 150 caracteres");
		
		if (tipo == null)
			throw new IllegalStateException("Tipo deve ser informado");
		
		if(ano == null)
			throw new IllegalStateException("Ano de fabricação deve ser informado");
		
		if(!ValidadorUtil.isPlacaDeVeiculoValida(placa))
			throw new IllegalStateException("Placa inválida! Utilizar formato: AAA-9999");
	}
	
	private Integer calculaIdade(Integer ano) {
		return LocalDate.now().getYear() - ano;
	}
	
	public Integer getIdadeIdade() {
		return calculaIdade(getAno());
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