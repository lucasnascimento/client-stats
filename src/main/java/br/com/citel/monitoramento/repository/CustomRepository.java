package br.com.citel.monitoramento.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

	<S extends T> void bulkSaveWithoutCheksExists(Iterable<S> entities);

}
