package br.edu.faculdadedelta.modelo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public abstract class Pessoa extends BaseEntity<String> {

	private static final long serialVersionUID = 6548447923572856584L;

	@Id
	@GeneratedValue(generator = "UUIDGenerator")
	@GenericGenerator(name = "UUIDGenerator", strategy = "br.edu.faculdadedelta.util.UUIDGenerator")
	@Column(name = "id_pessoa", length = 32)
	private String id;

	@Basic(optional = false)
	@Column(length = 150)
	private String nome;

	@Basic(fetch = FetchType.LAZY, optional = false)
	@Column(length = 20)
	private String cpf;

	public Pessoa() {

	}

	public Pessoa(String nome) {

		this.nome = nome;
	}

	public Pessoa(String id, String nome) {

		this.id = id;
		this.nome = nome;
	}

	@Override
	public String getId() {

		return id;
	}

	public String getNome() {

		return nome;
	}

	public Pessoa setNome(String nome) {

		this.nome = nome;
		return this;
	}

	public String getCpf() {

		return cpf;
	}

	public Pessoa setCpf(String cpf) {

		this.cpf = cpf;
		return this;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (!super.equals(obj))
			return false;

		if (getClass() != obj.getClass())
			return false;

		Pessoa other = (Pessoa) obj;
		if (cpf == null) {
			if (other.cpf != null) {
				return false;
			}
		} else if (!cpf.equals(other.cpf)) {
			return false;
		}

		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}

		return true;
	}

}