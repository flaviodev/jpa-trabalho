package br.edu.faculdadedelta.repositorio;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.edu.faculdadedelta.modelo.AgendamentoDeExame;
import br.edu.faculdadedelta.modelo.BancaExaminadora;
import br.edu.faculdadedelta.modelo.ProcessoHabilitacao;
import br.edu.faculdadedelta.repositorio.base.RepositorioHibernateBase;

public class RepositorioAgendamentoImpl extends RepositorioHibernateBase implements RepositorioAgendamento {

	private static final long serialVersionUID = -4774352639597724214L;

	@Override
	public AgendamentoDeExame getAgendamentoPelaBancaEDataHora(BancaExaminadora banca, Date dataHora) {

		Criteria criteria = createCriteria(AgendamentoDeExame.class);
		criteria.add(Restrictions.eq(AgendamentoDeExame.Atributos.BANCA, banca));
		criteria.add(Restrictions.eq(AgendamentoDeExame.Atributos.DATA_HORA, dataHora));

		return (AgendamentoDeExame) criteria.uniqueResult();
	}

	@Override
	public AgendamentoDeExame getAgendamentoPelaBancaEProcesso(BancaExaminadora banca, ProcessoHabilitacao processo) {

		Criteria criteria = createCriteria(AgendamentoDeExame.class);
		criteria.add(Restrictions.eq(AgendamentoDeExame.Atributos.BANCA, banca));
		criteria.add(Restrictions.eq(AgendamentoDeExame.Atributos.PROCESSO, processo));

		return (AgendamentoDeExame) criteria.uniqueResult();
	}
}
