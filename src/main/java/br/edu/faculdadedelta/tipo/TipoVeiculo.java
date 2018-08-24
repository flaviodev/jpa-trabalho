package br.edu.faculdadedelta.tipo;

public enum TipoVeiculo {
	CARRO(CategoriaExame.CARRO), MOTO(CategoriaExame.MOTO);

	private CategoriaExame categoria;

	private TipoVeiculo(CategoriaExame categoria) {
		this.categoria = categoria;
	}

	public CategoriaExame getCategoria() {
		return categoria;
	}

}
