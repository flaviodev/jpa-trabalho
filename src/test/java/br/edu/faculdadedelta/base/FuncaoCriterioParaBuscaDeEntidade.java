package br.edu.faculdadedelta.base;

import java.io.Serializable;

import org.hibernate.criterion.Criterion;

import br.edu.faculdadedelta.modelo.BaseEntity;

public interface FuncaoCriterioParaBuscaDeEntidade<I extends Serializable, E extends BaseEntity<I>> {

	Criterion getCriterio();
}
