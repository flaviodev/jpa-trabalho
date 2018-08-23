package br.edu.faculdadedelta.modelo.test;

import br.edu.faculdadedelta.test.base.BaseTest;

public class RelatorioTest extends BaseTest {
	
//	private static final String CPF_PADRAO = "111.111.111-11";
//
//
//	@Test
//	public void deveConsultarClientesChaveValor() {
//		
//		criarClientes(5);
//
//		ProjectionList projectionList = Projections.projectionList().add(Projections.property("c.id").as("id"))
//				.add(Projections.property("c.nome").as("nome"));
//
//		Criteria criteria = createCriteria(Aluno.class, "c");
//		criteria.setProjection(projectionList);
//
//		@SuppressWarnings("unchecked")
//		List<Map<String, Object>> clientes = criteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
//
//		assertTrue("Verifica se teve pelomenos 3 produtos", clientes.size() >= 5);
//
//		clientes.forEach(clienteMap -> {
//			clienteMap.forEach((chave, valor) -> {
//				assertTrue("primeiro deve ser string", chave instanceof String);
//				assertTrue("segundo pode ser string ou long", valor instanceof String || valor instanceof Long);
//			});
//		});
//	}
//
//	@Test
//	public void deveConsultarIdENomeProduto() {
//		criarProdutos(1);
//
//		ProjectionList projectionList = Projections.projectionList().add(Projections.property("p.id").as("id"))
//				.add(Projections.property("p.nome").as("nome"));
//
//		Criteria criteria = createCriteria(Veiculo.class, "p");
//		criteria.setProjection(projectionList);
//
//		@SuppressWarnings("unchecked")
//		List<Object[]> produtos = criteria.setResultTransformer(Criteria.PROJECTION).list();
//
//		assertTrue("Verifica se teve pelomenos 3 produtos", produtos.size() >= 1);
//
//		produtos.forEach(produto -> {
//			assertTrue("primeiro deve ser id", produto[0] instanceof Long);
//			assertTrue("segundo deve ser id", produto[1] instanceof String);
//		});
//	}
//
//	@Test
//	public void deveConsultarIdENomeConverterCliente() {
//		
//		criarClientes(3);
//
//		ProjectionList projectionList = Projections.projectionList().add(Projections.property("c.id").as("id"))
//				.add(Projections.property("c.nome").as("nome"));
//
//		Criteria criteria = createCriteria(Aluno.class, "c");
//		criteria.setProjection(projectionList);
//
//		@SuppressWarnings("unchecked")
//		List<Aluno> clientes = criteria.setResultTransformer(Transformers.aliasToBean(Aluno.class)).list();
//
//		assertTrue("Deve ter de 3 a mais clientes ", clientes.size() >= 3);
//
//		clientes.forEach(cliente -> {
//			assertTrue(cliente.getId() != null);
//			assertTrue(cliente.getNome() != null);
//			assertTrue(cliente.getCpf() == null);
//		});
//	}
//
//	
//	@Test
//	public void deveConsultarVendaENomeClienteCasoExista() {
//
//		criarVendas(1);
//
//		Criteria criteria = createCriteria(Agendamento.class, "v").createAlias("v.cliente", "c", JoinType.LEFT_OUTER_JOIN)
//				.add(Restrictions.ilike("c.nome", "Flávio de Souza", MatchMode.START));
//
//		@SuppressWarnings("unchecked")
//		List<Agendamento> vendas = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
//
//		assertTrue("Verifica se teve pelomenos 3 vendas", vendas.size() >= 1);
//
//		vendas.forEach(venda -> assertFalse(venda.isTransient()));
//	}
//
//	@Test
//	public void deveConsultarNotebooksSamsungOuDell() {
//		
//		criarProdutos(3);
//
//		Criteria criteria = createCriteria(Veiculo.class, "p");
//		criteria.add(Restrictions.or(Restrictions.eq("p.fabricante", "Dell"), Restrictions.eq("p.fabricante", "Samsung")));
//
//		@SuppressWarnings("unchecked")
//		List<Veiculo> notebooks = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
//
//		assertTrue("Verifica se teve pelomenos 3 vendas", notebooks.size() >= 3);
//
//		notebooks.forEach(notebook -> assertFalse(notebook.isTransient()));
//	}
//
//	@Test
//	public void deveConsultarProdutosContendoParteDoNome() {
//		
//		criarProdutos(3);
//
//		Criteria criteria = createCriteria(Veiculo.class, "p")
//				.add(Restrictions.ilike("p.nome", "book", MatchMode.ANYWHERE));
//
//		@SuppressWarnings("unchecked")
//		List<Veiculo> notebooks = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
//
//		assertTrue("Verifica se teve pelomenos 3 vendas", notebooks.size() >= 3);
//
//		notebooks.forEach(notebook -> assertFalse(notebook.isTransient()));
//	}
//
//	@Test
//	public void deveConsultarQuantidadeVendasPorCliente() {
//		
//		criarVendas(3);
//
//		Criteria criteria = createCriteria(Agendamento.class, "v").createAlias("v.cliente", "c")
//				.add(Restrictions.eq("c.cpf", CPF_PADRAO)).setProjection(Projections.rowCount());
//
//		Long qtdRegistros = (Long) criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).uniqueResult();
//
//		assertTrue("Verifica se teve pelomenos 3 vendas", qtdRegistros >= 3);
//	}
//
//	@Test
//	public void deveConsultarDezPrimeirosProdutos() {
//		
//		criarProdutos(20);
//
//		Criteria criteria = createCriteria(Veiculo.class, "p").setFirstResult(1).setMaxResults(10);
//
//		@SuppressWarnings("unchecked")
//		List<Veiculo> notebooks = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
//
//		assertTrue("Verifica se teve pelomenos 3 vendas", notebooks.size() == 10);
//
//		notebooks.forEach(notebook -> assertFalse(notebook.isTransient()));
//	}
//
//	@Test
//	public void deveConsultarNotebooks() {
//		
//		criarProdutos(3);
//
//		Criteria criteria = createCriteria(Veiculo.class, "p")
//				.add(Restrictions.in("p.nome", "Notebook", "Netbook", "Macbook")).addOrder(Order.desc("p.fabricante"));
//
//		@SuppressWarnings("unchecked")
//		List<Veiculo> notebooks = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
//
//		assertTrue("Verifica se teve pelomenos 3 vendas", notebooks.size() >= 3);
//
//		notebooks.forEach(notebook -> assertFalse(notebook.isTransient()));
//	}
//
//	@Test
//	public void deveConsultarVendaDaUltimaSemana() {
//		
//		criarVendas(3);
//		
//		Calendar ultimaSemana = Calendar.getInstance();
//		ultimaSemana.add(Calendar.WEEK_OF_YEAR, -1);
//
//		Criteria criteria = createCriteria(Agendamento.class, "v")
//				.add(Restrictions.between("v.dataHora", ultimaSemana.getTime(), new Date()))
//				.setProjection(Projections.rowCount());
//
//		Long qtdRegistros = (Long) criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).uniqueResult();
//
//		assertTrue("Verifica se teve pelomenos 3 vendas", qtdRegistros >= 3);
//	}
//
//	@Test
//	public void deveConsultarMaiorIdCliente() {
//		
//		criarClientes(3);
//
//		Criteria criteria = createCriteria(Aluno.class, "c").setProjection(Projections.max("c.id"));
//
//		Long maiorId = (Long) criteria.setResultTransformer(Criteria.PROJECTION).uniqueResult();
//
//		assertTrue("Verifica se o maximo registro ficou maior ou igual a 3", maiorId >= 3L);
//	}
//
//	@Test
//	public void deveConsultarVendaPorNomeClienteUsandoSubquery() {
//		
//		criarVendas(1);
//
//		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Aluno.class, "c")
//				.add(Restrictions.in("c.id", 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L))
//				.setProjection(Projections.property("c.nome"));
//
//		Criteria criteria = createCriteria(Agendamento.class, "v").createAlias("v.cliente", "cli")
//				.add(Subqueries.propertyIn("cli.nome", detachedCriteria));
//
//		@SuppressWarnings("unchecked")
//		List<Agendamento> vendas = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
//
//		assertTrue("Verifica se teve pelomenos 1 vendas", vendas.size() >= 1);
//
//		vendas.forEach(venda -> assertFalse(venda.getCliente().isTransient()));
//	}
//	
//	@Test
//	public void deveConsultarTodosclientes() {
//
//		criarClientes(3);
//		Criteria criteria = createCriteria(Aluno.class, "c");
//
//		@SuppressWarnings("unchecked")
//		List<Aluno> clientes = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY) // evida dados repetidos
//				.list();
//
//		assertTrue("Deve ter de 3 a mais clientes ", clientes.size() >= 3);
//
//		clientes.forEach(cliente -> assertFalse(cliente.isTransient()));
//	}
//
//	private void criarVendas(int quantidade) {
//		
//		getEntityManager().getTransaction().begin();
//
//		IntStream.range(0, quantidade).forEach(i -> {
//			Agendamento venda = criarVenda();
//			venda.getProdutos().add(new Veiculo("Notebook", "Sony"));
//			venda.getProdutos().add(new Veiculo("Mouse", "Razer"));
//
//			getEntityManager().persist(venda);
//		});
//		
//		getEntityManager().getTransaction().commit();
//	}
//
//	private void criarProdutos(int quantidade) {
//		
//		getEntityManager().getTransaction().begin();
//
//		IntStream.range(0, quantidade).forEach(i -> {
//			Veiculo produto = new Veiculo("Notebook", "Dell");
//
//			getEntityManager().persist(produto);
//		});
//
//		getEntityManager().getTransaction().commit();
//	}
//
//	private void criarClientes(int quantidade) {
//		
//		getEntityManager().getTransaction().begin();
//
//		IntStream.range(0, quantidade).forEach(i -> {
//			Aluno cliente = new Aluno("Flávio de Souza" , CPF_PADRAO);
//
//			getEntityManager().persist(cliente);
//		});
//
//		getEntityManager().getTransaction().commit();
//	}
//
//	private Agendamento criarVenda() {
//		
//		return criarVenda(null);
//	}
//
//	private Agendamento criarVenda(String cpf) {
//		
//		Aluno cliente = new Aluno("Flávio de Souza", CPF_PADRAO);
//
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