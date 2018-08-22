package br.edu.faculdadedelta.util;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class UUIDGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SessionImplementor session, Object object) {
		return UUIDGenerator.generate();
	}

	public static String generate() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}