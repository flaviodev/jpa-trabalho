package br.edu.faculdadedelta.modelo;

import javax.persistence.Entity;

@Entity
public class Examinador extends Pessoa {

	private static final long serialVersionUID = 6336447923572856584L;

	public static class Atributos {
		private Atributos() {
		}

		public static final String ID = "id";
		public static final String NOME = "nome";
		public static final String CPF = "cpf";
	}

	public Examinador() {

	}

	public Examinador(String id, String nome) {

		super(id, nome);
	}

	public Examinador(String nome) {

		super(nome);
	}

	@Override
	public Examinador setNome(String nome) {

		return (Examinador) super.setNome(nome);
	}

	@Override
	public Examinador setCpf(String cpf) {

		return (Examinador) super.setCpf(cpf);
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