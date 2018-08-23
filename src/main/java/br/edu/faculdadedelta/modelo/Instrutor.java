package br.edu.faculdadedelta.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import br.edu.faculdadedelta.tipo.StatusInstrutor;
import br.edu.faculdadedelta.tipo.base.TipoEdicaoCRUD;

@Entity
public class Instrutor extends Pessoa {

	private static final long serialVersionUID = 1476447923572856584L;

	public static class Atributos {
		private Atributos() {
		}

		public static final String ID = "id";
		public static final String NOME = "nome";
		public static final String CPF = "cpf";
		public static final String STATUS = "status";
	}

	@Enumerated(EnumType.STRING)
	@Basic(fetch = FetchType.LAZY, optional = false)
	@Column(length = 7)
	private StatusInstrutor status;
	
	@OneToMany(mappedBy = "instrutor", fetch = FetchType.LAZY)
	private List<ProcessoHabilitacao> processos;

	public Instrutor() {

	}

	public Instrutor(String id, String nome) {

		super(id, nome);
	}

	public Instrutor(String nome) {

		super(nome);
	}

	public StatusInstrutor getStatus() {

		return status;
	}

	public Instrutor setStatus(StatusInstrutor status) {

		this.status = status;
		return this;
	}

	@Override
	public Instrutor setNome(String nome) {

		return (Instrutor) super.setNome(nome);
	}

	@Override
	public Instrutor setCpf(String cpf) {

		return (Instrutor) super.setCpf(cpf);
	}

	public List<ProcessoHabilitacao> getProcessos() {
		
		if(processos == null)
			processos = new ArrayList<>();
		
		return processos;
	}

	public void setProcessos(List<ProcessoHabilitacao> processos) {
		
		this.processos = processos;
	}
	
	@Override
	public void validaDados(TipoEdicaoCRUD tipo) {
		super.validaDados(tipo);

		if (status == null)
			throw new IllegalStateException("Status deve ser informado");

		if (tipo == TipoEdicaoCRUD.INCLUSAO && status == StatusInstrutor.INATIVO)
			throw new IllegalStateException("Não é permitido INCLUIR um instrutor com status inativo");
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