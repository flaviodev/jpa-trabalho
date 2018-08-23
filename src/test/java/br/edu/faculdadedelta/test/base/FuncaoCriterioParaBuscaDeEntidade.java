package br.edu.faculdadedelta.test.base;

import java.io.Serializable;

import org.hibernate.criterion.Criterion;

import br.edu.faculdadedelta.modelo.base.BaseEntity;

public interface FuncaoCriterioParaBuscaDeEntidade<I extends Serializable, E extends BaseEntity<I>> {

	Criterion getCriterio();
}
