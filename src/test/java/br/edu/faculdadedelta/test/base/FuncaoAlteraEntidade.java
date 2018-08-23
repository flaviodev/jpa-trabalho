package br.edu.faculdadedelta.test.base;

import java.io.Serializable;

import br.edu.faculdadedelta.modelo.base.BaseEntity;

public interface FuncaoAlteraEntidade<I extends Serializable, E extends BaseEntity<I>> {

	void altera(E entidade);
}
