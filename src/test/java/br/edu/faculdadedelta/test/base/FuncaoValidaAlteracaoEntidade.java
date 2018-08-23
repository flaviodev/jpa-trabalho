package br.edu.faculdadedelta.test.base;

import java.io.Serializable;

import br.edu.faculdadedelta.modelo.base.BaseEntity;

public interface FuncaoValidaAlteracaoEntidade<I extends Serializable, E extends BaseEntity<I>> {

	void validaAlteracao(E entidade);
}
