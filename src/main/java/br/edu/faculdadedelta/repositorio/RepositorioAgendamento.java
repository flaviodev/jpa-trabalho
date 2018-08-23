package br.edu.faculdadedelta.repositorio;

import java.util.Date;

import br.edu.faculdadedelta.modelo.AgendamentoDeExame;
import br.edu.faculdadedelta.modelo.BancaExaminadora;
import br.edu.faculdadedelta.modelo.ProcessoHabilitacao;
import br.edu.faculdadedelta.repositorio.base.Repositorio;

public interface RepositorioAgendamento extends Repositorio {

	AgendamentoDeExame getAgendamentoPelaBancaEDataHora(BancaExaminadora banca, Date dataHora);

	AgendamentoDeExame getAgendamentoPelaBancaEProcesso(BancaExaminadora banca, ProcessoHabilitacao processo);

}
