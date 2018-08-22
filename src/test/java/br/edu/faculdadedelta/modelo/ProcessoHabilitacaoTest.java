package br.edu.faculdadedelta.modelo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;

import br.edu.faculdadedelta.base.BaseCrudTest;
import br.edu.faculdadedelta.base.FuncaoAlteraEntidade;
import br.edu.faculdadedelta.base.FuncaoCriterioParaBuscaDeEntidade;
import br.edu.faculdadedelta.tipo.Sexo;
import br.edu.faculdadedelta.tipo.StatusInstrutor;
import br.edu.faculdadedelta.tipo.TipoVeiculo;

public class ProcessoHabilitacaoTest extends BaseCrudTest<String, ProcessoHabilitacao> {

	private static final LocalDate DATA_ABERTURA_PADARO = LocalDate.of(2018, 8, 10);

	@Override
	public ProcessoHabilitacao getInstanciaDaEntidade() {

		getEntityManager().getTransaction().begin();

		Aluno aluno = new Aluno("Sebastião Antônio").setCpf("333.333.333-333")
				.setDataNascimento(LocalDate.of(1964, 2, 12)).setSexo(Sexo.MASCULINO);
		getEntityManager().persist(aluno);

		Veiculo veiculo = new Veiculo("Honda").setModelo("GC 125 Fan").setAno(2016).setCor("Vermelha")
				.setPlaca("CCC-3333").setTipo(TipoVeiculo.MOTO);
		getEntityManager().persist(veiculo);

		Instrutor instrutor = new Instrutor("João da Silveira").setCpf("444.444.444-44")
				.setStatus(StatusInstrutor.ATIVO);
		getEntityManager().persist(instrutor);
		getEntityManager().getTransaction().commit();

		return new ProcessoHabilitacao(DATA_ABERTURA_PADARO).setAluno(aluno).setVeiculo(veiculo)
				.setInstrutor(instrutor);
	}

	public FuncaoAlteraEntidade<String, ProcessoHabilitacao> alteracaoEntidade() {
		return (processo) -> processo.setDataAbertura(LocalDate.of(2018, 8, 15));
	}

	@Override
	public FuncaoCriterioParaBuscaDeEntidade<String, ProcessoHabilitacao> getCriterioBuscaEntidades() {
		return () -> Restrictions.eq(ProcessoHabilitacao.Atributos.DATA_ABERTURA,
				Date.from(DATA_ABERTURA_PADARO.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}

	@AfterClass
	public static void deveLimparBase() {

		deveLimparBase(ProcessoHabilitacao.class);
		deveLimparBase(Aluno.class);
		deveLimparBase(Veiculo.class);
		deveLimparBase(Instrutor.class);
	}
}