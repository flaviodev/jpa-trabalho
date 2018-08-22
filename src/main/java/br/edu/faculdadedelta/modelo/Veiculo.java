package br.edu.faculdadedelta.modelo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

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
	}
	
	@Id
	@GeneratedValue(generator = "UUIDGenerator")
	@GenericGenerator(name = "UUIDGenerator", strategy = "br.edu.faculdadedelta.util.UUIDGenerator")
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
	
	@Override
	public boolean equals(Object obj) {
		
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		
		return super.hashCode();
	}
}