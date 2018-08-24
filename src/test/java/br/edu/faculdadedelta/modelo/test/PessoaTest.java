package br.edu.faculdadedelta.modelo.test;

import static org.junit.Assert.fail;

import java.util.stream.IntStream;

import org.junit.AfterClass;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.Aluno;
import br.edu.faculdadedelta.modelo.Pessoa;
import br.edu.faculdadedelta.test.base.BaseCrudTest;
import br.edu.faculdadedelta.test.base.BaseTest;
import br.edu.faculdadedelta.test.util.FabricaTeste;

public class PessoaTest extends BaseTest {

	public Pessoa getEntidadeParaTeste() {

		return FabricaTeste.criaAluno();
	}

	@Test(expected = IllegalStateException.class)
	public void naoDeveSalvarComNomeNulo() {

		Pessoa pessoa = getEntidadeParaTeste();
		pessoa.setNome(null);

		pessoa.persiste();

		fail("deveria disparar IllegalStateException na validação dos dados");
	}

	@Test(expected = IllegalStateException.class)
	public void naoDeveSalvarComNomeVazio() {

		Pessoa pessoa = getEntidadeParaTeste();
		pessoa.setNome("");

		pessoa.persiste();

		fail("deveria disparar IllegalStateException na validação dos dados");
	}

	@Test(expected = IllegalStateException.class)
	public void naoDeveSalvarComNomeMuitoGrande() {

		Pessoa pessoa = getEntidadeParaTeste();

		StringBuilder nomeComMaisDe150Caracteres = new StringBuilder(pessoa.getNome());
		IntStream.range(0, 151).forEach(i -> nomeComMaisDe150Caracteres.append("-"));
		pessoa.setNome(nomeComMaisDe150Caracteres.toString());

		pessoa.persiste();

		fail("deveria disparar IllegalStateException na validação dos dados");
	}

	@Test(expected = IllegalArgumentException.class)
	public void naoDeveSalvarComCpfNulo() {

		Pessoa pessoa = getEntidadeParaTeste();
		pessoa.setCpf(null);

		pessoa.persiste();

		fail("deveria disparar IllegalStateException na validação dos dados");
	}

	@Test(expected = IllegalStateException.class)
	public void naoDeveSalvarComCpfInvalido() {

		Pessoa pessoa = getEntidadeParaTeste();
		pessoa.setCpf("123.123.123-00");

		pessoa.persiste();

		fail("deveria disparar IllegalStateException na validação dos dados");
	}

	@Test(expected = IllegalStateException.class)
	public void naoDeveSalvarComFormatoCpfInvalido() {

		Pessoa pessoa = getEntidadeParaTeste();
		pessoa.setCpf("1231.23.123-00");

		pessoa.persiste();

		fail("deveria disparar IllegalStateException na validação dos dados");
	}

	@Test(expected = IllegalStateException.class)
	public void naoDeveSalvarComTamanhoCpfInvalido() {

		Pessoa pessoa = getEntidadeParaTeste();
		pessoa.setCpf("1123.123.123-00");

		pessoa.persiste();

		fail("deveria disparar IllegalStateException na validação dos dados");
	}

	@Test(expected = IllegalStateException.class)
	public void naoDeveSalvarComCaracteresDoCpfInvalido() {

		Pessoa pessoa = getEntidadeParaTeste();
		pessoa.setCpf("AAA.123.%%%-00");

		pessoa.persiste();

		fail("deveria disparar IllegalStateException na validação dos dados");
	}

	@AfterClass
	public static void deveLimparBase() {

		BaseCrudTest.deveLimparBase(Aluno.class);
	}
}