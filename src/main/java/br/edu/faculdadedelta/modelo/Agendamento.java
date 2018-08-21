package br.edu.faculdadedelta.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Agendamento extends BaseEntity<Long> {
	
	private static final long serialVersionUID = 3254479207828575981L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name= "id_venda")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dh_venda", nullable = false)
	private Date dataHora;

	@ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente", nullable = false, insertable = true, updatable = false)
	private Aluno cliente;
	
	@ManyToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinTable(name = "venda_produto",
			joinColumns = @JoinColumn(name = "id_venda"),
			inverseJoinColumns = @JoinColumn(name = "id_produto"))
	private List<Veiculo> produtos;
	
	@Override
	public Long getId() {
		
		return this.id;
	}

	public Aluno getCliente() {
		
		return this.cliente;
	}

	public void setCliente(Aluno cliente) {
		
		this.cliente = cliente;
	}

	public List<Veiculo> getProdutos() {

		if(produtos == null)
			produtos = new ArrayList<>();
		
		return produtos;
	}

	public Date getDataHora() {
		
		return this.dataHora;
	}

	public void setDataHora(Date dataHora) {

		this.dataHora = dataHora;
	}
		
}