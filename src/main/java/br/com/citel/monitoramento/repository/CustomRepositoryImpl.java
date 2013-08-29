package br.com.citel.monitoramento.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class CustomRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CustomRepository<T, ID> {

	private EntityManager entityManager;

	private int bulkSize = 1000;

	// There are two constructors to choose from, either can be used.
	public CustomRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);

		// This is the recommended method for accessing inherited class
		// dependencies.
		this.entityManager = entityManager;
	}

	@Override
	public <S extends T> void bulkSaveWithoutCheksExists(Iterable<S> entities) {
		int count = 0;
		for (S s : entities) {
			entityManager.persist(s);
			if ((++count % bulkSize) == 0) {
				entityManager.flush();
				entityManager.clear();
			}
		}

	}
}
