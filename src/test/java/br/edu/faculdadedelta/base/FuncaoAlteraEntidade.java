package br.edu.faculdadedelta.base;

import java.io.Serializable;

import br.edu.faculdadedelta.modelo.BaseEntity;

public interface FuncaoAlteraEntidade<I extends Serializable, E extends BaseEntity<I>> {

	void altera(E entidade);
}
