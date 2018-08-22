package br.edu.faculdadedelta.modelo;

import java.time.LocalDate;

import org.junit.Test;

import br.edu.faculdadedelta.base.BaseCrudTest;
import br.edu.faculdadedelta.tipo.Sexo;

public class AgendamentoTest extends BaseCrudTest<String, Aluno> {

	private static final String CPF_PADRAO = "111.111.111-11";

	@Override
	public Aluno getInstanciaDaEntidade() {

		return new Aluno("Joaquim Barcelos").setCpf(CPF_PADRAO).setSexo(Sexo.MASCULINO)
				.setDataNascimento(LocalDate.of(1980, 8, 12));
	}

	@Test
	public void deveAlterarEntidade() {
		
	}
	
//	@Test
//	public void deveConsultarQuantidadeDeProdutosVendidos() {
//
//		//Agendamento venda = criarVenda("010.020.030-04");
//
//		IntStream.range(0, 10).forEach(i -> venda.getProdutos().add(new Veiculo("Produto" + i, "Marca" + i)));
//
//		getEntityManager().getTransaction().begin();
//		getEntityManager().persist(venda);
//		getEntityManager().getTransaction().commit();
//
//		assertFalse("Deve ter persistido a venda", venda.isTransient());
//
//		int qtdProdutosAdicionados = venda.getProdutos().size();
//
//		assertTrue("Lista de produtos deve ter itens", qtdProdutosAdicionados > 0);
//
//		StringBuilder jpql = new StringBuilder();
//		jpql.append(" SELECT COUNT(p.id) ");
//		jpql.append(" FROM Venda v ");
//		jpql.append(" INNER JOIN v.produtos p ");
//		jpql.append(" INNER JOIN v.cliente c ");
//		jpql.append(" WHERE c.cpf = :cpf ");
//
//		Query query = getEntityManager().createQuery(jpql.toString());
//		query.setParameter("cpf", "010.020.030-04");
//
//		Long qtdProdutosDaVenda = (Long) query.getSingleResult();
//
//		assertEquals("quantidade de produtos deve ser igual a quantidade da lista", qtdProdutosDaVenda.intValue(),
//				qtdProdutosAdicionados);
//	}
//
//	@Test(expected = IllegalStateException.class)
//	public void naoDeveFazerMergeEmObjetosTransient() {
//
//		Agendamento venda = criarVenda();
//
//		venda.getProdutos().add(new Veiculo("Notebook", "Dell"));
//		venda.getProdutos().add(new Veiculo("Mouse", "Razer"));
//
//		assertTrue("Não deve ter id definido", venda.isTransient());
//
//		getEntityManager().getTransaction().begin();
//		venda = getEntityManager().merge(venda);
//		getEntityManager().getTransaction().commit();
//
//		fail("Não deveria ter salvo (merge) uma venda nova com relacionamentos transient");
//	}
//
//	@Test
//	public void deveSalvarVendaComRelacionamentosEmCascataForeach() {
//
//		Agendamento venda = criarVenda();
//
//		venda.getProdutos().add(new Veiculo("Notebook", "Dell"));
//		venda.getProdutos().add(new Veiculo("Mouse", "Razer"));
//
//		assertTrue("Não deve ter id definido", venda.isTransient());
//
//		getEntityManager().getTransaction().begin();
//		getEntityManager().persist(venda);
//		getEntityManager().getTransaction().commit();
//
//		assertFalse("Deve ter id definido", venda.isTransient());
//		assertFalse("Deve ter id definido", venda.getCliente().isTransient());
//
//		venda.getProdutos().forEach(produto -> assertFalse("Deve ter id definido", produto.isTransient()));
//	}
//
//	@Test
//	public void deveSalvarVendaComRelacionamentosEmCascata() {
//
//		Agendamento venda = criarVenda();
//
//		venda.getProdutos().add(new Veiculo("Notebook", "Dell"));
//		venda.getProdutos().add(new Veiculo("Mouse", "Razer"));
//
//		assertTrue("Não deve ter id definido", venda.isTransient());
//
//		getEntityManager().getTransaction().begin();
//		getEntityManager().persist(venda);
//		getEntityManager().getTransaction().commit();
//
//		assertFalse("Deve ter id definido", venda.isTransient());
//		assertFalse("Deve ter id definido", venda.getCliente().isTransient());
//
//		venda.getProdutos().forEach(produto -> assertFalse("Deve ter id definido", produto.isTransient()));
//	}
//
//	private Agendamento criarVenda() {
//
//		return criarVenda(null);
//	}
//
//	private Agendamento criarVenda(String cpf) {
//
//		Aluno cliente = new Aluno("Flavio de Souza", cpf == null ? CPF_PADRAO : cpf);
//		assertTrue("Não deve ter id definido", cliente.isTransient());
//
//		Agendamento venda = new Agendamento();
//		venda.setDataHora(new Date());
//		venda.setCliente(cliente);
//
//		return venda;
//	}
//
//	@AfterClass
//	public static void deveLimparBase() {
//
//		deveLimparBase(Agendamento.class);
//		deveLimparBase(Aluno.class);
//		deveLimparBase(Veiculo.class);
//	}
}