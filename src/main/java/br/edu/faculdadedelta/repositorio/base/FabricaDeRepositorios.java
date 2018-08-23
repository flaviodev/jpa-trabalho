package br.edu.faculdadedelta.repositorio.base;

import java.util.HashMap;
import java.util.Map;

public class FabricaDeRepositorios {

	private static Map<Class<? extends Repositorio>, Repositorio> repositorios;

	private FabricaDeRepositorios() {

		throw new IllegalStateException("Classe utilitária");
	}

	private static Map<Class<? extends Repositorio>, Repositorio> getRepositorios() {

		if (repositorios == null)
			repositorios = new HashMap<>();

		return repositorios;
	}

	public static void registraRepositorio(Class<? extends Repositorio> classeRepositorio, Repositorio repositorio) {

		getRepositorios().put(classeRepositorio, repositorio);
	}

	public static Repositorio getRepositorio(Class<? extends Repositorio> classeRepositorio) {

		return getRepositorios().get(classeRepositorio);
	}

}
